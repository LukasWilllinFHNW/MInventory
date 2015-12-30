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
    private SimpleListProperty<MInventoryObject> mInventoryObjectListProxy;


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
        }
        if (proxy.getIdentifier() == 's' && temporaryObject != null) {
            mInventoryObjectList.add(new MInventoryStorage(newID, temporaryObject));
        }
        unselect(currentSelectedId.get());
        select(newID);
    }

    // -- filter and search --
    public void noFilter() {
        mInventoryObjectListProxy.setValue(FXCollections.observableList(mInventoryObjectList.stream()
                .map(object -> getById(object.getId()))
                .collect(Collectors.toList())));
    }
    public void filterByStorage() {
        mInventoryObjectListProxy.setValue(FXCollections.observableList(mInventoryObjectList.stream()
                .filter(mInventoryObject -> (mInventoryObject instanceof MInventoryStorage))
                .map(object -> getById(object.getId()))
                .collect(Collectors.toList())));
    }
    public void filterByItem() {
        mInventoryObjectListProxy.setValue(FXCollections.observableList(mInventoryObjectList.stream()
                .filter(mInventoryObject -> (mInventoryObject instanceof MInventoryItem))
                .map(object -> getById(object.getId()))
                .collect(Collectors.toList())));
    }
    public void searchFor(String search) {
        search.trim();
        search.toLowerCase();
        if (search.isEmpty()) {
            noFilter();
        } if (search.length() < 2) {
            try {
                int id = Integer.parseInt(search);
                mInventoryObjectListProxy.setValue(FXCollections.observableList(mInventoryObjectListProxy.stream()
                        .filter(object -> (object.getId() == id))
                        .map(object -> getById(object.getId()))
                        .collect(Collectors.toList())));
            } catch (NumberFormatException nfe) {
                System.out.println(nfe.getMessage() + nfe.getStackTrace().toString());
            }
        } else {
            mInventoryObjectListProxy.setValue(FXCollections.observableList(mInventoryObjectListProxy.stream()
                    .filter(object -> (infoAsLine(object.getId()).toLowerCase().contains(search)))
                    .map(object -> getById(object.getId()))
                    .collect(Collectors.toList())));
        }
    }


    // -- selection handling --
    public void updateSelection(int newSelectedId){
        int newID = newSelectedId;
        int oldID = currentSelectedId.get();
        // DO unbinding
        this.unselect(oldID);
        this.select(newID);
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
        this.controller.writeObjectsToFile();

        for (Map.Entry entry : imagesToSave.entrySet()) {
            copyImage((CustomImage) entry.getValue());
            imagesToSave.remove(entry.getKey());
        }
    }
    public void delete() {
        this.getMInventoryObjectSimpleListProperty().remove(getById(currentSelectedId.get()));
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

    public void copyImage(CustomImage ci) {
        String fileExtension = ci.getUrl().substring(ci.getUrl().lastIndexOf('.'));
        String newFileName = currentSelectedId.get() + "-1" + fileExtension;
        controller.copyImage(ci, newFileName);
    }


    // --- GETTER ---
    private List<MInventoryObject> getMInventoryObjectList() {
        return (List<MInventoryObject>) this.mInventoryObjectList.getValue();
    }

    public MInventoryObject getById(int id) {
        if (id == 0) {
            return temporaryObject;
        }
        Optional<MInventoryObject> object = mInventoryObjectList.stream()
                .filter(mInventoryObject -> mInventoryObject.getId() == id)
                .findAny();
        return object.isPresent() ? object.get() : null;
    }

    public MInventoryObjectProxy getProxy() {
        return this.proxy;
    }


    // --- PROPERTY GETTER ---
    public SimpleListProperty<MInventoryObject> getMInventoryObjectSimpleListProperty() {
        return this.mInventoryObjectList;
    }
    public SimpleListProperty<MInventoryObject> getMInventoryObjectProxySimpleListProperty() {
        return this.mInventoryObjectListProxy;
    }


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
