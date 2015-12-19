package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
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
    private static Stage detailedViewStage;

    /** The PresentationModel for main UI */
    private MInventoryPresentationModel presModel;
    private MInventoryDataModel dataModel;

    /** Column Constraint For The InventoryUI */
    private ColumnConstraints ccUI;
    /** Row Constraint For The InventoryUI */
    private RowConstraints rcUI;
    private RowConstraints rcUITopBar;
    private HBox hBoxTopBar;

    BorderPane mainPane;

    /**The Super Layout Pane Of The UI */
    private GridPane generalGridPane;
        // -- Used Instances Along The Super Pane generalGridPane --
        private ColumnConstraints gccListView;
        private ColumnConstraints gccMainWindow;
        private RowConstraints grcWholePane;
        private ListView<MInventoryObject> listView;


    public MInventoryUI(MInventoryPresentationModel presModel,
                        MInventoryDataModel dataModel){

        mainPane = new BorderPane();

        this.presModel = presModel;
        this.dataModel = dataModel;

        // -- Perform Startup Methods --
        initSequence();
    }

    public void initializeControls() {
    }

    public void initializeLayout() {

        detailedViewStage = new Stage();

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

    }

    public void applySpecialStyles() {
        this.setId("MInventoryUI");
    }

    // get views
    public static MInventoryDetailedView getMInventoryDetailedView() { return mInventoryDetailedView; }
    public static MInventoryListView getMInventoryListView() { return mInventoryListView; }

    // show views
    public static void showMInventoryDetailedView() {

    }

    // hide views





}
