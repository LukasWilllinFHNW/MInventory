package minventory.control;

import minventory.model.MInventoryDataModel;
import minventory.model.MInventoryItem;
import minventory.model.MInventoryObject;
import minventory.model.MInventoryObjectProxy;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Class created by Lukas Willin on 03.01.2016.
 * @author Dieter Holz
 */
public class RemoveCommand implements Command {
    private final MInventoryDataModel dataModel;
    private final MInventoryObject removed;
    private final int position;

    public RemoveCommand(MInventoryDataModel dataModel, MInventoryObject removed, int position) {
        this.dataModel = dataModel;
        this.removed = removed;
        this.position = position;
    }

    @Override
    public void undo() {
        dataModel.addToList(position, removed);
    }

    @Override
    public void redo() {
        dataModel.delete(removed);
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
