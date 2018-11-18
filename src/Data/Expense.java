package Data;

import javafx.beans.property.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;

public class Expense implements Serializable, Comparable<Expense> {

    private static final long serialVersionUID = 1L;

    private IntegerProperty id;
    private ObjectProperty<LocalDate> date;
    private StringProperty description;
    private DoubleProperty total;

    public Expense(int id, LocalDate date, String description, double total) {
        this.id = new SimpleIntegerProperty(id);
        this.date = new SimpleObjectProperty<>(date);
        this.description = new SimpleStringProperty(description);
        this.total = new SimpleDoubleProperty(total);
    }

    public void serialize(ObjectOutputStream os) throws IOException {
        os.writeInt(getId());
        os.writeLong(getDate().toEpochDay());
        os.writeUTF(getDescription());
        os.writeDouble(getTotal());
    }

    public static Expense deserialize(ObjectInputStream is) throws IOException {
        int id = is.readInt();
        LocalDate date = LocalDate.ofEpochDay(is.readLong());
        String description = is.readUTF();
        double total = is.readDouble();
        return new Expense(id, date, description, total);
    }

    public int getId() {
        return id.get();
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public double getTotal() {
        return total.get();
    }

    public DoubleProperty totalProperty() {
        return total;
    }

    public void setTotal(double total) {
        this.total.set(total);
    }

    @Override
    public int compareTo(Expense o) {
        return this.getDate().compareTo(o.getDate());
    }
}
