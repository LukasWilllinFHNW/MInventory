package ch.fhnw.oop2.model;

import ch.fhnw.oop2.control.*;
import ch.fhnw.oop2.gui.CustomImage;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.util.*;
import java.util.stream.Collectors;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;

/**
 * Created by Lukas Willin on 02.12.2015.
 * @author Lukas Willin
 * @author Dieter Holz (Undo Redo)
 */
public class MInventoryDataModel {

    private MInventoryFilesController controller;

    private final IntegerProperty currentSelectedId = new SimpleIntegerProperty(-1);


    private final MInventoryObjectProxy proxy = MInventoryObjectProxy.emptyObjectProxy();
    private MInventoryObject temporaryObject;

    private final Map<Integer, Image> imagesToSave = new HashMap<>();

    public final String ITEM_IDENTIFER = "i";
    public final String STORAGE_IDENTIFIER = "s";

    private SimpleListProperty<MInventoryObject> mInventoryObjectList;
    private SimpleListProperty<MInventoryObject> mInventoryObjectListProxy = new SimpleListProperty<>();
    private SimpleBooleanProperty proxyListIsFiltered;

    private final ObservableList<Command> undoStack = FXCollections.observableArrayList();
    private final ObservableList<Command> redoStack = FXCollections.observableArrayList();

    private final BooleanProperty undoDisabled = new SimpleBooleanProperty();
    private final BooleanProperty redoDisabled = new SimpleBooleanProperty();
    private final BooleanProperty removeFromStorageDisabled = new SimpleBooleanProperty();
    private final BooleanProperty lookIntoStorageDisabled = new SimpleBooleanProperty();

    private long lastLoggedTimeInMillis = Calendar.getInstance().getTimeInMillis();
    private long waitTime = 1000;
    private ObservableValue lastChangedProperty;
    private final ChangeListener<Object> propertyChangeListenerForUndoSupport = (observable, oldValue, newValue) -> {
        if (!undoStack.isEmpty()) {
            // If last logged time is younger -> update or create command
            if (Calendar.getInstance().getTimeInMillis() - waitTime < lastLoggedTimeInMillis) {
                // Update only ValueChangeCommands and if property is still the same
                if (lastChangedProperty == observable && undoStack.get(0) instanceof ValueChangeCommand) {
                    undoStack.get(0).updateRedo(newValue);
                    // Change occured -> reset the timer
                    lastLoggedTimeInMillis = Calendar.getInstance().getTimeInMillis();
                // It is not the same property or not instance of ValueChangeCommand -> Create new
                } else {
                    redoStack.clear();
                    undoStack.add(0, new ValueChangeCommand(MInventoryDataModel.this, (Property) observable, oldValue, newValue));
                    // New property -> update last changed property
                    lastChangedProperty = observable;
                    // Change occured -> reset the timer
                    lastLoggedTimeInMillis = Calendar.getInstance().getTimeInMillis();
                }
            // The last logged time is too old
            } else {
                redoStack.clear();
                undoStack.add(0, new ValueChangeCommand(MInventoryDataModel.this, (Property) observable, oldValue, newValue));
                // New property -> update last changed property
                lastChangedProperty = observable;
                // Change occured -> reset the timer
                lastLoggedTimeInMillis = Calendar.getInstance().getTimeInMillis();
            }
        } else {
            redoStack.clear();
            undoStack.add(0, new ValueChangeCommand(MInventoryDataModel.this, (Property) observable, oldValue, newValue));
            // New property -> update last changed property
            lastChangedProperty = observable;
            // Change occured -> reset the timer
            lastLoggedTimeInMillis = Calendar.getInstance().getTimeInMillis();
        }
    };

    // --- CONSTRUCTORS ---
    public MInventoryDataModel() {

        proxyListIsFiltered = new SimpleBooleanProperty();

        undoDisabled.bind(Bindings.isEmpty(undoStack));
        redoDisabled.bind(Bindings.isEmpty(redoStack));

        currentSelectedId.addListener((observable, oldValue, newValue) -> {
            MInventoryObject oldSelection = getById((int) oldValue);

            if (oldSelection != null) {
                unselect(oldSelection);
                disableUndoSupport(oldSelection);
                removeFromStorageDisabled.set(true);
                lookIntoStorageDisabled.set(true);
            }

            if (newValue.intValue() != -1) {
                MInventoryObject newSelection = getById((int) newValue);

                if (newSelection != null) {
                    select(newSelection);
                    enableUndoSupport(newSelection);
                    removeFromStorageDisabled.set((getContainingStorage(newValue.intValue()) == null));
                    lookIntoStorageDisabled.set(newSelection instanceof MInventoryItem);
                    if (newSelection instanceof MInventoryStorage)
                        lookIntoStorageDisabled.set(((MInventoryStorage)newSelection).getContainedObjectIds().isEmpty());
                }
            }
        });

    }

    // --- API ---
    // -- undo redo --
    public void undo() {
        if (undoStack.isEmpty()) {
            return;
        }
        Command cmd = undoStack.get(0);
        undoStack.remove(0);
        redoStack.add(0, cmd);

        cmd.undo();
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            return;
        }
        Command cmd = redoStack.get(0);
        redoStack.remove(0);
        undoStack.add(0, cmd);

        cmd.redo();
    }
    /**
     * Set a value for any property
     * @param property the property of which the value should be changes
     * @param newValue the new Value of the property
     */
    public void setPropertyValue(Property property, Object newValue){
        property.removeListener(propertyChangeListenerForUndoSupport);
        property.setValue(newValue);
        property.addListener(propertyChangeListenerForUndoSupport);
    }
    private void disableUndoSupport(MInventoryObject object) {
        // MAKE SURE ONLY ONE LISTENER PER OBJECT IS REMOVED
        object.getNameProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getDescriptionProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getColorProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getImageProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getLengthProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getHeightProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getDepthProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getWeightProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getDistinctAttributeProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getTypeProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getUsageTypeProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getStateOfDecayProperty().removeListener(propertyChangeListenerForUndoSupport);
    }
    private void enableUndoSupport(MInventoryObject object) {
        // MAKE SURE ONLY ONE LISTENER PER PROPERTY IS ADDED
        object.getNameProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getDescriptionProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getColorProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getImageProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getLengthProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getHeightProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getDepthProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getWeightProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getDistinctAttributeProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getTypeProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getUsageTypeProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getStateOfDecayProperty().addListener(propertyChangeListenerForUndoSupport);
    }


    // -- filter and search --
    public void noFilter(ListProperty<MInventoryObject> inList, ListProperty<MInventoryObject> toList) {
        toList.setValue(FXCollections.observableList(inList.stream()
                .map(object -> getById(object.getId()))
                .collect(Collectors.toList())));
        proxyListIsFiltered.set(false);
    }
    public void filterByStorage(ListProperty<MInventoryObject> inList, ListProperty<MInventoryObject> toList) {
        toList.setValue(FXCollections.observableList(inList.stream()
                .filter(mInventoryObject -> (mInventoryObject instanceof MInventoryStorage))
                .map(object -> getById(object.getId()))
                .collect(Collectors.toList())));
        proxyListIsFiltered.set(true);
    }
    public void filterByItem(ListProperty<MInventoryObject> inList, ListProperty<MInventoryObject> toList) {
        toList.setValue(FXCollections.observableList(inList.stream()
                .filter(mInventoryObject -> (mInventoryObject instanceof MInventoryItem))
                .map(object -> getById(object.getId()))
                .collect(Collectors.toList())));
        proxyListIsFiltered.set(true);
    }
    public void searchFor(String oldSearch, String newSearch,ListProperty<MInventoryObject> inList , ListProperty<MInventoryObject> toList) {
        newSearch.trim();
        String loweredNewSearch = newSearch.toLowerCase();
        if (newSearch.length() > oldSearch.length()) { //faster search in proxy list -> shrinking object amount
            toList.setValue(FXCollections.observableList(toList.stream()
                    .filter(object -> {
                        String containing = (object.searchInfo()).toLowerCase();
                        if (containing.contains(loweredNewSearch)) {
                            return true;
                        } else {
                            return false;
                        }
                    })
                    .map(object -> getById(object.getId()))
                    .collect(Collectors.toList())));
        } else { // Slower search in object list if new search string is shorter -> growing object amount
            toList.setValue(FXCollections.observableList(inList.stream()
                    .filter(object -> {
                        String containing = (object.searchInfo()).toLowerCase();
                        if (containing.contains(loweredNewSearch)) {
                            return true;
                        } else {
                            return false;
                        }
                    })
                    .map(object -> getById(object.getId()))
                    .collect(Collectors.toList())));
        }
        if (toList.size() == 1) exceptionSafeSelectFirst(toList);
    }

    // -- selection handling --

    /**
     * Update selected id
     * @param newObject beeing the object to select, null if no selection
     */
    public void updateSelection(MInventoryObject newObject){
        if (newObject != null) {
            currentSelectedId.set(newObject.getId());
        } else {
            currentSelectedId.set(-1);
        }
    }

    /**
     * Selects the first object in object list proxy catching all expected exceptions
     */
    private void exceptionSafeSelectFirst(ListProperty<MInventoryObject> fromList){
        try {
            updateSelection(fromList.get(0));
        } catch (IndexOutOfBoundsException iobe) {
            System.out.println(iobe.getMessage() + iobe.getStackTrace().toString() + "No object in list");
        }
    }
    public void unselect(MInventoryObject object) {
        if (object != null) {
            int oldID = object.getId();
            proxy.getNameProperty().unbindBidirectional(object.getNameProperty());
            proxy.getDescriptionProperty().unbindBidirectional(object.getDescriptionProperty());
            proxy.getImageProperty().unbindBidirectional(object.getImageProperty());
            proxy.getStateOfDecayProperty().unbindBidirectional(object.getStateOfDecayProperty());
            proxy.getTypeProperty().unbindBidirectional(object.getTypeProperty());
            proxy.getUsageTypeProperty().unbindBidirectional(object.getUsageTypeProperty());
            proxy.getWeightProperty().unbindBidirectional(object.getWeightProperty());
            proxy.getHeightProperty().unbindBidirectional(object.getHeightProperty());
            proxy.getDepthProperty().unbindBidirectional(object.getDepthProperty());
            proxy.getLengthProperty().unbindBidirectional(object.getLengthProperty());
            proxy.getColorProperty().unbindBidirectional(object.getColorProperty());
            proxy.getDistinctAttributeProperty().unbindBidirectional(object.getDistinctAttributeProperty());

            proxy.setId(-1);
            proxy.setIdentifier(' ');

            if (oldID < 1) {
                temporaryObject = null;
            }
        } else {

        }
    }

    /**
     * Selects a MInventoryObject
     * @param newObject the object to select
     */
    public void select(MInventoryObject newObject) {
        if (newObject != null) {
            int newID = newObject.getId();

            proxy.getNameProperty().bindBidirectional(newObject.getNameProperty());
            proxy.getDescriptionProperty().bindBidirectional(newObject.getDescriptionProperty());
            proxy.getImageProperty().bindBidirectional(newObject.getImageProperty());
            proxy.getStateOfDecayProperty().bindBidirectional(newObject.getStateOfDecayProperty());
            proxy.getTypeProperty().bindBidirectional(newObject.getTypeProperty());
            proxy.getUsageTypeProperty().bindBidirectional(newObject.getUsageTypeProperty());
            proxy.getWeightProperty().bindBidirectional(newObject.getWeightProperty());
            proxy.getHeightProperty().bindBidirectional(newObject.getHeightProperty());
            proxy.getDepthProperty().bindBidirectional(newObject.getDepthProperty());
            proxy.getLengthProperty().bindBidirectional(newObject.getLengthProperty());
            proxy.getColorProperty().bindBidirectional(newObject.getColorProperty());
            proxy.getDistinctAttributeProperty().bindBidirectional(newObject.getDistinctAttributeProperty());

            proxy.setId(newObject.getId());
            //if(newID > 0 ) this.currentSelectedId.setValue(newObject.getId());
            if (newObject instanceof MInventoryStorage) proxy.setIdentifier('s');
            if (newObject instanceof MInventoryItem) proxy.setIdentifier('i');
        }
    }


    // -- data operations --
    public void save() {
        // Write csv
        this.controller.writeObjectsToFile();
        // copy images the user has previously choosen to a dedicated location
        List<Integer> ids = new ArrayList<>();
        for (Map.Entry entry : imagesToSave.entrySet()) {
            this.copyImage((int)entry.getKey(), (CustomImage) entry.getValue());
            ids.add((int)entry.getKey());
        }
        for (int id : ids) {
            imagesToSave.remove(id);
        }
    }
    public void delete(MInventoryObject object) {
        // pay attention to the order -> first remove from proxy
        if (object == null) {
            MInventoryObject remove = getById(currentSelectedId.get());
            unselect(remove);
            disableUndoSupport(remove);
            if (mInventoryObjectListProxy.contains(remove))
                mInventoryObjectListProxy.remove(remove);
            mInventoryObjectList.remove(remove);
            undoStack.add(0, new RemoveCommand(MInventoryDataModel.this, remove, 0));
            if(!mInventoryObjectList.isEmpty()) {
                updateSelection(mInventoryObjectList.get(0));
            } else {
                updateSelection(null);
            }
            return;
        } else {
            disableUndoSupport(object);
            if (mInventoryObjectListProxy.contains(object))
                mInventoryObjectListProxy.remove(object);
            mInventoryObjectList.remove(object);

            if(!mInventoryObjectList.isEmpty()){
                updateSelection(object);
            }
            return;
        }
    }
    public void prepareNewObject(char identifier) {

        temporaryObject = null;

        StringProperty name = new SimpleStringProperty();
        StringProperty description = new SimpleStringProperty();

        this.unselect(getById(currentSelectedId.get()));
        if (identifier == 'i')
            temporaryObject = MInventoryItem.emptyObject();
        if (identifier == 's')
            temporaryObject = MInventoryStorage.emptyObject();
        if (temporaryObject == null)
            throw new IllegalArgumentException("Wrong identifier");
        proxy.setIdentifier(identifier);
        this.select(getById(0));
    }
    /**
     * Append or insert in object list
     * @param position index of object, if null object is appended at last index+1
     * @param object the object to be added
     */
    public void addToList(Integer position, MInventoryObject object) {
        // Be aware that the object must be added to proxy last
        // Otherwise calls on getById will return null because addToProxy updateTheSelection
        if (position != null) {
            if (position >= 0) {
                mInventoryObjectList.add(object);
                addToProxyList(position, object);
            } else {
                System.out.print("No position defined for object " + object.getId() + " " + object.getName());
                mInventoryObjectList.add(object);
                addToProxyList(0, object);
            }
        } else {
            System.out.print("No position defined for object " + object.getId() + " " + object.getName());
            mInventoryObjectList.add(object);
            addToProxyList(0, object);
        }
        return;
    }
    private void addToProxyList(int position, MInventoryObject object) {
        if (proxyListIsFiltered.get()) {
            if (!mInventoryObjectListProxy.isEmpty()) {
                if (object instanceof MInventoryStorage && mInventoryObjectListProxy.get(0) instanceof MInventoryStorage) {
                    mInventoryObjectListProxy.add(0, object);
                    updateSelection(object);
                }
                if (object instanceof MInventoryItem && mInventoryObjectListProxy.get(0) instanceof MInventoryItem) {
                    mInventoryObjectListProxy.add(0, object);
                    updateSelection(object);
                }
            } else {
                noFilter(mInventoryObjectList, mInventoryObjectListProxy);
                updateSelection(object);
            }
        } else {
            mInventoryObjectListProxy.add(object);
            //noFilter(mInventoryObjectList, mInventoryObjectListProxy);
            updateSelection(object);
        }
    }
    /**
     * Creates a new object based on data stored in a temporary object
     * which is connected to the proxy
     */
    public void createNewObject() {
        int newID;
        try {
            newID = mInventoryObjectList.stream()
                    .map(MInventoryObject::getId)
                    .max((i1, i2) -> i1.compareTo(i2))
                    .get() + 1;
        } catch (NoSuchElementException e) {
            newID = 1;
        }
        if (newID > (Integer.MAX_VALUE -1)) {
            // TODO: Optimize ids (fill up gaps) if max value is reached
        };
        if (temporaryObject instanceof MInventoryItem && proxy.getIdentifier() == 'i' && temporaryObject != null) {
            mInventoryObjectList.add(0, new MInventoryItem(newID, temporaryObject));
            filterByItem(mInventoryObjectList, mInventoryObjectListProxy);
        }
        if (temporaryObject instanceof MInventoryStorage && proxy.getIdentifier() == 's' && temporaryObject != null) {
            mInventoryObjectList.add(0, new MInventoryStorage(newID, temporaryObject));
            filterByStorage(mInventoryObjectList, mInventoryObjectListProxy);
        }
        updateSelection(getById(newID));
        undoStack.add(0, new AddCommand(MInventoryDataModel.this, getById(newID), 0));
    }
    public void addImage(CustomImage ci) {
        imagesToSave.put(currentSelectedId.get(), ci);
        proxy.getImageProperty().setValue(ci);
    }
    public void cancelNewObject() {
        unselect(getById(0));
        select(getById(currentSelectedId.get()));
    }
    // -- put --
    public void putObjectIntoStorage(MInventoryStorage storage, MInventoryObject put) {
        redoRemoveFromStorage(put);
        storage.addObjectById(put.getId());
        removeFromStorageDisabled.set(false);
        undoStack.add(0, new AddToStorageCommand(MInventoryDataModel.this, put, storage));
        lookIntoStorageDisabled.set(storage.getContainedObjectIds().isEmpty());
    }
    public void redoPutObjectIntoStorage(MInventoryStorage storage, MInventoryObject put) {
        redoRemoveFromStorage(put);
        storage.addObjectById(put.getId());
        removeFromStorageDisabled.set(false);
        lookIntoStorageDisabled.set(storage.getContainedObjectIds().isEmpty());
    }
    // -- remove --
    public void removeFromStorage(MInventoryObject remove) {
        MInventoryStorage storage = redoRemoveFromStorage(remove);
        undoStack.add(0, new RemoveFromStorageCommand(MInventoryDataModel.this, storage,  remove));
    }
    public MInventoryStorage redoRemoveFromStorage(MInventoryObject remove) {
        Integer containerId = getContainingStorage(remove.getId());
        try {
            if (containerId == null) {
                throw new IllegalStateException("The storage containing ["
                        + remove.getId() + " " + remove.getName() + "] could not be found");
            }
        } catch (IllegalStateException ise) {
            System.out.println(ise);
        }
        if (containerId != null) {
            MInventoryStorage storage = ((MInventoryStorage) getById(containerId));
            storage.getContainedObjectIds().remove((Integer) remove.getId());
            removeFromStorageDisabled.set(true);
            lookIntoStorageDisabled.set(storage.getContainedObjectIds().isEmpty());
            return storage;
        } else return null;
    }

    public String infoAsLine(int objectId){
        MInventoryObject requestedObject = this.getById(objectId);
        StringBuffer info = new StringBuffer();

        Integer containerId = getContainingStorage(objectId);
        // Write storage id into string
        if (containerId != null) {
            info.append(containerId.intValue() + ";");
        } else {
            info.append(-1 + ";");
        }

        // Check if object is storage and mark it
        if (requestedObject instanceof MInventoryStorage)
            info.append(STORAGE_IDENTIFIER + ";");
        if (requestedObject instanceof MInventoryItem)
            info.append(ITEM_IDENTIFER + ";");

        // Append general information
        info.append(requestedObject.infoAsLine());

        return info.toString();
    }

    public Integer getContainingStorage(int objectId){
        // Search for storage containing the requested object
        List<Integer> list = mInventoryObjectList.stream()
                .filter(object -> object instanceof MInventoryStorage )
                .filter(object -> ((MInventoryStorage)object).getContainedObjectIds().contains(objectId))
                .map(mInventoryObject -> mInventoryObject.getId() )
                .collect(Collectors.toList());
        Integer containerId = null;
        if (!list.isEmpty()) {
            containerId = list.get(0);
        }
        return containerId;
    }

    public void copyImage(int id,CustomImage ci) {
        String fileExtension = ci.getUrl().substring(ci.getUrl().lastIndexOf('.'));
        String newFileName = id + "-1" + fileExtension;
        controller.copyImage(ci, newFileName);
    }


    // --- GETTER ---
    public MInventoryObject getById(int id) {
        if (id == 0) {
            return temporaryObject;
        }
        Optional<MInventoryObject> object = mInventoryObjectList.stream()
                .filter(mInventoryObject -> mInventoryObject.getId() == id)
                .findAny();
        return object.isPresent() ? object.get() : null;
    }
    public int getCurrentId() { return currentSelectedId.intValue(); }
    public MInventoryObjectProxy getProxy() {
        return this.proxy;
    }
    public MInventoryObject getCurrentSelectedObject() {
        return getById(currentSelectedId.intValue());
    }


    // --- PROPERTY GETTER ---
    public SimpleListProperty<MInventoryObject> getMInventoryObjectList() {
        return this.mInventoryObjectList;
    }
    public SimpleListProperty<MInventoryObject> getMInventoryObjectListProxy() { return this.mInventoryObjectListProxy; }
    public IntegerProperty getCurrentSelectedIdProperty() { return currentSelectedId; }
    public BooleanProperty getRemoveFromStorageDisabled() { return removeFromStorageDisabled; }
    public BooleanProperty getLookIntoStorageDisabled() { return lookIntoStorageDisabled; }


    // --- SETTER ---
    public void setMInventoryObjectListProperty(SimpleListProperty<MInventoryObject> mInventoryObjectSimpleListProperty) {
        if (mInventoryObjectList == null) this.mInventoryObjectList = mInventoryObjectSimpleListProperty;
    }

    public void setMInventoryObjectListProxyProperty(SimpleListProperty<MInventoryObject> mInventoryObjectProxySimpleListProperty) {
        if (mInventoryObjectListProxy == null) {
            this.mInventoryObjectListProxy = new SimpleListProperty<>();
            mInventoryObjectListProxy.setValue(FXCollections.observableList(mInventoryObjectProxySimpleListProperty.stream()
                    .map(object -> getById(object.getId()))
                    .collect(Collectors.toList())));
        }
    }

    public void setDataMode(SimpleListProperty<MInventoryObject> mInventoryObjectList) {
        this.mInventoryObjectList = mInventoryObjectList;
    }

    public void setMInventoryController(MInventoryFilesController mInventoryFilesController) {
        if (controller == null) controller = mInventoryFilesController;
    }
}
