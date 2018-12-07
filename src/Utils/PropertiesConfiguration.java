package Utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class PropertiesConfiguration extends Properties {

    private static final File CONFIG_FILE = new File(PathUtils.SETTINGS_DIR_PATH + "/" + "config.properties");

    private static PropertiesConfiguration theInstance;

    public static PropertiesConfiguration getInstance() {
        if (theInstance == null) {
            theInstance = new PropertiesConfiguration();
        }

        return theInstance;
    }

    private PropertiesConfiguration() {
        super();
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            store(writer, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
