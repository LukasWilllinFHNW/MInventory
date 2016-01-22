package minventory.gui;

/**
 * Created by Lukas on 15.12.2015.
 */
public interface ViewTemplate {

    default void initSequence() {
        initializeControls();
        initializeLayout();
        layoutPanes();
        layoutControls();
        addListeners();
        addBindings();
        addEvents();
        applyStylesheet();
        applySpecialStyles();
    }

    public void initializeControls() ;

    public void initializeLayout() ;

    public void layoutPanes() ;

    public void layoutControls() ;

    default void addListeners() {}

    default void addBindings() {}

    public void addEvents() ;

    default void applyStylesheet() {}

    default void applySpecialStyles() {}
}
