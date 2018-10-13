package GUI;

import Data.Invoice;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static GUI.GuiUtils.SAVE_DIR_PATH;

public class InvoiceStore {

    public static InvoiceStore getInstance() {
        if (instance == null) {
            instance = new InvoiceStore();
        }
        return instance;
    }

    private static InvoiceStore instance;

    private final Map<String, Invoice> invoiceMap;

    private InvoiceStore() {
        invoiceMap = new HashMap<>();

        try {
            GuiUtils.createDirectoryIfNecessary(GuiUtils.SAVE_DIR_PATH);
            Files.walk(GuiUtils.SAVE_DIR_PATH).forEach(file -> {
                try (ObjectInputStream is = new ObjectInputStream(Files.newInputStream(file))) {
                    final Invoice invoice = Invoice.deserialize(is);
                    invoiceMap.put(invoice.getInvoiceNumber(), invoice);
                } catch (IOException ignore) {}
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Invoice get(String invoiceNumber) {
        return invoiceMap.get(invoiceNumber);
    }

    public boolean contains(String invoiceNumber) {
        return invoiceMap.containsKey(invoiceNumber);
    }

    public void save(Invoice invoice) {
        final Invoice invoiceCopy = invoice.copy();
        invoiceMap.put(invoiceCopy.getInvoiceNumber(), invoiceCopy);

        // save to disk
        File outputFile = new File(SAVE_DIR_PATH + "/" + invoiceCopy.getInvoiceNumber() + ".dat");
        try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(outputFile))) {
            invoiceCopy.serialize(fos);
        } catch (IOException e) {
            System.err.println("Save invoice failed.");
        }
    }

    public int getSize() {
        return invoiceMap.size();
    }


}
