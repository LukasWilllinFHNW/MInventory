package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryObject;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

/**
 * Created by Lukas on 01.12.2015.
 */
public class MInventoryListView extends ListView {

    private CustomListCell customListCell;

    public MInventoryListView(MInventoryDataModel dataModel){

        customListCell = new CustomListCell();

        super.setCellFactory(lv -> {
            // create nodes, register listeners on them, populate cellRoot, etc...
            CustomListCell cell = new CustomListCell();
            cell.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null) {
                    cell.updateItem(newItem, false);
                }
            });
            cell.setOnMouseClicked( (MouseEvent event) -> {
                if (cell.getItem() != null) dataModel.updateSelection(cell.getItem().getId());
            });
            cell.setContentDisplay(ContentDisplay.LEFT);

            return cell ;
        });

        // -- Perform Startup Methods --
        initializeControls();
        //initializeLayout();
        //layoutPanes();
        //layoutControls();
        addActionEvents();
        addListeners();
        //applyStylesheet();
        //applySpecialStyles();
    }

    public void initializeControls() {

    }

    public void layoutControls() {

    }

    public void addActionEvents() {
        //onMouseClickedProperty( node -> MInventoryUI.getMInventoryDetailedView().updateControls() );
    }

    public void addListeners() {

    }


}
