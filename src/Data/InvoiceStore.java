package Data;

import Utils.PathUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static Utils.PathUtils.SAVE_DIR_PATH;

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
        invoiceMap = new TreeMap<>();

        try {
            PathUtils.createDirectoryIfNecessary(SAVE_DIR_PATH);
            Files.walk(SAVE_DIR_PATH).forEach(file -> {
                try (ObjectInputStream is = new ObjectInputStream(Files.newInputStream(file))) {
                    final Invoice invoice = Invoice.deserialize(is);
                    invoiceMap.put(invoice.getInvoiceNumber(), invoice);
                } catch (IOException ignore) {
                }
            });
            System.out.println("Loaded " + invoiceMap.size() + " invoices.");

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

    public ObservableList<Invoice> all() {
        return FXCollections.observableList(new ArrayList<>(invoiceMap.values()));
    }

}
