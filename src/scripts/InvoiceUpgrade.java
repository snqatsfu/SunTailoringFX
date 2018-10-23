package scripts;

import Data.Invoice;
import Data.LegacyInvoiceConverter;
import GUI.InvoiceStore;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InvoiceUpgrade {

    static int count = 0;

    public static void main(String[] args) throws Exception {
        final InvoiceStore store = InvoiceStore.getInstance();

        Files.walk(Paths.get("src/scripts/OldInvoices"))
                .map(Path::toFile).filter(File::isFile)
                .forEach(file -> {
                    try {
                        final Invoice invoice = LegacyInvoiceConverter.convert(file);
                        store.save(invoice);
                        System.out.println(count + " - Converted invoice " + invoice.getInvoiceNumber());
                        count++;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

}
