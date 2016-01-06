package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryObject;
import ch.fhnw.oop2.model.MInventoryPresentationModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;

/**
 * Created by Lukas on 01.12.2015.
 */
public class MInventoryListView extends ListView implements ViewTemplate{

    private MInventoryPresentationModel presModel;
    private MInventoryDataModel dataModel;

    private final IntegerProperty currentSelectedObjectId;

    private KeyEvent delPressed;


    // --- CONSTRUCTORS ---
    public MInventoryListView(MInventoryPresentationModel presModel, MInventoryDataModel dataModel){

        this.presModel = presModel;
        this.dataModel = dataModel;

        this.currentSelectedObjectId = new SimpleIntegerProperty();

        initSequence();
    }


    // --- API ---
    public int getCurrentSelectedObjectId() {
        return currentSelectedObjectId.get();
    }

    public ListProperty<MInventoryObject> connectToModel() {
        this.itemsProperty().bind(dataModel.getMInventoryObjectListProxy());

        return dataModel.getMInventoryObjectListProxy();
    }

    public ListProperty<MInventoryObject> connectToListProperty(ListProperty<MInventoryObject> otherProxy) {
        this.itemsProperty().bind(otherProxy);
        return otherProxy;
    }
    public IntegerProperty getCurrentSelectedIdProperty() {
        return currentSelectedObjectId;
    }

    // -- init sequence --
    @Override
    public void initializeControls() {

        this.setCellFactory(listView -> {
            CustomListCell cell = new CustomListCell(presModel, dataModel);
            // Track the selected Object
            cell.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue && cell.getItem() != null)
                    currentSelectedObjectId.set(cell.getItem().getId());

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
