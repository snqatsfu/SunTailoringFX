package Data;

import Utils.PathUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static Utils.PathUtils.EXPENSES_DIR_PATH;

public class ExpenseStore {

    public static ExpenseStore getInstance() {
        if (instance == null) {
            instance = new ExpenseStore();
        }
        return instance;
    }

    private static ExpenseStore instance;

    private final Map<Integer, Expense> expenses;

    private int maxId;

    private ExpenseStore() {
        expenses = new HashMap<>();

        try {
            PathUtils.createDirectoryIfNecessary(EXPENSES_DIR_PATH);
            Files.walk(EXPENSES_DIR_PATH).forEach(file -> {
                try (ObjectInputStream is = new ObjectInputStream(Files.newInputStream(file))) {
                    Expense expense = Expense.deserialize(is);
                    int id = expense.getId();
                    if (id > maxId) {
                        maxId = id;
                    }
                    expenses.put(id, expense);

                } catch (IOException ignore) {
                }
            });

            System.out.println("Loaded " + expenses.size() + " expenses");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Expense> all() {
        return FXCollections.observableList(new ArrayList<>(expenses.values()));
    }

    public void createExpenseAndSave(LocalDate date, String description, double total) {
        int id = maxId + 1;
        maxId = id;
        Expense expense = new Expense(id, date, description, total);
        save(expense);
    }

    public void duplicateExpenseAndSave(Expense expense) {
        int id = maxId + 1;
        maxId = id;
        Expense duplicatedExpense = new Expense(id, expense.getDate(), expense.getDescription() + " (Duplicated)", expense.getTotal());
        save(duplicatedExpense);
    }

    public void save(Expense expense) {
        expenses.put(expense.getId(), expense);
        File outputFile = new File(EXPENSES_DIR_PATH + "/" + expense.getId() + ".dat");
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(outputFile))) {
            expense.serialize(os);
        } catch (IOException e) {
            System.err.println("Save Expense failed");
        }
    }

    public void delete(Expense expense) {
        expenses.remove(expense.getId());
        try {
            Files.deleteIfExists(Paths.get(EXPENSES_DIR_PATH + "/" + expense.getId() + ".dat"));
        } catch (IOException e) {
            System.err.println("Failed deleting expense file " + expense.getId() + ".dat");
        }
    }

}
