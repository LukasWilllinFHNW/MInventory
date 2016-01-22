package minventory.control;

/**
 * Created by Lukas on 12.12.2015.
 */
public class CSVFileDefinition {

    // -- object --
    public final String CSV_OBJECTS_FILE = "MInventorySave.min";
	
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

    public final String OBJECT_FILE_HEADER = "containerId;identifier;objectId;name;description;type;usageType;"
                + "imageFileExtension;weight;dimensions;color;stateOfDecay;distinctAttribute";


    // -- app settings --
    public final String CSV_APP_SETTINGS_FILE = "MInventorySettings.min";

    public final int WINDOW_POSITION_X = 0;
    public final int WINDOW_POSITION_Y = 1;
    public final int WINDOW_WIDTH = 2;
    public final int WINDOW_HEIGHT = 3;
    public final int LAST_PROFILE = 4;

    public final String APP_SETTINGS_FILE_HEADER = "windowPosX;windowPosY;windowWidth;windowHeight;lastProfile";


    // -- profile settings --
    public final String CSV_PROFILE_FILE = "MInventoryProfile.min";
    
    public final int OBJECTS_AMOUNT = 0;
    public final int WINDOW_COLOR = 1;
    public final int FULL_PROFILE_NAME = 2;
    public final int ADDRESS = 3;

    public final String PRO_FILE_HEADER = "objectsAmount;windowColor;windowBackgroundImage;fullName;address";
}
