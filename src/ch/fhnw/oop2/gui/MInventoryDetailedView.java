package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryObject;
import ch.fhnw.oop2.model.MInventoryObjectProxy;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * Created by Lukas on 01.12.2015.
 */
public class MInventoryDetailedView extends GridPane{

    private ColumnConstraints cc;
    private static RowConstraints rcPreview;
    private RowConstraints rcDetail;

    private MInventoryPreviewView mInventoryPreviewView;

    private GridPane grid;
        private RowConstraints rc;

    private TextField nameField;
    private TextField descriptionField;

    public MInventoryDetailedView(MInventoryDataModel dataModel){

        cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
        rcPreview = new RowConstraints();
            rcPreview.setPercentHeight(35);
            rcPreview.setVgrow(Priority.ALWAYS);
        rcDetail = new RowConstraints();
            rcDetail.setPercentHeight(65);
            rcDetail.setVgrow(Priority.ALWAYS);

        this.getColumnConstraints().addAll(cc);
        this.getRowConstraints().addAll(rcPreview, rcDetail);
        this.setPadding(new Insets(0, 0, 0, 12));

        rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS);

        // -- Perform Startup Methods --
        initializeControls();
        initializeLayout(dataModel);
        layoutPanes();
        layoutControls();
        addListeners(dataModel.getProxy());
        applyStylesheet();
        //applySpecialStyles();
    }

    public void initializeControls() {
        nameField = new TextField();
        descriptionField = new TextField();
    }

    public void initializeLayout(MInventoryDataModel dataModel) {
        mInventoryPreviewView = new MInventoryPreviewView(dataModel);
        grid = new GridPane();
            grid.getColumnConstraints().addAll(cc, cc, cc, cc);
            grid.getRowConstraints().addAll(rc, rc, rc, rc, rc);
    }

    public void layoutPanes(){

        add(grid, 0, 1);
        add(mInventoryPreviewView, 0, 0);
    }

    public void layoutControls() {
        grid.add(nameField, 0, 1, 2, 1);
    }

    public void addListeners(MInventoryObjectProxy proxy) {
        if (proxy != null) {
            this.nameField.textProperty().bindBidirectional(proxy.getNameProperty());
            this.descriptionField.textProperty().bindBidirectional(proxy.getDescriptionProperty());
        }
    }

    public void applyStylesheet() {
        grid.setId("detailedViewGrid");
    }
}
