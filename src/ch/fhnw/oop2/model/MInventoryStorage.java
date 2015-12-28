package ch.fhnw.oop2.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Lukas W on 17.11.2015.
 */
public class MInventoryStorage extends MInventoryObject {

    private ObservableList<Integer> objectIds;

    private final char STORAGE_IDENTIFIER = 's';


    // --- CONSTRUCTORS ---
    /**
     * Creates a new storage.
     * @param id id of storage
     * @param name name of the storage
     * @param description description of the storage
     * @param image or symbol to use. */
    public MInventoryStorage(int id, String name, String description, Image image) { //, String description, int symbolId) {
        super(id, name, description, image);
        this.objectIds = FXCollections.observableArrayList();
    }

    public MInventoryStorage(int id, MInventoryObject object) {
        super(id, object);
        this.objectIds = FXCollections.observableArrayList();
    }

    public static MInventoryObject emptyObject() {
        return new MInventoryStorage(-1, "", "", null);
    }


    // --- API ---
    /**
     * Adds an MInventoryObject if it is not bigger then the inventory itself.
     * @param objectId the item or storage to put inside */
    public void addObjectById(int objectId) throws MInventoryException {
        //TODO: Check that object fits inside this storage
        this.objectIds.add(objectId);

    }


    // --- GETTER ---
    public List<Integer> getContainedObjectIds() {
        return objectIds.stream().collect(Collectors.toList());
    }


    // --- HELPER ---
    /* Check if another object fits into this storage *
    private boolean verifyDimensionsFit(MInventoryObject mInventoryObject) throws MInventoryException{
        try {
            if (super.getDimensions()[0] > mInventoryObject.getDimensions()[0]) {
                if (super.getDimensions()[1] > mInventoryObject.getDimensions()[1]
                        || super.getDimensions()[2] > mInventoryObject.getDimensions()[2]
                        || super.getDimensions()[3] > mInventoryObject.getDimensions()[3]) {
                    return true;
                }
            }
            return false;
        } catch (MInventoryException mie) {
            throw new MInventoryException("It cannot be determined if the obejct fits into this storage because one or both had insufficient information.");
        }
    } */
}
