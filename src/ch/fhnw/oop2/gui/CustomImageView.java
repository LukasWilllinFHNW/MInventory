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

/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

/**
 * Source from: https://blog.idrsolutions.com/2015/05/how-to-implement-drag-and-drop-function-in-a-javafx-application/
 * creation date: May 5, 2015
 * accessed on Dec 28, 2015
 * author: Ernest Duodu
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
        this.setPreserveRatio(true);
        this.setStyle("-fx-background-color: whitesmoke");
    }

    @Override
    public void layoutPanes() {

    }

    @Override
    public void layoutControls() {

    }

    @Override
    public void addBindings() {
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

    public void connectToModel() {
        dataModel.getProxy().getImageProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.imageProperty().setValue(newValue);
            }
            else {
                this.imageProperty().setValue(
                        new CustomImage("/resources/images/NoImage.png", "/resources/images/NoImage.png"));
            }
        });
        if (dataModel.getProxy().getImage() == null) {
            this.imageProperty().setValue(
                    new CustomImage("/resources/images/NoImage.png", "/resources/images/NoImage.png"));
        }
    }

    void addImage(CustomImage ci){

        dataModel.addImage(ci);
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
                    CustomImage ci = new CustomImage(file.toURI().toString(), file.getPath());

                    addImage(ci);
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
