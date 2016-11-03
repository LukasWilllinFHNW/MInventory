package minventory.model;

//import java.lang.Exception.*;
import minventory.gui.CustomImage;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by Lukas W on 17.11.2015.
 */
public abstract class MInventoryObject {
    
    public enum PropIndex {        
        NAME (0),
        DESCRIPTION (1),
        WEIGHT (2),
        COLOR (3),
        //DIMENSIONS (4),
        STATE_OF_DECAY (4),
        DISTINCT_ATTRIBUTES (5),
        IMAGE_PROPERTY (6),
        IMAGE_FILE_EXTENSION (7),
        //TYPE (8),
        LENGTH (8),
        HEIGHT (9),
        DEPTH (10),
        INITIAL_SIZE (20);
        
        private int index;
        private PropIndex(int index) { this.index = index; }
        
        public int get() { return index; }
    };
    
    private final ArrayList<Property> propertyList;
    private final int id;
    private Type type;
    
    {
        propertyList = new ArrayList<>(PropIndex.INITIAL_SIZE.get());
    }

    // --- CONSTRUCTORS ---
    /** Create a new MInventoryObject */
    public MInventoryObject(int id, String name, String description, Image image,double weight, Color color,
                                       double[] dimensions, int stateOfDecay, Type type, String distinctAttribute) {
        this.id = id;
        this.type = type;
        
        this.initializePropertyList(id, name, description, image, weight, color, dimensions, stateOfDecay, type, distinctAttribute);
    }

    public MInventoryObject(int id, MInventoryObject object) {
        this.id = id;
        this.type = new Type(object.getType().getType(), object.getType().getUsageType());
        
        this.initializePropertyList(object.getId(), object.getName(), object.getDescription(), object.getImage(), object.getWeight(), object.getColor(), object.getDimensions(), object.getStateOfDecay(), object.getType(), object.getDistinctAttribute());
    }
    
    private void initializePropertyList(int id, String name, String description, Image image, 
            double weight, Color color, double[] dimensions, int stateOfDecay, Type type, String distinctAttribute) {

        propertyList.ensureCapacity(PropIndex.INITIAL_SIZE.get());
        propertyList.add(PropIndex.NAME.get(), new SimpleStringProperty());
        propertyList.add(PropIndex.DESCRIPTION.get(), new SimpleStringProperty());
        propertyList.add(PropIndex.WEIGHT.get(), new SimpleDoubleProperty());
        propertyList.add(PropIndex.COLOR.get(), new SimpleObjectProperty<>());
        propertyList.add(PropIndex.STATE_OF_DECAY.get(), new SimpleIntegerProperty());
        propertyList.add(PropIndex.DISTINCT_ATTRIBUTES.get(), new SimpleStringProperty());
        propertyList.add(PropIndex.IMAGE_PROPERTY.get(), new SimpleObjectProperty<CustomImage>());
        propertyList.add(PropIndex.IMAGE_FILE_EXTENSION.get(), new SimpleStringProperty());
        propertyList.add(PropIndex.LENGTH.get(), new SimpleDoubleProperty());
        propertyList.add(PropIndex.HEIGHT.get(), new SimpleDoubleProperty());
        propertyList.add(PropIndex.DEPTH.get(), new SimpleDoubleProperty());
        
        propertyList.get(PropIndex.NAME.get()).setValue(name);
        propertyList.get(PropIndex.DESCRIPTION.get()).setValue(description);
        propertyList.get(PropIndex.IMAGE_PROPERTY.get()).setValue(image);
        propertyList.get(PropIndex.WEIGHT.get()).setValue(weight);
        propertyList.get(PropIndex.STATE_OF_DECAY.get()).setValue(stateOfDecay);
        propertyList.get(PropIndex.DISTINCT_ATTRIBUTES.get()).setValue(distinctAttribute);
        propertyList.get(PropIndex.HEIGHT.get()).setValue(dimensions[1]);
        propertyList.get(PropIndex.LENGTH.get()).setValue(dimensions[0]);
        propertyList.get(PropIndex.DEPTH.get()).setValue(dimensions[2]);
        propertyList.get(PropIndex.COLOR.get()).setValue(color);
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
        info.append(this.getLength() + ":"
                + this.getHeight() + ":"
                + this.getDepth() + ";");
        info.append(this.getColor().getRed() + ":"
                + this.getColor().getGreen() + ":"
                + this.getColor().getBlue() + ":"
                + this.getColor().getOpacity() + ";");
        info.append(this.getStateOfDecay() + ";");
        info.append(this.getDistinctAttribute() + ";");

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
            return ((CustomImage)this.getImage()).getPath()
                    .trim()
                    .substring( ((CustomImage)this.getImage()).getPath()
                            .trim()
                            .lastIndexOf('.')
                    );
        } else {
            return "";
        }

    }


    // --- GETTER ---
    public String getName(){return this.getNameProperty().get();}
    public String getDescription() { return this.getDescriptionProperty().get(); }
    public Image getImage() { return (Image)this.getImageProperty().getValue(); }
    public int getId() { return id; }
    public double getWeight() { return this.getWeightProperty().get(); }
    public Color getColor() { return this.getColorProperty().get(); }
    public double[] getDimensions() {
        double[] dims = {
                (double)propertyList.get(PropIndex.LENGTH.get()).getValue(),
                (double)propertyList.get(PropIndex.HEIGHT.get()).getValue(), 
                (double)propertyList.get(PropIndex.DEPTH.get()).getValue()};
        return dims; }
    public double getLength() { return this.getLengthProperty().doubleValue(); }
    public double getHeight() { return this.getHeightProperty().doubleValue(); }
    public double getDepth() { return this.getDepthProperty().doubleValue(); }
    public int getStateOfDecay() { return this.getStateOfDecayProperty().get(); }
    public Type getType() { return type; }
    public String getDistinctAttribute() { return this.getDistinctAttributeProperty().get(); }


    // --- PROPERTY GETTER ---
    public StringProperty getNameProperty() { return (StringProperty)this.propertyList.get(PropIndex.NAME.get()); }
    public StringProperty getDescriptionProperty() { return (StringProperty)this.propertyList.get(PropIndex.DESCRIPTION.get()); }
    public ObjectProperty<CustomImage> getImageProperty() { return (ObjectProperty<CustomImage>) this.propertyList.get(PropIndex.IMAGE_PROPERTY.get()); }
    public DoubleProperty getWeightProperty() { return (DoubleProperty) this.propertyList.get(PropIndex.WEIGHT.get()); }
    public ObjectProperty<Color> getColorProperty() { return (ObjectProperty<Color>) this.propertyList.get(PropIndex.COLOR.get()); }
    public IntegerProperty getStateOfDecayProperty() { return (IntegerProperty)propertyList.get(PropIndex.STATE_OF_DECAY.get()); }
    public DoubleProperty getHeightProperty() { return (DoubleProperty) this.propertyList.get(PropIndex.HEIGHT.get()); }
    public DoubleProperty getLengthProperty() { return (DoubleProperty)propertyList.get(PropIndex.LENGTH.get()); }
    public DoubleProperty getDepthProperty() { return (DoubleProperty) propertyList.get(PropIndex.DEPTH.get()); }
    public StringProperty getTypeProperty() { return type.getTypeProperty(); }
    public StringProperty getUsageTypeProperty() { return type.getUsageTypeProperty(); }
    public StringProperty getDistinctAttributeProperty() { return (StringProperty)propertyList.get(PropIndex.DISTINCT_ATTRIBUTES.get()); }
    
    public ArrayList<Property> getPropertyList() {
        return propertyList;
    }


    // --- SETTER ---
    public void setName(String name) {
        this.propertyList.get(PropIndex.NAME.get()).setValue(name);
    }
    public void setDescription(String description) { 
        this.propertyList.get(PropIndex.DESCRIPTION.get()).setValue(description); 
    }
    /**
     * set weight in kilogram
     * @param weight in kilogram */
    public void setWeight(double weight) { 
        this.propertyList.get(PropIndex.WEIGHT.get()).setValue(weight); }
    public void setColor(Color color) { this.propertyList.get(PropIndex.COLOR.get()).setValue(color); }
    public void setLength(double length) { this.propertyList.get(PropIndex.LENGTH.get()).setValue(length); }
    public void setHeight(double height) { this.propertyList.get(PropIndex.HEIGHT.get()).setValue(height); }
    public void setDepth(double depth) { this.propertyList.get(PropIndex.DEPTH.get()).setValue(depth); }
    /**
     * Set dimension by array in the following order
     * 0 being the length
     * 1 being the height
     * 2 being the depth
     * @param dimensions an array of length, height, depth */
    public void setDimensions(double[] dimensions) {
        this.propertyList.get(PropIndex.LENGTH.get()).setValue(dimensions[0]);
        this.propertyList.get(PropIndex.HEIGHT.get()).setValue(dimensions[1]);
        this.propertyList.get(PropIndex.DEPTH.get()).setValue(dimensions[2]);
    }
    public void setStateOfDecay(int stateOfDecay) { this.propertyList.get(PropIndex.STATE_OF_DECAY.get()).setValue(stateOfDecay); }
    public void setType(String type) { this.type.setType(type); }
    public void setUsageType(String usageType) { this.type.setUsageType(usageType); }
    public void setDistinctAttribute(String distinctAttribute) { this.propertyList.get(PropIndex.DISTINCT_ATTRIBUTES.get()).setValue(distinctAttribute); }
}
