package ch.fhnw.oop2.control;

/**
 * Class created by Lukas Willin on 03.01.2016.
 * @author Dieter Holz
 *
public class ValueChangeCommand implements Command {
    private final EuropePM europe;
    private final Property property;
    private final Object oldValue;
    private final Object   newValue;

    public ValueChangeCommand(EuropePM europe, Property property, Object oldValue, Object newValue) {
        this.europe = europe;
        this.property = property;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public void undo() {
        europe.setPropertyValue(property, oldValue);
    }

    public void redo() {
        europe.setPropertyValue(property, newValue);
    }
}*/
