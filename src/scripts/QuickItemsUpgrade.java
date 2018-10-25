package scripts;

import Data.Item;
import Data.QuickItems;
import GUI.GuiUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class QuickItemsUpgrade {
    private static final String QUICK_ITEMS_CSV_FILE_HEADER = "Name,Unit Price,";

    public static void main(String[] args) throws IOException {
        // read old csv file
        List<Item> quickItemsList = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get("src/scripts/QuickDress.csv"))) {
            stream.forEach(line -> {
                if (!line.equals(QUICK_ITEMS_CSV_FILE_HEADER)) {
                    final String[] split = line.split(",", -1);
                    String name = split[0].trim();
                    double unitPrice = Double.parseDouble(split[1].trim());
                    quickItemsList.add(new Item(name, 1, unitPrice));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        QuickItems quickItems = new QuickItems(quickItemsList);

        // save
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(GuiUtils.getQuickItemsDatFile("Dress")))) {
            quickItems.serialize(os);
        }
    }
}
