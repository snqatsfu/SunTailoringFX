package Data;

import java.util.LinkedHashMap;
import java.util.Map;

public class CashCalculator {
    public enum BillType {
        HUNDRED("$100", 10000),
        FIFTY("$50", 5000),
        TWENTY("$20", 2000),
        TEN("$10", 1000),
        FIVE("$5", 500),
        TOONIE("$2", 200),
        LOONIE("$1", 100),
        QUARTER("25C", 25),
        DIME("10C", 10),
        NICKEL("5C", 5);

        private final String name;
        private final int cents;

        BillType(String name, int cents) {
            this.name = name;
            this.cents = cents;
        }
    }

    public static double round(double cash) {
        int cashCents = (int) (cash * 1000 / 10);
        int centsLeft = cashCents % 10;
        int newCents = 0;
        if (centsLeft == 5) {
            newCents = 5;
        } else if (centsLeft > 5) {
            newCents = 10;
        }
        return (cashCents - centsLeft + newCents) * 10.0 / 1000.0;
    }

    public static Map<BillType, Integer> distribute(double cash) {
        Map<BillType, Integer> retVal = new LinkedHashMap<>();

        int cashCents = (int) (cash * 1000 / 10);   // x100 is not good enough - e.g. 8.95 gives 894!
        for (BillType billType : BillType.values()) {
            int num = cashCents / billType.cents;
            int remainder = cashCents % billType.cents;
            if (num > 0) {
                retVal.put(billType, num);
                cashCents -= num * billType.cents;
            }
            if (remainder == 0) {
                break;
            }
        }

        return retVal;
    }

    public static String asString(Map<BillType, Integer> distribution) {
        String retVal = "";
        for (BillType bill : distribution.keySet()) {
            Integer number = distribution.get(bill);
            retVal += number + "x" + bill.name + " ";
        }
        return retVal;
    }

}
