package ch.fhnw.oop2.model;

import java.security.InvalidParameterException;

/**
 * Created by Lukas on 25.11.2015.
 */
public class Type {

    /** Type of the entity. */
    private String type;
    /** For what the entity is used. */
    private String usageType;
    /** Is a storage? Otherwise it's an item. */
    private boolean isStorage;

    // --- GETTTER ---
    /** Check if object is a storage. */
    public boolean getIsStorage() { return this.isStorage; }
    /** Get definition on how the object is used or what it is used for. */
    public String getUsageType() { return new String(this.usageType.toString()); }
    /** Get the general type identifier of the object. */
    public String getType() { return new String(this.type.toString()); }

    // --- SETTER ---
    /** @param isStorage True if the object is a storage */
    protected void setIsStorage(boolean isStorage) { this.isStorage = isStorage; }
    /** @param usageType How the object is used or what it is used for. */
    public void setUsageType(String usageType) {
        if (usageType == null) {
            throw new NullPointerException("Usage type cannot be set to null.");
        }
        this.usageType = usageType;
    }
    /** @param type A general type identifier of the object. */
    public void setType(String type) {
        if (type == null) {
            throw new NullPointerException("Type cannot be set to null.");
        }
        this.type = type;
    }
}
