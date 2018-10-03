package Data;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListBindingTest {

    static class Item {
        IntegerProperty number;

        public Item(int number) {
            this.number = new SimpleIntegerProperty(number);
        }

        public int getNumber() {
            return number.get();
        }

        public IntegerProperty numberProperty() {
            return number;
        }

        public void setNumber(int number) {
            this.number.set(number);
        }

        public static Callback<Item, Observable[]> extractor() {
            return param -> new Observable[]{param.numberProperty()};
        }
    }

    @Test
    public void test() {

        Item item1 = new Item(1);
        Item item2 = new Item(2);
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        ObservableList<Item> itemObservableList = FXCollections.observableArrayList(Item.extractor());
        itemObservableList.addListener((ListChangeListener<Item>) c -> {
            int sum = 0;
            while (c.next()) {
                for (Item item : c.getList()) {
                    sum += item.getNumber();
                }
//                if (c.wasPermutated()) {
//                    for (int i = c.getFrom(); i < c.getTo(); ++i) {
//                        System.out.println("Permuted: " + i + " " + itemObservableList.get(i));
//                    }
//                } else if (c.wasUpdated()) {
//                    for (int i = c.getFrom(); i < c.getTo(); ++i) {
//                        System.out.println("Updated: " + i + " " + itemObservableList.get(i));
//                    }
//                } else {
//                    for (Item removedItem : c.getRemoved()) {
//                        System.out.println("Removed: " + removedItem);
//                    }
//                    for (Item addedItem : c.getAddedSubList()) {
//                        System.out.println("Added: " + addedItem);
//                    }
//                }
            }
            System.out.println("sum = " + sum);
        });

        itemObservableList.addAll(items);
//        itemObservableList.add(item1);
//        itemObservableList.add(item2);

        item1.setNumber(3);
//        itemObservableList.get(0).setNumber(3);
    }
}
