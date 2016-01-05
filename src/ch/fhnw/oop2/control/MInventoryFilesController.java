package ch.fhnw.oop2.control;

import ch.fhnw.oop2.gui.CustomImage;
import ch.fhnw.oop2.gui.ViewTemplate;
import ch.fhnw.oop2.model.*;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//com.sun.org.apache.xpath.internal.operations.String TODO: Whot iis thiis?
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Lukas on 01.12.2015.
 */
public class MInventoryFilesController {

    private final CSVFileDefinition csv = new CSVFileDefinition();

    private final String CSV_FILE_NAME = "MInventorySave.min";
    private final String IMAGE_FOLDER_NAME = "images";
    private final String DATA_FOLDER_NAME = "MInventory";

    private final String SAMPLE_STORAGE = "-1;s;1;Your first storage :);Try to put this item into  the storage.;"
            + "a type;a usage type;.jpg;0.0;3.0:2.0:0.0;"
            + "0.8:0.2:0.2:1.0;"
            + "0;;#endOfLine;";
    private final String SAMPLE_ITEM = "-1;i;2;Your first item :D;Try to put me into the storage.;"
            + "a type;a usage type;.jpg;0.0;0.0:0.0:0.0;"
            + "0.2:0.8:0.2:1.0;"
            + "0;;#endOfLine;";

    private MInventoryDataModel dataModel;

    private Map<Integer, Integer> itemToStorage = new TreeMap<>();

    private SimpleListProperty<MInventoryObject> mInventoryObjectList;

    public void addDataModel(MInventoryDataModel dataModel) {
        this.dataModel = dataModel;
    }

    private final boolean filesInSameFolder = false;

    private ArrayList<String> pathAsList;


    // --- CONSTRUCTORs ---
    public MInventoryFilesController(MInventoryDataModel dataModel) {
        this.dataModel = dataModel;
    }


    // --- API ---
    /**
     * Reads MInventory objects from a .csv file
     * @return List with storage- and item-objects
     */
    public SimpleListProperty<MInventoryObject> readObjectsFromFile() {
        List<MInventoryObject> objectList;
        try {
            objectList = getStreamOfLines(CSV_FILE_NAME)
                    .skip(1)
                    .map(s -> createMInventoryObject(s.split(";")))
                    .collect(Collectors.toList());
        } catch (NullPointerException npe) {
            objectList = new ArrayList<>();
            objectList.add(createMInventoryObject(SAMPLE_STORAGE.split(";")));
            objectList.add(createMInventoryObject(SAMPLE_ITEM.split(";")));
        } catch (IllegalStateException ise) {
            objectList = new ArrayList<>();
            objectList.add(createMInventoryObject(SAMPLE_STORAGE.split(";")));
            objectList.add(createMInventoryObject(SAMPLE_ITEM.split(";")));
        }
        ObservableList<MInventoryObject> observableList = FXCollections.observableArrayList(objectList);
        mInventoryObjectList = new SimpleListProperty<>();
        mInventoryObjectList.setValue(observableList);

        if (!mInventoryObjectList.isEmpty()) {
            itemToStorage.forEach((integer, integer2) -> {
                ((MInventoryStorage) mInventoryObjectList.stream()
                        .filter(mInventoryObject -> mInventoryObject.getId() == integer)
                        .collect(Collectors.toList())
                        .get(0)).addObjectById(integer2);
            });
        }
        return mInventoryObjectList;
    }

    public void writeObjectsToFile() {
        File save = new File(getPath(CSV_FILE_NAME, filesInSameFolder).toString());
        File path = new File(getPath(null, filesInSameFolder).toString());
        File images = new File(getPath(IMAGE_FOLDER_NAME, filesInSameFolder).toString());
        // Check for non existing folders and files
        if (!save.exists() || !path.exists() || !images.exists()) {
            try {
                if (!path.exists())
                    path.mkdir();
                if (!images.exists())
                    images.mkdir();
                if (!save.exists())
                    save.createNewFile();
            } catch (IOException ioe) {
                System.out.println("Creating save file failed " + ioe.getMessage());
            }
        }
        try (BufferedWriter writer = Files.newBufferedWriter(getPath(CSV_FILE_NAME, filesInSameFolder), StandardCharsets.UTF_8)) {
            writer.write(csv.FILE_HEADER);
            writer.newLine();
            dataModel.getMInventoryObjectList().stream().forEach(object -> {
                try {
                    if(object != null) {
                        writer.write(dataModel.infoAsLine(object.getId()));
                        writer.newLine();
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            });
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new IllegalStateException("save failed " + e.getMessage());
        }

    }

    /**
     * Copies an image to save location with a new
     * @param i image to be copied
     * @param newFileName target folder and name
     */
    public void copyImage(CustomImage i, String newFileName) {
        List<String> list = (List<String>) pathAsList.clone();
            list.add(IMAGE_FOLDER_NAME);
        String target = composePath(list, newFileName);
        CopyOption[] options = new CopyOption[]{
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES
        };
        try {
            Files.copy(Paths.get(i.getPath()), Paths.get(target), options);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage() + " " + ioe.toString() + ioe.getLocalizedMessage());
        }
    }


    // --- HELPERS ---

    /**
     * Creates a path appends a file or folder name to it
     * The path can be either user.home or the same folder like the app
     * is System independent
     * @param fileName the file or folder name to append
     * @param locatedInSameFolder true if files should be saved in same folder, false if files should be saved in user.home folder
     * @return
     */
    private Path getPath(String fileName, boolean locatedInSameFolder) {
        ArrayList<String> decomposedPath;
        Path p;
        try {
            if(locatedInSameFolder) {
                return Paths.get(getClass().getResource(fileName).toURI());
            } else {
                // Get all folders from a path as list (user home)
                decomposedPath = decomposePath(Paths.get(System.getProperty("user.home")), false);
                decomposedPath.add(DATA_FOLDER_NAME);

                pathAsList = decomposedPath;
                // Compose strings to path and return it
                return Paths.get(composePath(decomposedPath, fileName));
            }
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Creates an array from a path containing the folders in order
     * @param path the path to be decomposed
     * @return a list with the folder name
     */
    private ArrayList<String> decomposePath(Path path, boolean containsFile) {
        // Delete file name from folder path
        if (containsFile) {
            path = path.getParent();
        }
        ArrayList<String> sList = new ArrayList<String>();
        // Decompose path to list
        while (path != path.getRoot()) {
            try {
                sList.add(path.getFileName().toString());
            } catch (NullPointerException npe1) { sList.add(path.toString().substring(0, 2)); break; }
            try {
                path = path.getParent();
            } catch (NullPointerException npe2) { break; }
        }
        // Setup sort order
        Object[] sArray = sList.toArray();
        sList = new ArrayList<String>();
        // change sort order of list
        for (int i = sArray.length-1; i >= 0; --i) {
            sList.add((String)sArray[i]);
        }
        return sList;
    }

    /**
     * Composes a path as a string from a list of folder names
     * @param list list with names of folders in the right order without file seperators
     *             which should be composed to a string
     * @param additionalFileName Include file name if you need else null if only path is needed
     * @return the path as string
     */
    private String composePath(List<String> list, String additionalFileName){
        StringBuffer sb = new StringBuffer();

        String osName = System.getProperty("os.name");
        osName = osName.toLowerCase();
        if (osName.contains("mac")) sb.append(File.separatorChar);

        for (String folderName : list) {
            sb.append(folderName);
            sb.append(File.separatorChar);
        }
        if (additionalFileName != null && !additionalFileName.isEmpty()) sb.append(additionalFileName);
        return sb.toString();
    }

    private Stream<String> getStreamOfLines(String fileName) {
        try {
            return Files.lines(getPath(fileName, filesInSameFolder), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private MInventoryObject createMInventoryObject(String[] arguments) {

        int id = Integer.parseInt(arguments[csv.OBJECT_ID]);

        /*if ( Integer.parseInt(arguments[csv.CONTAINER_ID]) != -1) {
            itemToStorage.put(id, Integer.parseInt(arguments[csv.OBJECT_ID]));
        }*/
        String name;
            try {
                name = arguments[csv.NAME];
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e.getMessage() + "\n" + e.getStackTrace().toString() + "\n" + e.getCause());
                name = new String("");
            }
        String description;
            try {
                description = arguments[csv.DESCRIPTION];
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e.getMessage() + "\n" + e.getStackTrace().toString() + "\n" + e.getCause());
                description = new String("");
            }
        CustomImage image;
            try {
                List<String> list = (List<String>) pathAsList.clone();
                    list.add(IMAGE_FOLDER_NAME);
                File testFile = new File(Paths.get(composePath(list, ("" + id + "-1.jpg"))).toUri());
                if (testFile.exists())
                    image = new CustomImage(Paths.get(composePath(list, ("" + id + "-1.jpg"))).toUri().toString(), composePath(list, ("" + id + "-1.jpg")));
                else
                    throw new InvalidPathException("Image not found", "" + id + "-1.jpg" + " in " + composePath(list, null));
            } catch (InvalidPathException ipe) {
                System.out.println(ipe.getMessage() + "\n" + ipe.getStackTrace());
                image = null;
            }
        Color color;
            try {
                String[] rgbaText = arguments[csv.COLOR].split(":");
                double red = Double.parseDouble(rgbaText[0]);
                double green = Double.parseDouble(rgbaText[1]);
                double blue = Double.parseDouble(rgbaText[2]);
                double opacity = Double.parseDouble(rgbaText[3]);
                color = new Color(red, green, blue, opacity);
            } catch (NumberFormatException nfe) {
                System.out.println(nfe.getMessage() + "\n" + nfe.getStackTrace());
                color = Color.WHITESMOKE;
            }
        double[] dimensions = new double[3];
        try {
                String[] dimsText = arguments[csv.DIMENSIONS].split(":");
                dimensions[0] = Double.parseDouble(dimsText[0]);
                dimensions[1] = Double.parseDouble(dimsText[1]);
                dimensions[2] = Double.parseDouble(dimsText[2]);
            } catch (NumberFormatException nfe) {
                System.out.println(nfe.getMessage() + "\n" + nfe.getStackTrace());
                dimensions[0] = 0;
                dimensions[1] = 0;
                dimensions[2] = 0;
            }
        Type type;
            type = new Type(arguments[csv.TYPE], arguments[csv.USAGE_TYPE]);
        int stateOfDecay;
            try {
                stateOfDecay = Integer.parseInt(arguments[csv.STATE_OF_DECAY]);
            } catch (NumberFormatException nfe) {
                System.out.println(nfe.getMessage() + "\n" + nfe.getStackTrace());
                stateOfDecay = 1;
            }
        double weight;
            try {
                weight = Double.parseDouble(arguments[csv.WEIGHT]);
            } catch (NumberFormatException nfe) {
                System.out.println(nfe.getMessage() + "\n" + nfe.getStackTrace());
                weight = 1;
            }
        String distinctAttribute;
            distinctAttribute = arguments[csv.DISTINCT_ATTRIBUTE];

        try {
            if (Integer.parseInt(arguments[csv.CONTAINER_ID]) > 0)
                itemToStorage.put(Integer.parseInt(arguments[csv.CONTAINER_ID]), id);
        } catch (NumberFormatException nfe) {
            System.out.println(nfe.getMessage() + "\n" + nfe.getStackTrace());
        }


        if(arguments[csv.IDENTIFIER].equals(dataModel.STORAGE_IDENTIFIER)) {

            return new MInventoryStorage(id, name, description, image , weight, color, dimensions, stateOfDecay, type, distinctAttribute);
        } else {
            return new MInventoryItem(id, name, description, image, weight, color, dimensions, stateOfDecay, type, distinctAttribute);
        }
    }
}