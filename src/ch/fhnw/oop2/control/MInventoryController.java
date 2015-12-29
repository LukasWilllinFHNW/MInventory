package ch.fhnw.oop2.control;

import ch.fhnw.oop2.gui.CustomImage;
import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryItem;
import ch.fhnw.oop2.model.MInventoryObject;
import ch.fhnw.oop2.model.MInventoryStorage;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
public class MInventoryController {

    private final CSVFileDefinition csv = new CSVFileDefinition();

    private final String CSV_FILE_NAME = "MInventorySave.min";
    private final String IMAGE_FOLDER_NAME = "images";
    private final String DATA_FOLDER_NAME = "MInventory";

    private final String SPLITTER = ";";

    private final String[] SAMPLE_STORAGE = {"-1", "s", "1", "Your first storage :)", "Try to drag item into storage."};
    private final String[] SAMPLE_ITEM = {"-1", "i", "2", "Your first item :D", "Try to drag me into the storage."};

    private MInventoryDataModel dataModel;

    private Map<Integer, Integer> itemToStorage = new TreeMap<>();

    private SimpleListProperty<MInventoryObject> mInventoryObjectList;

    public void addDataModel(MInventoryDataModel dataModel) {
        this.dataModel = dataModel;
    }

    private final boolean filesInSameFolder = false;

    private ArrayList<String> pathAsList;


    // --- CONSTRUCTORs ---
    public MInventoryController(MInventoryDataModel dataModel) {
        this.dataModel = dataModel;
    }


    // --- API ---
    /**
     * Reads MInventory objects from a .csv file
     * @return List with storage- and item-objects
     */
    public SimpleListProperty<MInventoryObject> readObjectsFromFile() {

        System.getProperties().list(System.out);
        List<MInventoryObject> objectList;
        try {
            objectList = getStreamOfLines(CSV_FILE_NAME)
                    .skip(1)
                    .map(s -> createMInventoryObject(s.split(";")))
                    .collect(Collectors.toList());
        } catch (NullPointerException npe) {
            objectList = new ArrayList<>();
            objectList.add(createMInventoryObject(SAMPLE_STORAGE));
            objectList.add(createMInventoryObject(SAMPLE_ITEM));
        }
        ObservableList<MInventoryObject> observableList = FXCollections.observableArrayList(objectList);
        mInventoryObjectList = new SimpleListProperty<>();
        mInventoryObjectList.setValue(observableList);
        return mInventoryObjectList;
    }

    public void writeObjectsToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(getPath(CSV_FILE_NAME, filesInSameFolder), StandardCharsets.UTF_8) ) {
            writer.write(csv.FILE_HEADER);
            writer.newLine();
            mInventoryObjectList.stream().forEach(object -> {
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
            throw new IllegalStateException("save failed");
        }
    }

    public void copyImage(CustomImage i, String newFileName) {
        List<String> list = pathAsList;
        list.add(IMAGE_FOLDER_NAME);
        String target = composePath(pathAsList, newFileName);
        CopyOption[] options = new CopyOption[]{
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES
        };
        try {
            Files.copy(Paths.get(i.getUrl()), Paths.get(target), options);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage() + ioe.getStackTrace());
        }
    }


    // --- HELPERS ---

    private Path getPath(String fileName, boolean locatedInSameFolder) {
        ArrayList<String> decomposedPath;
        Path p;
        try {
            if(locatedInSameFolder) {
                return Paths.get(getClass().getResource(fileName).toURI());
            } else {
                // Get all folders from a path as list (user home)
                decomposedPath = decomposePath(Paths.get(System.getProperty("user.home")), false);
                decomposedPath.add("MInventory");

                pathAsList = decomposedPath;
                // Compose strings to path and return it
                return Paths.get(composePath(decomposedPath, null));
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
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

        for (String folderName : list) {
            sb.append(folderName);
            sb.append(File.separatorChar);
        }
        if (additionalFileName != null) sb.append(additionalFileName);

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

        if ( Integer.parseInt(arguments[csv.CONTAINER_ID]) != -1) {
            itemToStorage.put(id, Integer.parseInt(arguments[csv.OBJECT_ID]));
        }
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
            try { image = new CustomImage("../resources/images/" + id + "-1.jpg");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + "\n" + e.getStackTrace());
                image = new CustomImage("/resources/images/NoImage.png");
            }

        if(arguments[csv.IDENTIFIER].equals(dataModel.STORAGE_IDENTIFIER)) {
            return new MInventoryStorage(id, name, description, image); // TODO: Image
        } else {
            //TODO: Add item to storage if storageId exists
            return new MInventoryItem(id, name, description, image); // TODO image
        }
    }
}
