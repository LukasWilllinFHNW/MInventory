package ch.fhnw.oop2.control;

/**
 * Class created by Lukas Willin on 03.01.2016.
 * @author Dieter Holz
 *
public class AddCommand implements Command {

    private final EuropePM  europe;
    private final CountryPM added;
    private final int       position;

    public AddCommand(EuropePM europe, CountryPM added, int position) {
        this.europe = europe;
        this.added = added;
        this.position = position;
    }

    @Override
    public void undo() {
        europe.removeFromList(added);
    }

    @Override
    public void redo() {
        europe.addToList(position, added);
    }
}*/
