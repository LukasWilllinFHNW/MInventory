package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;

import ch.fhnw.oop2.model.MInventoryPresentationModel;
import javafx.scene.text.Text;
import javafx.stage.Stage;
// TODO: Add button symbols
// TODO: Add tile alternative for listView
// TODO: Make basic layout
// TODO: Make basic animations
// TODO: Make extensive styling
// TODO: Make extensive animations

/**
 * Created by Lukas W on 20.10.2015.
 */
public class MInventoryUI extends StackPane implements ViewTemplate{

    // List of all sub views
    private MInventoryTopBarView mInventoryTopBarView;
    private MInventoryPreviewView mInventoryPreviewView;
    private MInventoryDetailedView mInventoryDetailedView;
    private VBoxList vBoxList;

    private BorderPane mainPane;
    private SplitPane splitPane;
    private GridPane gridPane;
        private RowConstraints rcPreview;
        private RowConstraints rcDetail;
        private ColumnConstraints cc;


    /** The PresentationModel for main UI */
    private MInventoryPresentationModel presModel;
    private MInventoryDataModel dataModel;


    public MInventoryUI(MInventoryPresentationModel presModel, MInventoryDataModel dataModel){

        this.presModel = presModel;
        this.dataModel = dataModel;

        initSequence();
    }

    // show views

    // -- add and show views --
    public void addNode(Node node) {
        this.getChildren().add(node);
        System.out.println();
    }

    public void removeNode(Node node) {
        this.getChildren().remove(node);
    }

    // hide views


    // --- INIT SEQUENCE ---
    public void initializeControls() {

    }

    public void initializeLayout() {
        mainPane = new BorderPane();
        mInventoryTopBarView = new MInventoryTopBarView(presModel, dataModel);
        mInventoryPreviewView = new MInventoryPreviewView(presModel, dataModel);
        splitPane = new SplitPane();
        gridPane = new GridPane();
            rcPreview = new RowConstraints();
                rcPreview.setPercentHeight(35);
                rcPreview.setVgrow(Priority.ALWAYS);
            rcDetail = new RowConstraints();
                rcDetail.setPercentHeight(65);
                rcDetail.setVgrow(Priority.ALWAYS);
            cc = new ColumnConstraints();
                cc.setPercentWidth(100);
                cc.setHgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().addAll(rcPreview, rcDetail);
            gridPane.getColumnConstraints().add(cc);
        vBoxList = new VBoxList(presModel, dataModel);
        mInventoryDetailedView = new MInventoryDetailedView(presModel, dataModel);
    }

    public void layoutPanes() {
        gridPane.add(mInventoryPreviewView,0 ,0);
        gridPane.add(mInventoryDetailedView, 0, 1);

        splitPane.getItems().addAll(vBoxList, gridPane);

        mainPane.setTop(mInventoryTopBarView);
        mainPane.setCenter(splitPane);

        this.getChildren().add(mainPane);
    }

    public void layoutControls(){

    }

    public void addListeners(){

    }

    @Override
    public void addEvents() {

    }

    @Override
    public void applyStylesheet() {
        this.setId("MInventoryUI");
    }

    @Override
    public void applySpecialStyles() {
        IntegerProperty blur = new SimpleIntegerProperty();
        blur.bind(presModel.getBlurProperty());
        BoxBlur bb = new BoxBlur();
        bb.widthProperty().bind(blur);
        bb.heightProperty().bind(blur);
        bb.setIterations(5);

        mainPane.setEffect(bb);
    }
}

class VBoxList extends VBox implements ViewTemplate{

    MInventoryPresentationModel presModel;
    MInventoryDataModel dataModel;

    private HBox hBoxListOptions;
    private HBox hBoxListSearch;
    private MInventoryListView mInventoryListView;

    Button filterByStorage;
    Button filterByItem;
    Button noFilter;

    TextField txtSearch;

    public VBoxList(MInventoryPresentationModel presModel, MInventoryDataModel dataModel) {
        this.presModel = presModel;
        this.dataModel = dataModel;

        initSequence();
    }

    @Override
    public void initializeControls() {
        filterByStorage = new Button("storages");
        filterByItem = new Button("items");
        noFilter = new Button("everything");

        txtSearch = new TextField("");
    }

    @Override
    public void initializeLayout() {
        hBoxListOptions = new HBox();
            hBoxListOptions.setAlignment(Pos.CENTER_LEFT);
            hBoxListOptions.setPrefHeight(20);
        hBoxListSearch = new HBox();
            hBoxListSearch.setAlignment(Pos.CENTER_LEFT);
            hBoxListSearch.setPrefHeight(20);

        mInventoryListView = new MInventoryListView(presModel, dataModel);

    }

    @Override
    public void layoutPanes() {
        this.getChildren().addAll(hBoxListOptions, hBoxListSearch, mInventoryListView);
    }

    @Override
    public void layoutControls() {
        hBoxListOptions.getChildren().addAll(noFilter, filterByStorage, filterByItem);
        hBoxListSearch.getChildren().add(txtSearch);
    }

    @Override
    public void addListeners() {
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) dataModel.searchFor(newValue);
            else dataModel.noFilter();});
    }

    @Override
    public void addEvents() {
        noFilter.setOnMouseClicked(event -> {
            dataModel.noFilter(); });
        filterByStorage.setOnMouseClicked(event -> {
            dataModel.filterByStorage(); });
        filterByItem.setOnMouseClicked(event -> {
            dataModel.filterByItem(); });
    }
}
