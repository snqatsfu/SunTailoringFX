package Utils;

import org.junit.Test;

public class PropertiesConfigurationTest {

    @Test
    public void testWrite() {
        PropertiesConfiguration config = PropertiesConfiguration.getInstance();
        config.setProperty("test.property", "2");
        config.save();
//        config.setProperty("test.property", "1");
//        config.save();
    }

}
