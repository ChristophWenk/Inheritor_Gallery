package inheritorgallery.view;

public interface ViewMixin {

    default void init() {
        initializeControls();
        layoutControls();
        setupEventHandlers();
        setupValueChangedListeners();
        setupBindings();
    }

    void initializeControls();

    void layoutControls();

    default void setupEventHandlers() {
    }

    default void setupValueChangedListeners() {
    }

    default void setupBindings() {
    }
}
