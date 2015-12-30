package ch.fhnw.oop2.model;

import ch.fhnw.oop2.gui.CustomImage;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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
    public MInventoryItem(int id, String name, String description, CustomImage image, double weight,
                          Color color, double[] dimensions,
                          double stateOfDecay, Type type, String distinctAttribute) {

        super(id, name, description, image, weight, color, dimensions, stateOfDecay, type, distinctAttribute);
    }

    public MInventoryItem(int id, MInventoryObject object) {
        super(id, object);
    }

    public static MInventoryObject emptyObject() {
        double[] dims = {0, 0, 0};
        Type type = new Type("", "");
        return new MInventoryItem(-1, "", "", null, 0, Color.WHITESMOKE, dims, 0, type, "");
    }
}
