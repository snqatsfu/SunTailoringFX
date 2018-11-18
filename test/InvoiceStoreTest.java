import Data.InvoiceStore;
import org.junit.Test;

public class InvoiceStoreTest {

    @Test
    public void testGetInstance() {
        final InvoiceStore instance = InvoiceStore.getInstance();
        System.out.println(instance.getSize());
    }

}
