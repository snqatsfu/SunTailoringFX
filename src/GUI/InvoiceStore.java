package GUI;

import Data.Invoice;
import Html.InvoiceHtml;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static GUI.GuiUtils.REPORT_DIR_PATH;
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
        invoiceMap = new TreeMap<>();

        try {
            GuiUtils.createDirectoryIfNecessary(GuiUtils.SAVE_DIR_PATH);
            GuiUtils.createDirectoryIfNecessary(GuiUtils.REPORT_DIR_PATH);
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

        // generate HTML report
        File reportFile = new File(REPORT_DIR_PATH + "/" + invoiceCopy.getInvoiceNumber() + ".html");
        try {
            InvoiceHtml.createHtml(invoiceCopy, reportFile);
        } catch (FileNotFoundException e) {
            System.err.println("Generate report failed");
        }
    }

    public int getSize() {
        return invoiceMap.size();
    }

    public ObservableList<Invoice> all() {
        return FXCollections.observableList(new ArrayList<>(invoiceMap.values()));
    }

}
