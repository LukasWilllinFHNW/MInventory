package ch.fhnw.oop2.control;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryObject;
import ch.fhnw.oop2.model.MInventoryStorage;
import javafx.beans.value.ObservableValue;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Lukas on 05.01.2016.
 */
public class AddToStorageCommand implements Command {

    private final MInventoryDataModel dataModel;
    private final MInventoryObject added;
    private final MInventoryStorage containing;

    public AddToStorageCommand(MInventoryDataModel dataModel, MInventoryObject added, MInventoryStorage containing) {
        this.dataModel = dataModel;
        this.added = added;
        this.containing = containing;
    }

    @Override
    public void undo() {
        dataModel.redoRemoveFromStorage(added);
    }

    @Override
    public void redo() {
        dataModel.redoPutObjectIntoStorage(containing, added);
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
