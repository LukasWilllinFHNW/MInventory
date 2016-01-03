package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryObject;
import ch.fhnw.oop2.model.MInventoryPresentationModel;
import javafx.beans.property.ListProperty;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;

/**
 * Created by Lukas on 01.12.2015.
 */
public class MInventoryListView extends ListView implements ViewTemplate{

    private MInventoryPresentationModel presModel;
    private MInventoryDataModel dataModel;

    private int currentSelectedObjectId;

    private KeyEvent delPressed;


    // --- CONSTRUCTORS ---
    public MInventoryListView(MInventoryPresentationModel presModel, MInventoryDataModel dataModel){

        this.presModel = presModel;
        this.dataModel = dataModel;

        this.autosize();

        initSequence();
    }


    // --- API ---
    public int getCurrentSelectedObjectId() {
        return currentSelectedObjectId;
    }

    public ListProperty<MInventoryObject> connectToModel() {
        this.itemsProperty().bind(dataModel.getMInventoryObjectListProxy());
        this.disableProperty().bind(presModel.getAddDisabledProperty());

        return dataModel.getMInventoryObjectListProxy();
    }

    public ListProperty<MInventoryObject> connectToListProperty(ListProperty<MInventoryObject> otherProxy) {
        this.itemsProperty().bind(otherProxy);
        return otherProxy;
    }

    // -- init sequence --
    @Override
    public void initializeControls() {

        this.setCellFactory(listView -> {
            CustomListCell cell = new CustomListCell(presModel, dataModel);
            // Track the selected Object
            cell.focusedProperty().addListener((observable, oldValue, newValue) -> {
                currentSelectedObjectId = cell.getItem().getId();
            });
            return cell ; });
    }

    @Override
    public void layoutControls() {
    }


    @Override
    public void addBindings(){

    }

    @Override
    public void addListeners() {

    }

    @Override
    public void initializeLayout() {

    }

    @Override
    public void layoutPanes() {

    }

    @Override
    public void addEvents() {

    }
}
