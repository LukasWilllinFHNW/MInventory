package ch.fhnw.oop2.main;

import ch.fhnw.oop2.control.MInventoryCmd;
import ch.fhnw.oop2.control.MInventoryController;
import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryPresentationModel;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.stage.Stage;

import ch.fhnw.oop2.gui.MInventoryUI;

// TODO: What is Stage?
//

/**
 * Created by Lukas W on 17.11.2015.
 */
public class MInventoryApp extends Application{

    private MInventoryPresentationModel presModel;
    private MInventoryDataModel dataModel;
    private MInventoryController mInventoryController;
    private MInventoryCmd mInventoryCmd;

    private int startHeight = 600;
    private int startWidth = 800;

    @Override
    public void start(Stage primaryStage) throws Exception{

        dataModel = new MInventoryDataModel();
        presModel = new MInventoryPresentationModel();
        mInventoryController = new MInventoryController(dataModel);
        dataModel.setMInventoryObjectListProperty(mInventoryController.readObjectsFromFile());

        // -- setup the main UI --
        Parent rootPanel = new MInventoryUI(presModel, dataModel);

        IntegerProperty blur = new SimpleIntegerProperty();
            blur.bind(presModel.getBlurProperty());
        BoxBlur bb = new BoxBlur();
            bb.widthProperty().bind(blur);
            bb.heightProperty().bind(blur);
            bb.setIterations(3);

        rootPanel.setEffect(bb);

        // -- prepare scene & add stylesheet --
        Scene scene = new Scene(rootPanel);
        String stylesheet = getClass().getResource("../gui/appStylesheet.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);

        // -- prepare the main window --
        primaryStage.setTitle(presModel.getWindowTitleTextProperty().get());
        primaryStage.setScene(scene);
        primaryStage.setHeight(startHeight);
        primaryStage.setWidth(startWidth);

        primaryStage.xProperty().addListener((observable, oldValue, newValue) -> {
            presModel.setX(newValue.doubleValue());
        });
        primaryStage.yProperty().addListener((observable, oldValue, newValue) -> {
            presModel.setY(newValue.doubleValue());
        });
        presModel.getHeightProperty().setValue(startHeight);
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            presModel.getHeightProperty().setValue(newValue);
        });
        presModel.getWidthProperty().setValue(startWidth);
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            presModel.getWidthProperty().setValue(newValue);
        });

        primaryStage.centerOnScreen();

        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            mInventoryController.writeObjectsToFile();
        });

        mInventoryCmd = new MInventoryCmd();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
