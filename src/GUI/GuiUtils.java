package GUI;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.util.Optional;

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

    public static boolean showConfirmationAlertAndWait(String confirmationMessage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(confirmationMessage);
        final Optional<ButtonType> option = alert.showAndWait();
        return option.get() == ButtonType.OK;
    }

    public static final KeyCombination KEY_COMBO_CTRL_S = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination KEY_COMBO_CTRL_F = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination KEY_COMBO_CTRL_N = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination KEY_COMBO_CTRL_P = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination KEY_COMBO_CTRL_D = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination KEY_COMBO_CTRL_ENTER = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN);

}
