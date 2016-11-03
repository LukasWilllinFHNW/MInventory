package minventory.gui;

import minventory.model.MInventoryDataModel;
import minventory.model.MInventoryPresentationModel;
import minventory.model.UnRedoCtrl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

/**
 * Created by Lukas on 01.12.2015.
 */
public class MInventoryTopBarView extends GridPane implements ViewTemplate{

    HBox hBoxAddDeleteSave;
    HBox hBoxUndoRedo;
    HBox hBoxSettings;

    private ColumnConstraints cc; int ccAmount;
    private RowConstraints rc; int rcAmount;

    private final MInventoryPresentationModel presModel;
    private final MInventoryDataModel dataModel;

    private Button addButton;
    private Button deleteButton;
    private Button saveButton;

    private Button undoButton;
    private Button redoButton;

    private Button createButton;
    private Button cancelButton;

    private Button askIfItem;
    private Button askIfStorage;

    private Overlay overlay;
    private MInventoryDetailedView addView;

    public MInventoryTopBarView(MInventoryPresentationModel presModel, MInventoryDataModel dataModel){

        this.presModel = presModel;
        this.dataModel = dataModel;

        cc = new ColumnConstraints(); ccAmount = 3;
            cc.setPercentWidth((100/ccAmount));
            cc.setHgrow(Priority.ALWAYS);
        rc = new RowConstraints(); rcAmount = 1;
            rc.setPercentHeight((100/rcAmount));
            rc.setVgrow(Priority.ALWAYS);

        initSequence();
    }

    @Override
    public void initializeLayout() {
        for (int i = 0; i < ccAmount; ++i) this.getColumnConstraints().add(cc);
        for (int i = 0; i < rcAmount; ++i) this.getRowConstraints().add(rc);

        hBoxAddDeleteSave = new HBox();
            hBoxAddDeleteSave.setSpacing(6);
            hBoxAddDeleteSave.setAlignment(Pos.CENTER_LEFT);
            hBoxAddDeleteSave.setPadding(new Insets(0, 0, 0, 6));
        hBoxUndoRedo = new HBox();
            hBoxUndoRedo.setSpacing(6);
            hBoxUndoRedo.setAlignment(Pos.CENTER);
            hBoxUndoRedo.setPadding(new Insets(0, 6, 0, 6));
        hBoxSettings = new HBox();
            hBoxSettings.setSpacing(6);
            hBoxSettings.setAlignment(Pos.CENTER_RIGHT);
            hBoxSettings.setPadding(new Insets(0, 6, 0, 0));
    }

    @Override
    public void layoutPanes() {
        this.add(hBoxAddDeleteSave, 0, 0);
        this.add(hBoxUndoRedo, 1, 0);
    }

    @Override
    public void initializeControls() {

        addButton = presModel.createButton("+");
            addButton.disableProperty().bind(presModel.getAddDisabledProperty());
        deleteButton = presModel.createButton("-");
            deleteButton.disableProperty().bind(presModel.getAddDisabledProperty());
        saveButton = presModel.createButton("save");
            saveButton.disableProperty().bind(presModel.getAddDisabledProperty());

        undoButton = presModel.createButton("undo");
            undoButton.disableProperty().bind(presModel.getAddDisabledProperty());
            
        redoButton = presModel.createButton("redo");
            redoButton.disableProperty().bind(presModel.getAddDisabledProperty());
    }

    @Override
    public void layoutControls() {

        hBoxAddDeleteSave.getChildren().addAll(addButton, deleteButton, saveButton);
        hBoxUndoRedo.getChildren().addAll(undoButton, redoButton);
    }

    @Override
    public void addEvents() {

        this.addButton.setOnMouseClicked(event -> {
            createNewObjectDialog();
        });

        this.deleteButton.setOnMouseClicked(event -> {
            dataModel.delete(null); });

        this.saveButton.setOnMouseClicked(event -> {
            dataModel.save(); });

        this.undoButton.setOnMouseClicked(event -> {
            UnRedoCtrl.undo();
        });
        this.redoButton.setOnMouseClicked(event -> {
            UnRedoCtrl.redo();
        });
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
            dataModel.prepareNewObject('i');
            createInputDialog();
        });
        this.askIfStorage.setOnMouseClicked(event -> {
            dataModel.prepareNewObject('s');
            createInputDialog();
        });

        HBox box = new HBox();
            box.getChildren().addAll(askIfItem, askIfStorage);
            box.setPadding(new Insets(5));

        overlay.addNode(box);
    }

    private void createInputDialog() {

        overlay.close();

        MInventoryDetailedView detailedView = new MInventoryDetailedView(presModel, dataModel);

        this.createButton = new Button("create");
        createButton.disableProperty().bind(presModel.getSaveDisabledProperty());
        this.cancelButton = new Button("cancel");
        cancelButton.disableProperty().bind(presModel.getSaveDisabledProperty());

        this.createButton.setOnMouseClicked(event -> {
            if (detailedView.hasSufficientInfo()) {
                presModel.useEditorStyle();
                overlay.close();
                dataModel.createNewObject();
            } else {
                Overlay reminder = new Overlay(presModel, dataModel, 230, 300);
                reminder.addNode(new Label("Please name your creation."));
                reminder.setIsLowerLevel();
                Button closeReminder = presModel.createButton("ok");
                HBox reminderHbox = new HBox();
                    reminderHbox.setPadding(new Insets(6));
                    reminderHbox.setAlignment(Pos.CENTER_RIGHT);
                    reminderHbox.setMaxHeight(30);
                    reminderHbox.getChildren().add(closeReminder);
                closeReminder.setOnMouseClicked(event1 -> reminder.close());
                reminder.addNode(reminderHbox);
            }
        });
        this.cancelButton.setOnMouseClicked(event -> {
            presModel.useEditorStyle();
            overlay.close();
            dataModel.cancelNewObject();
        });

        HBox box = new HBox();
            box.getChildren().addAll(createButton, cancelButton);
            box.setAlignment(Pos.CENTER_RIGHT);
            box.setSpacing(6);
            box.setPadding(new Insets(6));
            box.setMaxHeight(30);

        overlay = new Overlay(presModel, dataModel, 600, 600);
        presModel.useCreationStyle();
        overlay.addNode(detailedView);
        overlay.addNode(box);

    }
}
