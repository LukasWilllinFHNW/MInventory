package ch.fhnw.oop2.model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Lukas on 02.12.2015.
 */
public class MInventoryDataModel {

    private final IntegerProperty currentSelectedId = new SimpleIntegerProperty(-1);

    private final MInventoryObjectProxy proxy = new MInventoryObjectProxy('o', -1, null, null, -1);
    private MInventoryObject temporaryObject;

    public final String ITEM_IDENTIFER = "i";
    public final String STORAGE_IDENTIFIER = "s";

    private SimpleListProperty<MInventoryObject> mInventoryObjectList;


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

    public void save() {
        int newID = mInventoryObjectList.stream()
                .map(MInventoryObject::getId)
                .max((i1, i2) -> i1.compareTo(i2))
                .get()+1;
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

    public void cancel() {
        unselect(0);
        select(currentSelectedId.get());
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
            proxy.setId(-1);
            //if(oldID < 1) this.currentSelectedId.setValue(-1);
            proxy.setId('o');
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
            proxy.setId(newObject.getId());
            if(newID > 0 )this.currentSelectedId.setValue(newObject.getId());
            proxy.setId('o');
        }
    }

    public void unset() {
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


    // --- SETTER ---
    public void setMInventoryObjectListProperty(SimpleListProperty<MInventoryObject> mInventoryObjectSimpleListProperty) {
        this.mInventoryObjectList = mInventoryObjectSimpleListProperty;
    }

    public void setDataMode(SimpleListProperty<MInventoryObject> mInventoryObjectList) {
        this.mInventoryObjectList = mInventoryObjectList;
    }
}
