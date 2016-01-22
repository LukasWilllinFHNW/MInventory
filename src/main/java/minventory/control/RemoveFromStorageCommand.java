package minventory.control;

import minventory.model.MInventoryDataModel;
import minventory.model.MInventoryObject;
import minventory.model.MInventoryStorage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Lukas on 05.01.2016.
 */
public class RemoveFromStorageCommand implements Command {

    private final MInventoryDataModel dataModel;
    private final MInventoryObject remove;
    private final MInventoryStorage containing;

    public RemoveFromStorageCommand(MInventoryDataModel dataModel, MInventoryStorage containing, MInventoryObject remove) {
        this.dataModel = dataModel;
        this.remove = remove;
        this.containing = containing;
    }

    @Override
    public void undo() {
        dataModel.redoPutObjectIntoStorage(containing, remove);
    }

    @Override
    public void redo() {
        dataModel.redoRemoveFromStorage(remove);
    }

    @Override
    public void updateUndo(Object oldValue) {
        throw new NotImplementedException();
    }

    @Override
    public void updateRedo(Object newValue) {
        throw new NotImplementedException();
    }
}
