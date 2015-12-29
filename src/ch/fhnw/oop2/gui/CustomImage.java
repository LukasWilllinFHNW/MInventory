package ch.fhnw.oop2.gui;

import javafx.scene.image.Image;

/**
 * Created by Lukas on 28.12.2015.
 */
public class CustomImage extends Image{

    private final String url;

    public CustomImage(String url) {
        super(url);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
