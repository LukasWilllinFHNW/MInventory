package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryPresentationModel;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Lukas on 28.12.2015.
 */
public class CustomImageView extends ImageView implements ViewTemplate {

    MInventoryDataModel dataModel;
    MInventoryPresentationModel presModel;


    // --- CONSTRUCTORS ---
    public CustomImageView(MInventoryPresentationModel presModel, MInventoryDataModel dataModel) {
        this.dataModel = dataModel;
        this.presModel = presModel;

        initSequence();
    }


    // --- init ---
    @Override
    public void initializeControls() {

    }

    @Override
    public void initializeLayout() {
        this.setFitHeight(150);
        this.setFitWidth(150);
        this.setPreserveRatio(true);
        this.setStyle("-fx-background-color: #adbfff");
    }

    @Override
    public void layoutPanes() {

    }

    @Override
    public void layoutControls() {

    }

    @Override
    public void addBindings() {
        this.imageProperty().bind(dataModel.getProxy().getImageProperty());
    }

    @Override
    public void addEvents() {

        ImageView accessFromInnerClass = this;

        this.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                mouseDragOver(event);
            }
        });

        this.setOnDragDropped(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                mouseDragDropped(event);
            }
        });

        this.setOnDragExited(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                //accessFromInnerClass.setStyle("-fx-border-color: #C6C6C6;");
            }
        });
    }

    void addImage(CustomImage ci){

        dataModel.copyImage(ci);
        dataModel.getProxy().getImageProperty().setValue(ci);
    }

    private void mouseDragDropped(final DragEvent e) {
        final Dragboard db = e.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            // Only get the first file from the list
            final File file = db.getFiles().get(0);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println(file.getAbsolutePath());
                    // try {
                        CustomImage ci = new CustomImage(file.toURI().toString());
                        //Image customImage = new Image(new FileInputStream(file.getAbsolutePath()));

                        addImage(ci);
                    /* } catch (FileNotFoundException ex) {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                    } */
                }
            });
        }
        e.setDropCompleted(success);
        e.consume();
    }

    private  void mouseDragOver(final DragEvent e) {
        final Dragboard db = e.getDragboard();

        final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase().endsWith(".png")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpeg")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpg");

        if (db.hasFiles()) {
            if (isAccepted) {
                /*this.setStyle("-fx-border-color: red;"
                        + "-fx-border-width: 5;"
                        + "-fx-background-color: #C6C6C6;"
                        + "-fx-border-style: solid;");*/
                e.acceptTransferModes(TransferMode.COPY);
            }
        } else {
            e.consume();
        }
    }
}
