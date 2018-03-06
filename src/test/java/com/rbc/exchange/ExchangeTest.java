package com.rbc.exchange;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * End-to-end test that tests the Exchange in its entirety. It spawns an instance of exchange that consists of
 * all the processing components (input, matching engine and output). It tests the wiring of the components of
 * the system in order to ensure that they are properly instantiated and communicate with each other to execute
 * the overall functionality of the system.
 */
public class ExchangeTest extends BaseTest {

    @Test
    public void testExchange() throws Exception {
        CountDownLatch matchingEngineLatch = new CountDownLatch(7);

        // Instantiate and start the exchange
        Exchange testExchange = new Exchange();

        // Set up call back to gather processing results every time an order is put through the matching engine
        final List<Double> executedPrices = new ArrayList();
        testExchange.registerMatcherCallback(statistics -> {
                executedPrices.add(statistics.averageExecutionPrice());
                matchingEngineLatch.countDown();
        });

        // Send the orders to the exchange
        List<String> testOrders = rawTestOrders();
        testOrders.forEach(order -> testExchange.exchangeInput().onOrderReceived(order));

        // Wait for the processing thread (that runs order matcher to process the orders)
        matchingEngineLatch.await();

        // Verify the processing results
        Assert.assertEquals(Double.NaN, executedPrices.get(0), 0.0001);
        Assert.assertEquals(100.2, executedPrices.get(1), 0.0001);
        Assert.assertEquals(100.2, executedPrices.get(2), 0.0001);
        Assert.assertEquals(100.2, executedPrices.get(3), 0.0001);
        Assert.assertEquals(100.2, executedPrices.get(4), 0.0001);
        Assert.assertEquals(101.1333, executedPrices.get(5), 0.0001);
        Assert.assertEquals(99.88, executedPrices.get(6), 0.0001);
    }
}
