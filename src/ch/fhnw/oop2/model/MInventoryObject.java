package ch.fhnw.oop2.model;

//import java.lang.Exception.*;
import ch.fhnw.oop2.gui.CustomImage;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.lang.*;

/**
 * Created by Lukas W on 17.11.2015.
 */
public abstract class MInventoryObject {

    private final int id;
    private StringProperty name;
    private Image image;
    private ObjectProperty imageProperty = new SimpleObjectProperty<CustomImage>();
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
    public MInventoryObject(int id, String name, String description, CustomImage image) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.imageProperty.setValue(image);
    }

    public MInventoryObject(int id, MInventoryObject object) {
        this.id = id;
        this.name = new SimpleStringProperty(object.getName());
        this.description = new SimpleStringProperty(object.getDescription());
        this.imageProperty.setValue(object.getImage());
    }


    // --- API ---
    protected String infoAsLine(){
        StringBuffer info = new StringBuffer();

        info.append(this.id + ";");
        info.append(this.getName() + ";");
        info.append(this.getDescription() + ";");

        return info.toString();
    }


    // --- GETTER ---
    public String getName(){
        return name.getValue();
    }
    public String getDescription() { return description.getValue(); }
    public Image getImage() { return (Image)imageProperty.getValue(); }
    public int getId() { return id; }

    // --- PROPERTY GETTER ---
    public StringProperty getNameProperty() { return this.name; }
    public StringProperty getDescriptionProperty() { return this.description; }
    public ObjectProperty<CustomImage> getImageProperty() { return this.imageProperty; }

    // --- SETTER ---
    public void setName(String name) {
        this.name.setValue(name);
    }
    public void setDescription(String description) { this.description.setValue(description); }
    public void setSymbolId(int symbolId) { this.symbolId.setValue(symbolId); }


}
