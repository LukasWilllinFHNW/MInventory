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
public class MInventoryUI extends GridPane {

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

    /**The Super Layout Pane Of The UI */
    private GridPane generalGridPane;
        // -- Used Instances Along The Super Pane generalGridPane --
        private ColumnConstraints gccListView;
        private ColumnConstraints gccMainWindow;
        private RowConstraints grcWholePane;
        private ListView<MInventoryObject> listView;


    public MInventoryUI(MInventoryPresentationModel presModel,
                        MInventoryDataModel dataModel){

        this.presModel = presModel;
        this.dataModel = dataModel;

        // -- Initialize The MInventoryUI Window --
        ccUI = new ColumnConstraints();
            ccUI.setHgrow(Priority.ALWAYS);
        this.getColumnConstraints().add(ccUI);
        rcUITopBar = new RowConstraints();
            int barHeight = 40;
            rcUITopBar.setMinHeight(barHeight);
            rcUITopBar.setMaxHeight(barHeight);
        rcUI = new RowConstraints();
            rcUI.setVgrow(Priority.ALWAYS);
            rcUI.setPercentHeight(100);
        this.getRowConstraints().addAll(rcUITopBar, rcUI);
        this.setPadding(new Insets(0, 12, 16, 12));



        // -- Perform Startup Methods --
        initializeControls();
        initializeLayout();
        layoutPanes();
        layoutControls();
        addListeners();
        prepareStyles();
        applySpecialStyles();
    }

    private void initializeControls() {
    }

    private void initializeLayout() {

        detailedViewStage = new Stage();

        // -- Main Window Grid --
        generalGridPane = new GridPane();
        gccListView = new ColumnConstraints();
            gccListView.setPercentWidth(30);
            gccListView.setHgrow(Priority.ALWAYS);
        gccMainWindow = new ColumnConstraints();
            gccMainWindow.setPercentWidth(70);
            gccMainWindow.setHgrow(Priority.ALWAYS);
        grcWholePane = new RowConstraints();
            grcWholePane.setVgrow(Priority.ALWAYS);
        generalGridPane.getColumnConstraints().addAll(gccListView, gccMainWindow);
        generalGridPane.getRowConstraints().add(grcWholePane);

        mInventoryTopBarView = new MInventoryTopBarView(presModel, dataModel);
        mInventoryListView = new MInventoryListView(presModel, dataModel);
        mInventoryDetailedView = new MInventoryDetailedView(presModel, dataModel);
    }

    private void layoutPanes() {

        this.add(mInventoryTopBarView, 0, 0);
        this.generalGridPane.add(mInventoryListView, 0, 0);
        this.generalGridPane.add(mInventoryDetailedView, 1, 0);

        this.add(generalGridPane,0,1);
    }

    private void layoutControls(){

    }

    private void addListeners(){
    }

    private void prepareStyles() {
        this.setId("MInventoryUI");
    }

    private void applySpecialStyles() {

    }

    // get views
    public static MInventoryDetailedView getMInventoryDetailedView() { return mInventoryDetailedView; }
    public static MInventoryListView getMInventoryListView() { return mInventoryListView; }

    // show views
    public static void showMInventoryDetailedView() {

    }

    // hide views





}
