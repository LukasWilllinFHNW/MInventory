package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryPresentationModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
        this.setSpacing(5);
    }

    @Override
    public void layoutPanes() {

    }

    @Override
    public void initializeControls() {

        this.addButton = presModel.createButton("+");
            addButton.disableProperty().bind(presModel.getAddDisabledProperty());
        this.deleteButton = presModel.createButton("-");
            deleteButton.disableProperty().bind(presModel.getAddDisabledProperty());
        this.saveButton = presModel.createButton("save");
            saveButton.disableProperty().bind(presModel.getAddDisabledProperty());
    }

    @Override
    public void layoutControls() {

        this.getChildren().addAll(addButton, deleteButton, saveButton);
    }

    @Override
    public void addEvents() {

        this.addButton.setOnMouseClicked(event -> {
            createNewObjectDialog();
        });

        this.deleteButton.setOnMouseClicked(event -> {
            dataModel.delete(); });

        this.saveButton.setOnMouseClicked(event -> {
            dataModel.save(); });
    }

    @Override
    public void addListeners() {

    }

    @Override
    public void applyStylesheet() {
        this.setId("topBarView");
    }

    private void createNewObjectDialog() {

        presModel.doBlur();

        overlay = new Overlay(presModel, dataModel, 160, 250);

        this.askIfItem = new Button();
            askIfItem.setText("an item");
            //askIfItem.setPadding(new Insets(6, 6, 3, 6));
        this.askIfStorage = new Button();
            askIfStorage.setText("a storage");
            //askIfStorage.setPadding(new Insets(3, 6, 6, 6));

        this.askIfItem.setOnMouseClicked(event -> {
            dataModel.addNewObject('i');
            createInputDialog();
        });
        this.askIfStorage.setOnMouseClicked(event -> {
            dataModel.addNewObject('s');
            createInputDialog();
        });

        HBox box = new HBox();
            box.getChildren().addAll(askIfItem, askIfStorage);
            box.setPadding(new Insets(5));

        overlay.addNode(box);
    }

    private void createInputDialog() {

        overlay.close();

        this.createButton = new Button("create");
        createButton.disableProperty().bind(presModel.getSaveDisabledProperty());
        this.cancelButton = new Button("cancel");
        cancelButton.disableProperty().bind(presModel.getSaveDisabledProperty());

        this.createButton.setOnMouseClicked(event -> {
            presModel.useEditorStyle();
            overlay.close();
            dataModel.createNewObject();
        });
        this.cancelButton.setOnMouseClicked(event -> {
            presModel.useEditorStyle();
            overlay.close();
            dataModel.cancelNewObject();
        });

        HBox box = new HBox();
            box.getChildren().addAll(createButton, cancelButton);
            box.setAlignment(Pos.CENTER);

        overlay = new Overlay(presModel, dataModel, 600, 600);
        presModel.useCreationStyle();
        overlay.addNode(box);
        overlay.addNode(new MInventoryDetailedView(presModel, dataModel));
    }
}
