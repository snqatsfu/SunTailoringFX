package Data;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * TODO: CLASS JAVA DOC HERE
 */
public class AutoCompletionTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        TextField textField = new TextField();

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
        });

        VBox layout = new VBox();
        layout.getChildren().addAll(textField, addButton);

        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
