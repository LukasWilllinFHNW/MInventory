package ch.fhnw.oop2.control;

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
import java.io.FileWriter;
import java.io.IOException;
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

    private final int CONTAINER_ID = 0;
    private final int IDENTIFIER = 1;
    private final int OBJECT_ID = 2;
    private final int NAME = 3;
    private final int DESCRIPTION = 4;
    private final int SYMBOL_ID = 5;

    private final String FILENAME = "MInventorySave.min";

    private final String SPLITTER = ";";

    private MInventoryDataModel dataModel;

    private Map<Integer, Integer> itemToStorage = new TreeMap<>();

    private SimpleListProperty<MInventoryObject> mInventoryObjectList;

    public void addDataModel(MInventoryDataModel dataModel) {
        this.dataModel = dataModel;
    }

    /**
     * Only for testing purposes to prevent having to change method calls in other classes
     * and to switch between test data and saved data easily
     * TODO: Make readObjectsFromFile work and use it instead of this method
     * TODO: Remove fakeRead() method if actual file reading works
     * @return List with storage- and item-objects
     */
    public SimpleListProperty<MInventoryObject> testRead(){
        //return readObjectsFromFile();
        return fakeRead();
    }

    /**
     * Reads MInventory objects from a .csv file
     * TODO: Make this work and use it instead of testRead() (String lines are not beeing encoded properly -> numbers are not included in array)
     * @return List with storage- and item-objects
     */
    private SimpleListProperty<MInventoryObject> readObjectsFromFile() {

        File file = new File(FILENAME);
        List<MInventoryObject> objectList = getStreamOfLines(FILENAME)
                    .skip(1)
                // The string array will only contain text but no numbers causing a index out of bounds exception
                    .map(s -> createMInventoryObject(s.split(";")))
                    .collect(Collectors.toList());
        ObservableList<MInventoryObject> observableList = FXCollections.observableArrayList(objectList);

        mInventoryObjectList = new SimpleListProperty<>();
        mInventoryObjectList.setValue(observableList);
        return mInventoryObjectList;
    }

    /**
     * This method only provide test data
     * TODO: Delete this method when actual file reading works -> readObjectFromFile()
     * @return List with storage- and item-objects
     */
    private SimpleListProperty<MInventoryObject> fakeRead() {
        ObservableList<MInventoryObject> observableList = FXCollections.observableArrayList();
        observableList.add(new MInventoryStorage(1, "The First Storage", "This is a storage.", 0));
        observableList.add(new MInventoryItem(2, "The First Item", "This is a item.", 0));
        mInventoryObjectList = new SimpleListProperty<>();
        mInventoryObjectList.setValue(observableList);
        return mInventoryObjectList;
    }

    public void writeObjectsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(FILENAME)))) {
            writer.write("containerId;identifier;objectId;name;description;symbolId;");
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
        try {
            if(!locatedInSameFolder) {
                fileName = "/" + fileName;
            }
            return Paths.get(getClass().getResource(fileName).toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        } catch (NullPointerException e) {
            return Paths.get("/"+fileName);
        }
    }

    private Stream<String> getStreamOfLines(String fileName) {
        try {
            // TODO: There seems to be a problem with encoding number characters causing only letters to be encoded
            return Files.lines(getPath(fileName, true), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private MInventoryObject createMInventoryObject(String[] arguments) {

        int id = Integer.parseInt(arguments[IDENTIFIER]);

        if ( Integer.parseInt(arguments[CONTAINER_ID]) != -1) {
            itemToStorage.put(id, Integer.parseInt(arguments[OBJECT_ID]));
        }
        String name = arguments[NAME];
        String description = arguments[DESCRIPTION];
        int symbolId = Integer.parseInt(arguments[SYMBOL_ID]);
        if(arguments[IDENTIFIER] == dataModel.STORAGE_IDENTIFIER) {
            return new MInventoryStorage(id, name, description, symbolId);
        } else {
            //TODO: Add item to storage if storageId exists
            return new MInventoryItem(id, name, description, symbolId);
        }
    }

}
