package com.rbc.exchange.model;

import java.util.Map;

/**
 * Total quantity of all open orders and direction at each price point
 */
public class OpenInterest {

    private Map<Double, Long> totalOpenBidQuantityAtPricePoints;
    private Map<Double, Long> totalOpenAskQuantityAtPricePoints;

    public OpenInterest(Map<Double, Long> totalOpenBidQuantityAtPricePoints, Map<Double, Long> totalOpenAskQuantityAtPricePoints) {
        this.totalOpenBidQuantityAtPricePoints = totalOpenBidQuantityAtPricePoints;
        this.totalOpenAskQuantityAtPricePoints = totalOpenAskQuantityAtPricePoints;
    }

    public Map<Double, Long> getTotalOpenBidQuantityAtPricePoints() {
        return totalOpenBidQuantityAtPricePoints;
    }

    public Map<Double, Long> getTotalOpenAskQuantityAtPricePoints() {
        return totalOpenAskQuantityAtPricePoints;
    }
}
