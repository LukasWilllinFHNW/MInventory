package ch.fhnw.oop2.main;

import ch.fhnw.oop2.control.MInventoryCmd;
import ch.fhnw.oop2.control.MInventoryController;
import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryPresentationModel;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    @Override
    public void start(Stage primaryStage) throws Exception{

        mInventoryController = new MInventoryController();
        dataModel = new MInventoryDataModel(mInventoryController.testRead());
        mInventoryController.addDataModel(dataModel);
        presModel = new MInventoryPresentationModel();

        Parent rootPanel = new MInventoryUI(presModel, dataModel);

        // -- prepare scene & add stylesheet --
        Scene scene = new Scene(rootPanel);
        String stylesheet = getClass().getResource("../gui/appStylesheet.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);

        // -- prepare the main window --
        primaryStage.setTitle(presModel.getWindowTitleTextProperty().get());
        primaryStage.setScene(scene);
        primaryStage.setHeight(600);
        primaryStage.setWidth(800);
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
