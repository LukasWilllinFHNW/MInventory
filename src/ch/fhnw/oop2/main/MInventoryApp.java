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

    private int startHeight = 500;
    private int startWidth = 700;

    @Override
    public void start(Stage primaryStage) throws Exception{

        dataModel = new MInventoryDataModel();
        presModel = new MInventoryPresentationModel();
        mInventoryController = new MInventoryController(dataModel);
        dataModel.setMInventoryObjectListProperty(mInventoryController.readObjectsFromFile());
        dataModel.setMInventoryObjectListProxyProperty(mInventoryController.readObjectsFromFile());
        dataModel.setMInventoryController(mInventoryController);

        // -- setup the main UI --
        Parent rootPanel = new MInventoryUI(presModel, dataModel);

        presModel.setMainUI(rootPanel);

        // -- prepare scene & add stylesheet --
        Scene scene = new Scene(rootPanel);
        String stylesheet = getClass().getResource("../gui/appStylesheet.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);

        // -- prepare the main window --
        primaryStage.setTitle(presModel.getWindowTitleTextProperty().get());
        primaryStage.setScene(scene);
        primaryStage.setHeight(startHeight);
        primaryStage.setWidth(startWidth);
        primaryStage.setMinHeight(startHeight);
        primaryStage.setMinWidth(startWidth);

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

        mInventoryCmd = new MInventoryCmd();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// Actual class ends here
// Further down follows an extract from: https://bugs.openjdk.java.net/secure/attachment/40457/Bug.java
















/**
 * This is just to complete documentation for classes MediaViewPane and ImageViewPane
 *
 * bug posted on https://bugs.openjdk.java.net/browse/JDK-8091216
 * creation date: 2012-05-03 08:45
 * last update date: 2015-06-12 15:54
 * accessed on Dec 29, 2015
 */


/*

import javafx.application.Application;
        import javafx.scene.Scene;
        import javafx.scene.control.SplitPane;
        import javafx.scene.control.SplitPaneBuilder;
        import javafx.scene.image.ImageView;
        import javafx.scene.media.Media;
        import javafx.scene.media.MediaPlayer;
        import javafx.scene.media.MediaView;
        import javafx.stage.Stage;


public class Bug extends Application {

    static {
//        System.setProperty("http.proxyHost", "");
//        System.setProperty("http.proxyPort", "");
    }

    @Override public void start(Stage stage) {
        MediaPlayer mediaPlayer = new MediaPlayer(new Media("http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv"));
        mediaPlayer.play();
        ImageView imageView = new ImageView("http://www.oracleimg.com/us/assets/oralogo-small.gif");
        imageView.setPreserveRatio(true);

        SplitPane pane = SplitPaneBuilder.create()
                .items(
                        new ImageViewPane(imageView),
                        new MediaViewPane(new MediaView(mediaPlayer))
                ).build();

        Scene scene = new Scene(pane);

        stage.setScene(scene);
        stage.show();

    }

    /**
     * @param args the command line arguments
     * /
    public static void main(String[] args) {
        Application.launch(args);
    }
} */
