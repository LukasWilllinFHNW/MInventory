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
    /** Dimensions in meter
     * index 0 being the length
     * index 1 being the height
     * index 2 being the depth */
    private DoubleProperty[] dimensions = new SimpleDoubleProperty[3];
    /** Per cent value indicating how damaged the object is. */
    private IntegerProperty stateOfDecay = new SimpleIntegerProperty();;
    /** Type of the object. */
    private Type type;
    /** What makes is it different. */
    private StringProperty distinctAttribute = new SimpleStringProperty();
    private ObjectProperty imageProperty = new SimpleObjectProperty<CustomImage>();
    private StringProperty imageFileExtensions;



    // --- CONSTRUCTORS ---
    /**
     * Create a new MInventoryObject */
    public MInventoryObject(int id, String name, String description, CustomImage image, double weight, Color color, double[] dimensions,
                            int stateOfDecay, Type type, String distinctAttribute) {
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
        this.weight.setValue(object.getWeight());
        this.color.setValue(object.getColor());
        this.dimensions[0] = new SimpleDoubleProperty(object.getLength());
        this.dimensions[1] = new SimpleDoubleProperty(object.getHeight());
        this.dimensions[2] = new SimpleDoubleProperty(object.getDepth());
        this.stateOfDecay.setValue(object.getStateOfDecay());
        this.type = new Type(object.getType().getType(), object.getType().getUsageType());
        this.distinctAttribute.setValue(object.getDistinctAttribute());
    }


    // --- API ---
    protected String infoAsLine(){
        StringBuffer info = new StringBuffer();

        info.append(this.id + ";");
        info.append(this.getName() + ";");
        info.append(this.getDescription() + ";");
        info.append(this.type.getType() + ";");
        info.append(this.type.getUsageType() + ";");
        info.append(this.getImageFileExtension() + ";");
        info.append(this.getWeight() + ";");
        info.append(this.dimensions[0].get() + ":"
                + this.dimensions[1].get() + ":"
                + this.dimensions[2].get() + ";");
        info.append(this.color.get().getRed() + ":"
                + this.color.get().getGreen() + ":"
                + this.color.get().getBlue() + ":"
                + this.color.get().getOpacity() + ";");
        info.append(this.getStateOfDecay() + ";");
        info.append(this.getDistinctAttribute() + ";");
        info.append("#endOfLine;");

        return info.toString();
    }

    protected String searchInfo() {
        StringBuffer info = new StringBuffer();

        info.append(this.getName()
                + this.getDescription()
                + this.type.getType()
                + this.type.getUsageType()
                + this.getDistinctAttribute());

        return info.toString();
    }


    public String getImageFileExtension() {
        if (getImage() != null) {
            return ((CustomImage)imageProperty.get()).getPath()
                    .trim()
                    .substring( ((CustomImage)imageProperty.get()).getPath()
                            .trim()
                            .lastIndexOf('.')
                    );
        } else {
            return "";
        }

    }


    // --- GETTER ---
    public String getName(){return name.get();}
    public String getDescription() { return description.get(); }
    public Image getImage() { return (Image)imageProperty.getValue(); }
    public int getId() { return id; }
    public double getWeight() { return weight.get(); }
    public Color getColor() { return color.get(); }
    public double[] getDimensions() {
        double[] dims = {dimensions[0].get(), dimensions[1].get(), dimensions[2].get()};
        return dims; }
    public double getLength() { return this.getLengthProperty().doubleValue(); }
    public double getHeight() { return this.getHeightProperty().doubleValue(); }
    public double getDepth() { return this.getDepthProperty().doubleValue(); }
    public int getStateOfDecay() { return stateOfDecay.get(); }
    public Type getType() { return type; }
    public String getDistinctAttribute() { return distinctAttribute.get(); }


    // --- PROPERTY GETTER ---
    public StringProperty getNameProperty() { return this.name; }
    public StringProperty getDescriptionProperty() { return this.description; }
    public ObjectProperty<CustomImage> getImageProperty() { return this.imageProperty; }
    public DoubleProperty getWeightProperty() { return this.weight; }
    public ObjectProperty<Color> getColorProperty() { return color; }
    public IntegerProperty getStateOfDecayProperty() { return stateOfDecay; }
    public DoubleProperty getHeightProperty() { return dimensions[1]; }
    public DoubleProperty getLengthProperty() { return dimensions[0]; }
    public DoubleProperty getDepthProperty() { return dimensions[2]; }
    public StringProperty getTypeProperty() { return type.getTypeProperty(); }
    public StringProperty getUsageTypeProperty() { return type.getUsageTypeProperty(); }
    public StringProperty getDistinctAttributeProperty() { return distinctAttribute; }


    // --- SETTER ---
    public void setName(String name) {
        this.name.setValue(name);
    }
    public void setDescription(String description) { this.description.setValue(description); }
    /**
     * set weight in kilogram
     * @param weight in kilogram */
    public void setWeight(double weight) { this.weight.set(weight); }
    public void setColor(Color color) { this.color.set(color); }
    public void setLength(double length) { this.dimensions[0].setValue(length); }
    public void setHeight(double height) { this.dimensions[1].setValue(height); }
    public void setDepth(double depth) { this.dimensions[2].setValue(depth); }
    /**
     * Set dimension by array in the following order
     * 1 being the length
     * 2 being the height
     * 3 being the depth
     * @param dimensions array of length, height, depth */
    public void setDimensions(double[] dimensions) {
        this.dimensions[0].setValue(dimensions[0]);
        this.dimensions[1].setValue(dimensions[1]);
        this.dimensions[2].setValue(dimensions[2]);
    }
    public void setStateOfDecay(int stateOfDecay) { this.stateOfDecay.setValue(stateOfDecay); }
    public void setType(String type) { this.type.setType(type); }
    public void setUsageType(String usageType) { this.type.setUsageType(usageType); }
    public void setDistinctAttribute(String distinctAttribute) { this.distinctAttribute.setValue(distinctAttribute); }
}
