package ch.fhnw.oop2.model;

import ch.fhnw.oop2.gui.MInventoryUI;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.File;

/**
 * Created by Lukas W on 03.11.2015.
 */
public class MInventoryPresentationModel {

    private Parent mainUI;

    private final StringProperty windowTitle = new SimpleStringProperty("MInventory App");
    private final StringProperty buttontext = new SimpleStringProperty("thisisabutton");

    private final BooleanProperty saveDisabledProperty = new SimpleBooleanProperty(true);
    private final BooleanProperty addDisabledProperty = new SimpleBooleanProperty(false);

    private final IntegerProperty heightProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty widthProperty = new SimpleIntegerProperty(0);

    private final IntegerProperty blurProperty = new SimpleIntegerProperty(0);

    private static IntegerProperty x = new SimpleIntegerProperty(0);
    private static IntegerProperty y = new SimpleIntegerProperty(0);

    // --- CONSTRUCTORS ---
    /**
     * Create a new MInventoryPresentationModel.
     */
    public MInventoryPresentationModel() {

    }


    // --- API ---
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
        path.append(File.separatorChar + "src"
                + File.separatorChar + "resources"
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


    // --- GETTER ---
    public StringProperty getWindowTitleTextProperty() {
        return this.windowTitle;
    }

    public double getX() { return x.getValue().doubleValue(); }
    public double getY() { return y.getValue().doubleValue(); }

    // --- GETTER PROPERTY ---
    public IntegerProperty getHeightProperty() { return heightProperty; }
    public IntegerProperty getWidthProperty() { return widthProperty; }

    public BooleanProperty getSaveDisabledProperty() { return saveDisabledProperty; }
    public BooleanProperty getAddDisabledProperty() { return addDisabledProperty; }

    public IntegerProperty getBlurProperty() { return blurProperty; }

    // --- SETTER ---
    public void setMainUI(Parent mainUI) {
        this.mainUI = mainUI;
    }

    public void setX(double x) { this.x.setValue(x); }
    public void setY(double y) { this.y.setValue(y); }

    public void doBlur() { this.blurProperty.setValue(80);}
    public void undoBlur() { this.blurProperty.setValue(0);}

}