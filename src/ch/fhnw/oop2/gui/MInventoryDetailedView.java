package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import sun.plugin.dom.exception.InvalidStateException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

/**
 * Created by Lukas on 01.12.2015.
 */
public class MInventoryDetailedView extends GridPane implements ViewTemplate{

    private MInventoryDataModel dataModel;
    private MInventoryPresentationModel presModel;

    private ColumnConstraints cc; int ccAmount;
    private RowConstraints rc; int rcAmount;


    private TextField nameField;
    private TextField descriptionField;
    private TextArea descriptionArea;
    private TextField weightField;
    private ColorPicker colorPicker;
    private Slider stateOfDecaySlider;
    private TextField stateOfDecayNumberField;
    private ComboBox<String> typePickerEditor;
    private ComboBox<String> usageTypePickerEditor;
    private TextField distinctAttributeField;
    private TextField heightField;
    private TextField depthField;
    private TextField lengthField;

    public MInventoryDetailedView(MInventoryPresentationModel presModel, MInventoryDataModel dataModel){

        this.dataModel = dataModel;
        this.presModel = presModel;

        cc = new ColumnConstraints(); ccAmount = 8;
            cc.setPercentWidth((100/ccAmount));
            cc.setHgrow(Priority.ALWAYS);
        rc = new RowConstraints(); rcAmount = 10;
            rc.setPercentHeight((100/rcAmount));
            rc.setVgrow(Priority.ALWAYS);

        this.setPadding(new Insets(0, 0, 0, 12));
        this.setHgap(5);

        // -- Perform Startup Methods --
        initSequence();
    }

    @Override
    public void initializeControls() {
        nameField = new TextField();
            nameField.setPromptText("Enter a name");
        weightField = new TextField();
            weightField.setPromptText("Enter weight");
        heightField = new TextField();
            heightField.setPromptText("Enter a height");
        depthField = new TextField();
            depthField.setPromptText("Enter a depth");
        lengthField = new TextField();
            lengthField.setPromptText("Enter a length");


        descriptionArea = new TextArea();
            descriptionArea.setPromptText("Describe the object");

        colorPicker = new ColorPicker();

        stateOfDecaySlider = new Slider();
            stateOfDecaySlider.setMin(0);
            stateOfDecaySlider.setMax(100);
        stateOfDecayNumberField = new TextField();

        typePickerEditor = new ComboBox();
            typePickerEditor.setPromptText("Insert type");
            typePickerEditor.setEditable(true);
            typePickerEditor.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue ov, String t, String t1) {
                    //address = t1;
                }
            });
        usageTypePickerEditor = new ComboBox<>();
            usageTypePickerEditor.setPromptText("Insert type");
            usageTypePickerEditor.setEditable(true);
            usageTypePickerEditor.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue ov, String t, String t1) {
                    //address = t1;
                }
            });




    }

    @Override
    public void initializeLayout() {
        for (int i = 0; i < ccAmount; ++i) this.getColumnConstraints().add(cc);
        for (int i = 0; i < rcAmount; ++i) this.getRowConstraints().add(rc);
    }

    @Override
    public void layoutPanes(){

    }

    @Override
    public void layoutControls() {

        this.add(new Label("Name")          , 0, 0);
        this.add(nameField                  , 0, 1, 3, 1);
        this.add(new Label("Type")          , 3, 0);
        this.add(typePickerEditor           , 3, 1, 3, 1);
        this.add(new Label("Color")         , 6, 0, 1, 1);
        this.add(colorPicker                , 6, 1, 1, 1);
        this.add(new Label("Description")   , 0, 2, 3, 1);
        this.add(descriptionArea            , 0, 3, 8, 2);
        this.add(new Label("Weight (kg)")   , 0, 5, 2, 1);
        this.add(weightField                , 0, 6, 2, 1);
        this.add(new Label("Length (m)")    , 2, 5, 2, 1);
        this.add(lengthField                , 2, 6, 2, 1);
        this.add(new Label("Height (m)")    , 4, 5, 2, 1);
        this.add(heightField                , 4, 6, 2, 1);
        this.add(new Label("Depth (m)")     , 6, 5, 2, 1);
        this.add(depthField                 , 6, 6, 2, 1);
        this.add(new Label("State of Decay"), 0, 7, 2, 1);
        this.add(stateOfDecayNumberField    , 2, 8, 1, 1);
        this.add(stateOfDecaySlider         , 0, 8, 2, 1);
        this.add(new Label("Type of use")   , 3, 7, 3, 1);
        this.add(usageTypePickerEditor      , 3, 8, 3, 1);
    }

    @Override
    public void addListeners() {
        MInventoryObjectProxy proxy = dataModel.getProxy();
        if (proxy != null) {
            dataModel.getProxy().getNameProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.contains(";")) this.nameField.textProperty().setValue(newValue); });
            dataModel.getProxy().getDescriptionProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.contains(";")) this.descriptionArea.textProperty().setValue(newValue); });


            this.nameField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.contains(";")) proxy.getNameProperty().setValue(newValue);
                else {this.nameField.setText(oldValue);} });
            this.descriptionArea.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.contains(";")) proxy.getDescriptionProperty().setValue(newValue);
                else {this.descriptionArea.setText(oldValue); }});
        }
    }

    @Override
    public void addBindings() {

    }

    @Override
    public void addEvents() {

    }

    public void applyStylesheet() {

        this.setId("detailedViewGrid");
    }

    @Override
    public void applySpecialStyles() {
    }
}