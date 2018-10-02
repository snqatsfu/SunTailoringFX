package Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: CLASS JAVA DOC HERE
 */
public class LegacyInvoiceConverter {

    public static Invoice convert(File legacyInvoiceDatFile) throws Exception {
        try (ObjectInputStream fis = new ObjectInputStream(new FileInputStream(legacyInvoiceDatFile))) {
            String invoiceNumber = fis.readUTF();
            long invoiceDate = fis.readLong();
            long dueDate = fis.readLong();
            String customerAddress = fis.readUTF();

            int itemListSize = fis.readInt();
            List<String> itemNames = new ArrayList<>();
            List<Integer> itemQuantities = new ArrayList<>();
            List<Double> itemUnitPrices = new ArrayList<>();
            for (int i = 0; i < itemListSize; i++) {
                itemNames.add(fis.readUTF());
                itemQuantities.add(fis.readInt());
                itemUnitPrices.add(fis.readDouble());
            }

            double credit = fis.readDouble();
            boolean paid = fis.readBoolean();
            boolean done = fis.readBoolean();
            boolean pickedUp = fis.readBoolean();

            return new Invoice(invoiceNumber,
                    invoiceDate,
                    dueDate,
                    paid, done, pickedUp);
        }

    }

}
