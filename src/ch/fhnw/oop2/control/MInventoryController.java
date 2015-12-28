package ch.fhnw.oop2.control;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryItem;
import ch.fhnw.oop2.model.MInventoryObject;
import ch.fhnw.oop2.model.MInventoryStorage;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

//com.sun.org.apache.xpath.internal.operations.String TODO: Whot iis thiis?
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Lukas on 01.12.2015.
 */
public class MInventoryController {

    private final CSVFileDefinition csv = new CSVFileDefinition();

    private final String FILENAME = "MInventorySave.min";

    private final String SPLITTER = ";";

    private MInventoryDataModel dataModel;

    private Map<Integer, Integer> itemToStorage = new TreeMap<>();

    private SimpleListProperty<MInventoryObject> mInventoryObjectList;

    public void addDataModel(MInventoryDataModel dataModel) {
        this.dataModel = dataModel;
    }

    private final boolean filesInSameFolder = false;


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

        List<MInventoryObject> objectList = getStreamOfLines(FILENAME)
                    .skip(1)
                    .map(s -> createMInventoryObject(s.split(";")))
                    .collect(Collectors.toList());
        ObservableList<MInventoryObject> observableList = FXCollections.observableArrayList(objectList);

        mInventoryObjectList = new SimpleListProperty<>();
        mInventoryObjectList.setValue(observableList);
        return mInventoryObjectList;
    }

    public void writeObjectsToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(getPath(FILENAME, filesInSameFolder), StandardCharsets.UTF_8) ) {
            writer.write("containerId;identifier;objectId;name;description;");
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

    private Path getPath(String fileName, boolean locatedInSameFolder) {
        ArrayList<String> decomposedPath;
        Path p;
        try {
            if(locatedInSameFolder) {
                //fileName = "/" + fileName;
                return Paths.get(getClass().getResource(fileName).toURI());
            } else {
                // p = Paths.get(getClass().getResource(fileName).toURI());

                // Get all folders from a path as list (user home)
                decomposedPath = decomposePath(Paths.get(System.getProperty("user.home")), false);

                StringBuffer sb = new StringBuffer();
                decomposedPath.add("MInventory");

                // Compose string to files
                for (String folderName : decomposedPath) {
                    sb.append(folderName);
                    sb.append(File.separatorChar);
                }
                sb.append(fileName);
                return Paths.get(sb.toString());
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
        // Setup sort
        Object[] sArray = sList.toArray();
        sList = new ArrayList<String>();
        // Sort list
        for (int i = sArray.length-1; i >= 0; --i) {
            sList.add((String)sArray[i]);
        }
        return sList;
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
        Image image;
            try { image = new Image("../resources/images/" + id + "-1.jpg");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + "\n" + e.getStackTrace());
                image = new Image("/resources/images/NoImage.png");
            }

        if(arguments[csv.IDENTIFIER].equals(dataModel.STORAGE_IDENTIFIER)) {
            return new MInventoryStorage(id, name, description, image); // TODO: Image
        } else {
            //TODO: Add item to storage if storageId exists
            return new MInventoryItem(id, name, description, image); // TODO image
        }
    }

}
