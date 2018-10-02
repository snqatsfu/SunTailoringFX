package Data;

import javafx.beans.property.*;

/**
 * TODO: CLASS JAVA DOC HERE
 */
public class Invoice {

    private StringProperty invoiceNumber;
    private LongProperty invoiceDate;
    private LongProperty dueDate;

    private BooleanProperty paid;
    private BooleanProperty done;
    private BooleanProperty pickedUp;

    public Invoice(String invoiceNumber, long invoiceDate, long dueDate, boolean paid, boolean done, boolean pickedUp) {
        this.invoiceNumber = new SimpleStringProperty(invoiceNumber);
        this.invoiceDate = new SimpleLongProperty(invoiceDate);
        this.dueDate = new SimpleLongProperty(dueDate);
        this.paid = new SimpleBooleanProperty(paid);
        this.done = new SimpleBooleanProperty(done);
        this.pickedUp = new SimpleBooleanProperty(pickedUp);
    }


    public String getInvoiceNumber() {
        return invoiceNumber.get();
    }

    public StringProperty invoiceNumberProperty() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber.set(invoiceNumber);
    }

    public long getInvoiceDate() {
        return invoiceDate.get();
    }

    public LongProperty invoiceDateProperty() {
        return invoiceDate;
    }

    public void setInvoiceDate(long invoiceDate) {
        this.invoiceDate.set(invoiceDate);
    }

    public long getDueDate() {
        return dueDate.get();
    }

    public LongProperty dueDateProperty() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate.set(dueDate);
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
}
