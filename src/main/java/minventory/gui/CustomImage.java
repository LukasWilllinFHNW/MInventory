package minventory.gui;

import javafx.scene.image.Image;

/**
 * Created by Lukas on 28.12.2015.
 */
public class CustomImage extends Image{

    private final String url;
    private final String path;

    public CustomImage(String url, String path) {
        super(url);
        this.url = url;
        this.path = path;
    }

    public String getUrl() {
        return url;
    }
    public String getPath() { return path; }
}
