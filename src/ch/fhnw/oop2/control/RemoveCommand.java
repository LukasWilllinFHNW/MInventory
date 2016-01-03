package ch.fhnw.oop2.control;

/**
 * Class created by Lukas Willin on 03.01.2016.
 * @author Dieter Holz
 *
public class RemoveCommand implements Command {
    private final EuropePM  europe;
    private final CountryPM removed;
    private final int       position;

    public RemoveCommand(EuropePM europe, CountryPM removed, int position) {
        this.europe = europe;
        this.removed = removed;
        this.position = position;
    }

    @Override
    public void undo() {
        europe.allCountries().add(position, removed);
        europe.setSelectedCountryId(removed.getId());
    }

    @Override
    public void redo() {
        europe.removeFromList(removed);
    }
}*/
