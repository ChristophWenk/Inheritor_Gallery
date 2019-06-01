package ch.fhnw.oop2.countryfx.view;

/**
 * Created by Hamster on 31.05.2017.
 */
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
