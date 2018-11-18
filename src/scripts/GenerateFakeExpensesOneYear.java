package scripts;

import Data.Expense;
import Data.ExpenseStore;

import java.time.LocalDate;
import java.util.Random;

public class GenerateFakeExpensesOneYear {
    public static void main(String[] args) {
        ExpenseStore store = ExpenseStore.getInstance();

        int count = 0;
        final LocalDate today = LocalDate.now();
        final Random random = new Random();
        for (int i = 0; i < 365; i++) {
            final LocalDate date = today.minusDays(i);
            int id = (int) date.toEpochDay();
            String description = "Fake expense on " + date;
            double total = random.nextInt(100);
            store.save(new Expense(id, date, description, total));
            count++;
        }

        System.out.println("Generated " + count + " fake expenses");
    }

}
