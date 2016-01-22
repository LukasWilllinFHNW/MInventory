package minventory.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Lukas on 25.11.2015.
 */
public class Type {

    /** Type of the object. */
    private StringProperty type = new SimpleStringProperty();
    /** For what the object is used. */
    private StringProperty usageType = new SimpleStringProperty();

    public Type () {

    }
    public Type(String type, String usageType) {
        this.type.setValue(type);
        this.usageType.setValue(usageType);
    }


    // --- GETTER ---
    /** Get definition on how the object is used or what it is used for. */
    public String getUsageType() { return usageType.get(); }
    /** Get the general type identifier of the object. */
    public String getType() { return type.get(); }


    // --- GETTER PROPERTY ---
    /** Get definition on how the object is used or what it is used for. */
    public StringProperty getUsageTypeProperty() { return usageType; }
    /** Get the general type identifier of the object. */
    public StringProperty getTypeProperty() { return type; }


    // --- SETTER ---
    /** @param usageType How the object is used or what it is used for. */
    public void setUsageType(String usageType) {
        if (usageType == null) {
            throw new NullPointerException("Usage type cannot be set to null.");
        }
        this.usageType.setValue(usageType);
    }
    /** @param type A general type identifier of the object. */
    public void setType(String type) {
        if (type == null) {
            throw new NullPointerException("Type cannot be set to null.");
        }
        this.type.setValue(type);
    }
}
