package com.rbc.exchange.matchingengine;

import com.rbc.exchange.BaseTest;
import com.rbc.exchange.model.OpenOrder;
import com.rbc.exchange.model.OrderExecution;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

/**
 * Tests the core business logic of the exchange: the matching engine (Each RIC has got its own matching engine in the
 * exchange application). Orders are put through the matching engine, and following results are verified:
 * a. The average execution prices
 * b. Executed quantities for users
 * c. The open buy and sell interests
 */
public class MatchingEngineTest extends BaseTest {

    @Test
    public void testOrderProcessing() {

        MatchingEngine matchingEngine = new MatchingEngine();
        List<OpenOrder> testOrders = testOrders();

        Optional<OrderExecution> execution = matchingEngine.submitOrder(testOrders.get(0));
        Assert.assertFalse(execution.isPresent());
        Assert.assertTrue(matchingEngine.statistics().openInterest().getTotalOpenBidQuantityAtPricePoints().size() == 0);
        Assert.assertTrue(matchingEngine.statistics().openInterest().getTotalOpenAskQuantityAtPricePoints().get(100.2) == 1000);

        execution = matchingEngine.submitOrder(testOrders.get(1));
        Assert.assertTrue(execution.isPresent());
        Assert.assertEquals(-1000, matchingEngine.statistics().executedQuantity("User1"), 0);
        Assert.assertEquals(1000, matchingEngine.statistics().executedQuantity("User2"), 0);
        Assert.assertEquals(100.2, matchingEngine.statistics().averageExecutionPrice(), 0);
        Assert.assertTrue(matchingEngine.statistics().openInterest().getTotalOpenBidQuantityAtPricePoints().size() == 0);
        Assert.assertTrue(matchingEngine.statistics().openInterest().getTotalOpenAskQuantityAtPricePoints().size() == 0);

        execution = matchingEngine.submitOrder(testOrders.get(2));
        Assert.assertFalse(execution.isPresent());
        Assert.assertEquals(-1000, matchingEngine.statistics().executedQuantity("User1"), 0);
        Assert.assertEquals(1000, matchingEngine.statistics().executedQuantity("User2"), 0);
        Assert.assertEquals(100.2, matchingEngine.statistics().averageExecutionPrice(), 0);
        Assert.assertTrue(matchingEngine.statistics().openInterest().getTotalOpenBidQuantityAtPricePoints().get(99.0) == 1000);
        Assert.assertTrue(matchingEngine.statistics().openInterest().getTotalOpenAskQuantityAtPricePoints().size() == 0);

        execution = matchingEngine.submitOrder(testOrders.get(3));
        Assert.assertFalse(execution.isPresent());
        Assert.assertEquals(-1000, matchingEngine.statistics().executedQuantity("User1"), 0);
        Assert.assertEquals(100.2, matchingEngine.statistics().averageExecutionPrice(), 0);
        Assert.assertEquals(1000, matchingEngine.statistics().executedQuantity("User2"), 0);
        Assert.assertTrue(matchingEngine.statistics().openInterest().getTotalOpenBidQuantityAtPricePoints().get(101.0) == 1000);
        Assert.assertTrue(matchingEngine.statistics().openInterest().getTotalOpenBidQuantityAtPricePoints().get(99.0) == 1000);
        Assert.assertTrue(matchingEngine.statistics().openInterest().getTotalOpenAskQuantityAtPricePoints().size() == 0);

        execution = matchingEngine.submitOrder(testOrders.get(4));
        Assert.assertFalse(execution.isPresent());
        Assert.assertEquals(-1000, matchingEngine.statistics().executedQuantity("User1"), 0);
        Assert.assertEquals(1000, matchingEngine.statistics().executedQuantity("User2"), 0);
        Assert.assertEquals(100.2, matchingEngine.statistics().averageExecutionPrice(), 0);
        Assert.assertTrue(matchingEngine.statistics().openInterest().getTotalOpenBidQuantityAtPricePoints().get(101.0) == 1000);
        Assert.assertTrue(matchingEngine.statistics().openInterest().getTotalOpenBidQuantityAtPricePoints().get(99.0) == 1000);
        Assert.assertTrue(matchingEngine.statistics().openInterest().getTotalOpenAskQuantityAtPricePoints().get(102.0) == 500);

        execution = matchingEngine.submitOrder(testOrders.get(5));
        Assert.assertTrue(execution.isPresent());
        Assert.assertEquals(-500, matchingEngine.statistics().executedQuantity("User1"), 0);
        Assert.assertEquals(500, matchingEngine.statistics().executedQuantity("User2"), 0);
        Assert.assertEquals(101.1333, matchingEngine.statistics().averageExecutionPrice(), 0.0001);
        Assert.assertTrue(matchingEngine.statistics().openInterest().getTotalOpenBidQuantityAtPricePoints().get(101.0) == 1000);
        Assert.assertTrue(matchingEngine.statistics().openInterest().getTotalOpenBidQuantityAtPricePoints().get(99.0) == 1000);
        Assert.assertTrue(matchingEngine.statistics().openInterest().getTotalOpenAskQuantityAtPricePoints().size() == 0);

        execution = matchingEngine.submitOrder(testOrders.get(6));
        Assert.assertTrue(execution.isPresent());
        Assert.assertEquals(500, matchingEngine.statistics().executedQuantity("User1"), 0);
        Assert.assertEquals(-500, matchingEngine.statistics().executedQuantity("User2"), 0);
        Assert.assertEquals(99.88, matchingEngine.statistics().averageExecutionPrice(), 0.0001);
        Assert.assertTrue(matchingEngine.statistics().openInterest().getTotalOpenBidQuantityAtPricePoints().get(99.0) == 1000);
        Assert.assertTrue(matchingEngine.statistics().openInterest().getTotalOpenAskQuantityAtPricePoints().size() == 0);
    }
}
