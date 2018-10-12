package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SunTailoringGUI extends Application {

    private static final String APP_TITLE = "Sun Tailoring";

    @Override
    public void start(Stage primaryStage) throws Exception {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SunTailoringGUI.fxml"));
        Parent root = fxmlLoader.load();
        final SunTailoringGUIController controller = fxmlLoader.getController();
        primaryStage.setTitle(APP_TITLE);
        primaryStage.getIcons().add(Assets.ST_LOGO);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> controller.saveAddressBook());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
