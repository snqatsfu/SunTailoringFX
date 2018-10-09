package Data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CustomerInfo {

    public static final CustomerInfo getSAMPLE() {
        return new CustomerInfo("FirstName LastName", "604-123-4567", "first.last@gmail.com");
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
                + (getPhone().isEmpty() ? " ; " + getPhone() : "")
                + (getEmail().isEmpty() ? " ; " + getEmail() : "");
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

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
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

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }
}
