package Data;


import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CashCalculatorTest {

    @Test
    public void testSingleBill() {
        Map<CashCalculator.BillType, Integer> result = CashCalculator.distribute(100);
        assertEquals(1, result.size());
        int numBills = result.get(CashCalculator.BillType.HUNDRED);
        assertEquals(1, numBills);

        result = CashCalculator.distribute(50);
        assertEquals(1, result.size());
        numBills = result.get(CashCalculator.BillType.FIFTY);
        assertEquals(1, numBills);

        result = CashCalculator.distribute(20);
        assertEquals(1, result.size());
        numBills = result.get(CashCalculator.BillType.TWENTY);
        assertEquals(1, numBills);

        result = CashCalculator.distribute(10);
        assertEquals(1, result.size());
        numBills = result.get(CashCalculator.BillType.TEN);
        assertEquals(1, numBills);

        result = CashCalculator.distribute(5);
        assertEquals(1, result.size());
        numBills = result.get(CashCalculator.BillType.FIVE);
        assertEquals(1, numBills);

        result = CashCalculator.distribute(2);
        assertEquals(1, result.size());
        numBills = result.get(CashCalculator.BillType.TOONIE);
        assertEquals(1, numBills);

        result = CashCalculator.distribute(1);
        assertEquals(1, result.size());
        numBills = result.get(CashCalculator.BillType.LOONIE);
        assertEquals(1, numBills);

        result = CashCalculator.distribute(0.25);
        assertEquals(1, result.size());
        numBills = result.get(CashCalculator.BillType.QUARTER);
        assertEquals(1, numBills);

        result = CashCalculator.distribute(0.1);
        assertEquals(1, result.size());
        numBills = result.get(CashCalculator.BillType.DIME);
        assertEquals(1, numBills);

        result = CashCalculator.distribute(0.05);
        assertEquals(1, result.size());
        numBills = result.get(CashCalculator.BillType.NICKEL);
        assertEquals(1, numBills);
    }

    @Test
    public void testCombination() {
        Map<CashCalculator.BillType, Integer> result = CashCalculator.distribute(388.65);
        int numBills = result.get(CashCalculator.BillType.HUNDRED);
        assertEquals(3, numBills);
        numBills = result.get(CashCalculator.BillType.FIFTY);
        assertEquals(1, numBills);
        numBills = result.get(CashCalculator.BillType.TWENTY);
        assertEquals(1, numBills);
        numBills = result.get(CashCalculator.BillType.TEN);
        assertEquals(1, numBills);
        numBills = result.get(CashCalculator.BillType.FIVE);
        assertEquals(1, numBills);
        numBills = result.get(CashCalculator.BillType.TOONIE);
        assertEquals(1, numBills);
        numBills = result.get(CashCalculator.BillType.LOONIE);
        assertEquals(1, numBills);
        numBills = result.get(CashCalculator.BillType.QUARTER);
        assertEquals(2, numBills);
        numBills = result.get(CashCalculator.BillType.DIME);
        assertEquals(1, numBills);
    }

    @Test
    public void testRound() {
        assertEquals(10.0, CashCalculator.round(10.0), 1e-6);
        assertEquals(10.10, CashCalculator.round(10.11), 1e-6);
        assertEquals(10.10, CashCalculator.round(10.14), 1e-6);
        assertEquals(10.15, CashCalculator.round(10.15), 1e-6);
        assertEquals(10.20, CashCalculator.round(10.16), 1e-6);
        assertEquals(10.20, CashCalculator.round(10.20), 1e-6);
    }

}
