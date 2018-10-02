import Data.Invoice;
import Data.LegacyInvoiceConverter;

import java.io.File;

/**
 * TODO: CLASS JAVA DOC HERE
 */
public class LegacyInvoiceConverterTest {

    public static void main(String[] args) throws Exception {
        Invoice newInvoice = LegacyInvoiceConverter.convert(new File("test/1809050Upgraded.dat"));
        System.out.println();
    }
}
