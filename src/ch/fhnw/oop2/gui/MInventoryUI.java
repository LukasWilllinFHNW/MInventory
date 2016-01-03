package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;

import ch.fhnw.oop2.model.MInventoryPresentationModel;
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
    private MInventoryFeaturedList vBoxList;

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
        // Right view in split pane
        gridPane = new GridPane();
            gridPane.setMinWidth(400);
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
        // left list in split pane
        vBoxList = new MInventoryFeaturedList(presModel, dataModel, true);
            vBoxList.setMinWidth(240);
            vBoxList.connectToModel();
            vBoxList.setMaxWidth(240);
        mInventoryDetailedView = new MInventoryDetailedView(presModel, dataModel);
    }

    public void layoutPanes() {
        gridPane.add(mInventoryPreviewView,0 ,0);
        gridPane.add(mInventoryDetailedView, 0, 1);

        splitPane.getItems().addAll(vBoxList, gridPane);

        mainPane.setTop(mInventoryTopBarView);
        mainPane.setCenter(splitPane);

        splitPane.setDividerPosition(0, 400);

        this.getChildren().add(mainPane);
    }

    public void layoutControls(){

    }

    public void addListeners(){
        splitPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            vBoxList.setPrefHeight(newValue.doubleValue()); });
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
