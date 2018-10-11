package Data;

import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private final SortedSet<CustomerInfo> entries;

    public AddressBook(Collection<CustomerInfo> startingEntries) {
        entries = new TreeSet<>(startingEntries);
    }

    public boolean add(CustomerInfo customerInfo) {
        return entries.add(customerInfo);
    }

    public boolean remove(CustomerInfo customerInfo) {
        return entries.remove(customerInfo);
    }

    public void serialize(ObjectOutputStream os) throws IOException {
        os.writeInt(entries.size());
        for (CustomerInfo entry : entries) {
            os.writeUTF(entry.getName());
            os.writeUTF(entry.getPhone());
            os.writeUTF(entry.getEmail());
        }
    }

    public static AddressBook deserialize(ObjectInputStream is) throws IOException {
        final int numEntries = is.readInt();
        SortedSet<CustomerInfo> entries = new TreeSet<>();
        for (int i = 0; i < numEntries; i++) {
            final String name = is.readUTF();
            final String phone = is.readUTF();
            final String email = is.readUTF();
            entries.add(new CustomerInfo(name, phone, email));
        }
        return new AddressBook(entries);
    }

}
