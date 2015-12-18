package ch.fhnw.oop2.model;

import java.awt.*;

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
     * @param symbolId Id of symbol to use along with the item
     */
    public MInventoryItem(int id, String name, String description, int symbolId) {

        super(id, name, description, symbolId); //description, symbolId, false);
    }

    public MInventoryItem(int id, MInventoryObject object) {
        super(id, object);
    }

    public static MInventoryObject emptyObject() {

        return new MInventoryItem(-1, "", "", -1);
    }
}
