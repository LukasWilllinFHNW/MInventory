package ch.fhnw.oop2.control;

/**
 * Class created by Lukas Willin on 03.01.2016.
 * @author Dieter Holz
 */
public interface Command {
    void undo();

    void redo();

    public void updateUndo(Object oldValue);

    public void updateRedo(Object newValue);
}
