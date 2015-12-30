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
    private StringProperty name = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    /** Weight of object in kilo gram*/
    private DoubleProperty weight = new SimpleDoubleProperty();
    private ObjectProperty<Color> color = new SimpleObjectProperty<>();
    /** Dimensions in meter */
    private DoubleProperty[] dimensions = new SimpleDoubleProperty[3];
    /** Per cent value indicating how damaged the object is. */
    private DoubleProperty stateOfDecay = new SimpleDoubleProperty();;
    /** Type of the object. */
    private Type type;
    /** What makes is it different. */
    private StringProperty distinctAttribute = new SimpleStringProperty();
    private ObjectProperty imageProperty = new SimpleObjectProperty<CustomImage>();



    // --- CONSTRUCTORS ---
    /**
     * Create a new MInventoryObject */
    public MInventoryObject(int id, String name, String description, CustomImage image, double weight, Color color, double[] dimensions,
                            double stateOfDecay, Type type, String distinctAttribute) {
        this.id = id;
        this.name.setValue(name);
        this.description.setValue(description);
        this.imageProperty.setValue(image);
        this.weight.setValue(weight);
        this.color.setValue(color);
        this.dimensions[0] = new SimpleDoubleProperty();
        this.dimensions[1] = new SimpleDoubleProperty();
        this.dimensions[2] = new SimpleDoubleProperty();
        this.dimensions[0].setValue(dimensions[0]);
        this.dimensions[1].setValue(dimensions[1]);
        this.dimensions[2].setValue(dimensions[2]);
        this.stateOfDecay.setValue(stateOfDecay);
        this.type = type;
        this.distinctAttribute.setValue(distinctAttribute);
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
    public double getWeight() { return weight.getValue(); }
    public Color getColor() { return color.getValue(); }
    public double[] getDimensions() {
        double[] dims = {dimensions[0].getValue(), dimensions[1].getValue(), dimensions[2].getValue()};
        return dims; }
    public double getStateOfDecay() { return stateOfDecay.getValue(); }
    public Type getType() { return type; }
    public String getDistinctAttribtue() { return distinctAttribute.getValue(); }


    // --- PROPERTY GETTER ---
    public StringProperty getNameProperty() { return this.name; }
    public StringProperty getDescriptionProperty() { return this.description; }
    public ObjectProperty<CustomImage> getImageProperty() { return this.imageProperty; }


    // --- SETTER ---
    public void setName(String name) {
        this.name.setValue(name);
    }
    public void setDescription(String description) { this.description.setValue(description); }


}
