package minventory.control;

import java.util.Calendar;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import minventory.model.MInventoryDataModel;
import minventory.model.MInventoryObject;

public class UnRedoCtrl {
    
    // Stacks for UndoRedo
    private static final ObservableList<Command> undoStack = FXCollections.observableArrayList();
    private static final ObservableList<Command> redoStack = FXCollections.observableArrayList();

    // If node should be disabled
    public static final BooleanProperty undoDisabled = new SimpleBooleanProperty();
    public static final BooleanProperty redoDisabled = new SimpleBooleanProperty();

    // --- Variables for delayed UndoRedo ---
    private static long lastLoggedTimeInMillis = Calendar.getInstance().getTimeInMillis();
    private static long waitTime = 6000;
    private static ObservableValue lastChangedProperty;
    private static Object lastOldValue; private static Object lastNewValue;
    private static int lastObjectId = -3; // TODO: check if selection changed
    
    public static final ChangeListener<Object> propertyChangeListenerForUndoSupport = (observable, oldValue, newValue) -> {
        trackChange(observable, oldValue, newValue);
    };
    public static final ChangeListener<? super Number> objectIdListener = (observable, oldValue, newValue) -> {
        //trackChange(lastChangedProperty, lastOldValue, lastNewValue);
    };
    
    // --- INITIALIZE ---
    {
        undoDisabled.bind(Bindings.isEmpty(undoStack));
        redoDisabled.bind(Bindings.isEmpty(redoStack));
        // TODO This line isnt able to add the listener to the currentSelectedId-property find out why?
        // For now the objectIdListener is added in the MInventoryModel's constructor (Which is pretty hacky)
        MInventoryDataModel.getInstance().currentSelectedId.addListener(objectIdListener);
    }
    
    // --- API ---
    /**
     * Creates a new undo instance and applies it to the undo-stack
     * deleting the redo-stack's content and resetting the timer
     * the undo stack values are like a snapshot of the property on command creation
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private static void createUndo(ObservableValue observable, Object oldValue, Object newValue) {
        redoStack.clear();
        undoStack.add(0, new ValueChangeCommand(MInventoryDataModel.inst, (Property) observable, oldValue, newValue));
        // New property -> update last changed property values
        lastChangedProperty = observable;
        lastOldValue = oldValue;
        lastNewValue = newValue;
        lastLoggedTimeInMillis = Calendar.getInstance().getTimeInMillis();
        lastObjectId = MInventoryDataModel.getInstance().getCurrentSelectedId();
    }
    /**
     * Updates the last added undo instance with newer value
     * and then resets the timer
     * TODO there might be an error in the code here
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private static void updateUndo(ObservableValue observable, Object oldValue, Object newValue) {
        undoStack.get(0).updateRedo(newValue);
        // New property -> update last changed property values
        lastLoggedTimeInMillis = Calendar.getInstance().getTimeInMillis();
        lastObjectId = MInventoryDataModel.getInstance().getCurrentSelectedId();
        lastChangedProperty = observable;
        lastOldValue = oldValue;
        lastNewValue = newValue;
    }
    public static void addUndo(Command command) {
        undoStack.add(0, command);
    }
    
    // --- undo redo ---
    /**
     * Applies the last added undo-instance
     */
    public static void undo() {
        if (undoStack.isEmpty()) {
            return;
        }
        Command cmd = undoStack.get(0);
        undoStack.remove(0);
        redoStack.add(0, cmd);

        cmd.undo();
    }
    /**
     * applies the last added redo-instance
     */
    public static void redo() {
        if (redoStack.isEmpty()) {
            return;
        }
        Command cmd = redoStack.get(0);
        redoStack.remove(0);
        undoStack.add(0, cmd);

        cmd.redo();
    }
    
    // disableUndoRedo
    public static void disableUndoSupport(MInventoryObject object) {
        // MAKE SURE ONLY ONE LISTENER PER OBJECT IS REMOVED
        object.getNameProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getDescriptionProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getColorProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getImageProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getLengthProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getHeightProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getDepthProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getWeightProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getDistinctAttributeProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getTypeProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getUsageTypeProperty().removeListener(propertyChangeListenerForUndoSupport);
        object.getStateOfDecayProperty().removeListener(propertyChangeListenerForUndoSupport);
    }
    public static void enableUndoSupport(MInventoryObject object) {
        // MAKE SURE ONLY ONE LISTENER PER PROPERTY IS ADDED
        object.getNameProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getDescriptionProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getColorProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getImageProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getLengthProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getHeightProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getDepthProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getWeightProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getDistinctAttributeProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getTypeProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getUsageTypeProperty().addListener(propertyChangeListenerForUndoSupport);
        object.getStateOfDecayProperty().addListener(propertyChangeListenerForUndoSupport);
    }
    
    /**
     * Sets a value for any property
     * @param property the property of which the value should be changed
     * @param newValue the new Value of the property
     */
    public static void setPropertyValue(Property property, Object newValue){
        property.removeListener(UnRedoCtrl.propertyChangeListenerForUndoSupport);
        property.setValue(newValue);
        property.addListener(UnRedoCtrl.propertyChangeListenerForUndoSupport);
    }

    /**
     * This function keeps track of the cahnges made in inventory objects
     * It manages:
     *  - the timer used to delay the undo-instance creation
     *  - undo-instance creation and/or update procedure
     * An new undo-instance will be created when:
     *  - last tracked change is older than x milli seconds
     *  - a new object is selected
     *  - the selected property has changed
     * The last undo-instance is updated when:
     *  - last tracked change is younger than x milli seconds
     *      & neither the property nor the object has changed
     *      
     * TODO: Maybe I should only one time do the create new command at the start and then decide whether i update it or not?
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private static void trackChange(ObservableValue observable, Object oldValue, Object newValue) {
        if (!undoStack.isEmpty()) {
            if (lastObjectId == MInventoryDataModel.getInstance().getCurrentSelectedId()) {
                // Still the same object selected
                //if (undoStack.get(0) instanceof ValueChangeCommand) {
                if (lastChangedProperty == observable) {
                    // property still the same
                    if (Calendar.getInstance().getTimeInMillis() > lastLoggedTimeInMillis + waitTime) {
                        // Last logged time is further in the past then wait time and should create a new undo
                        createUndo(observable, oldValue, newValue);
                    } else {
                        // Last logged time is not far back enough to create a new undo
                        updateUndo(observable, oldValue, newValue);
                    }
                } else {
                    // changed the property then must create new undo for old property
                    if (lastChangedProperty!=null)
                        //createUndo(lastChangedProperty, lastOldValue, lastNewValue);
                    createUndo(observable, oldValue, newValue);
                }                
            } else {
                // other object has been selected
                if (lastObjectId != -3)
                    //createUndo(lastChangedProperty, lastOldValue, lastNewValue);
                createUndo(observable, oldValue, newValue);
                //lastObjectId = MInventoryDataModel.getInstance().getCurrentSelectedId();
            }
        } else {
            createUndo(observable, oldValue, newValue);
        }
    }
    
}
