package scripts;

import Data.CustomerInfo;
import Data.Invoice;
import Data.InvoiceStore;
import Data.Item;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * TODO: CLASS JAVA DOC HERE
 */
public class GenerateFakeInvoicesOneYear {
    public static void main(String[] args) {
        int numDays = 365;
        if (args.length > 0) {
            numDays = Integer.parseInt(args[0]);
        }

        final InvoiceStore store = InvoiceStore.getInstance();

        int count = 0;
        final LocalDate today = LocalDate.now();
        final Random random = new Random();
        for (int i = 0; i < numDays; i++) {
            final LocalDate invoiceDate = today.minusDays(i);
            final int numInvoicesThisDay = random.nextInt(10);
            for (int j = 0; j < numInvoicesThisDay; j++) {
                String invoiceNumber = invoiceDate + "_" + j;
                final LocalDate dueDate = invoiceDate.plusDays(new Random().nextInt(7));
                final int numItems = random.nextInt(10);
                List<Item> items = new ArrayList<>(numItems);
                for (int k = 0; k < numItems; k++) {
                    items.add(new Item("Fake Item", random.nextInt(3), random.nextInt(20)));
                }
                store.save(new Invoice(invoiceNumber, invoiceDate, dueDate, CustomerInfo.getSAMPLE(), items, 0, false, false, false));
                count++;
            }
        }

        System.out.println("Generated " + count + " fake invoices");
    }

}
