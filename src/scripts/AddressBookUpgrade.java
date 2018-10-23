package scripts;

import Data.AddressBook;
import Data.CustomerInfo;
import GUI.GuiUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static GUI.GuiUtils.ADDRESS_BOOK_DAT_FILE;

public class AddressBookUpgrade {
    private static final String ADDRESS_BOOK_CSV_FILE_HEADER = "Name,Address Name,Address Street,Address City,Address Province,Address Postal Code,Phone,Email,";

    public static void main(String[] args) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GuiUtils.ADDRESS_BOOK_DAT_FILE))) {
            // read current address book
            AddressBook addressBook = AddressBook.deserialize(ois);

            // read old address book csv file
            try (Stream<String> stream = Files.lines(Paths.get("src/scripts/AddressBook.csv"))) {
                stream.forEach(line -> {
                    if (!line.equals(ADDRESS_BOOK_CSV_FILE_HEADER)) {
                        final String[] split = line.split(",");
                        String name = split[0].trim();
                        String addressName = split[1].trim();
                        String addressStreet = split[2].trim();
                        String addressCity = split[3].trim();
                        String addressProvince = split[4].trim();
                        String addressPostalCode = split[5].trim();
                        String phone = split[6].trim();
                        String email = split[7].trim();
                        addressBook.add(new CustomerInfo(name, phone, email));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            // save
            try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(ADDRESS_BOOK_DAT_FILE))) {
                addressBook.serialize(os);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
