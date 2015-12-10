package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryObject;
import ch.fhnw.oop2.model.MInventoryStorage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Created by Lukas on 01.12.2015.
 */
public class MInventoryTopBarView extends HBox{

    private Button addButton;

    public MInventoryTopBarView(MInventoryDataModel dataModel){

        // -- Perform Startup Methods --
        initializeControls();
        //initializeLayout();
        //layoutPanes();
        layoutControls();
        addListeners();
        //prepareStyles();
        //applySpecialStyles();
    }

    public void initializeControls() {
        this.addButton = new Button("+");
    }

    public void layoutControls() {
        this.getChildren().add(addButton);
    }

    public void addListeners() {
    }
}
