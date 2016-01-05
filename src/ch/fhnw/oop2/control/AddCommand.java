package ch.fhnw.oop2.control;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Class created by Lukas Willin on 03.01.2016.
 * @author Dieter Holz
 */
public class AddCommand implements Command {

    private final MInventoryDataModel dataModel;
    private final MInventoryObject added;
    private final int position;

    public AddCommand(MInventoryDataModel dataModel, MInventoryObject added, int position) {
        this.dataModel = dataModel;
        this.added = added;
        this.position = position;
    }

    @Override
    public void undo() {
        dataModel.delete(added);
    }

    @Override
    public void redo() {
        dataModel.addToList(position, added);
    }

    /**
     * Not supportet function throws NotImplementedException
     * @param oldValue can not be set
     * @throws NotImplementedException
     */
    @Override
    public void updateUndo(Object oldValue) {
        throw new NotImplementedException();
    }

    /**
     * Not supportet function throws NotImplementedException
     * @param newValue can not be set
     * @throws NotImplementedException
     */
    @Override
    public void updateRedo(Object newValue) {
        throw new NotImplementedException();
    }
}
