package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.*;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * Created by Lukas on 01.12.2015.
 */
public class MInventoryDetailedView extends GridPane implements ViewTemplate{

    private MInventoryDataModel dataModel;
    private MInventoryPresentationModel presModel;

    private ColumnConstraints cc; int ccAmount;
    //private ColumnConstraints ccDetail;

    private RowConstraints rc; int rcAmount;

    private TextField nameField;
    private TextField descriptionField;
    private TextArea descriptionArea;

    public MInventoryDetailedView(MInventoryPresentationModel presModel, MInventoryDataModel dataModel){

        this.dataModel = dataModel;
        this.presModel = presModel;

        cc = new ColumnConstraints(); ccAmount = 6;
            cc.setPercentWidth((100/ccAmount));
            cc.setHgrow(Priority.ALWAYS);
        /*ccDetail = new ColumnConstraints();
            ccDetail.setPercentWidth(100);
            ccDetail.setHgrow(Priority.ALWAYS);*/
        rc = new RowConstraints(); rcAmount = 10;
            rc.setPercentHeight((100/rcAmount));
            rc.setVgrow(Priority.ALWAYS);

        this.setPadding(new Insets(0, 0, 0, 12));

        // -- Perform Startup Methods --
        initSequence();
    }

    @Override
    public void initializeControls() {
        nameField = new TextField();
        descriptionField = new TextField();
        descriptionArea = new TextArea();
    }

    @Override
    public void initializeLayout() {
        for (int i = 0; i < ccAmount; ++i) this.getColumnConstraints().add(cc);
        for (int i = 0; i < rcAmount; ++i) this.getRowConstraints().add(rc);
    }

    public void layoutPanes(){

    }

    public void layoutControls() {

        this.add(new Label("Name"), 0, 0);
        this.add(nameField, 0, 1, 2, 1);
        this.add(new Label("Description"), 0, 2);
        this.add(descriptionArea, 0, 3, 6, 2);
    }

    @Override
    public void addListeners() {
        MInventoryObjectProxy proxy = dataModel.getProxy();
        if (proxy != null) {
            dataModel.getProxy().getNameProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.contains(";")) this.nameField.textProperty().setValue(newValue); });
            dataModel.getProxy().getDescriptionProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.contains(";")) this.descriptionArea.textProperty().setValue(newValue); });

            this.nameField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.contains(";")) proxy.getNameProperty().setValue(newValue);
                else {this.nameField.setText(oldValue);} });
            this.descriptionArea.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.contains(";")) proxy.getDescriptionProperty().setValue(newValue);
                else {this.descriptionArea.setText(oldValue); }});
        }
    }

    @Override
    public void addBindings() {

    }

    @Override
    public void addEvents() {

    }

    public void applyStylesheet() {
        this.setId("detailedViewGrid");
    }
}
