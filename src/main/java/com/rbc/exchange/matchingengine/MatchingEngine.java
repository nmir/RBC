package com.rbc.exchange.matchingengine;

import com.rbc.exchange.model.Side;
import com.rbc.exchange.model.OpenOrder;
import com.rbc.exchange.model.OrderExecution;

import java.util.*;

public class MatchingEngine {

    private final int NUMBER_OF_ORDERS_WITH_SAME_QUANTITY = 20;
    private final Map<Long, Set<OpenOrder>> buyOrders;
    private final Map<Long, Set<OpenOrder>> sellOrders;
    private final Statistics statistics;

    public MatchingEngine() {
        this.buyOrders = new HashMap<>();
        this.sellOrders = new HashMap<>();
        this.statistics = new Statistics();
    }

    public Statistics statistics() {
        return statistics;
    }

    public Optional<OrderExecution> submitOrder(OpenOrder openOrder) {
        statistics.update(openOrder);
        if (openOrder.getSide().equals(Side.BID)) {
            buyOrders.putIfAbsent(openOrder.getQuantity(), new HashSet<>(NUMBER_OF_ORDERS_WITH_SAME_QUANTITY));
            buyOrders.get(openOrder.getQuantity()).add(openOrder);
            return matchBuyOrder(openOrder);
        } else {
            sellOrders.putIfAbsent(openOrder.getQuantity(), new HashSet<>(NUMBER_OF_ORDERS_WITH_SAME_QUANTITY));
            sellOrders.get(openOrder.getQuantity()).add(openOrder);
            return matchSellOrder(openOrder);
        }
    }

    private Optional<OrderExecution> matchBuyOrder(OpenOrder buyOrder) {
        // Check if there is a match. If not, return.
        if (!sellOrders.containsKey(buyOrder.getQuantity())) {
            return Optional.empty();
        }

        Set<OpenOrder> potentialMatches = sellOrders.get(buyOrder.getQuantity());
        OpenOrder bestMatch = null;
        for (OpenOrder sellOrder : potentialMatches) {
            if (buyOrder.getPrice() < sellOrder.getPrice()) {
                continue;
            }
            if (bestMatch == null) {
                bestMatch = sellOrder;
            } else if (sellOrder.getPrice() == bestMatch.getPrice() && sellOrder.getSequence() < bestMatch.getSequence()) {
                bestMatch = sellOrder;
            } else if (sellOrder.getPrice() < bestMatch.getPrice()) {
                bestMatch = sellOrder;
            }
        }

        // Match found. Execute
        if (bestMatch != null) {
            OrderExecution execution = new OrderExecution(bestMatch.getRic(), buyOrder.getUser(), bestMatch.getUser(),
                    bestMatch.getQuantity(), buyOrder.getPrice(), buyOrder.getSide());
            buyOrders.get(buyOrder.getQuantity()).remove(buyOrder);
            sellOrders.get(bestMatch.getQuantity()).remove(bestMatch);
            statistics.update(execution, bestMatch);
            return Optional.of(execution);
        } else {
            return Optional.empty();
        }
    }

    private Optional<OrderExecution> matchSellOrder(OpenOrder sellOrder) {
        // Check if there is a match. If not, return.
        if (!buyOrders.containsKey(sellOrder.getQuantity())) {
            return Optional.empty();
        }

        Set<OpenOrder> potentialMatches = buyOrders.get(sellOrder.getQuantity());
        OpenOrder bestMatch = null;
        for (OpenOrder buyOrder : potentialMatches) {
            if (buyOrder.getPrice() < sellOrder.getPrice()) {
                continue;
            }
            if (bestMatch == null) {
                bestMatch = buyOrder;
            } else if (buyOrder.getPrice() == bestMatch.getPrice() && buyOrder.getSequence() < bestMatch.getSequence()) {
                bestMatch = buyOrder;
            } else if (buyOrder.getPrice() > bestMatch.getPrice()) {
                bestMatch = buyOrder;
            }
        }

        // Match found. Execute
        if (bestMatch != null) {
            OrderExecution execution = new OrderExecution(bestMatch.getRic(), bestMatch.getUser(), sellOrder.getUser(),
                    bestMatch.getQuantity(), sellOrder.getPrice(), sellOrder.getSide());
            sellOrders.get(sellOrder.getQuantity()).remove(sellOrder);
            buyOrders.get(bestMatch.getQuantity()).remove(bestMatch);
            statistics.update(execution, bestMatch);
            return Optional.of(execution);
        } else {
            return Optional.empty();
        }
    }
}