package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryObject;
import ch.fhnw.oop2.model.MInventoryPresentationModel;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * Created by Lukas on 01.12.2015.
 */
public class MInventoryListView extends VBox implements ViewTemplate{

    private MInventoryPresentationModel presModel;
    private MInventoryDataModel dataModel;

    private ColumnConstraints cc;

    ListView list = new ListView();

    public MInventoryListView(MInventoryPresentationModel presModel, MInventoryDataModel dataModel){

        this.presModel = presModel;
        this.dataModel = dataModel;

        cc = new ColumnConstraints();
            cc.setPercentWidth(30);
            cc.setHgrow(Priority.ALWAYS);

        initSequence();
    }

    @Override
    public void initializeControls() {

        list.setCellFactory(lv -> { CustomListCell cell = new CustomListCell(presModel, dataModel);
            return cell ; });
    }

    @Override
    public void layoutControls() {
        this.getChildren().add(list);
    }


    @Override
    public void addBindings(){
        list.itemsProperty().bind(dataModel.getMInventoryObjectSimpleListProperty());
        this.disableProperty().bind(presModel.getAddDisabledProperty());
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
