package Data;

import org.junit.Test;

import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

/**
 * TODO: CLASS JAVA DOC HERE
 */
public class CustomerInfoTest {

    @Test
    public void testSort() {
        SortedSet<CustomerInfo> set = new TreeSet<>();
        set.add(new CustomerInfo("Nathan", "604-123-4567", ""));
        set.add(new CustomerInfo("Emma", "604-123-4567", ""));

        int i = 0;
        for (CustomerInfo customerInfo : set) {
            if (i == 0) {
                assertEquals("Emma", customerInfo.getName());
            } else if (i == 1) {
                assertEquals("Nathan", customerInfo.getName());
            }
            i++;
        }

        set.add(new CustomerInfo("Andy", "", ""));
        i = 0;
        for (CustomerInfo customerInfo : set) {
            if (i == 0) {
                assertEquals("Andy", customerInfo.getName());
            } else if (i == 1) {
                assertEquals("Emma", customerInfo.getName());
            } else if (i == 2) {
                assertEquals("Nathan", customerInfo.getName());
            }
            i++;
        }

        // add the same entry into a set should be ignored
        set.add(new CustomerInfo("Andy", "", ""));
        i = 0;
        for (CustomerInfo customerInfo : set) {
            if (i == 0) {
                assertEquals("Andy", customerInfo.getName());
            } else if (i == 1) {
                assertEquals("Emma", customerInfo.getName());
            } else if (i == 2) {
                assertEquals("Nathan", customerInfo.getName());
            }
            i++;
        }
    }
}
