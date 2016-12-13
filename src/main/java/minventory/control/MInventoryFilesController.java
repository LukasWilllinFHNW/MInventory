package minventory.control;

import minventory.gui.CustomImage;
import minventory.model.*;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Lukas on 01.12.2015.
 */
public class MInventoryFilesController {

	private final Logger logger = (Logger) LogManager.getLogger(MInventoryFilesController.class);

	private final CSVFileDefinition csv = new CSVFileDefinition();

	// File Names
	// Folder Names
	private final String IMAGE_FOLDER_NAME = "images";
	private final String DATA_FOLDER_NAME = "MInventory";
	// Sample Objects
	private final String SAMPLE_STORAGE = "-1;s;1;Your first storage :);Try to put this item into  the storage.;"
			+ "a type;a usage type;.jpg;0.0;3.0:2.0:0.0;" + "0.8:0.2:0.2:1.0;" + "0;;#endOfLine;";
	private final String SAMPLE_ITEM = "-1;i;2;Your first item :D;Try to put me into the storage.;"
			+ "a type;a usage type;.jpg;0.0;0.0:0.0:0.0;" + "0.2:0.8:0.2:1.0;" + "0;;#endOfLine;";
	// The current open profile
	private final StringProperty CURRENT_PROFILE = new SimpleStringProperty();
	/** Value between 0 and 100000 to indicate the loading progress */
	private final DoubleProperty progressProperty = new SimpleDoubleProperty(0);
	
	public final DoubleProperty progressInPercent = new SimpleDoubleProperty(0);
	
	/** Value to indicate how many objects it has to load */
	private int objectsToLoad;
	
	private MInventoryDataModel dataModel;
	private MInventoryPresentationModel presModel;

	private Map<Integer, Integer> itemToStorage = new TreeMap<>();

	private final boolean filesInSameFolder = false;

	private ArrayList<String> pathAsList;

	// --- CONSTRUCTORS ---
	public MInventoryFilesController(MInventoryDataModel dataModel) {
		this.dataModel = dataModel;

		progressProperty.addListener((observable, oldValue, newValue) -> {
			progressInPercent.set(progressProperty.get()/100000);
		});
		logger.info("file controller created");
	}

	// --- API ---
	public void addDataModel(MInventoryDataModel dataModel) {
		if (this.dataModel == null && dataModel != null) {
			this.dataModel = dataModel;
			logger.info("dataModel has been added");
		} else {
			throw new IllegalArgumentException("data model has already been set or passed data model was null");
		}
	}

	/**
	 * Reads the app settings from a csv file App settings are as basic as
	 * possible
	 */
	public MInventoryPresentationModel readAppSettingsFromFile() {
		logger.trace("Attemp to read app settings");

		MInventoryPresentationModel presentationModel;

		try {
			presentationModel = getStreamOfLines(csv.CSV_APP_SETTINGS_FILE).skip(1)
					.map(s -> parseAppSettings(s.split(";"))).collect(Collectors.toList()).get(0);
		} catch (NullPointerException npe) {
			// Set standard presentation model when file does not exist
			presentationModel = MInventoryPresentationModel.STANDARD;
			logger.error("Unable to read app settings -> Setting standard options");
		} catch (IllegalStateException ise) {
			// Set standard presentation model when file does not exist
			presentationModel = MInventoryPresentationModel.STANDARD;
			logger.error("Unable to read app settings -> Setting standard options");
		}

		try {
			String[] arguments = getStreamOfLines(csv.CSV_PROFILE_FILE).skip(1).collect(Collectors.toList()).get(0)
					.split(";");
			parseProfileData(arguments, presentationModel);
		} catch (IllegalStateException ise) {
			setStandardProfile(presentationModel);
		} catch (IndexOutOfBoundsException ioobe) {
			setStandardProfile(presentationModel);
		}
		presentationModel.setDataModel(dataModel);
		this.presModel = presentationModel;
		return presentationModel;
	}

	/**
	 * Reads MInventory objects from a .csv file
	 * 
	 * @return List with storage- and item-objects
	 */
	public SimpleListProperty<MInventoryObject> readObjectsFromFile() {

		// Try read object information from file
	    // TODO: resolve quick and dirty solution objectList = null
		List<MInventoryObject> objectList = null;

		try {
			// Get Stream of string lines 
			Stream<String> lines = getStreamOfLines(csv.CSV_OBJECTS_FILE);
			// Save the stream to a list
			List<String> lineList = lines.collect(Collectors.toList());
			// Count objects to know how many objects will be loaded in the next step
			objectsToLoad = lineList.size();
			if (objectsToLoad < 1) {
			    objectsToLoad = 1000;
			}
			// Load objects
			objectList = lineList.stream().skip(1).map(s -> createMInventoryObject(s.split(";")))
					.collect(Collectors.toList());
		} catch (Exception e) {
            logger.error("Failed to load objects. Exception is: " +e.getMessage());
            objectList = new ArrayList<>();
            objectList.add(createMInventoryObject(SAMPLE_STORAGE.split(";")));
            objectList.add(createMInventoryObject(SAMPLE_ITEM.split(";")));
        }

		// Create observable list from available object list
		SimpleListProperty<MInventoryObject> mInventoryObjectList = new SimpleListProperty<>(
		            FXCollections.observableArrayList(objectList));

		// When all objects have been created add objects into their storage
		if (!mInventoryObjectList.isEmpty()) {
			itemToStorage.forEach((integer, integer2) -> {
				((MInventoryStorage) mInventoryObjectList.stream()
						.filter(mInventoryObject -> mInventoryObject.getId() == integer).collect(Collectors.toList())
						.get(0)).addObjectById(integer2);
			});
		} else {
		    logger.trace("there were no objects in save file");
		}

		return mInventoryObjectList;
	}
	
	/**
	 * Writes all object from data model to a csv file
	 *
	 */
	public void writeObjects() {
	    checkSaveLocation();
		// Do the saving
		try (BufferedWriter writer = Files.newBufferedWriter(getPath(csv.CSV_OBJECTS_FILE, filesInSameFolder),
				StandardCharsets.UTF_8)) {
			writer.write(csv.OBJECT_FILE_HEADER);
			writer.newLine();
			dataModel.getMInventoryObjectList().stream().forEach(object -> {
				try {
					if (object != null) {
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
		    logger.error("writing objects to file failed");
			throw new IllegalStateException("save failed " + e.getMessage());
		}
		logger.info("successfully wrote objects to file");
	}

	/**
	 * Copies an image to save location with optional new
	 * 
	 * @param image
	 *            to be copied
	 * @param newFileName
	 *            name of image
	 * @throws IllegalArgumentException
	 *             when path is set instead of only a file name
	 */
	public void copyImage(CustomImage image, String newFileName) {
		if (newFileName.contains("" + File.separatorChar)) {
			throw new IllegalArgumentException("Path instead of new image name is not allowed");
		}
		List<String> list = (List<String>) pathAsList.clone();
		list.add(IMAGE_FOLDER_NAME);
		String target = composePath(list, newFileName);
		CopyOption[] options = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES };
		try {
			Files.copy(Paths.get(image.getPath()), Paths.get(target), options);
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage() + " " + ioe.toString() + ioe.getLocalizedMessage());
		}
	}

	/**
	 * Write app settings to a csv file
	 */
	public void writeAppSettings() {
	    checkSaveLocation();
		try (BufferedWriter writer = Files.newBufferedWriter(getPath(csv.CSV_APP_SETTINGS_FILE, filesInSameFolder),
				StandardCharsets.UTF_8)) {
			writer.write(csv.APP_SETTINGS_FILE_HEADER);
			writer.newLine();
			try {
				writer.write(presModel.getAppSettingsAsLine());
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
			writer.flush();
			writer.close();
			System.out.println("Save app settings successfull");

			// Writes the profile data
			writeProfileData();

		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * Checks if the location to save the files in is correct
	 * If the folder structure is incorrect it attempts to rebuild the structure
	 * This method should only return false when the app is started the first time.
	 */
	private void checkSaveLocation() {
	    // Define Files and Folders needed for saving
        File save = new File(getPath(csv.CSV_OBJECTS_FILE, filesInSameFolder).toString());
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
                System.out.println("Creating save folders failed " + ioe.getMessage());
            }
        }
	}

	/**
	 * Write profile data
	 */
	public void writeProfileData() {
	    checkSaveLocation();
		try (BufferedWriter writer = Files.newBufferedWriter(getPath(csv.CSV_PROFILE_FILE, filesInSameFolder),
				StandardCharsets.UTF_8)) {
			// TODO copy background image
			writer.write(csv.PRO_FILE_HEADER);
			writer.newLine();
			try {
				writer.write(presModel.getProfileSettingsAsLine());
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
			writer.flush();
			writer.close();
			System.out.println("Saving profile successfull");
		} catch (IOException e) {
			throw new IllegalStateException("saving profile settings failed " + e.getMessage());
		}
	}

	// --- HELPERS ---

	/**
	 * Creates a path appends a file or folder name to it. The path can be either
	 * user.home or the same folder as the app
	 * 
	 * @param fileName
	 *            the file or folder name to append
	 * @param locatedInSameFolder
	 *            true if files should be saved in same folder, false if files
	 *            should be saved in user.home folder
	 * @return returns the path to the save location in user.home variable
	 */
	private Path getPath(String fileName, boolean locatedInSameFolder) {
		ArrayList<String> decomposedPath;
		Path p;
		try {
			if (locatedInSameFolder) {
				// String path to file in same folder
				return Paths.get(getClass().getResource(fileName).toURI());
			} else {
				// Get all folders from any path as list (user home) and append
				// save location
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
	 * Creates an array from a path containing the folders in order.
	 * Crops the file name when contains file name true
	 * 
	 * @param path
	 *            the path to be decomposed
	 * @param containsFileName
	 *            if it contains a file name
	 * @return a list with the folder name
	 */
	private ArrayList<String> decomposePath(Path path, boolean containsFileName) {
		// Delete file name from folder path
		if (containsFileName) {
			path = path.getParent();
		}
		ArrayList<String> sList = new ArrayList<String>();
		// Decompose path to list
		while (path != path.getRoot()) {
			try {
				sList.add(path.getFileName().toString());
			} catch (NullPointerException npe1) {
				sList.add(path.toString().substring(0, 2));
				break;
			}
			try {
				path = path.getParent();
			} catch (NullPointerException npe2) {
				break;
			}
		}
		// Setup sort order
		Object[] sArray = sList.toArray();
		sList = new ArrayList<String>();
		// change sort order of list
		for (int i = sArray.length - 1; i >= 0; --i) {
			sList.add((String) sArray[i]);
		}
		return sList;
	}

	/**
	 * Composes a path as a string from a list of folder names
	 * 
	 * @param list
	 *            list with names of folders in the right order without file
	 *            seperators which should be composed to a string
	 * @param additionalFileName
	 *            Include file name if you need else null if only path is needed
	 * @return the path as string
	 */
	private String composePath(List<String> list, String additionalFileName) {
		StringBuffer sb = new StringBuffer();

		String osName = System.getProperty("os.name");
		osName = osName.toLowerCase();
		if (osName.contains("mac"))
			sb.append(File.separatorChar);

		for (String folderName : list) {
			sb.append(folderName);
			sb.append(File.separatorChar);
		}
		if (additionalFileName != null && !additionalFileName.isEmpty())
			sb.append(additionalFileName);
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
		if(objectsToLoad < 1) objectsToLoad = 1000;
		int progressStepInA100k = 100000/objectsToLoad;

		String name; // Loading the name takes 1 of 30
		try {
			name = arguments[csv.NAME];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(e.getMessage() + "\n" + e.getStackTrace().toString() + "\n" + e.getCause());
			name = new String("");
		}
		progressProperty.set(progressProperty.get()+(progressStepInA100k/30)*1);
		
		String description; // Loading the description takes 1 of 30
		try {
		    char newLine = 10; char carriageReturn = 13; char bell = 7;
			description = arguments[csv.DESCRIPTION];
			description= description.replace(bell, newLine);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(e.getMessage() + "\n" + e.getStackTrace().toString() + "\n" + e.getCause());
			description = new String("");
		}
		progressProperty.set(progressProperty.get()+(progressStepInA100k/30)*1);

		CustomImage image; //Loading the image takes 10 of 30
		try {
			List<String> list = (List<String>) pathAsList.clone();
			list.add(IMAGE_FOLDER_NAME);
			String imageExtension = arguments[csv.IMAGE_FILE_EXTENSION];
			logger.info(id+" " +imageExtension);
			File testFile = new File(Paths.get(composePath(list, ("" + id + "-1" + imageExtension))).toUri());
			if (testFile.exists())
				image = new CustomImage(Paths.get(composePath(list, ("" + id + "-1" + imageExtension))).toUri().toString(),
						composePath(list, ("" + id + "-1" + imageExtension)));
			else
				throw new InvalidPathException("Image not found",
						"" + id + "-1"+ imageExtension + " in " + composePath(list, null));
		} catch (InvalidPathException ipe) {
			logger.error(ipe.getMessage() + "\n" + ipe.getStackTrace());
			image = null;
		}
		progressProperty.set(progressProperty.get()+(progressStepInA100k/30)*10);

		Color color; // Loading the color takes 6 of 30
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
		progressProperty.set(progressProperty.get()+(progressStepInA100k/30)*6);

		double[] dimensions = new double[3]; // Loading the dimensions takes 4 of 30
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
		progressProperty.set(progressProperty.get()+(progressStepInA100k/30)*4);

		Type type; // Loading the type takes 2 of 30
		type = new Type(arguments[csv.TYPE], arguments[csv.USAGE_TYPE]);
		progressProperty.set(progressProperty.get()+(progressStepInA100k/30)*2);

		int stateOfDecay; // loading the state of decay takes 1 of 30
		try {
			stateOfDecay = Integer.parseInt(arguments[csv.STATE_OF_DECAY]);
		} catch (NumberFormatException nfe) {
			System.out.println(nfe.getMessage() + "\n" + nfe.getStackTrace());
			stateOfDecay = 1;
		}
		progressProperty.set(progressProperty.get()+(progressStepInA100k/30)*1);

		double weight; //loading the weight takes 2 of 30
		try {
			weight = Double.parseDouble(arguments[csv.WEIGHT]);
		} catch (NumberFormatException nfe) {
			System.out.println(nfe.getMessage() + "\n" + nfe.getStackTrace());
			weight = 1;
		}
		progressProperty.set(progressProperty.get()+(progressStepInA100k/30)*2);

		String distinctAttribute; // Loading disitnct attribute takes 1 of 30
		distinctAttribute = arguments[csv.DISTINCT_ATTRIBUTE];
		progressProperty.set(progressProperty.get()+(progressStepInA100k/30)*1);


		try { // Putting ids into map takes 1 of 30
			if (Integer.parseInt(arguments[csv.CONTAINER_ID]) > 0)
				itemToStorage.put(Integer.parseInt(arguments[csv.CONTAINER_ID]), id);
		} catch (NumberFormatException nfe) {
			System.out.println(nfe.getMessage() + "\n" + nfe.getStackTrace());
		}
		progressProperty.set(progressProperty.get()+(progressStepInA100k/30)*1);

		// Returning the object takes 1 of 30
		if (arguments[csv.IDENTIFIER].equals(dataModel.STORAGE_IDENTIFIER)) {
			progressProperty.set(progressProperty.get()+(progressStepInA100k/30)*1);
			return new MInventoryStorage(id, name, description, image, weight, color, dimensions, stateOfDecay, type,
					distinctAttribute);
		} else {
			progressProperty.set(progressProperty.get()+(progressStepInA100k/30)*1);
			return new MInventoryItem(id, name, description, image, weight, color, dimensions, stateOfDecay, type,
					distinctAttribute);
		}
	}

	/**
	 * Parse app settings
	 */
	private MInventoryPresentationModel parseAppSettings(String[] arguments) {
		MInventoryPresentationModel presentationModel;

		if (arguments == null) {
			presentationModel = MInventoryPresentationModel.STANDARD;
		} else {
			double width;
			try {
				width = Double.parseDouble(arguments[csv.WINDOW_WIDTH]);
			} catch (NumberFormatException nfe) {
				width = MInventoryPresentationModel.WINDOW_WIDTH;
			}
			double height;
			try {
				height = Double.parseDouble(arguments[csv.WINDOW_HEIGHT]);
			} catch (NumberFormatException nfe) {
				height = MInventoryPresentationModel.WINDOW_HEIGHT;
			}
			double posX;
			try {
				posX = Double.parseDouble(arguments[csv.WINDOW_POSITION_X]);
			} catch (NumberFormatException nfe) {
				posX = 20;// TODO: Get display size and calculate posX so that
							// the window is centered;
			}
			double posY;
			try {
				posY = Double.parseDouble(arguments[csv.WINDOW_POSITION_Y]);
			} catch (NumberFormatException nfe) {
				posY = 20;// TODO: Get display size and calculate posY so that
							// the window is centered;
			}

			presentationModel = new MInventoryPresentationModel(false);
			presentationModel.setWindowWidth(width);
			presentationModel.setWindowHeight(height);
			presentationModel.setX(posX);
			presentationModel.setY(posY);

		}
		return presentationModel;
	}

	/**
	 * Read profile settings
	 */
	private void parseProfileData(String[] arguments, MInventoryPresentationModel presentationModel) {
		logger.trace("Attemp to read profile data");

		if (arguments == null) {
			// TODO set standart presentationmdel settings
		} else {
			String fullProfileName;
			try {
				fullProfileName = (arguments[csv.FULL_PROFILE_NAME]);
				logger.trace("Loaded profile name");
			} catch (NumberFormatException nfe) {
				fullProfileName = MInventoryPresentationModel.FULL_PROFILE_NAME;
				logger.debug("Failed to load profile name");
			}
			Color color = MInventoryPresentationModel.WINDOW_COLOR;
			double red;
			double green;
			double blue;
			double opacity;
			String[] colorProps = arguments[csv.WINDOW_COLOR].split(":");
			try {
				red = Double.parseDouble(colorProps[0]);
				green = Double.parseDouble(colorProps[1]);
				blue = Double.parseDouble(colorProps[2]);
				opacity = Double.parseDouble(colorProps[3]);
				color = new Color(red, green, blue, opacity);
				logger.trace("Loaded window color");
			} catch (NumberFormatException nfe) {
				logger.debug("Failed to load window color");
			} catch (IndexOutOfBoundsException ioobe) {
				logger.debug("Failed to load window color");
			}
			String address;
			try {
				address = arguments[csv.ADDRESS];
				logger.trace("Loaded address");
			} catch (NumberFormatException nfe) {
				address = "";
				logger.debug("Failed to load address");
			}

			presentationModel.setBackgroundImage(null); // TODO get Image
			presentationModel.setWindowColor(color);
			presentationModel.setAddress(address);
			presentationModel.setFullProfileName(fullProfileName);
			logger.trace("All loaded data set in presentation model");
		}
	}

	private void setStandardProfile(MInventoryPresentationModel presentationModel) {
		presentationModel.setAddress("");
		presentationModel.setBackgroundImage(null);
		presentationModel.setFullProfileName(MInventoryPresentationModel.FULL_PROFILE_NAME);
		presentationModel.setWindowColor(MInventoryPresentationModel.WINDOW_COLOR);
	}
}