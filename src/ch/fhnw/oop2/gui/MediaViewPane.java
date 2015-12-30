package ch.fhnw.oop2.gui;

/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

/**
 * Source can be found here: https://bugs.openjdk.java.net/secure/attachment/40456/MediaViewPane.java
 * accessed on Dec 29, 2015
 *
 * Source found over following sites by following the links:
 *      1) http://stackoverflow.com/questions/22993550/how-to-resize-an-image-when-resizing-the-window-in-javafx
 *      2) http://stackoverflow.com/questions/15951284/javafx-image-resizing
 */



import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.media.MediaView;
import javafx.scene.layout.Region;


/**
 *
 * @author akouznet
 */
public class MediaViewPane extends Region {

    private ObjectProperty<MediaView> mediaViewProperty = new SimpleObjectProperty<MediaView>();

    public ObjectProperty<MediaView> mediaViewProperty() {
        return mediaViewProperty;
    }

    public MediaView getMediaView() {
        return mediaViewProperty.get();
    }

    public void setMediaView(MediaView mediaView) {
        this.mediaViewProperty.set(mediaView);
    }

    public MediaViewPane() {
        this(new MediaView());
    }

    @Override
    protected void layoutChildren() {
        MediaView mediaView = mediaViewProperty.get();
        if (mediaView != null) {
            mediaView.setFitWidth(getWidth());
            mediaView.setFitHeight(getHeight());
            layoutInArea(mediaView, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
        }
        super.layoutChildren();
    }

    public MediaViewPane(MediaView mediaView) {
        mediaViewProperty.addListener(new ChangeListener<MediaView>() {

            @Override
            public void changed(ObservableValue<? extends MediaView> arg0, MediaView oldIV, MediaView newIV) {
                if (oldIV != null) {
                    getChildren().remove(oldIV);
                }
                if (newIV != null) {
                    getChildren().add(newIV);
                }
            }
        });
        this.mediaViewProperty.set(mediaView);
    }
}
