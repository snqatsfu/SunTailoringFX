package GUI;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

public class CalendarDialogController implements Initializable {

    public GridPane calendarGrid;
    public HBox weekdayHeader;
    public ScrollPane scrollPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCalendarGrid();
        initializeWeekdayHeader();
        generateCalendar();
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

    private void generateCalendar() {
        loadCalendarLabels();
        populateMonthWithEvents();
    }

    private void loadCalendarLabels() {
        Calendar now = Calendar.getInstance();
        // initialize a Gregorian calendar with current year / month and the first day of the month
        GregorianCalendar gc = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 1);

        int firstDay = gc.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
        int offset = firstDay;
        int gridCount = 1;
        int dayLabelCount = 1;

        for (Node node : calendarGrid.getChildren()) {
            VBox day = (VBox) node;
            day.getChildren().clear();
            day.setStyle("-fx-background-color: white");
            day.setStyle("-fx-font: 14px");

            if (gridCount < offset) {
                gridCount++;
                // darken days before the first day of the month
                day.setStyle("-fx-background-color: lightgray");
            } else {
                if (dayLabelCount > daysInMonth) {
                    // darken days after the last day of the month
                    day.setStyle("-fx-background-color: lightgray");

                } else {
                    // make a new day label
                    Label dayLabel = new Label(Integer.toString(dayLabelCount));
                    dayLabel.setPadding(new Insets(5));
                    dayLabel.setStyle("-fx-text-fill: darkslategray");

                    day.getChildren().add(dayLabel);
                }

                dayLabelCount++;
            }
        }
    }

    private void populateMonthWithEvents() {

    }

}
