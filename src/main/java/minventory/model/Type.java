package minventory.model;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Lukas on 25.11.2015.
 */
public class Type extends SimpleObjectProperty<Object>{

    /** Type of the object. */
    private StringProperty type = new SimpleStringProperty();
    /** For what the object is used. */
    private StringProperty usageType = new SimpleStringProperty();

    
    // --- CONSTRUCTOR ---
    public Type () { }
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
    
    @Override
    public boolean isBound() {
        return false;
    }
    @Override
    public Object getBean() {
        return super.getBean();
    }
    @Override
    public String getName() {
        return super.getName();
    }
    @Override
    public void addListener(ChangeListener arg0) {
        type.addListener(arg0);
        usageType.addListener(arg0);
    }
    @Override
    public void removeListener(ChangeListener arg0) {
        type.removeListener(arg0);
        usageType.removeListener(arg0);
    }
    @Override
    public void addListener(InvalidationListener arg0) {
        type.addListener(arg0);
        usageType.addListener(arg0);
    }
    @Override
    public void removeListener(InvalidationListener arg0) {
        type.removeListener(arg0);
        usageType.removeListener(arg0);
    }
    @Override
    /**
     * Concatenates values from both properties to a single string
     * @return string type.get + usageType.get
     */
    public Object get() {
        return type.getValue()+";"+usageType.getValue();
    }
    
    // Not implemented Property functions from this class
    @Override
    public void set(Object arg0) {
        throw new NotImplementedException();
    }
    @Override
    public void bind(ObservableValue arg0) {
        throw new NotImplementedException();
    }
    @Override
    public void unbind() {
        throw new NotImplementedException();
    }
}
