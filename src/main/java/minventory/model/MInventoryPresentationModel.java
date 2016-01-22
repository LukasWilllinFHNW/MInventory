package minventory.model;

import minventory.gui.CustomImage;
import minventory.gui.MInventoryUI;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Lukas W on 03.11.2015.
 */
public class MInventoryPresentationModel {
    
    private MInventoryDataModel dataModel;

    private final BooleanProperty saveDisabledProperty = new SimpleBooleanProperty(true);
    private final BooleanProperty addDisabledProperty = new SimpleBooleanProperty(false);

    private final IntegerProperty blurProperty = new SimpleIntegerProperty();

    public static final MInventoryPresentationModel STANDARD = new MInventoryPresentationModel(true);
    
    
    // general app
    public static final double WINDOW_WIDTH = 665;
    public static final double WINDOW_HEIGHT = 565;
    public static final String WINDOW_TITLE = "MInventory App";
    public static final String FULL_PROFILE_NAME = "Your Inventory";
    public static final Color WINDOW_COLOR = Color.WHITESMOKE;

    private final DoubleProperty SCREEN_SIZE_WIDTH = new SimpleDoubleProperty();
    private final DoubleProperty SCREEN_SIZE_HEIGHT = new SimpleDoubleProperty();
    
    private final IntegerProperty x = new SimpleIntegerProperty();
    private final IntegerProperty y = new SimpleIntegerProperty();

    private final DoubleProperty windowWidth = new SimpleDoubleProperty();
    private final DoubleProperty windowHeight = new SimpleDoubleProperty();
    
    private final StringProperty lastProfile = new SimpleStringProperty();
    
    // profiling
    private final StringProperty windowTitle = new SimpleStringProperty("MInventory App");
    private final ObjectProperty<Image> backgroundImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Color> windowColor = new SimpleObjectProperty<>();
    private final StringProperty fullProfileName = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();


    // --- CONSTRUCTORS ---
    /**
     * Create a standard presentation model.
     */
    public MInventoryPresentationModel(boolean isStandart) {

        if (isStandart) {
            windowHeight.set(WINDOW_HEIGHT);
            windowWidth.set(WINDOW_WIDTH);
            x.set((int) ((SCREEN_SIZE_WIDTH.get()/2) - (WINDOW_WIDTH/2)));
            y.set((int) ((SCREEN_SIZE_HEIGHT.get()/2) - (WINDOW_HEIGHT/2)));
            windowTitle.set(MInventoryPresentationModel.WINDOW_TITLE);
        } else {
            // Leave properties unset
        }
    }


    // --- API ---
    public Label createImmersiveLabel(String text){
        Label label = new Label(text);
        label.setId("immersiveLabel");
        return label;
    }

    public Button createButton(String text) {
        Button button = new Button(text);
        button.setId("styledButton");
        return button;
    }

    public Button createImmersiveButton(String text) {
        Button button = new Button(text);
        button.setId("styledImmersiveButton");
        return button;
    }

    public String getIcon(String name) {
        StringBuffer path = new StringBuffer();
        path.append(
                File.separatorChar + "resources"
                + File.separatorChar + "icons"
                + File.separatorChar + "png" + File.separatorChar);
        path.append(name);
        return path.toString();
    }

    public void useCreationStyle() {
        getSaveDisabledProperty().setValue(false);
        getAddDisabledProperty().setValue(true);
        doBlur();
    }

    public void useEditorStyle() {
        getSaveDisabledProperty().setValue(true);
        getAddDisabledProperty().setValue(false);
        undoBlur();
    }
    
    public String getAppSettingsAsLine() {
    	return new String(x.get() + ";" 
    			+ y.get() + ";" 
    			+ windowWidth.get() + ";" 
    			+ windowHeight.get() + ";" 
    			+ lastProfile + ";"
    			+ "#endOfLine;");
    }
    
    public String getProfileSettingsAsLine() {
    	// make sure we have the acctual amount of objects
    	int objectAmount = dataModel.getMInventoryObjectList().stream()
    			.collect(Collectors.toList()).size();
    	return new String(objectAmount + ";" 
    			+ windowColor.get().getRed() + ":" 
    				+ windowColor.get().getGreen() + ":"
    				+ windowColor.get().getBlue() + ":"
    				+ windowColor.get().getOpacity() + ";" 
    			+ fullProfileName.get() + ";" 
    			+ address.get() + ";"
    			+ "#endOfLine;");
    }

    // --- GETTER ---
    public double getX() { return x.getValue().doubleValue(); }
    public double getY() { return y.getValue().doubleValue(); }

    public double getWindowHeight() {
        // windowHeight should never be smaller than standard
        if (windowHeight.get() < MInventoryPresentationModel.WINDOW_HEIGHT) {
            windowHeight.set(MInventoryPresentationModel.WINDOW_HEIGHT);
            return windowHeight.get();
        } else {
            return windowHeight.get();
        }
    }
    public double getWindowWidth() {
        // windowWidth should never be smaller than standard
        if (windowWidth.get() < MInventoryPresentationModel.WINDOW_WIDTH) {
            windowWidth.set(MInventoryPresentationModel.WINDOW_WIDTH);
            return windowWidth.get();
        } else {
            return windowWidth.get();
        }
    }


    // --- GETTER PROPERTY ---
    /** @deprecated Might be removed in the future */
    public DoubleProperty getHeightProperty() { return windowHeight; }
    /** @deprecated Might be removed in the future */	
    public DoubleProperty getWidthProperty() { return windowWidth; }

    public BooleanProperty getSaveDisabledProperty() { return saveDisabledProperty; }

    public BooleanProperty getAddDisabledProperty() { return addDisabledProperty; }

    public IntegerProperty getBlurProperty() { return blurProperty; }

    public StringProperty getWindowTitleTextProperty() { return this.windowTitle; }


    // --- SETTER ---
    public void setX(double x) { this.x.setValue(x); }
    public void setY(double y) { this.y.setValue(y); }

    public void setWindowWidth(double width) { this.windowWidth.set(width); }
    public void setWindowHeight(double height) { this.windowHeight.set(height); }
    
    public void setWindowTitle(String title) { this.windowTitle.set(title); }
    public void setWindowColor(Color color) { this.windowColor.set(color); }
    public void setBackgroundImage(CustomImage image) { this.backgroundImage.set(image); }
    public void setFullProfileName(String name) { this.fullProfileName.set(name); }
    public void setAddress(String address) { this.address.set(address); } 

    public void doBlur() { this.blurProperty.setValue(80); }
    public void undoBlur() { this.blurProperty.setValue(0); }
    
    public void setDataModel(MInventoryDataModel dataModel) { this.dataModel = dataModel; }
}