package inheritorgallery.view;

/**
 * Interface that defines the default methods a view should implement.
 */
public interface ViewMixin {

    /**
     * Initialize the whole pane
     */
    default void init() {
        initializeControls();
        layoutControls();
        setupEventHandlers();
        setupValueChangedListeners();
        setupBindings();
    }

    /**
     * Initialize and create the controls implemented by the panel
     */
    void initializeControls();

    /**
     * Layout and design the controls implemented by the panel
     */
    void layoutControls();

    /**
     * Setup and create any event handlers implemented by the panel
     */
    default void setupEventHandlers() {
    }

    /**
     * Setup any change listeners implemented by the panel
     */
    default void setupValueChangedListeners() {
    }

    /**
     * Setup any bindings implemented by the panel.
     */
    default void setupBindings() {
    }
}
