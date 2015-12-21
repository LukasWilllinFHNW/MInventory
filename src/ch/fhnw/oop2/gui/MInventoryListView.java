package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryObject;
import ch.fhnw.oop2.model.MInventoryPresentationModel;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * Created by Lukas on 01.12.2015.
 */
public class MInventoryListView extends ListView implements ViewTemplate{

    private MInventoryPresentationModel presModel;
    private MInventoryDataModel dataModel;

    private KeyEvent delPressed;

    public MInventoryListView(MInventoryPresentationModel presModel, MInventoryDataModel dataModel){

        this.presModel = presModel;
        this.dataModel = dataModel;

        this.autosize();

        initSequence();
    }

    @Override
    public void initializeControls() {

        this.setCellFactory(lv -> {
            CustomListCell cell = new CustomListCell(presModel, dataModel);
            return cell ; });
    }

    @Override
    public void layoutControls() {
    }


    @Override
    public void addBindings(){
        this.itemsProperty().bind(dataModel.getMInventoryObjectSimpleListProperty());
        this.disableProperty().bind(presModel.getAddDisabledProperty());
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
