package ch.fhnw.oop2.model;

import javafx.scene.image.Image;

/**
 * Created by Lukas W on 17.11.2015.
 */
public class MInventoryItem extends MInventoryObject {

    private final char ITEM_IDENTIFIER = 'i';

    /**
     * Create a new MInventoryObject
     *
     * @param name Name of the item
     * @param description Description of item
     * @param image or symbol to use along with the item
     */
    public MInventoryItem(int id, String name, String description, Image image) {

        super(id, name, description, image); //description, symbolId, false);
    }

    public MInventoryItem(int id, MInventoryObject object) {
        super(id, object);
    }

    public static MInventoryObject emptyObject() {

        return new MInventoryItem(-1, "", "", null);
    }
}
