package Data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuickItems implements Serializable {

    private static final long serialVersionUID = 1L;

    private final ObservableList<Item> items;

    public QuickItems(List<Item> items) {
        this.items = FXCollections.observableArrayList(items);
    }

    public ObservableList<Item> getItems() {
        return items;
    }

    public void serialize(ObjectOutputStream os) throws IOException {
        os.writeInt(items.size());
        for (Item item : items) {
            os.writeUTF(item.getName());
            // do not need to write quantity - quantity should always be 1
            os.writeDouble(item.getUnitPrice());
        }
    }

    public static QuickItems deserialize(ObjectInputStream is) throws IOException {
        final int numItems = is.readInt();
        List<Item> items = new ArrayList<>(numItems);
        for (int i = 0; i < numItems; i++) {
            final String name = is.readUTF();
            final double unitPrice = is.readDouble();
            items.add(new Item(name, 1, unitPrice));
        }
        return new QuickItems(items);
    }
}
