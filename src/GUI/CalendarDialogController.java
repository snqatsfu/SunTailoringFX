package GUI;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

public class CalendarDialogController implements Initializable {

    public GridPane calendarGrid;
    public HBox weekdayHeader;
    public ScrollPane scrollPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCalendarGrid();
        initializeWeekdayHeader();
    }

    private void initializeCalendarGrid() {
        int numRows = 6;
        int numCols = 7;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                VBox vPane = new VBox();
                vPane.getStyleClass().add("calendar_pane");
                vPane.setMinWidth(weekdayHeader.getPrefWidth() / 7);

                GridPane.setVgrow(vPane, Priority.ALWAYS);

                calendarGrid.add(vPane, col, row);
            }
        }

        // set up row constraints
        for (int i = 0; i < 7; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(scrollPane.getHeight() / 7);
            calendarGrid.getRowConstraints().add(rowConstraints);
        }
    }

    private void initializeWeekdayHeader() {
        int weekdays = 7;
        String[] weekAbbr = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        for (int i = 0; i < weekdays; i++) {
            StackPane pane = new StackPane();
            pane.getStyleClass().add("weekday-header");

            // Make panes take up equal space
            HBox.setHgrow(pane, Priority.ALWAYS);
            pane.setMaxWidth(Double.MAX_VALUE);

            // Note: After adding a label to this, it tries to resize itself..
            // So I'm setting a minimum width.
            pane.setMinWidth(weekdayHeader.getPrefWidth() / 7);

            weekdayHeader.getChildren().add(pane);

            pane.getChildren().add(new Label(weekAbbr[i]));
        }
    }


}
