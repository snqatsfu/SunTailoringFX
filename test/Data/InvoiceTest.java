package Data;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * TODO: CLASS JAVA DOC HERE
 */
public class InvoiceTest {

    @Test
    public void testBinding() {
        Item item1 = new Item("a", 1, 2.0);
        Item item2 = new Item("b", 2, 3.0);

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        Invoice invoice = new Invoice("Invoice 0", 0l, 0l, "Customer", items, 0, false, false, false);
        assertEquals(8.0, invoice.getSubtotal(), 1e-6);
        assertEquals(8.4, invoice.getTotal(), 1e-6);

        invoice.setCredit(1);
        assertEquals(8.0, invoice.getSubtotal(), 1e-6);
        assertEquals(7.4, invoice.getTotal(), 1e-6);

        item1.setQuantity(2);
        assertEquals(10.0, invoice.getSubtotal(), 1e-6);
    }
}
