package Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: CLASS JAVA DOC HERE
 */
public class LegacyInvoiceConverter {

    public static Invoice convert(File legacyInvoiceDatFile) throws Exception {
        try (ObjectInputStream fis = new ObjectInputStream(new FileInputStream(legacyInvoiceDatFile))) {
            String invoiceNumber = fis.readUTF();
            long invoiceDateLong = fis.readLong();
            long dueDateLong = fis.readLong();
            LocalDate invoiceDate = Instant.ofEpochMilli(invoiceDateLong).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dueDate = Instant.ofEpochMilli(dueDateLong).atZone(ZoneId.systemDefault()).toLocalDate();
            String customerAddress = fis.readUTF();

            int itemListSize = fis.readInt();
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < itemListSize; i++) {
                Item item = new Item(fis.readUTF(), fis.readInt(), fis.readDouble());
                items.add(item);
            }

            double credit = fis.readDouble();
            boolean paid = fis.readBoolean();
            boolean done = fis.readBoolean();
            boolean pickedUp = fis.readBoolean();

            return new Invoice(invoiceNumber,
                    invoiceDate,
                    dueDate,
                    customerAddress,
                    items,
                    credit,
                    paid, done, pickedUp);
        }

    }

}
