package GUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class Shortcut {

    public static final Shortcut F1 = new Shortcut("F1", "Show Help dialog",
            new KeyCodeCombination(KeyCode.F1));
    public static final Shortcut CTRL_N = new Shortcut("Ctrl + N", "Create a new invoice",
            new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
    public static final Shortcut CTRL_S = new Shortcut("Ctrl + S", "Save current invoice / Save quick item settings",
            new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
    public static final Shortcut CTRL_F = new Shortcut("Ctrl + F", "Show invoice store (find invoice)",
            new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));
    public static final Shortcut CTRL_SHIFT_F = new Shortcut("Ctrl + Shift + F", "Show calendar",
            new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
    public static final Shortcut CTRL_X = new Shortcut("Ctrl + X", "Show expense store",
            new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
    public static final Shortcut CTRL_P = new Shortcut("Ctrl + P", "Print current invoice",
            new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
    public static final Shortcut CTRL_D = new Shortcut("Ctrl + D", "Duplicate the current item in the table",
            new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
    public static final Shortcut CTRL_ENTER = new Shortcut("Ctrl + Enter", "In the mail dialog, send the email and close the dialog",
            new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN));

    public static ObservableList<Shortcut> all() {
        return FXCollections.observableArrayList(F1, CTRL_N, CTRL_S, CTRL_F, CTRL_SHIFT_F, CTRL_X, CTRL_P, CTRL_D, CTRL_ENTER);
    }

    private final StringProperty key;
    private final StringProperty description;
    private final KeyCombination keyCombo;

    public Shortcut(String key, String description, KeyCombination keyCombo) {
        this.key = new SimpleStringProperty(key);
        this.description = new SimpleStringProperty(description);
        this.keyCombo = keyCombo;
    }


    public String getKey() {
        return key.get();
    }

    public StringProperty keyProperty() {
        return key;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public KeyCombination getKeyCombo() {
        return keyCombo;
    }
}
