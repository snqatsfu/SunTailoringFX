import Data.Invoice;
import Data.LegacyInvoiceConverter;

import java.io.File;

/**
 * TODO: CLASS JAVA DOC HERE
 */
public class LegacyInvoiceConverterTest {

    public static void main(String[] args) throws Exception {
        Invoice newInvoice = LegacyInvoiceConverter.convert(new File("test/1809050Upgraded.dat"));

        System.out.println(newInvoice.getSubtotal());
        System.out.println(newInvoice.getTax());
        System.out.println(newInvoice.getTotal());

        newInvoice.setCredit(1);
        System.out.println(newInvoice.getSubtotal());
        System.out.println(newInvoice.getTax());
        System.out.println(newInvoice.getTotal());

        System.out.println(newInvoice.getItems().get(0).getQuantity());
        newInvoice.getItems().get(0).setQuantity(2);
        System.out.println(newInvoice.getItems().get(0).getQuantity());
        System.out.println(newInvoice.getItems().get(0).getPrice());
        System.out.println(newInvoice.getSubtotal());
        System.out.println(newInvoice.getTax());
        System.out.println(newInvoice.getTotal());

    }
}
