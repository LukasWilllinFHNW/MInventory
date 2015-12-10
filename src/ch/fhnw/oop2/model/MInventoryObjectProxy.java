package ch.fhnw.oop2.model;

/**
 * Created by Lukas on 08.12.2015.
 */
public class MInventoryObjectProxy extends MInventoryObject {

    private int id;

    // TODO: Add symbol instead of id or both


    /**
     * Create a new MInventoryObject
     *
     * @param name name of proxyObject
     * @param description description of proxyObject
     * @param symbolId id of symbol of proxyObject
     */
    public MInventoryObjectProxy(int id, String name, String description, int symbolId) {
        super(-1, name, description, symbolId);
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

    // --- SETTER ---
    public void setId(int id) {
        this.id = id;
    }
}
