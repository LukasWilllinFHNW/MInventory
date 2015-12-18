package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryPresentationModel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by Lukas on 15.12.2015.
 */
public class Overlay extends Stage implements ViewTemplate {

    private MInventoryPresentationModel presModel;
    private MInventoryDataModel dataModel;

    private ColumnConstraints cc;
    private RowConstraints rc;

    private GridPane grid;
    private VBox vbox;
    private Scene scene;


    // --- CONSTRUCTORS ---
    public Overlay(MInventoryPresentationModel presModel, MInventoryDataModel dataModel){

        this.presModel = presModel;
        this.dataModel = dataModel;

        initSequence();

        this.initStyle(StageStyle.TRANSPARENT);
        this.toFront();
        this.show();
    }


    // --- API ---
    public void addNode(Node node) {

        node.opacityProperty().setValue(1f);
        this.vbox.getChildren().add(node);

    }


    // --- UI INIT ---
    @Override
    public void initializeControls() {

    }

    @Override
    public void initializeLayout() {

        presModel.doBlur();

        cc = new ColumnConstraints();
            cc.prefWidthProperty().bind(presModel.getWidthProperty());
        rc = new RowConstraints();
            rc.prefHeightProperty().bind(presModel.getWidthProperty());

        this.setX(presModel.getX());
        this.setY(presModel.getY());

        this.setHeight(presModel.getHeightProperty().getValue());
        this.setWidth(presModel.getWidthProperty().getValue());

        vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);

        grid = new GridPane();
            grid.getColumnConstraints().add(cc);
            grid.getRowConstraints().add(rc);
            grid.setAlignment(Pos.CENTER);

        scene = new Scene(grid);

    }

    @Override
    public void layoutPanes() {

        grid.add(vbox, 0, 0);
        this.setScene(scene);
    }

    @Override
    public void layoutControls() {

    }

    @Override
    public void addEvents() {
        this.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.close();
                presModel.undoBlur();
                presModel.getAddDisabledProperty().setValue(false);
                presModel.getSaveDisabledProperty().setValue(true);
            }
        });
    }

    @Override
    public void applyStylesheet() {
        Rectangle rect = new Rectangle();
            rect.setId("roundedShape");
        vbox.setShape(rect);

        this.opacityProperty().setValue(1f);
        grid.opacityProperty().setValue(1f);
        vbox.opacityProperty().setValue(1f);
    }



}
