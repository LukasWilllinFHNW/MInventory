package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;

import ch.fhnw.oop2.model.MInventoryPresentationModel;
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
    private static MInventoryListView mInventoryListView;
    private static MInventoryDetailedView mInventoryDetailedView;

    /** The PresentationModel for main UI */
    private MInventoryPresentationModel presModel;
    private MInventoryDataModel dataModel;

    BorderPane mainPane;

    public MInventoryUI(MInventoryPresentationModel presModel,
                        MInventoryDataModel dataModel){

        mainPane = new BorderPane();

        this.presModel = presModel;
        this.dataModel = dataModel;

        // -- Perform Startup Methods --
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

        mInventoryTopBarView = new MInventoryTopBarView(presModel, dataModel);
        mInventoryListView = new MInventoryListView(presModel, dataModel);
        mInventoryDetailedView = new MInventoryDetailedView(presModel, dataModel);
    }

    public void layoutPanes() {

        mainPane.setTop(mInventoryTopBarView);

        mainPane.setCenter(mInventoryDetailedView);
        mainPane.setLeft(mInventoryListView);

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
