package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryPresentationModel;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * Created by Lukas on 01.12.2015.
 */
public class MInventoryTopBarView extends HBox implements ViewTemplate{

    private final MInventoryPresentationModel presModel;
    private final MInventoryDataModel dataModel;

    private Button addButton;
    private Button deleteButton;
    private Button saveButton;

    private Button createButton;
    private Button cancelButton;

    private Button askIfItem;
    private Button askIfStorage;

    private Overlay overlay;
    private MInventoryDetailedView addView;

    public MInventoryTopBarView(MInventoryPresentationModel presModel, MInventoryDataModel dataModel){

        this.presModel = presModel;
        this.dataModel = dataModel;

        initSequence();
    }

    @Override
    public void initializeLayout() {

    }

    @Override
    public void layoutPanes() {

    }

    @Override
    public void initializeControls() {

        this.addButton = new Button("+");
            addButton.disableProperty().bind(presModel.getAddDisabledProperty());
        this.deleteButton = new Button("-");
            deleteButton.disableProperty().bind(presModel.getAddDisabledProperty());
        this.saveButton = new Button("save");
            saveButton.disableProperty().bind(presModel.getAddDisabledProperty());

        this.createButton = new Button("create");
            createButton.disableProperty().bind(presModel.getSaveDisabledProperty());
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

        this.getChildren().addAll(addButton, deleteButton, saveButton);
    }

    @Override
    public void addEvents() {

        this.addButton.setOnMouseClicked(event -> {
            presModel.doBlur();
            overlay = new Overlay(presModel, dataModel, presModel.getHeightProperty().get()-400, presModel.getWidthProperty().get()-400);
            overlay.addNode(askIfItem);
            overlay.addNode(askIfStorage);
        });

        this.deleteButton.setOnMouseClicked(event -> {
            dataModel.delete(); });

        this.saveButton.setOnMouseClicked(event -> {
            dataModel.save(); });

        this.askIfItem.setOnMouseClicked(event -> {
            overlay.close();
            presModel.enterAddingMode();
            dataModel.addNewObject('i');
            overlay = new Overlay(presModel, dataModel, presModel.getHeightProperty().get()-200, presModel.getWidthProperty().get()-200);
            overlay.addNode(createButton);
            overlay.addNode(cancelButton);
            overlay.addNode(new MInventoryDetailedView(presModel, dataModel));
        });
        this.askIfStorage.setOnMouseClicked(event -> {
            overlay.close();
            presModel.enterAddingMode();
            dataModel.addNewObject('s');

            HBox box = new HBox();
            box.getChildren().addAll(createButton, cancelButton);

            overlay = new Overlay(presModel, dataModel, presModel.getHeightProperty().get()-200, presModel.getWidthProperty().get()-200);
            overlay.addNode(box);
            overlay.addNode(new MInventoryDetailedView(presModel, dataModel));
        });

        this.createButton.setOnMouseClicked(event -> {
            presModel.enterEditMode();
            overlay.close();
            dataModel.createNewObject();
        });
        this.cancelButton.setOnMouseClicked(event -> {
            presModel.enterEditMode();
            overlay.close();
            dataModel.cancelNewObject();
        });
    }

    public void addListeners() {

    }
}
