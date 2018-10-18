package Utils;

import GUI.GuiUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesConfiguration {

    private static final File CONFIG_FIle = new File(GuiUtils.SETTINGS_DIR_PATH + "/" + "config.properties");

    private static Properties theInstance;

    public static Properties getInstance() {
        if (theInstance == null) {
            theInstance = new Properties();
            try (FileReader reader = new FileReader(CONFIG_FIle)) {
                theInstance.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return theInstance;
    }

}
