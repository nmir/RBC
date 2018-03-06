package com.rbc.exchange.matchingengine;

import com.rbc.exchange.model.OpenInterest;
import com.rbc.exchange.model.OpenOrder;
import com.rbc.exchange.model.OrderExecution;
import com.rbc.exchange.model.Side;

import java.util.HashMap;
import java.util.Map;

public class Statistics {
    private Map<Double, Long> totalOpenBidQuantityAtPricePoints;
    private Map<Double, Long> totalOpenAskQuantityAtPricePoints;

    private long totalExecutedQuantity;
    private double totalQuantityWeightedPrice;

    private Map<String, Long> totalExecutedQuantityPerUser;

    public Statistics() {
        totalOpenBidQuantityAtPricePoints = new HashMap<>();
        totalOpenAskQuantityAtPricePoints = new HashMap<>();

        totalExecutedQuantity = 0;
        totalQuantityWeightedPrice = 0;

        totalExecutedQuantityPerUser = new HashMap<>();
    }

    public void update(OpenOrder newOrder) {
        if (newOrder.getSide().equals(Side.ASK)) {
            totalOpenAskQuantityAtPricePoints.merge(newOrder.getPrice(), newOrder.getQuantity(), Long::sum);
        } else {
            totalOpenBidQuantityAtPricePoints.merge(newOrder.getPrice(), newOrder.getQuantity(), Long::sum);
        }
    }

    public void update(OrderExecution execution, OpenOrder bestMatch) {

        if (bestMatch.getSide().equals(Side.ASK)) {
            totalOpenAskQuantityAtPricePoints.merge(bestMatch.getPrice(), -1 * bestMatch.getQuantity(), Long::sum);
            totalOpenBidQuantityAtPricePoints.merge(execution.getPrice(), -1 * execution.getQuantity(), Long::sum);
        } else {
            totalOpenBidQuantityAtPricePoints.merge(bestMatch.getPrice(), -1 * bestMatch.getQuantity(), Long::sum);
            totalOpenAskQuantityAtPricePoints.merge(execution.getPrice(), -1 * execution.getQuantity(), Long::sum);
        }

        if (totalOpenBidQuantityAtPricePoints.containsKey(bestMatch.getPrice()) &&
                totalOpenBidQuantityAtPricePoints.get(bestMatch.getPrice()) == 0) {
            totalOpenBidQuantityAtPricePoints.remove(bestMatch.getPrice());
        }
        if (totalOpenAskQuantityAtPricePoints.containsKey(bestMatch.getPrice()) &&
                totalOpenAskQuantityAtPricePoints.get(bestMatch.getPrice()) == 0) {
            totalOpenAskQuantityAtPricePoints.remove(bestMatch.getPrice());
        }
        if (totalOpenBidQuantityAtPricePoints.containsKey(execution.getPrice()) &&
                totalOpenBidQuantityAtPricePoints.get(execution.getPrice()) == 0) {
            totalOpenBidQuantityAtPricePoints.remove(execution.getPrice());
        }
        if (totalOpenAskQuantityAtPricePoints.containsKey(execution.getPrice()) &&
                totalOpenAskQuantityAtPricePoints.get(execution.getPrice()) == 0) {
            totalOpenAskQuantityAtPricePoints.remove(execution.getPrice());
        }


        totalExecutedQuantity = totalExecutedQuantity + execution.getQuantity();
        totalQuantityWeightedPrice = totalQuantityWeightedPrice + (execution.getQuantity() * execution.getPrice());

        totalExecutedQuantityPerUser.merge(execution.getBuyer(), execution.getQuantity(), Long::sum);
        totalExecutedQuantityPerUser.merge(execution.getSeller(), -1 * execution.getQuantity(), Long::sum);
    }

    public OpenInterest openInterest() {
        return new OpenInterest(totalOpenBidQuantityAtPricePoints, totalOpenAskQuantityAtPricePoints);
    }

    public double averageExecutionPrice() {
        return totalQuantityWeightedPrice/totalExecutedQuantity;
    }

    public long executedQuantity(String user) {
        return totalExecutedQuantityPerUser.get(user);
    }
}