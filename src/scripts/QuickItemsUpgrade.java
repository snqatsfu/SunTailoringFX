package scripts;

import Data.Item;
import Data.QuickItems;
import Utils.PathUtils;

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
        extract("src/scripts/QuickJackets.csv", "Jacket");
        extract("src/scripts/QuickPants.csv", "Pant");
        extract("src/scripts/QuickShirts.csv", "Shirt");
        extract("src/scripts/QuickDress.csv", "Dress");
        extract("src/scripts/QuickDryClean.csv", "Dry Clean");
        extract("src/scripts/QuickOthers.csv", "Other");
    }

    private static void extract(String oldCsvPath, String quickItemName) throws IOException {
        // read old csv file
        List<Item> quickItemsList = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(oldCsvPath))) {
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
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(PathUtils.getQuickItemsDatFile(quickItemName)))) {
            quickItems.serialize(os);
        }
    }
}
