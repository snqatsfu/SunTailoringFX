package GUI;

import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GuiUtils {
    public static void showInfoAlertAndWait(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }

    public static void showWarningAlertAndWait(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }

    public static final KeyCombination KEY_COMBO_CTRL_S = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination KEY_COMBO_CTRL_F = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination KEY_COMBO_CTRL_N = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);

    static final Path SAVE_DIR_PATH = Paths.get("Save");
    static final Path REPORT_DIR_PATH = Paths.get("InvoiceReports");
    public static final Path SETTINGS_DIR_PATH = Paths.get("Settings");
    static final File ADDRESS_BOOK_DAT_FILE = new File(SETTINGS_DIR_PATH + "/" + "addressBook.dat");
    static File getQuickItemsDatFile(String name) {
        return new File(SETTINGS_DIR_PATH + "/" + "quick" + name.replaceAll(" ", "") + "Settings.dat");
    }

    public static void createDirectoryIfNecessary(Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
    }

}
