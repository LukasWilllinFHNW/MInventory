package minventory.gui;

import minventory.model.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lukas on 01.12.2015.
 */
public class MInventoryDetailedView extends GridPane implements ViewTemplate{

    private MInventoryDataModel dataModel;
    private MInventoryPresentationModel presModel;

    private ColumnConstraints cc; int ccAmount;
    private RowConstraints rc; int rcAmount;


    private TextField nameField;
    private TextField descriptionField;
    private TextArea descriptionArea;
    private TextField weightField;
    private ColorPicker colorPicker;
    private Slider stateOfDecaySlider;
    private TextField stateOfDecayNumberField;
    private ComboBox<String> typePickerEditor;
    private ComboBox<String> usageTypePickerEditor;
    private TextField distinctAttributeField;
    private TextField heightField;
    private TextField depthField;
    private TextField lengthField;

    private final BooleanProperty listenerShouldListen;

    public MInventoryDetailedView(MInventoryPresentationModel presModel, MInventoryDataModel dataModel){

        this.dataModel = dataModel;
        this.presModel = presModel;

        listenerShouldListen = new SimpleBooleanProperty(false);

        cc = new ColumnConstraints(); ccAmount = 8;
            cc.setPercentWidth((100/ccAmount));
            cc.setHgrow(Priority.ALWAYS);
        rc = new RowConstraints(); rcAmount = 10;
            rc.setPercentHeight((100/rcAmount));
            rc.setVgrow(Priority.ALWAYS);

        this.setPadding(new Insets(0, 0, 0, 12));
        this.setHgap(5);

        // -- Perform Startup Methods --
        initSequence();
    }


    // --- API ---
    public boolean hasSufficientInfo() {
        if (nameField.textProperty().get().length() > 2)
            return true;
        else return false;
    }


    // -- init sequence --
    @Override
    public void initializeControls() {
        nameField = new TextField();
            nameField.setPromptText("Enter a name");
        weightField = new TextField();
            weightField.setPromptText("Enter weight");
        heightField = new TextField();
            heightField.setPromptText("Enter a height");
        depthField = new TextField();
            depthField.setPromptText("Enter a depth");
        lengthField = new TextField();
            lengthField.setPromptText("Enter a length");
        distinctAttributeField = new TextField();
            distinctAttributeField.setPromptText("Enter attribute");


        descriptionArea = new TextArea();
            descriptionArea.setPromptText("Describe the object");

        colorPicker = new ColorPicker();

        stateOfDecaySlider = new Slider();
            stateOfDecaySlider.setMin(1);
            stateOfDecaySlider.setMax(100);
        stateOfDecayNumberField = new TextField();

        typePickerEditor = new ComboBox();
            typePickerEditor.setPromptText("Insert type");
            typePickerEditor.setEditable(true);
        usageTypePickerEditor = new ComboBox<>();
            usageTypePickerEditor.setPromptText("Insert type");
            usageTypePickerEditor.setEditable(true);
    }

    @Override
    public void initializeLayout() {
        for (int i = 0; i < ccAmount; ++i) this.getColumnConstraints().add(cc);
        for (int i = 0; i < rcAmount; ++i) this.getRowConstraints().add(rc);
    }

    @Override
    public void layoutPanes(){

    }

    @Override
    public void layoutControls() {

        this.add(new Label("Name")          , 0, 0);
        this.add(nameField                  , 0, 1, 3, 1);
        this.add(new Label("State of Decay"), 3, 0, 2, 1);
        this.add(stateOfDecaySlider         , 3, 1, 2, 1);
        this.add(stateOfDecayNumberField    , 5, 1, 1, 1);
        this.add(new Label("Color")         , 6, 0, 1, 1);
        this.add(colorPicker                , 6, 1, 1, 1);
        this.add(new Label("Description")   , 0, 2, 3, 1);
        this.add(descriptionArea            , 0, 3, 8, 2);
        this.add(new Label("Weight (kg)")   , 0, 5, 2, 1);
        this.add(weightField                , 0, 6, 2, 1);
        this.add(new Label("Length (m)")    , 2, 5, 2, 1);
        this.add(lengthField                , 2, 6, 2, 1);
        this.add(new Label("Height (m)")    , 4, 5, 2, 1);
        this.add(heightField                , 4, 6, 2, 1);
        this.add(new Label("Depth (m)")     , 6, 5, 2, 1);
        this.add(depthField                 , 6, 6, 2, 1);
        this.add(new Label("Type")          , 0, 7);
        this.add(typePickerEditor           , 0, 8, 3, 1);
        this.add(new Label("Type of use")   , 3, 7, 3, 1);
        this.add(usageTypePickerEditor      , 3, 8, 3, 1);
        this.add(new Label("Distinct attribute"), 6, 7, 3, 1);
        this.add(distinctAttributeField     , 6, 8, 3, 1);
    }

    @Override
    public void addListeners() {
        dataModel.getCurrentSelectedIdProperty().addListener((observable2, oldValue2, newValue2) -> {
            if (dataModel.getCurrentSelectedObject() != null) {
                listenerShouldListen.set(true);
            } else {
                listenerShouldListen.set(false);
            }
        });

        MInventoryObjectProxy proxy = dataModel.getProxy();

        if (proxy != null) {
                new BidirectionalListener(
                        this.nameField.textProperty(), proxy.getNameProperty()
                        , new Object[]{";"}, null, listenerShouldListen);
                new BidirectionalListener(
                        this.descriptionArea.textProperty(), proxy.getDescriptionProperty()
                        , new Object[]{";"}, null, listenerShouldListen);

                new BidirectionalListener(
                        this.distinctAttributeField.textProperty(), proxy.getDistinctAttributeProperty()
                        , new Object[]{";"}, null, listenerShouldListen);

                new BidirectionalListener(
                        this.stateOfDecaySlider.valueProperty(), proxy.getStateOfDecayProperty()
                        , null, null, listenerShouldListen);

                new BidirectionalListener(
                        this.colorPicker.valueProperty(), proxy.getColorProperty()
                        , null, null, listenerShouldListen);
                
                new BidirectionalListener(
                        this.typePickerEditor.getEditor().textProperty() , proxy.getTypeProperty()
                        , new Object[]{";"}, null, listenerShouldListen);
                
                new BidirectionalListener(
                        this.usageTypePickerEditor.getEditor().textProperty(), proxy.getUsageTypeProperty()
                        , new Object[]{";"}, null, listenerShouldListen);

                dataModel.getProxy().getLengthProperty().addListener((observable1, oldValue1, newValue1) -> {
                    this.lengthField.textProperty().setValue("" + newValue1.doubleValue());
                });

                dataModel.getProxy().getHeightProperty().addListener((observable1, oldValue1, newValue1) -> {
                    this.heightField.textProperty().setValue("" + newValue1.doubleValue());
                });

                dataModel.getProxy().getDepthProperty().addListener((observable1, oldValue1, newValue1) -> {
                    this.depthField.textProperty().setValue("" + newValue1.doubleValue());
                });

                dataModel.getProxy().getWeightProperty().addListener((observable1, oldValue1, newValue1) -> {
                    this.weightField.textProperty().setValue("" + newValue1.doubleValue());
                });

                dataModel.getProxy().getStateOfDecayProperty().addListener((observable1, oldValue1, newValue1) -> {
                    this.stateOfDecayNumberField.textProperty().setValue("" + newValue1);
                    this.stateOfDecaySlider.valueProperty().setValue(newValue1.intValue());
                });
                this.weightField.textProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        double weight = Double.parseDouble(newValue);
                        if (weight >= 0) proxy.getWeightProperty().setValue(weight);
                        else throw new IllegalStateException("Weight can not be null or negative");
                    } catch (NumberFormatException nfe) {
                        if (newValue.isEmpty()) weightField.setText("");
                        else weightField.setText(oldValue);
                        System.out.println(nfe.getMessage());
                    } catch (IllegalStateException ise) {
                        weightField.setText(oldValue);
                        weightField.setTooltip(new Tooltip(ise.getMessage()));
                    }
                });
                this.lengthField.textProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        double length = Double.parseDouble(newValue);
                        if (length >= 0) proxy.getLengthProperty().setValue(length);
                        else throw new IllegalStateException("Length can not be null or negative");
                    } catch (NumberFormatException nfe) {
                        if (newValue.isEmpty()) lengthField.setText("");
                        else lengthField.setText(oldValue);
                        System.out.println(nfe.getMessage());
                    } catch (IllegalStateException ise) {
                        lengthField.setText(oldValue);
                        lengthField.setTooltip(new Tooltip(ise.getMessage()));
                    }
                });
                this.heightField.textProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        double height = Double.parseDouble(newValue);
                        if (height >= 0) proxy.getHeightProperty().setValue(height);
                        else throw new IllegalStateException("Height can not be null or negative");
                    } catch (NumberFormatException nfe) {
                        if (newValue.isEmpty()) heightField.setText("");
                        else heightField.setText(oldValue);
                        System.out.println(nfe.getMessage());
                    } catch (IllegalStateException ise) {
                        heightField.setText(oldValue);
                        heightField.setTooltip(new Tooltip(ise.getMessage()));
                    }
                });
                this.depthField.textProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        double depth = Double.parseDouble(newValue);
                        if (depth >= 0) proxy.getDepthProperty().setValue(depth);
                        else throw new IllegalStateException("Depth can not be null or negative");
                    } catch (NumberFormatException nfe) {
                        if (newValue.isEmpty()) depthField.setText("");
                        else depthField.setText(oldValue);
                        System.out.println(nfe.getMessage());
                    } catch (IllegalStateException ise) {
                        depthField.setText(oldValue);
                        depthField.setTooltip(new Tooltip(ise.getMessage()));
                    }
                });
                this.stateOfDecayNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        int state = Integer.parseInt(newValue);
                        if (state > 0 && state <= 100) proxy.getStateOfDecayProperty().setValue(state);
                        else throw new IllegalStateException("Only numbers between 1 and 100 are allowed.");
                    } catch (NumberFormatException nfe) {
                        if (newValue.isEmpty()) stateOfDecayNumberField.setText("");
                        else stateOfDecayNumberField.setText(oldValue);
                        stateOfDecayNumberField.setTooltip(new Tooltip("Only numbers from 0-9 can be entered"));
                    } catch (IllegalStateException ise) {
                        stateOfDecayNumberField.setText(oldValue);
                        stateOfDecayNumberField.setTooltip(new Tooltip(ise.getMessage()));
                    }
                });

        }
        return;
    }

    @Override
    public void addBindings() {

    }

    @Override
    public void addEvents() {
        typePickerEditor.setOnMouseClicked(event -> {
            typePickerEditor.setItems(dataModel.getAllTypes());
        });
        usageTypePickerEditor.setOnMouseClicked(event -> {
            usageTypePickerEditor.setItems(dataModel.getAllUsageTypes());
        });
    }

    public void applyStylesheet() {

        this.setId("detailedViewGrid");
    }

    @Override
    public void applySpecialStyles() {
    }

    public void removeListeners() {

    }
}

/**
 * @author Lukas Willin
 * @implNote not fully implemented yet -> use with string properties only
 */
class BidirectionalListener implements ChangeListener {

    private final ChangeListener from1To2;
    private ChangeListener customListener;
    private final ChangeListener from2To1;
    private final Property changeable1;
    private final Property changeable2;
    private final List<Object> notAllowedValues;
    private final Map<Comparable, Comparable> checks;
    private boolean isListening;
    private final BooleanProperty shouldListen;

    public BidirectionalListener(Property property1, Property property2, Object[] notAllowedValues, Map<Comparable, Comparable> comparisonChecks, BooleanProperty shouldListen) {
        if (property1 == null || property2 == null) throw new IllegalArgumentException("Property values must not be null");

        this.changeable1 = property1;
        this.changeable2 = property2;

        // Check if not allowed values have been set
        this.notAllowedValues = new ArrayList<>();
        if (notAllowedValues != null) {
            for (int i = 0; i < notAllowedValues.length; ++i) {
                this.notAllowedValues.add(notAllowedValues[i]);
            }
        }
        // Check if any comparison checks have been set
        if (comparisonChecks == null) {
            this.checks = new HashMap<>();
        } else {
            this.checks = comparisonChecks;
        }
        // Check if a observable boolean variable has been set
        if (shouldListen != null) {
            this.shouldListen = shouldListen;
        } else {
            this.shouldListen = new SimpleBooleanProperty();
        }

        customListener = (observable3, oldValue3, newValue3) -> {
        };
        from1To2 = (observable1, oldValue1, newValue1) -> {
            changed(property1, oldValue1, newValue1);
        };
        from2To1 = (observable2, oldValue2, newValue2) -> {
            changed2(observable2, oldValue2, newValue2);
        };

        this.shouldListen.addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue) {
                    startListening();
                } else {
                    stopListening();
                }
            } catch (IllegalStateException ise) {
                System.out.println(ise);
            }
        });

        property1.addListener(from1To2);
        property1.addListener(customListener);
        property2.addListener(from2To1);
        isListening = true;
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if (newValue != null) {
            try {
                if (!notAllowedValues.isEmpty()) {
                    for (Object notAllowed : notAllowedValues) {
                        try {
                            if (((String) newValue).contains((String) notAllowed))
                                throw new IllegalArgumentException("The new value contains at least one of all not allowed values");
                        } catch (ClassCastException cce) {
                            throw new IllegalStateException("Changeable1 is not a StringProperty and can not be checked for not allowed values.");
                        }
                    }
                }
                if (!checks.isEmpty()) {
                    for (Map.Entry<Comparable, Comparable> entry : checks.entrySet()) {
                        if (entry.getKey().compareTo(entry.getValue()) < 0) {
                            throw new IllegalArgumentException("The new value doesn't match the comparison checks.");
                        }
                    }
                }
                this.changeable2.setValue(newValue);
            } catch (IllegalStateException ise) {
                this.changeable1.setValue(oldValue);
            } catch (IllegalArgumentException iae) {
                this.changeable1.setValue(oldValue);
            }
        }
    }

    public void changed2(ObservableValue observable, Object oldValue, Object newValue){
        if(newValue != null) {
            try {
                if (!notAllowedValues.isEmpty()) {
                    for (Object notAllowed : notAllowedValues) {
                        try {
                            if (((String) newValue).contains((String) notAllowed))
                                throw new IllegalArgumentException("The new value contains at least one of all not allowed values");
                        } catch (ClassCastException cce) {
                            throw new IllegalStateException("Changeable1 is not a StringProperty and can not be checked for not allowed values.");
                        }
                    }
                }
                if (!checks.isEmpty()) {
                    for (Map.Entry<Comparable, Comparable> entry : checks.entrySet()) {
                        if (entry.getKey().compareTo(entry.getValue()) < 0) {
                            throw new IllegalArgumentException("The new value doesn't match the checks.");
                        }
                    }
                }
                this.changeable1.setValue(newValue);
            } catch (IllegalStateException ise) {
                this.changeable2.setValue(oldValue);
            } catch (IllegalArgumentException iae) {
                this.changeable2.setValue(oldValue);
            }
        }
    }

    public void stopListening() {
        if (isListening) {
            changeable1.removeListener(from1To2);
            changeable1.removeListener(customListener);
            changeable2.removeListener(from2To1);
            isListening = false;
        } else {
            throw new IllegalStateException("Already listening");
        }
    }

    public void startListening() {
        if (!isListening) {
            changeable1.addListener(from1To2);
            changeable1.removeListener(customListener);
            changeable2.addListener(from2To1);
            isListening = true;
        } else {
            throw new IllegalStateException("AlreadyListening");
        }
    }

    public boolean isListening() {
        return isListening;
    }

    /**
     * Changes behaviour of listener from changeable1 to changeable2
     * not allowed values are no more applied
     * comparison checks are no more applied
     * @param customListener customListener
     */
    public void setCustomListener(ChangeListener customListener) {
        customListener = customListener;
    }
}