package Data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CustomerInfo implements Comparable<CustomerInfo> {

    public static CustomerInfo getSAMPLE() {
        return new CustomerInfo("FirstName LastName", "604-123-4567", "first.last@gmail.com");
    }

    public static CustomerInfo getEmpty() {
        return new CustomerInfo("Customer", "", "");
    }

    private final StringProperty name;
    private final StringProperty phone;
    private final StringProperty email;

    public CustomerInfo(String name, String phone, String email) {
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
    }

    @Override
    public String toString() {
        return getName()
                + (!getPhone().isEmpty() ? " ; " + getPhone() : "")
                + (!getEmail().isEmpty() ? " ; " + getEmail() : "");
    }

    public static CustomerInfo parse(String customerInfoString) {
        String[] strings = customerInfoString.split(";");
        int length = strings.length;

        String name = "no name";
        String phone = "";
        String email = "";
        if (length > 0) {
            name = strings[0];
        }
        if (length > 1) {
            phone = strings[1];
        }
        if (length > 2) {
            email = strings[2];
        }

        return new CustomerInfo(name, phone, email);
    }

    public void setFrom(CustomerInfo other) {
        name.setValue(other.getName());
        phone.setValue(other.getPhone());
        email.setValue(other.getEmail());
    }

    public boolean containsText(String text) {
        final String searchString = text.toLowerCase();
        return getName().toLowerCase().contains(searchString) ||
                getPhone().toLowerCase().contains(searchString) ||
                getEmail().toLowerCase().contains(searchString);
    }

    public CustomerInfo copy() {
        return new CustomerInfo(getName(), getPhone(), getEmail());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerInfo that = (CustomerInfo) o;

        if (!getName().equals(that.getName())) return false;
        if (!getPhone().equals(that.getPhone())) return false;
        return getEmail().equals(that.getEmail());

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getPhone().hashCode();
        result = 31 * result + getEmail().hashCode();
        return result;
    }

    @Override
    public int compareTo(CustomerInfo other) {
        return this.getName().compareTo(other.getName());
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }


}
