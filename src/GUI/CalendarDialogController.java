package GUI;

import Data.Invoice;
import Data.InvoiceStore;
import Data.Item;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.converter.CurrencyStringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class CalendarDialogController implements Initializable {

    public GridPane calendarGrid;
    public HBox weekdayHeader;
    public ScrollPane scrollPane;

    private InvoiceStore invoiceStore;
    private ReadOnlyObjectWrapper<String> selectedInvoiceNumber;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCalendarGrid();
        initializeWeekdayHeader();
        selectedInvoiceNumber = new ReadOnlyObjectWrapper<>();
    }

    public void setInvoiceStore(InvoiceStore invoiceStore) {
        this.invoiceStore = invoiceStore;
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
        populateMonthWithInvoices();
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
            VBox dayBox = (VBox) node;
            dayBox.getChildren().clear();
            dayBox.setStyle("-fx-background-color: white");
            dayBox.setStyle("-fx-font: 14px");
            dayBox.setSpacing(2.0);

            if (gridCount < offset) {
                gridCount++;
                // darken days before the first day of the month
                dayBox.getStyleClass().add("inactive_day_box");
            } else {
                if (dayLabelCount > daysInMonth) {
                    // darken days after the last day of the month
                    dayBox.getStyleClass().add("inactive_day_box");

                } else {
                    if (dayLabelCount == LocalDate.now().getDayOfMonth()) {
                        dayBox.getStyleClass().add("today_box");
                    }

                    // make a new day label
                    Label dayLabel = new Label(Integer.toString(dayLabelCount));
                    dayLabel.setPadding(new Insets(5));
                    dayLabel.setStyle("-fx-text-fill: darkslategray");

                    dayBox.getChildren().add(dayLabel);
                }

                dayLabelCount++;
            }
        }
    }

    private void populateMonthWithInvoices() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());

        Map<Integer, List<Invoice>> map = new HashMap<>();

        invoiceStore.all().stream()
                .filter(invoice -> !(invoice.getInvoiceDate().isBefore(start) || invoice.getInvoiceDate().isAfter(end)))
                .forEach(invoice -> {
                    int dayOfMonth = invoice.getInvoiceDate().getDayOfMonth();
                    if (map.get(dayOfMonth) == null) {
                        map.put(dayOfMonth, new ArrayList<>());
                    }
                    map.get(dayOfMonth).add(invoice);
                });

        calendarGrid.getChildren().stream().map(node -> ((VBox) node))
                .filter(dayBox -> !dayBox.getChildren().isEmpty())
                .forEach(dayBox -> {
                    Label dayLabel = (Label) dayBox.getChildren().get(0);
                    int dayNumber = Integer.parseInt(dayLabel.getText());
                    List<Invoice> invoicesOfThisDay = map.get(dayNumber);
                    if (invoicesOfThisDay != null) {
                        invoicesOfThisDay.stream().forEach(invoice -> {
                            Label invoiceLabel = new Label(invoice.getInvoiceNumber());
                            invoiceLabel.getStyleClass().add(invoice.getDone() ? "invoice_label" : "invoice_label_not_done");
                            invoiceLabel.setMaxWidth(Double.MAX_VALUE);

                            // double click on the label will close this window and load the selected invoice in main window
                            invoiceLabel.setOnMouseClicked(event -> {
                                if (event.getClickCount() == 2) {
                                    selectedInvoiceNumber.setValue(invoiceLabel.getText());
                                    final Stage stage = (Stage) invoiceLabel.getScene().getWindow();
                                    stage.close();
                                }
                            });

                            invoiceLabel.setTooltip(new Tooltip(getInvoiceTooltip(invoice)));

                            // todo: mouse enter to display invoice summary. mouse exit to dismiss.
//                            invoiceLabel.setOnMouseEntered(event -> System.out.println("mouse entered"));
//                            invoiceLabel.setOnMouseExited(event -> System.out.println("mouse exited"));

                            dayBox.getChildren().add(invoiceLabel);
                        });
                    }
                });

    }

    public ReadOnlyObjectWrapper<String> selectedInvoiceNumberProperty() {
        return selectedInvoiceNumber;
    }

    private static String getInvoiceTooltip(Invoice invoice) {
        String retVal = invoice.getCustomerInfo().getName();
        retVal += "\nTotal: " + new CurrencyStringConverter().toString(invoice.getTotal());
        retVal += "\n" + (invoice.getPaid() ? "Paid" : "Not Paid")
                + ", " + (invoice.getDone() ? "Done" : "Not Done")
                + ", " + (invoice.getPickedUp() ? "Picked Up" : "Not Picked Up");
        for (Item item : invoice.getItems()) {
            retVal += "\n" + item.shortSummary();
        }
        return retVal;
    }
}
