package Data;

import Utils.PropertiesConfiguration;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO: CLASS JAVA DOC HERE
 */
public class Invoice implements Serializable, Comparable<Invoice> {

    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_NUM_DAYS_DUE = Integer.parseInt(PropertiesConfiguration.getInstance()
            .getProperty("invoice.maker.default.due.days", "7"));
    private static final double SERVICE_TAX_RATE = 0.05;

    private StringProperty invoiceNumber;
    private ObjectProperty<LocalDate> invoiceDate;
    private ObjectProperty<LocalDate> dueDate;
    private CustomerInfo customerInfo;

    private ListProperty<Item> items;

    private DoubleProperty credit;

    private BooleanProperty paid;
    private BooleanProperty done;
    private BooleanProperty pickedUp;

    // derived properties
    private DoubleProperty subtotal;
    private DoubleProperty tax;
    private DoubleProperty total;

    public Invoice(String invoiceNumber, LocalDate invoiceDate, LocalDate dueDate, CustomerInfo customerInfo,
                   List<Item> items,
                   double credit, boolean paid, boolean done, boolean pickedUp) {

        this.invoiceNumber = new SimpleStringProperty(invoiceNumber);
        this.invoiceDate = new SimpleObjectProperty<>(invoiceDate);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
        this.customerInfo = customerInfo;

        subtotal = new SimpleDoubleProperty(0);
        tax = new SimpleDoubleProperty(0);
        total = new SimpleDoubleProperty(0);

        // bind subtotal to item list changes
        ObservableList<Item> itemsObservableList = FXCollections.observableArrayList(Item.priceExtractor());
        itemsObservableList.addListener((ListChangeListener<Item>) c -> {
            double sum = 0;
            while (c.next()) {
                for (Item item : c.getList()) {
                    sum += item.getPrice();
                }
            }
            subtotal.setValue(sum);
        });
        itemsObservableList.addAll(items);
        this.items = new SimpleListProperty<>(itemsObservableList);

        this.credit = new SimpleDoubleProperty(credit);

        this.paid = new SimpleBooleanProperty(paid);
        this.done = new SimpleBooleanProperty(done);
        this.pickedUp = new SimpleBooleanProperty(pickedUp);

        tax.bind(Bindings.multiply(subtotal, SERVICE_TAX_RATE));
        total.bind(Bindings.add(subtotal, tax).subtract(this.credit));
    }

    public static Invoice createEmptyInvoice(String invoiceNumber) {
        LocalDate invoiceDate = LocalDate.now();
        LocalDate dueDate = invoiceDate.plusDays(DEFAULT_NUM_DAYS_DUE);
        return new Invoice(invoiceNumber, invoiceDate, dueDate, CustomerInfo.getEmpty(),
                Collections.emptyList(), 0, false, false, false);
    }

    public void serialize(ObjectOutputStream os) throws IOException {
        os.writeUTF(getInvoiceNumber());
        os.writeLong(getInvoiceDate().toEpochDay());
        os.writeLong(getDueDate().toEpochDay());

        os.writeUTF(getCustomerInfo().getName());
        os.writeUTF(getCustomerInfo().getPhone());
        os.writeUTF(getCustomerInfo().getEmail());

        os.writeInt(getItems().size());
        for (Item item : getItems()) {
            os.writeUTF(item.getName());
            os.writeInt(item.getQuantity());
            os.writeDouble(item.getUnitPrice());
        }

        os.writeDouble(getCredit());
        os.writeBoolean(getPaid());
        os.writeBoolean(getDone());
        os.writeBoolean(getPickedUp());
    }

    public static Invoice deserialize(ObjectInputStream is) throws IOException {
        String invoiceNumber = is.readUTF();
        LocalDate invoiceDate = LocalDate.ofEpochDay(is.readLong());
        LocalDate dueDate = LocalDate.ofEpochDay(is.readLong());

        String customerName = is.readUTF();
        String customerPhone = is.readUTF();
        String customerEmail = is.readUTF();
        CustomerInfo customerInfo = new CustomerInfo(customerName, customerPhone, customerEmail);

        int numItems = is.readInt();
        List<Item> items = new ArrayList<>(numItems);
        for (int i = 0; i < numItems; i++) {
            String itemName = is.readUTF();
            int quantity = is.readInt();
            double unitPrice = is.readDouble();
            items.add(new Item(itemName, quantity, unitPrice));
        }

        double credit = is.readDouble();
        boolean paid = is.readBoolean();
        boolean done = is.readBoolean();
        boolean pickedUp = is.readBoolean();

        return new Invoice(invoiceNumber, invoiceDate, dueDate, customerInfo, items, credit, paid, done, pickedUp);
    }

    public void cloneFrom(Invoice otherInvoice) {
        invoiceNumber.setValue(otherInvoice.getInvoiceNumber());
        invoiceDate.setValue(otherInvoice.getInvoiceDate());
        dueDate.setValue(otherInvoice.getDueDate());

        customerInfo.setFrom(otherInvoice.getCustomerInfo());

        ObservableList<Item> itemsObservableList = getItems();
        itemsObservableList.clear();
        itemsObservableList.addAll(otherInvoice.getItems().stream().map(Item::copy).collect(Collectors.toList()));

        credit.setValue(otherInvoice.getCredit());
        paid.setValue(otherInvoice.getPaid());
        done.setValue(otherInvoice.getDone());
        pickedUp.setValue(otherInvoice.getPickedUp());
    }

    public Invoice copy() {
        return new Invoice(getInvoiceNumber(),
                getInvoiceDate(),
                getDueDate(),
                getCustomerInfo().copy(),
                getItems().stream().map(Item::copy).collect(Collectors.toList()),
                getCredit(),
                getPaid(),
                getDone(),
                getPickedUp());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invoice invoice = (Invoice) o;

        if (!getInvoiceNumber().equals(invoice.getInvoiceNumber())) return false;
        if (!getInvoiceDate().equals(invoice.getInvoiceDate())) return false;
        if (!getDueDate().equals(invoice.getDueDate())) return false;
        if (!getCustomerInfo().equals(invoice.getCustomerInfo())) return false;
        if (!getItems().equals(invoice.getItems())) return false;
        if (!(getCredit() == invoice.getCredit())) return false;
        if (!(getPaid() == invoice.getPaid())) return false;
        if (!(getDone() == invoice.getDone())) return false;
        return getPickedUp() == invoice.getPickedUp();
    }

    @Override
    public int hashCode() {
        return getInvoiceNumber().hashCode();
    }

    public String getItemsAsString() {
        String retVal = "";
        for (Item item : getItems()) {
            retVal += item.shortSummary() + "; ";
        }
        retVal = retVal.trim();
        if (retVal.endsWith(";")) {
            retVal = retVal.substring(0, retVal.length() - 1);
        }
        return retVal;
    }

    public boolean isDryCleanOnly() {
        if (getItems().isEmpty()) {
            return false;
        }

        boolean retVal = true;
        for (Item item : getItems()) {
            if (!item.getName().toLowerCase().trim().startsWith("dry clean")) {
                retVal = false;
                break;
            }
        }
        return retVal;
    }

    public String getInvoiceNumber() {
        return invoiceNumber.get();
    }

    public StringProperty invoiceNumberProperty() {
        return invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate.get();
    }

    public ObjectProperty<LocalDate> invoiceDateProperty() {
        return invoiceDate;
    }

    public LocalDate getDueDate() {
        return dueDate.get();
    }

    public ObjectProperty<LocalDate> dueDateProperty() {
        return dueDate;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public ObservableList<Item> getItems() {
        return items.get();
    }

    public ListProperty<Item> itemsProperty() {
        return items;
    }

    public double getCredit() {
        return credit.get();
    }

    public DoubleProperty creditProperty() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit.set(credit);
    }

    public boolean getPaid() {
        return paid.get();
    }

    public BooleanProperty paidProperty() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid.set(paid);
    }

    public boolean getDone() {
        return done.get();
    }

    public BooleanProperty doneProperty() {
        return done;
    }

    public void setDone(boolean done) {
        this.done.set(done);
    }

    public boolean getPickedUp() {
        return pickedUp.get();
    }

    public BooleanProperty pickedUpProperty() {
        return pickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp.set(pickedUp);
    }

    public double getSubtotal() {
        return subtotal.get();
    }

    public DoubleProperty subtotalProperty() {
        return subtotal;
    }

    public double getTax() {
        return tax.get();
    }

    public DoubleProperty taxProperty() {
        return tax;
    }

    public double getTotal() {
        return total.get();
    }

    public DoubleProperty totalProperty() {
        return total;
    }

    @Override
    public int compareTo(Invoice o) {
        return this.getInvoiceNumber().compareTo(o.getInvoiceNumber());
    }
}
