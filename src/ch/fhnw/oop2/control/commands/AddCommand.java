package ch.fhnw.oop2.control.commands;

import ch.fhnw.oop2.model.MInventoryObject;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * Created by Lukas on 01.12.2015.
 */
@Parameters ( commandDescription = "Adds a new object to the inventory.")
public class AddCommand {
    @Parameter
    private MInventoryObject mInventoryObject;

    public MInventoryObject getmInventoryObject(){
        return this.mInventoryObject;
    }
}
