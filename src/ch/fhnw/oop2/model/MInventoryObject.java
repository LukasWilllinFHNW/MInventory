package ch.fhnw.oop2.model;

//import java.lang.Exception.*;
import javafx.beans.property.*;
import javafx.css.SimpleStyleableIntegerProperty;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.*;
import java.security.InvalidParameterException;
import java.awt.Color;
import java.io.File;
import java.util.Vector;

/**
 * Created by Lukas W on 17.11.2015.
 */
public abstract class MInventoryObject {

    private final int id;
    private StringProperty name;
    private File picture;
    private IntegerProperty symbolId;
    private StringProperty description;
    /** Weight of object in kilo gram*/
    private DoubleProperty weight;
    private Color color;
    /** Dimensions in meter */
    private DoubleProperty[] dimensions;
    /** Per cent value indicating how damaged the object is. */
    private DoubleProperty stateOfDecay;
    /** Type of the object. */
    private Type type;
    /** What makes is it different. */
    private StringProperty distinctAttribute;


    // --- CONSTRUCTORS ---
    /**
     * Create a new MInventoryObject */
    public MInventoryObject(int id, String name, String description, int symbolId) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.symbolId = new SimpleIntegerProperty(symbolId);
    }

    public MInventoryObject(int id, MInventoryObject object) {
        this.id = id;
        this.name = new SimpleStringProperty(object.getName());
        this.description = new SimpleStringProperty(object.getDescription());
        this.symbolId = new SimpleIntegerProperty(object.getSymbolId());
    }


    // --- API ---
    protected String infoAsLine(){
        StringBuffer info = new StringBuffer();

        info.append(this.id + ";");
        info.append(this.getName() + ";");
        info.append(this.getDescription() + ";");
        info.append(this.getSymbolId() + ";");

        return info.toString();
    }


    // --- GETTER ---
    public String getName(){
        return name.getValue();
    }
    public String getDescription() { return description.getValue(); }
    public int getSymbolId() { return symbolId.getValue(); }
    public int getId() { return id; }

    // --- PROPERTY GETTER ---
    public StringProperty getNameProperty() { return this.name; }
    public StringProperty getDescriptionProperty() { return this.description; }
    public IntegerProperty getSymbolIdProperty() { return this.symbolId; }

    // --- SETTER ---
    public void setName(String name) {
        this.name.setValue(name);
    }
    public void setDescription(String description) { this.description.setValue(description); }
    public void setSymbolId(int symbolId) { this.symbolId.setValue(symbolId); }


}
