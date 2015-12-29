package ch.fhnw.oop2.model;

import ch.fhnw.oop2.gui.CustomImage;
import javafx.scene.image.Image;

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
    public MInventoryObjectProxy(char identifier,int id, String name, String description, CustomImage image) {
        super(-1, name, description, image);
        this.identifier = identifier;
    }


    // --- API ---
    public void update(int id, String name, String description, int symbolId) {
        this.id = id;
        super.setName(name);
        super.setDescription(description);
        super.setSymbolId(symbolId);
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
