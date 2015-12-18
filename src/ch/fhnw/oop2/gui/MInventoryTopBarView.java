package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryPresentationModel;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Created by Lukas on 01.12.2015.
 */
public class MInventoryTopBarView extends HBox implements ViewTemplate{

    private final MInventoryPresentationModel presModel;
    private final MInventoryDataModel dataModel;

    private Button addButton;
    private Button saveButton;
    private Button cancelButton;

    private Button askIfItem;
    private Button askIfStorage;

    Overlay overlay;

    public MInventoryTopBarView(MInventoryPresentationModel presModel, MInventoryDataModel dataModel){

        this.presModel = presModel;
        this.dataModel = dataModel;

        initSequence();
    }

    @Override
    public void initializeLayout() {

        overlay = new Overlay(presModel, dataModel);
    }

    @Override
    public void layoutPanes() {

    }

    @Override
    public void initializeControls() {

        this.addButton = new Button("+");
             addButton.disableProperty().bind(presModel.getAddDisabledProperty());
        this.saveButton = new Button("save");
             saveButton.disableProperty().bind(presModel.getSaveDisabledProperty());
        this.cancelButton = new Button("cancel");
            cancelButton.disableProperty().bind(presModel.getSaveDisabledProperty());

        this.askIfItem = new Button();
            askIfItem.setText("an item");
            askIfItem.setPadding(new Insets(6, 6, 3, 6));
        this.askIfStorage = new Button();
            askIfStorage.setText("a storage");
            askIfStorage.setPadding(new Insets(3, 6, 6, 6));
    }

    @Override
    public void layoutControls() {

        this.getChildren().addAll(addButton, saveButton, cancelButton);
    }

    @Override
    public void addEvents() {

        this.addButton.setOnMouseClicked(event -> {

            overlay = new Overlay(presModel, dataModel);
            overlay.addNode(askIfItem);
            overlay.addNode(askIfStorage);
        });

        this.askIfItem.setOnMouseClicked(event -> {
            overlay.close();
            enterAddingMode();
            dataModel.addNewObject('i'); });
        this.askIfStorage.setOnMouseClicked(event -> {
            overlay.close();
            enterAddingMode();
            dataModel.addNewObject('s');
        });

        this.saveButton.setOnMouseClicked(event -> {
            enterEditMode();
            dataModel.save();
        });
        this.cancelButton.setOnMouseClicked(event -> {
            enterEditMode();
            dataModel.cancel();
        });
    }

    public void addListeners() {

    }

    private void enterAddingMode() {
        presModel.getSaveDisabledProperty().setValue(false);
        presModel.getAddDisabledProperty().setValue(true);
    }

    private void enterEditMode() {
        presModel.getSaveDisabledProperty().setValue(true);
        presModel.getAddDisabledProperty().setValue(false);
    }
}
