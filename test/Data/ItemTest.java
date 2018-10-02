package Data;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * TODO: CLASS JAVA DOC HERE
 */
public class ItemTest {

    @Test
    public void testBinding() {
        Item item = new Item("a", 1, 2.0);
        assertEquals(2.0, item.getPrice());
        item.setUnitPrice(3.0);
        assertEquals(3.0, item.getPrice());
        item.setQuantity(3);
        assertEquals(9.0, item.getPrice());
    }
}
