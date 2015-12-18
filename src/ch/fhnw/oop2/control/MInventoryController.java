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

    private final CSVFileDefinition csv = new CSVFileDefinition();

    private final String FILENAME = "MInventorySave.min";

    private final String SPLITTER = ";";

    private MInventoryDataModel dataModel;

    private Map<Integer, Integer> itemToStorage = new TreeMap<>();

    private SimpleListProperty<MInventoryObject> mInventoryObjectList;

    public void addDataModel(MInventoryDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public MInventoryController(MInventoryDataModel dataModel) {
        this.dataModel = dataModel;
    }


    /**
     * Reads MInventory objects from a .csv file
     * @return List with storage- and item-objects
     */
    public SimpleListProperty<MInventoryObject> readObjectsFromFile() {

        File file = new File(FILENAME);
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
        try (BufferedWriter writer = Files.newBufferedWriter(getPath(FILENAME, true), StandardCharsets.UTF_8) ) {
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
            return Files.lines(getPath(fileName, true), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private MInventoryObject createMInventoryObject(String[] arguments) {
        int id = Integer.parseInt(arguments[csv.OBJECT_ID]);

        if ( Integer.parseInt(arguments[csv.CONTAINER_ID]) != -1) {
            itemToStorage.put(id, Integer.parseInt(arguments[csv.OBJECT_ID]));
        }
        String name = arguments[csv.NAME];
        String description = arguments[csv.DESCRIPTION];
        int symbolId = Integer.parseInt(arguments[csv.SYMBOL_ID]);
        if(arguments[csv.IDENTIFIER].equals(dataModel.STORAGE_IDENTIFIER)) {
            return new MInventoryStorage(id, name, description, symbolId);
        } else {
            //TODO: Add item to storage if storageId exists
            return new MInventoryItem(id, name, description, symbolId);
        }
    }

}
