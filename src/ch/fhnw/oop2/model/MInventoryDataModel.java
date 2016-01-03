package ch.fhnw.oop2.model;

import ch.fhnw.oop2.control.MInventoryController;
import ch.fhnw.oop2.gui.CustomImage;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Lukas on 02.12.2015.
 */
public class MInventoryDataModel {

    private MInventoryController controller;

    private final IntegerProperty currentSelectedId = new SimpleIntegerProperty(-1);

    private final MInventoryObjectProxy proxy = MInventoryObjectProxy.emptyObjectProxy();
    private MInventoryObject temporaryObject;

    private final Map<Integer, Image> imagesToSave = new HashMap<>();

    public final String ITEM_IDENTIFER = "i";
    public final String STORAGE_IDENTIFIER = "s";

    private SimpleListProperty<MInventoryObject> mInventoryObjectList;
    private SimpleListProperty<MInventoryObject> mInventoryObjectListProxy = new SimpleListProperty<>();


    // --- API ---
    public void addNewObject(char identifier) {

        temporaryObject = null;

        StringProperty name = new SimpleStringProperty();
        StringProperty description = new SimpleStringProperty();
        this.unselect(currentSelectedId.get());
        if (identifier == 'i')
            temporaryObject = MInventoryItem.emptyObject();
        if (identifier == 's')
            temporaryObject = MInventoryStorage.emptyObject();
        if (temporaryObject == null)
            throw new InvalidParameterException("Wrong identifier");
        proxy.setIdentifier(identifier);
        this.select(0);
    }

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
        if (proxy.getIdentifier() == 'i' && temporaryObject != null) {
            mInventoryObjectList.add(new MInventoryItem(newID, temporaryObject));
            filterByItem(mInventoryObjectList, mInventoryObjectListProxy);
        }
        if (proxy.getIdentifier() == 's' && temporaryObject != null) {
            mInventoryObjectList.add(new MInventoryStorage(newID, temporaryObject));
            filterByStorage(mInventoryObjectList, mInventoryObjectListProxy);
        }
        unselect(currentSelectedId.get());
        select(newID);
    }

    // -- filter and search --
    public void noFilter(ListProperty<MInventoryObject> inList, ListProperty<MInventoryObject> toList) {
        toList.setValue(FXCollections.observableList(inList.stream()
                .map(object -> getById(object.getId()))
                .collect(Collectors.toList())));
    }
    public void filterByStorage(ListProperty<MInventoryObject> inList, ListProperty<MInventoryObject> toList) {
        toList.setValue(FXCollections.observableList(inList.stream()
                .filter(mInventoryObject -> (mInventoryObject instanceof MInventoryStorage))
                .map(object -> getById(object.getId()))
                .collect(Collectors.toList())));
    }
    public void filterByItem(ListProperty<MInventoryObject> inList, ListProperty<MInventoryObject> toList) {
        toList.setValue(FXCollections.observableList(inList.stream()
                .filter(mInventoryObject -> (mInventoryObject instanceof MInventoryItem))
                .map(object -> getById(object.getId()))
                .collect(Collectors.toList())));
    }
    public void searchFor(String oldSearch, String newSearch,ListProperty<MInventoryObject> inList , ListProperty<MInventoryObject> toList) {
        newSearch.trim();
        String loweredNewSearch = newSearch.toLowerCase();
        if (newSearch.length() > oldSearch.length()) { //faster search in proxy list -> shrinking object amount

            toList.setValue(FXCollections.observableList(toList.stream()
                    .filter(object -> {
                        String containing = (infoAsLine(object.getId())).toLowerCase();
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
                        String containing = (infoAsLine(object.getId())).toLowerCase();
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
    public void updateSelection(int newSelectedId){
        int newID = newSelectedId;
        int oldID = currentSelectedId.get();
        // DO unbinding
        this.unselect(oldID);
        this.select(newID);
    }

    /**
     * Selects the first object in object list proxy catching all expected exceptions
     */
    private void exceptionSafeSelectFirst(ListProperty<MInventoryObject> fromList){
        try {
            updateSelection(fromList.get(0).getId());
        } catch (IndexOutOfBoundsException iobe) {
            System.out.println(iobe.getMessage() + iobe.getStackTrace().toString() + "No object in list");
        }
    }
    public void unselect(int oldID) {
        MInventoryObject oldObject = getById(oldID);
        if (oldObject != null) {
            proxy.getNameProperty().unbindBidirectional(oldObject.getNameProperty());
            proxy.getDescriptionProperty().unbindBidirectional(oldObject.getDescriptionProperty());
            proxy.getImageProperty().unbindBidirectional(oldObject.getImageProperty());
            proxy.setId(-1);
            proxy.setIdentifier(' ');
        }
        if (oldID < 1) {
            temporaryObject = null;
        }
    }
    public void select(int newID) {
        MInventoryObject newObject = getById(newID);
        if (newObject != null) {
            proxy.getNameProperty().bindBidirectional(newObject.getNameProperty());
            proxy.getDescriptionProperty().bindBidirectional(newObject.getDescriptionProperty());
            proxy.getImageProperty().bindBidirectional(newObject.getImageProperty());
            proxy.setId(newObject.getId());
            if(newID > 0 ) this.currentSelectedId.setValue(newObject.getId());
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
    public void delete() {
        // pay attention to the order first remove from proxy
        mInventoryObjectListProxy.remove(getById(currentSelectedId.get()));
        mInventoryObjectList.remove(getById(currentSelectedId.get()));
    }

    public void addImage(CustomImage ci) {
        imagesToSave.put(currentSelectedId.get(), ci);
        proxy.getImageProperty().setValue(ci);
    }

    public void cancelNewObject() {
        unselect(0);
        select(currentSelectedId.get());
    }

    public String infoAsLine(int objectId){
        MInventoryObject requestedObject = this.getById(objectId);
        StringBuffer info = new StringBuffer();

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


    // --- PROPERTY GETTER ---
    public SimpleListProperty<MInventoryObject> getMInventoryObjectList() {
        return this.mInventoryObjectList;
    }
    public SimpleListProperty<MInventoryObject> getMInventoryObjectListProxy() { return this.mInventoryObjectListProxy; }
    public IntegerProperty getCurrentSelectedIdProperty() { return currentSelectedId; }


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

    public void setMInventoryController(MInventoryController mInventoryController) {
        if (controller == null) controller = mInventoryController;
    }
}
