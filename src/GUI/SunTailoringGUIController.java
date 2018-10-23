package GUI;

import Data.*;
import Html.Element;
import Html.InvoiceHtml;
import Utils.PropertiesConfiguration;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.CurrencyStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.ResourceBundle;

import static GUI.GuiUtils.*;

public class SunTailoringGUIController implements Initializable {

    @FXML
    public BorderPane rootPane;

    @FXML
    public MenuItem quickJacketsSettingsMenuItem;
    @FXML
    public MenuItem quickPantsSettingsMenuItem;
    @FXML
    public MenuItem quickShirtsSettingsMenuItem;
    @FXML
    public MenuItem quickDryCleansSettingsMenuItem;
    @FXML
    public MenuItem quickOthersSettingsMenuItem;

    @FXML
    private TextField findInvoiceNumberTextField;
    @FXML
    private Button newInvoiceButton;

    @FXML
    private TextField invoiceNumberTextField;
    @FXML
    private DatePicker invoiceDatePicker;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private CheckBox paidCheckBox;
    @FXML
    private CheckBox doneCheckBox;
    @FXML
    private CheckBox pickedUpCheckBox;

    @FXML
    public ImageView addressBookImageButton;
    @FXML
    public TextField customerNameTextField;
    @FXML
    public TextField customerPhoneTextField;
    @FXML
    public TextField customerEmailTextField;

    @FXML
    public Label subtotalLabel;
    @FXML
    public Label taxLabel;
    @FXML
    public Label totalLabel;
    @FXML
    public TextField creditTextField;

    @FXML
    public Button saveInvoiceButton;
    @FXML
    public Button mailInvoiceButton;

    @FXML
    public ComboBox<Item> quickJacketComboBox;
    @FXML
    public ComboBox<Item> quickPantComboBox;
    @FXML
    public ComboBox<Item> quickShirtComboBox;
    @FXML
    public ComboBox<Item> quickDryCleanComboBox;
    @FXML
    public ComboBox<Item> quickOtherComboBox;

    @FXML
    public TableView<Item> itemsTable;
    @FXML
    public TableColumn<Item, String> itemsTableNameCol;
    @FXML
    public TableColumn<Item, Integer> itemsTableQuantityCol;
    @FXML
    public TableColumn<Item, Double> itemsTableUnitPriceCol;
    @FXML
    public TableColumn<Item, Double> itemsTablePriceCol;

    private static final InvoiceStore invoiceStore = InvoiceStore.getInstance();
    private static final Properties config = PropertiesConfiguration.getInstance();

    private final Invoice activeInvoice;
    private Invoice baselineInvoice;
    private ActiveInvoiceState activeInvoiceState;
    private boolean suspendActiveInvoiceStateUpdate;
    private boolean suspendQuickItemComboBoxAction;
    private final AddressBook addressBook;

    private enum ActiveInvoiceState {
        NEW,
        EDITED,
        SAVED
    }

    private void setActiveInvoiceState(ActiveInvoiceState newState) {
        if (!suspendActiveInvoiceStateUpdate) {
            ActiveInvoiceState oldState = activeInvoiceState;
            switch (newState) {
                case NEW:
                case SAVED:
                    activeInvoiceState = newState;
                    break;
                case EDITED:
                    // any edit to a new invoice won't change the state to the EDITED state
                    if (oldState != ActiveInvoiceState.NEW) {
                        if (activeInvoice.equals(baselineInvoice)) {
                            activeInvoiceState = ActiveInvoiceState.SAVED;
                        } else {
                            activeInvoiceState = newState;
                        }
                    }
                    break;
                default:
                    break;
            }

            if (activeInvoiceState != oldState) {
                updateInvoiceNumberTextFieldBackgroundColor();
            }
        }
    }

    private void updateInvoiceNumberTextFieldBackgroundColor() {
        if (invoiceNumberTextField != null) {
            switch (activeInvoiceState) {
                case NEW:
                    invoiceNumberTextField.setStyle("-fx-control-inner-background: lightgreen");
                    break;
                case EDITED:
                    invoiceNumberTextField.setStyle("-fx-control-inner-background: lightpink");
                    break;
                case SAVED:
                    invoiceNumberTextField.setStyle("-fx-control-inner-background: white");
                    break;
                default:
                    break;
            }
        }
    }

    public SunTailoringGUIController() {
        activeInvoice = Invoice.createEmptyInvoice(generateInvoiceNumber());
        baselineInvoice = activeInvoice.copy();
        activeInvoiceState = ActiveInvoiceState.NEW;
        updateInvoiceNumberTextFieldBackgroundColor();

        AddressBook addressBook = null;
        if (ADDRESS_BOOK_DAT_FILE.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ADDRESS_BOOK_DAT_FILE))) {
                addressBook = AddressBook.deserialize(ois);
            } catch (Exception ignore) {
            }
        }

        if (addressBook == null) {
            GuiUtils.showWarningAlertAndWait("Can't find saved address book in Settings. Address book will be empty");
            addressBook = new AddressBook(Collections.emptyList());
        }
        this.addressBook = addressBook;
    }

    public void openInvoice() {
        String invoiceNumber = findInvoiceNumberTextField.getText();
        final Invoice invoice = invoiceStore.get(invoiceNumber);
        if (invoice != null) {
            setActiveInvoice(invoice, ActiveInvoiceState.SAVED);
        } else {
            GuiUtils.showInfoAlertAndWait(invoiceNumber + " does not exist");
        }
    }

    public void invoiceNumberTextFieldClicked() {
//        findInvoiceNumberTextField.setText("");
    }

    public void createNewInvoice() {
        String invoiceNumber = generateInvoiceNumber();
        setActiveInvoice(Invoice.createEmptyInvoice(invoiceNumber), ActiveInvoiceState.NEW);
    }

    private String generateInvoiceNumber() {
        final LocalDate now = LocalDate.now();
        final String base = now.format(DateTimeFormatter.ofPattern("yyMMdd"));
        int invoiceNum = 0;
        String invoiceNumber = base + String.format("%03d", invoiceNum);
        // todo: change to search invoice store
        while (invoiceStore.contains(invoiceNumber)) {
            invoiceNum++;
            if (invoiceNum > 999) {
                return "InvalidInvoiceNumber";
            }
            invoiceNumber = base + String.format("%03d", invoiceNum);
        }
        return invoiceNumber;
    }

    public void saveActiveInvoice() {
        invoiceStore.save(activeInvoice);
        baselineInvoice = activeInvoice.copy();
        setActiveInvoiceState(ActiveInvoiceState.SAVED);
        addressBook.add(activeInvoice.getCustomerInfo().copy());
        GuiUtils.showInfoAlertAndWait("Saved Invoice " + activeInvoice.getInvoiceNumber());
    }

    @SuppressWarnings("unchecked")
    public void quickItemComboBoxOnAction(ActionEvent actionEvent) {
        if (!suspendQuickItemComboBoxAction) {
            final Object source = actionEvent.getSource();
            if (source == quickJacketComboBox || source == quickPantComboBox || source == quickShirtComboBox
                    || source == quickDryCleanComboBox || source == quickOtherComboBox) {
                final Item selectedItem = ((ComboBox<Item>) source).getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    activeInvoice.getItems().add(selectedItem.copy());
                    setActiveInvoiceState(ActiveInvoiceState.EDITED);
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invoiceNumberTextField.setStyle("-fx-control-inner-background: lightgreen");

        rootPane.setOnKeyPressed(keyEvent -> {
            if (KEY_COMBO_CTRL_S.match(keyEvent)) {
                saveActiveInvoice();
            } else if (KEY_COMBO_CTRL_F.match(keyEvent)) {
                showInvoiceStoreDialog();
            } else if (KEY_COMBO_CTRL_N.match(keyEvent)) {
                createNewInvoice();
            }
        });

        invoiceNumberTextField.textProperty().bindBidirectional(activeInvoice.invoiceNumberProperty());
        invoiceDatePicker.valueProperty().bindBidirectional(activeInvoice.invoiceDateProperty());
        invoiceDatePicker.setConverter(new LocalDateConverter());
        invoiceDatePicker.setOnAction(e -> setActiveInvoiceState(ActiveInvoiceState.EDITED));
        dueDatePicker.valueProperty().bindBidirectional(activeInvoice.dueDateProperty());
        dueDatePicker.setConverter(new LocalDateConverter());
        dueDatePicker.setOnAction(e -> setActiveInvoiceState(ActiveInvoiceState.EDITED));

        doneCheckBox.selectedProperty().bindBidirectional(activeInvoice.doneProperty());
        doneCheckBox.setOnAction(e -> setActiveInvoiceState(ActiveInvoiceState.EDITED));
        paidCheckBox.selectedProperty().bindBidirectional(activeInvoice.paidProperty());
        paidCheckBox.setOnAction(e -> setActiveInvoiceState(ActiveInvoiceState.EDITED));
        pickedUpCheckBox.selectedProperty().bindBidirectional(activeInvoice.pickedUpProperty());
        pickedUpCheckBox.setOnAction(e -> setActiveInvoiceState(ActiveInvoiceState.EDITED));

        CustomerInfo customerInfo = activeInvoice.getCustomerInfo();
        customerNameTextField.textProperty().bindBidirectional(customerInfo.nameProperty());
        customerPhoneTextField.textProperty().bindBidirectional(customerInfo.phoneProperty());
        customerEmailTextField.textProperty().bindBidirectional(customerInfo.emailProperty());
        customerInfo.nameProperty().addListener((observable, oldValue, newValue) -> setActiveInvoiceState(ActiveInvoiceState.EDITED));
        customerInfo.phoneProperty().addListener((observable, oldValue, newValue) -> setActiveInvoiceState(ActiveInvoiceState.EDITED));
        customerInfo.emailProperty().addListener((observable, oldValue, newValue) -> setActiveInvoiceState(ActiveInvoiceState.EDITED));

        // use a formatter to commit on Enter / loss of focus
        TextFormatter<Number> formatter = new TextFormatter<>(new NumberStringConverter(), 0);
        creditTextField.setTextFormatter(formatter);
        formatter.valueProperty().bindBidirectional(activeInvoice.creditProperty());
        activeInvoice.creditProperty().addListener(event -> setActiveInvoiceState(ActiveInvoiceState.EDITED));

        subtotalLabel.textProperty().bindBidirectional(activeInvoice.subtotalProperty(), new CurrencyStringConverter());
        taxLabel.textProperty().bindBidirectional(activeInvoice.taxProperty(), new CurrencyStringConverter());
        totalLabel.textProperty().bindBidirectional(activeInvoice.totalProperty(), new CurrencyStringConverter());

        itemsTable.setItems(activeInvoice.getItems());
        itemsTableNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemsTableQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemsTableUnitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        itemsTablePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // table edit
        itemsTableNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        itemsTableNameCol.setOnEditCommit(event -> {
            Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();
            selectedItem.setName(event.getNewValue());
            setActiveInvoiceState(ActiveInvoiceState.EDITED);
        });
        itemsTableQuantityCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        itemsTableQuantityCol.setOnEditCommit(event -> {
            Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();
            selectedItem.setQuantity(event.getNewValue());
            setActiveInvoiceState(ActiveInvoiceState.EDITED);
        });
        itemsTableUnitPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        itemsTableUnitPriceCol.setOnEditCommit(event -> {
            Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();
            selectedItem.setUnitPrice(event.getNewValue());
            setActiveInvoiceState(ActiveInvoiceState.EDITED);
        });

        itemsTable.setOnKeyPressed(event -> {
            final int selectedIndex = itemsTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < activeInvoice.getItems().size()) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    activeInvoice.getItems().remove(selectedIndex);
                    setActiveInvoiceState(ActiveInvoiceState.EDITED);
                }
            }
        });

        quickJacketComboBox.setItems(loadQuickItems("Jacket").getItems());
        quickPantComboBox.setItems(loadQuickItems("Pant").getItems());
        quickShirtComboBox.setItems(loadQuickItems("Shirt").getItems());
        quickDryCleanComboBox.setItems(loadQuickItems("Dry Clean").getItems());
        quickOtherComboBox.setItems(loadQuickItems("Other").getItems());
    }

    public void showAddressBookDialog(Event event) {
        event.consume();
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddressBookDialog.fxml"));
            final Parent root = fxmlLoader.load();
            final AddressBookDialogController addressBookDialogController = fxmlLoader.getController();
            addressBookDialogController.setAddressBook(addressBook);
            addressBookDialogController.selectedCustomerInfoProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    activeInvoice.getCustomerInfo().setFrom(newValue);
                    setActiveInvoiceState(ActiveInvoiceState.EDITED);
                }
            });
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Address Book");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            GuiUtils.showWarningAlertAndWait("Failed loading address book dialog");
        }
    }

    public void saveAddressBook() {
        try {
            GuiUtils.createDirectoryIfNecessary(SETTINGS_DIR_PATH);
            try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(ADDRESS_BOOK_DAT_FILE))) {
                addressBook.serialize(os);
            }
            System.out.println("Successfully saved address book " + ADDRESS_BOOK_DAT_FILE.getPath());

        } catch (IOException e) {
            System.err.println("Address book saving failed");
        }
    }

    public void showQuickItemsSettingsDialog(ActionEvent actionEvent) {
        final Object source = actionEvent.getSource();
        String quickItemsName = "";
        ComboBox<Item> quickItemComboBox = null;
        if (source == quickJacketsSettingsMenuItem) {
            quickItemsName = "Jacket";
            quickItemComboBox = quickJacketComboBox;

        } else if (source == quickPantsSettingsMenuItem) {
            quickItemsName = "Pant";
            quickItemComboBox = quickPantComboBox;

        } else if (source == quickShirtsSettingsMenuItem) {
            quickItemsName = "Shirt";
            quickItemComboBox = quickShirtComboBox;

        } else if (source == quickDryCleansSettingsMenuItem) {
            quickItemsName = "Dry Clean";
            quickItemComboBox = quickDryCleanComboBox;

        } else if (source == quickOthersSettingsMenuItem) {
            quickItemsName = "Other";
            quickItemComboBox = quickOtherComboBox;

        } else {
            assert false;
        }
        QuickItems quickItems = loadQuickItems(quickItemsName);
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("QuickItemsSettingsDialog.fxml"));
            final Parent root = fxmlLoader.load();
            final QuickItemsSettingsDialogController quickItemsSettingsDialogController = fxmlLoader.getController();
            quickItemsSettingsDialogController.setQuickItems(quickItemsName, quickItems);

            Stage stage = new Stage();
            final ComboBox<Item> finalQuickItemComboBox = quickItemComboBox;
            stage.setOnCloseRequest(event -> {
                final ObservableList<Item> items = quickItemsSettingsDialogController.getQuickItems().getItems();
                suspendQuickItemComboBoxAction = true;
                finalQuickItemComboBox.setItems(items);
                suspendQuickItemComboBoxAction = false;
            });
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Configure Quick " + quickItemsName);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            GuiUtils.showWarningAlertAndWait("Failed loading quick items dialog");
        }
    }

    private QuickItems loadQuickItems(String name) {
        final File datFile = GuiUtils.getQuickItemsDatFile(name);
        QuickItems quickItems = null;
        if (datFile.exists()) {
            try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(datFile))) {
                quickItems = QuickItems.deserialize(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (quickItems == null) {
            quickItems = new QuickItems(new ArrayList<>());
        }
        return quickItems;
    }

    public void showInvoiceStoreDialog() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InvoiceStoreDialog.fxml"));
            final Parent root = fxmlLoader.load();
            final InvoiceStoreDialogController controller = fxmlLoader.getController();
            controller.setInvoiceStore(invoiceStore);
            controller.selectedInvoiceProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    setActiveInvoice(newValue, ActiveInvoiceState.SAVED);
                }
            });

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Invoice Store");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            GuiUtils.showWarningAlertAndWait("Failed loading invoice store dialog");
        }
    }

    public void showMailDialog() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MailDialog.fxml"));
            final Parent root = fxmlLoader.load();
            final MailDialogController controller = fxmlLoader.getController();
            controller.setAddressBook(addressBook);

            final String email = activeInvoice.getCustomerInfo().getEmail();
            if (!email.isEmpty()) {
                controller.setTo(email);
            }
            final String cc = PropertiesConfiguration.getInstance().getProperty("invoice.maker.default.mail.cc", "");
            if (!cc.isEmpty()) {
                controller.setCC(cc);
            }
            final String subject = String.format(config.getProperty("invoice.maker.default.mail.subject"), activeInvoice.getInvoiceNumber());
            controller.setSubject(subject);

            Element html = new Element("html");
            InvoiceHtml.buildHead(html);
            InvoiceHtml.buildBody(html, activeInvoice);
            controller.setBodyHtml(html.print());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Mail");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            GuiUtils.showWarningAlertAndWait("Failed loading mail dialog");
        }
    }

    private void setActiveInvoice(Invoice target, ActiveInvoiceState state) {
        suspendActiveInvoiceStateUpdate = true; // prevent unnecessary state updates
        activeInvoice.cloneFrom(target);
        baselineInvoice = activeInvoice.copy();
        suspendActiveInvoiceStateUpdate = false;
        setActiveInvoiceState(state);
    }
}
