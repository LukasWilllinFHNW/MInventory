package minventory.gui;

import minventory.model.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Lukas on 02.12.2015.
 */
public class MInventoryPreviewView extends StackPane implements ViewTemplate{

    private MInventoryObject mInventoryObject;

    private MInventoryPresentationModel presModel;
    private MInventoryDataModel dataModel;

    private ImageView cImageView;
    private ImageViewPane imageViewPane;
    private StackPane root;
    private VBox verticalBox;
    private HBox storagePickerHBox;
    private HBox contentViewHBox;
    private HBox removeFromStorageHBox;
    private GridPane gridPane;
    private Pane backgroundImagePane;
    private Pane backgroundDarkenPane;

    private ColumnConstraints cc; int ccAmount;
    private RowConstraints rc; int rcAmount;

    private Button openStoragePickerButton;
    private Button openContentViewButton;
    private Button removeFromStorageButton;


    public MInventoryPreviewView(MInventoryPresentationModel presModel, MInventoryDataModel dataModel){

        this.presModel = presModel;
        this.dataModel = dataModel;

        cc = new ColumnConstraints(); ccAmount = 8;
            cc.setPercentWidth((100/ccAmount));
            cc.setHgrow(Priority.ALWAYS);
            cc.setHalignment(HPos.RIGHT);
        rc = new RowConstraints(); rcAmount = 5;
            rc.setPercentHeight((100/rcAmount));
            rc.setVgrow(Priority.ALWAYS);

        initSequence();
    }


    // --- API ---
    private void updateColor(Color color) {
        try {
            if(dataModel.getProxy().getImage() == null) {
                Paint fill = color;
                backgroundImagePane.setBackground(new Background(new BackgroundFill(fill, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        } catch(NullPointerException npe) {
            Paint fill = color;
            backgroundImagePane.setBackground(new Background(new BackgroundFill(fill, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    private void updateButton() {

    }


    // -- init sequence --
    @Override
    public void initializeControls() {
        openStoragePickerButton = presModel.createImmersiveButton("pick");
        openContentViewButton = presModel.createImmersiveButton("open");
        removeFromStorageButton = presModel.createImmersiveButton("remove");
    }

    @Override
    public void initializeLayout() {
        backgroundImagePane = new Pane();
        backgroundDarkenPane = new Pane();

        storagePickerHBox = new HBox();
            storagePickerHBox.setSpacing(6);
            storagePickerHBox.setAlignment(Pos.CENTER_LEFT);
            storagePickerHBox.setPadding(new Insets(0, 6, 0, 6));
        contentViewHBox = new HBox();
            contentViewHBox.setSpacing(6);
            contentViewHBox.setAlignment(Pos.CENTER_LEFT);
            contentViewHBox.setPadding(new Insets(0, 6, 0, 6));
        removeFromStorageHBox = new HBox();
            removeFromStorageHBox.setSpacing(6);
            removeFromStorageHBox.setAlignment(Pos.CENTER_LEFT);
            removeFromStorageHBox.setPadding(new Insets(0, 6, 0, 6));

        gridPane = new GridPane();
            gridPane.setAlignment(Pos.CENTER_RIGHT);
        for (int i = 0; i < ccAmount; ++i) gridPane.getColumnConstraints().add(cc);
        for (int i = 0; i < rcAmount; ++i) gridPane.getRowConstraints().add(rc);

        root = new StackPane();
            root.setAlignment(Pos.TOP_CENTER);
        cImageView = new CustomImageView(presModel, dataModel);
            ((CustomImageView) cImageView).connectToModel();
        imageViewPane = new ImageViewPane(cImageView);
        verticalBox = new VBox();
            verticalBox.setEffect(new DropShadow(24d, Color.BLACK));
    }

    @Override
    public void layoutPanes() {
        root.getChildren().add(imageViewPane);
        verticalBox.getChildren().addAll(root);
        verticalBox.setVgrow(root, Priority.ALWAYS);
        verticalBox.setAlignment(Pos.CENTER);
        gridPane.add(verticalBox, 3, 0, 5, 5);
        gridPane.add(storagePickerHBox, 0, 3, 3, 1);

        this.getChildren().addAll(backgroundImagePane, backgroundDarkenPane, gridPane);
    }

    @Override
    public void layoutControls() {
        removeFromStorageHBox.getChildren().addAll(presModel.createImmersiveLabel("Remove from storage"), removeFromStorageButton);
        storagePickerHBox.getChildren().addAll(presModel.createImmersiveLabel("Put into storage"), openStoragePickerButton);
        contentViewHBox.getChildren().addAll(presModel.createImmersiveLabel("Look into"), openContentViewButton);
    }

    @Override
    public void addListeners() {
        // Update background image
        dataModel.getProxy().getImageProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                backgroundImagePane.setBackground(new Background(new BackgroundImage(newValue
                        , BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT
                        , BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            }
        });
        // Add or remove "look into storage"-button when selected object is a storage
        dataModel.getCurrentSelectedIdProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() != -1) {
                MInventoryObject currentObject = (dataModel.getById(newValue.intValue()));

                updateColor(dataModel.getById(newValue.intValue()).getColor());
            }
        });
        // Add or remove "remove from storage"-button on object is contained
        dataModel.getRemoveFromStorageDisabled().addListener((observable1, oldValue1, newValue1) -> {
            if (newValue1) {
                gridPane.getChildren().remove(removeFromStorageHBox);
            } else {
                gridPane.add(removeFromStorageHBox, 0, 4, 3, 1);
            }
        });
        // Add or remove "look into storage"-button on contained object list is empty
        dataModel.getLookIntoStorageDisabled().addListener((observable1, oldValue1, newValue1) -> {
            if (newValue1) {
                gridPane.getChildren().remove(contentViewHBox);
            } else {
                gridPane.add(contentViewHBox, 0, 2, 3, 1);
            }
        });
        // Update color when no image is set
        dataModel.getProxy().getColorProperty().addListener((observable, oldValue, newValue) -> {
            updateColor(newValue);
        });
    }

    @Override
    public void addBindings() {
    }

    @Override
    public void addEvents() {
        // Opens the "put into storage" dialog
        openStoragePickerButton.setOnMouseClicked(event -> {
            StoragePicker storagePicker = new StoragePicker(presModel, dataModel);
            storagePicker.open(); });

        // Opens the "look into storage"-button
        openContentViewButton.setOnMouseClicked(event -> {
            ContentView contentView = new ContentView(presModel, dataModel);
            contentView.open();
        });

        removeFromStorageButton.setOnMouseClicked(event -> {
            dataModel.removeFromStorage(dataModel.getCurrentSelectedObject());
        });
    }

    @Override
    public void applyStylesheet() {

        this.setId("PreviewView");
    }

    @Override
    public void applySpecialStyles() {
        backgroundImagePane.setEffect(new GaussianBlur(30));
        backgroundDarkenPane.setStyle("-fx-background-color: rgba(30, 30, 30, 0.3);");
        gridPane.setPadding(new Insets(6));
    }
}

class ContentView implements ViewTemplate {

    private MInventoryDataModel dataModel;
    private MInventoryPresentationModel presModel;

    HBox closeHBox;

    MInventoryFeaturedList featuredList;

    ListProperty<MInventoryObject> contentList;

    Button close;

    Overlay contentViewOverlay;

    int currentId;


    public ContentView(MInventoryPresentationModel presModel, MInventoryDataModel dataModel) {
        this.dataModel = dataModel;
        this.presModel = presModel;

        currentId = dataModel.getCurrentSelectedId();

        contentList = new SimpleListProperty<>();
        if(dataModel.getProxy().getIdentifier() == 's') {
            MInventoryStorage storage = (MInventoryStorage) dataModel.getById(dataModel.getProxy().getId());
            List<Integer> ids = storage.getContainedObjectIds();
            contentList.setValue(FXCollections.observableArrayList(ids.stream()
                            .map(integer -> dataModel.getById(integer))
                            .collect(Collectors.toList())));
        }

        initSequence();
    }

    @Override
    public void initializeControls() {
        close = presModel.createButton("close");
    }

    @Override
    public void initializeLayout() {
        closeHBox = new HBox();
            closeHBox.setPadding(new Insets(6));
            closeHBox.setAlignment(Pos.BOTTOM_RIGHT);

        featuredList = new MInventoryFeaturedList(presModel, dataModel, false);
            featuredList.connectToListProperty(contentList);
    }

    @Override
    public void layoutPanes() {
    }

    @Override
    public void layoutControls() {
        closeHBox.getChildren().add(close);
    }

    @Override
    public void addListeners() {
    }

    @Override
    public void addEvents() {
        close.setOnMouseClicked(event -> {
            contentViewOverlay.close();
        });
    }

    @Override
    public void applyStylesheet() {

    }

    @Override
    public void applySpecialStyles() {

    }

    public void open() {
        contentViewOverlay = new Overlay(presModel, dataModel, 650, 500);
            contentViewOverlay.addNode(featuredList);
            contentViewOverlay.addNode(closeHBox);
            contentViewOverlay.open();
    }
}


class StoragePicker implements ViewTemplate {

    private MInventoryDataModel dataModel;
    private MInventoryPresentationModel presModel;

    TilePane storagePane;
    HBox declineOrAccept;

    MInventoryFeaturedList featuredList;

    ListProperty<MInventoryObject> storageList;

    Button cancel;
    Button accept;

    Overlay pickerOverlay;

    private final int currentId;


    public StoragePicker(MInventoryPresentationModel presModel, MInventoryDataModel dataModel) {
        this.dataModel = dataModel;
        this.presModel = presModel;

        currentId = dataModel.getCurrentSelectedId();

        storageList = new SimpleListProperty<>();
        dataModel.filterByStorage(dataModel.getMInventoryObjectList(), storageList);
        storageList.remove(dataModel.getById(dataModel.getCurrentSelectedId()));

        initSequence();
    }

    @Override
    public void initializeControls() {
        cancel = presModel.createButton("cancel");
        accept = presModel.createButton("accept");
    }

    @Override
    public void initializeLayout() {
        declineOrAccept = new HBox();
        declineOrAccept.setSpacing(12);
        declineOrAccept.setPadding(new Insets(6));
        featuredList = new MInventoryFeaturedList(presModel, dataModel, false);
        featuredList.connectToListProperty(storageList);
    }

    @Override
    public void layoutPanes() {

    }

    @Override
    public void layoutControls() {
        declineOrAccept.getChildren().addAll(cancel, accept);
    }

    @Override
    public void addListeners() {

    }

    @Override
    public void addEvents() {
        accept.setOnMouseClicked(event -> {
            int storageId = featuredList.getCurrentSelectedObjectId();
            dataModel.putObjectIntoStorage(((MInventoryStorage)dataModel.getById(storageId))
                    , dataModel.getById(currentId));
            pickerOverlay.close(); });

        cancel.setOnMouseClicked(event -> {
            pickerOverlay.close();
        });
    }

    @Override
    public void applyStylesheet() {

    }

    @Override
    public void applySpecialStyles() {

    }

    public void open() {
        pickerOverlay = new Overlay(presModel, dataModel, 650, 500);
        pickerOverlay.addNode(featuredList);
        pickerOverlay.addNode(declineOrAccept);
        pickerOverlay.open();
    }
}
