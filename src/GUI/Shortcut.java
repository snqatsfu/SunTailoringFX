package GUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Shortcut {
    final StringProperty key;
    final StringProperty description;

    public Shortcut(String key, String description) {
        this.key = new SimpleStringProperty(key);
        this.description = new SimpleStringProperty(description);
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
}
