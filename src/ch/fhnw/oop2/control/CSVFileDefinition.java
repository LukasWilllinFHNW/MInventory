package ch.fhnw.oop2.control;

/**
 * Created by Lukas on 12.12.2015.
 */
public class CSVFileDefinition {

    public final int CONTAINER_ID = 0;
    public final int IDENTIFIER = 1;
    public final int OBJECT_ID = 2;
    public final int NAME = 3;
    public final int DESCRIPTION = 4;
    public final int TYPE = 5;
    public final int USAGE_TYPE = 6;
    public final int IMAGE_FILE_EXTENSION = 7;
    public final int WEIGHT = 8;
    public final int DIMENSIONS = 9;
    public final int COLOR = 10;
    public final int STATE_OF_DECAY = 11;
    public final int DISTINCT_ATTRIBUTE = 12;


    public final String FILE_HEADER = "containerId;identifier;objectId;name;description;type;usageType;"
                + "imageFileExtension;weight;dimensions;color;stateOfDecay;distinctAttribute";

}
