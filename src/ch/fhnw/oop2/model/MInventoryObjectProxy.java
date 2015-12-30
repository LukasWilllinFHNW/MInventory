package ch.fhnw.oop2.model;

import ch.fhnw.oop2.gui.CustomImage;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Created by Lukas on 08.12.2015.
 */
public class MInventoryObjectProxy extends MInventoryObject {

    private int id;

    private char identifier;

    // TODO: Add symbol instead of id or both


    /**
     * Create a new MInventoryObject
     *
     * @param name name of proxyObject
     * @param description description of proxyObject
     * @param image image or symbol of proxyObject
     */
    public MInventoryObjectProxy(char identifier, int id, String name, String description, CustomImage image,
                                 double weight, Color color, double[] dimensions,
                                 double stateOfDecay, Type type, String distinctAttribute) {
        super(-1, name, description, image, weight, color, dimensions, stateOfDecay, type, distinctAttribute);
        this.identifier = identifier;
    }


    // --- API ---
    public void update(int id, String name, String description, int symbolId) {
        this.id = id;
        super.setName(name);
        super.setDescription(description);
    }

    public static MInventoryObjectProxy emptyObjectProxy() {
        double[] dims = {0, 0, 0};
        Type type = new Type("", "");
        return new MInventoryObjectProxy(' ', -1, "", "", null, 0, Color.WHITESMOKE, dims, 0, type, "");
    }


    // --- GETTER ---
    @Override
    public int getId() { return this.id; }
    public char getIdentifier() { return this.identifier; }

    // --- SETTER ---
    public void setId(int id) {
        this.id = id;
    }
    public void setIdentifier(char identifier) { this.identifier = identifier; }
}
