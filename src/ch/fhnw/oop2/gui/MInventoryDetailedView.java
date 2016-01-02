package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.*;
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
    //private ColumnConstraints ccDetail;

    private RowConstraints rc; int rcAmount;

    private TextField nameField;
    private TextField descriptionField;
    private TextArea descriptionArea;

    public MInventoryDetailedView(MInventoryPresentationModel presModel, MInventoryDataModel dataModel){

        this.dataModel = dataModel;
        this.presModel = presModel;

        cc = new ColumnConstraints(); ccAmount = 6;
            cc.setPercentWidth((100/ccAmount));
            cc.setHgrow(Priority.ALWAYS);
        /*ccDetail = new ColumnConstraints();
            ccDetail.setPercentWidth(100);
            ccDetail.setHgrow(Priority.ALWAYS);*/
        rc = new RowConstraints(); rcAmount = 10;
            rc.setPercentHeight((100/rcAmount));
            rc.setVgrow(Priority.ALWAYS);

        this.setPadding(new Insets(0, 0, 0, 12));

        // -- Perform Startup Methods --
        initSequence();
    }

    @Override
    public void initializeControls() {
        nameField = new TextField();
        descriptionField = new TextField();
        descriptionArea = new TextArea();
    }

    @Override
    public void initializeLayout() {
        for (int i = 0; i < ccAmount; ++i) this.getColumnConstraints().add(cc);
        for (int i = 0; i < rcAmount; ++i) this.getRowConstraints().add(rc);
    }

    public void layoutPanes(){

    }

    public void layoutControls() {

        this.add(new Label("Name"), 0, 0);
        this.add(nameField, 0, 1, 2, 1);
        this.add(new Label("Description"), 0, 2);
        this.add(descriptionArea, 0, 3, 6, 2);
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
}

class StoragePickerOverlay extends ScrollPane implements ViewTemplate {

    private MInventoryDataModel dataModel;
    private MInventoryPresentationModel presModel;

    TilePane storagePane;
    HBox declineOrAccept;

    Button cancel;
    Button accept;

    Overlay pickerOverlay;


    public StoragePickerOverlay(MInventoryPresentationModel presModel, MInventoryDataModel dataModel) {
        this.dataModel = dataModel;
        this.presModel = presModel;

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
        storagePane = new TilePane();
            storagePane.getChildren().addAll(createNodeList(null));
        pickerOverlay = new Overlay(presModel, dataModel, 400, 500);
    }

    @Override
    public void layoutPanes() {
        this.getChildren().add(storagePane);
        pickerOverlay.addNode(this);
        pickerOverlay.addNode(declineOrAccept);
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
            storagePane.getChildren().filtered(node -> node.isFocused()).get(0); });
    }

    @Override
    public void applyStylesheet() {

    }

    @Override
    public void applySpecialStyles() {

    }

    private List<Node> createNodeList(String search) {
        List<Node> nodes = new ArrayList<>();
        List<MInventoryObject> objects = new ArrayList<>();

        if (search != null && !search.isEmpty()) {
            objects = dataModel.getMInventoryObjectProxySimpleListProperty().get().stream()
                    .filter(mInventoryObject -> mInventoryObject instanceof MInventoryStorage)
                    .map(mInventoryObject1 -> dataModel.getById(mInventoryObject1.getId()))
                    .collect(Collectors.toList());

        }
        if (objects != null) {
            for (MInventoryObject object : objects) {
                nodes.add(createTileCell(object));
            }
        }
        if (nodes == null || nodes.isEmpty()) throw new InvalidStateException("Building List failed.");
        return nodes;
    }

    private Node createTileCell(MInventoryObject object) {
        double cellSize = 50;

        StackPane cellStack = new StackPane();
            cellStack.setAlignment(Pos.BOTTOM_CENTER);
            cellStack.setStyle("-fx-background-color: black");
            cellStack.setMaxSize(cellSize, cellSize);
        CustomImageView customImageView = new CustomImageView(presModel, dataModel);
            customImageView.setImage(object.getImage());
        ImageViewPane imageViewPane = new ImageViewPane();

        Label name = new Label(object.getName());

        cellStack.getChildren().addAll(imageViewPane, name);

        return cellStack;
    }

    class cell extends StackPane {

    }
}
