package minventory;

import minventory.control.MInventoryFilesController;
import minventory.model.MInventoryDataModel;
import minventory.model.MInventoryPresentationModel;
import minventory.gui.MInventoryUI;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 * Created by Lukas W on 17.11.2015.
 */
public class MInventoryApp extends Application{

    Stage primaryStage;

    private MInventoryPresentationModel presModel;
    private MInventoryDataModel dataModel;
    private MInventoryFilesController mInventoryFilesController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println(System.getProperties());

        this.primaryStage = primaryStage;

        // Create empty models to use "create node" methods
        dataModel = new MInventoryDataModel();

        // Create Controller to get app settings
        mInventoryFilesController = new MInventoryFilesController(dataModel);


        presModel = mInventoryFilesController.readAppSettingsFromFile();

        // -- prepare the main window --
        primaryStage.setTitle(presModel.getWindowTitleTextProperty().get());
        primaryStage.setHeight(presModel.getWindowHeight());
        primaryStage.setWidth(presModel.getWindowWidth());
        primaryStage.setMinHeight(MInventoryPresentationModel.WINDOW_HEIGHT);
        primaryStage.setMinWidth(MInventoryPresentationModel.WINDOW_WIDTH);
        // load position
        primaryStage.setX(presModel.getX());
        primaryStage.setY(presModel.getY());

        // -- prepare scene & add stylesheet --
        MInventoryUI rootPanel = new MInventoryUI(presModel, dataModel);
        Parent root = rootPanel;
        Scene scene = new Scene(root);
        String stylesheet = getClass().getResource("gui/appStylesheet.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);

        primaryStage.setScene(scene);
                
        ProgressBar loaderBar = new ProgressBar();
        	mInventoryFilesController.progressInPercent.addListener((observable, oldValue, newValue) -> {
        		loaderBar.progressProperty().set(newValue.doubleValue());
        	});

        VBox loaderPane = new VBox();
        	loaderPane.setAlignment(Pos.CENTER);
        	loaderPane.getChildren().add(loaderBar);
        	loaderPane.setBackground(new Background(new BackgroundFill(new Color(0.3, 0.3, 0.3, 0d), null, null)));
        rootPanel.addNode(loaderPane);
        
        primaryStage.show();

        // Read Files
        dataModel.setMInventoryObjectListProperty(mInventoryFilesController.readObjectsFromFile());
        dataModel.setMInventoryController(mInventoryFilesController);

        trackWindow();

        // -- setup the main UI --
        ((MInventoryUI)rootPanel).initSequence();

        // Set no filter
        dataModel.noFilter(dataModel.getMInventoryObjectList(), dataModel.getMInventoryObjectListProxy());
        // Select the first object
        if (!dataModel.getMInventoryObjectList().isEmpty()) dataModel.updateSelection(dataModel.getMInventoryObjectList().get(0));
        
        addListeners();
    }

    public static void main(String[] args) {

        launch(args);
    }


    /**
     * Requires presentationModel
     */
    private void trackWindow() {
        //Track position of window
        primaryStage.xProperty().addListener((observable, oldValue, newValue) -> {
            presModel.setX(newValue.doubleValue());
        });
        primaryStage.yProperty().addListener((observable, oldValue, newValue) -> {
            presModel.setY(newValue.doubleValue());
        });
        // Track size of window
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            presModel.getHeightProperty().setValue(newValue);
        });
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            presModel.getWidthProperty().setValue(newValue);
        });
    }
    
    private void addListeners() {
    	primaryStage.setOnCloseRequest(event -> {
    		mInventoryFilesController.writeAppSettings();
    	});
    }
}

// Actual class ends here
// Further down follows an extract from: https://bugs.openjdk.java.net/secure/attachment/40457/Bug.java
















/**
 * This is just to complete documentation for ImageViewPane
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
