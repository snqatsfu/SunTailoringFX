package Data;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * TODO: CLASS JAVA DOC HERE
 */
public class InvoiceTest {

    private static final double LAMBDA = 1e-6;

    @Test
    public void testBinding() {
        Item item1 = new Item("a", 1, 2.0);
        Item item2 = new Item("b", 2, 3.0);

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        Invoice invoice = new Invoice("Invoice 0", LocalDate.now(), LocalDate.now(), CustomerInfo.getSAMPLE(), items, 0, false, false, false);
        assertEquals(8.0, invoice.getSubtotal(), LAMBDA);
        assertEquals(8.4, invoice.getTotal(), LAMBDA);

        // set credit
        invoice.setCredit(1);
        assertEquals(8.0, invoice.getSubtotal(), LAMBDA);
        assertEquals(7.4, invoice.getTotal(), LAMBDA);

        invoice.setCredit(0);
        assertEquals(8.0, invoice.getSubtotal(), LAMBDA);
        assertEquals(8.4, invoice.getTotal(), LAMBDA);

        // edit item quantity
        item1.setQuantity(2);
        assertEquals(10.0, invoice.getSubtotal(), LAMBDA);
        assertEquals(10.5, invoice.getTotal(), LAMBDA);

        // edit item unit price
        item2.setUnitPrice(4.0);
        assertEquals(12.0, invoice.getSubtotal(), LAMBDA);
        assertEquals(12.6, invoice.getTotal(), LAMBDA);

        // add item
        invoice.getItems().add(new Item("c", 2, 2.0));
        assertEquals(16.0, invoice.getSubtotal(), LAMBDA);
        assertEquals(16.8, invoice.getTotal(), LAMBDA);

        // remove item
        invoice.getItems().remove(item2);
        assertEquals(8.0, invoice.getSubtotal(), LAMBDA);
        assertEquals(8.4, invoice.getTotal(), LAMBDA);

        // clear all
        invoice.getItems().clear();
        assertEquals(0.0, invoice.getSubtotal(), LAMBDA);
        assertEquals(0.0, invoice.getTotal(), LAMBDA);
    }
}
