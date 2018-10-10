package Data;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.util.Callback;

public class Item {

    private StringProperty name;
    private IntegerProperty quantity;
    private DoubleProperty unitPrice;
    private DoubleProperty price;

    public Item(String name, int quantity, double unitPrice) {
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.price = new SimpleDoubleProperty(0);
        price.bind(Bindings.multiply(this.quantity, this.unitPrice));
    }

    public Item copy() {
        return new Item(getName(), getQuantity(), getUnitPrice());
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public double getUnitPrice() {
        return unitPrice.get();
    }

    public DoubleProperty unitPriceProperty() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice.set(unitPrice);
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public static Callback<Item, Observable[]> priceExtractor() {
        return param -> new Observable[]{param.price};
    }

    @Override
    public String toString() {
        return getName();
    }
}
