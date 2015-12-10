package ch.fhnw.oop2.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Lukas on 02.12.2015.
 */
public class MInventoryDataModel {

    private final IntegerProperty currentSelectedId = new SimpleIntegerProperty(-1);

    private final MInventoryObjectProxy proxy = new MInventoryObjectProxy(-1, null, null, -1);

    public final String ITEM_IDENTIFER = "i";
    public final String STORAGE_IDENTIFIER = "s";

    private SimpleListProperty<MInventoryObject> mInventoryObjectList;

    // --- CONSTRUCTORS ---
    public MInventoryDataModel(SimpleListProperty mInventoryObjectSimpleListProperty) {
        this.mInventoryObjectList = mInventoryObjectSimpleListProperty;
    }


    // --- API ---
    public void updateSelection(int newSelectedId){
        MInventoryObject newObject = this.getById(newSelectedId);
        MInventoryObject oldObject = this.getById(currentSelectedId.intValue());
        // DO unbinding
        if (oldObject != null) {
            proxy.getNameProperty().unbindBidirectional(oldObject.getNameProperty());
            proxy.getDescriptionProperty().unbindBidirectional(oldObject.getDescriptionProperty());
            proxy.setId(-1);
            this.currentSelectedId.setValue(-1);
        }
        if (newObject != null) {
            proxy.getNameProperty().bindBidirectional(newObject.getNameProperty());
            proxy.getDescriptionProperty().bindBidirectional(newObject.getDescriptionProperty());
            proxy.setId(newObject.getId());
            this.currentSelectedId.setValue(newSelectedId);
        }
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
        Optional<MInventoryObject> object = mInventoryObjectList.stream()
                .filter(mInventoryObject -> mInventoryObject.getId() == id)
                .findAny();
        return object.isPresent() ? object.get() : null;
    }

    public MInventoryObjectProxy getProxy() {
        return this.proxy;
    }

    // --- GETTER PROPERTY ---
    public SimpleListProperty<MInventoryObject> getMInventoryObjectSimpleListProperty() {
        return this.mInventoryObjectList;
    }

    // --- SETTER ---
    public void setMInventoryObjectSimpleListProperty(ObservableList<MInventoryObject> mInventoryObjectSimpleListProperty) {
        this.mInventoryObjectList.set(mInventoryObjectSimpleListProperty);
    }
}
