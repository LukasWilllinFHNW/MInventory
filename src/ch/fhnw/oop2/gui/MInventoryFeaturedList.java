package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryObject;
import ch.fhnw.oop2.model.MInventoryPresentationModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.stream.Collectors;

/**
 * Created by Lukas on 02.01.2016.
 */
class MInventoryFeaturedList extends VBox implements ViewTemplate{

    MInventoryPresentationModel presModel;
    MInventoryDataModel dataModel;

    private HBox hBoxListOptions;
    private HBox hBoxListSearch;
    private MInventoryListView mInventoryListView;

    private Pane pr;

    /** The origin list of filtered list should never be altered*/
    ListProperty<MInventoryObject> originList;
    /** The filtered list should only be filtered*/
    ListProperty<MInventoryObject> filteredList;
    /** The list that can be filtert and altered by search and list view is bound to */
    ListProperty<MInventoryObject> connectedList;

    Button filterByStorage;
    Button filterByItem;
    Button noFilter;

    TextField txtSearch;

    boolean withFilters;

    /**
     * A list with search feature and optional filter options
     * @param presModel the presentation model
     * @param dataModel the data model
     * @param withFilters if filters should be available
     */
    public MInventoryFeaturedList(MInventoryPresentationModel presModel, MInventoryDataModel dataModel,boolean withFilters) {
        this.presModel = presModel;
        this.dataModel = dataModel;

        this.withFilters = withFilters;

        initSequence();
    }


    // --- API ---
    public void connectToModel() {
        originList = dataModel.getMInventoryObjectList();
        connectedList = mInventoryListView.connectToModel();
    }

    public void connectToListProperty(ListProperty<MInventoryObject> list) {
        originList = new SimpleListProperty<>();
        originList.setValue(FXCollections.observableArrayList(list.stream()
                .map(mInventoryObject -> dataModel.getById(mInventoryObject.getId()))
                .collect(Collectors.toList())));
        connectedList = mInventoryListView.connectToListProperty(list);
    }

    public int getCurrentSelectedObjectId() {
        return mInventoryListView.getCurrentSelectedObjectId();
    }


    // --- HELPERS ---
    private void updateOriginList(ListProperty<MInventoryObject> newList) {
        originList = new SimpleListProperty<>();
        originList.setValue(FXCollections.observableArrayList(newList.stream()
                .map(mInventoryObject -> dataModel.getById(mInventoryObject.getId()))
                .collect(Collectors.toList())));
    }


    // -- init sequence --
    @Override
    public void initializeControls() {
        if (withFilters) {
            filterByStorage = presModel.createButton("storages");
            filterByItem = presModel.createButton("items");
            noFilter = presModel.createButton("everything");
        }
        txtSearch = new TextField("");
    }

    @Override
    public void initializeLayout() {
        if (withFilters) {
            hBoxListOptions = new HBox();
            hBoxListOptions.setAlignment(Pos.CENTER);
            hBoxListOptions.setMaxHeight(30);
            hBoxListOptions.setPadding(new Insets(5));
            hBoxListOptions.setSpacing(5);
        }
        hBoxListSearch = new HBox();
        hBoxListSearch.setAlignment(Pos.CENTER);
        hBoxListSearch.setMaxHeight(30);
        hBoxListSearch.setPadding(new Insets(5));
        hBoxListSearch.setSpacing(5);
        pr = new Pane();
        mInventoryListView = new MInventoryListView(presModel, dataModel);

    }

    @Override
    public void layoutPanes() {
        pr.getChildren().add(mInventoryListView);
        pr.setPrefWidth(300);
        if (withFilters)
            this.getChildren().add(hBoxListOptions);
        this.getChildren().addAll(hBoxListSearch, pr);
    }

    @Override
    public void layoutControls() {
        if (withFilters)
            hBoxListOptions.getChildren().addAll(noFilter, filterByStorage, filterByItem);
        hBoxListSearch.getChildren().add(txtSearch);
    }

    @Override
    public void addListeners() {
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) dataModel.searchFor(oldValue, newValue, originList, connectedList);
            else dataModel.noFilter(originList, connectedList);});

        pr.heightProperty().addListener((observable, oldValue, newValue) -> {
            mInventoryListView.setPrefHeight(newValue.doubleValue()); });
        pr.widthProperty().addListener((observable, oldValue, newValue) -> {
            mInventoryListView.setPrefWidth(newValue.doubleValue()); });
        this.heightProperty().addListener((observable, oldValue, newValue) -> {
            pr.setPrefHeight(newValue.doubleValue()); });
    }

    @Override
    public void addEvents() {
        if (withFilters) {
            noFilter.setOnMouseClicked(event -> {
                dataModel.noFilter(dataModel.getMInventoryObjectList(), dataModel.getMInventoryObjectListProxy());
                updateOriginList(dataModel.getMInventoryObjectList());
            });
            filterByStorage.setOnMouseClicked(event -> {
                dataModel.filterByStorage(dataModel.getMInventoryObjectList(), dataModel.getMInventoryObjectListProxy());
                filterByStorage.setGraphic(new ImageView(new CustomImage(
                        new File(presModel.getIcon("link70.png")).toURI().toString(),
                        presModel.getIcon("link70.png"))));
                filterByStorage.setContentDisplay(ContentDisplay.LEFT);
                noFilter.setGraphic(null);
                filterByItem.setGraphic(null);
                updateOriginList(dataModel.getMInventoryObjectListProxy());
            });
            filterByItem.setOnMouseClicked(event -> {
                dataModel.filterByItem(dataModel.getMInventoryObjectList(), dataModel.getMInventoryObjectListProxy());
                filterByItem.setGraphic(new ImageView(new CustomImage(
                        new File(presModel.getIcon("link70.png")).toURI().toString(),
                        presModel.getIcon("link70.png"))));
                filterByStorage.setContentDisplay(ContentDisplay.LEFT);
                filterByStorage.setGraphic(null);
                noFilter.setGraphic(null);
                updateOriginList(dataModel.getMInventoryObjectListProxy());
            });
        }
    }
}
