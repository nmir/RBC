package com.rbc.exchange.matchingengine;

import com.lmax.disruptor.EventHandler;
import com.rbc.exchange.model.NewOrderEvent;
import com.rbc.exchange.model.OpenOrder;
import com.rbc.exchange.model.OrderExecution;
import com.rbc.exchange.output.ExchangeOutput;

import java.util.*;

public class OrderMatcher implements EventHandler<NewOrderEvent> {

    private final Map<String, MatchingEngine> matchingEngines;
    private final ExchangeOutput exchangeOutput;
    private final List<OrderMatcherCallback> callbacks = new ArrayList();

    public OrderMatcher(ExchangeOutput exchangeOutput) {
        this.matchingEngines = new HashMap<>();
        this.exchangeOutput = exchangeOutput;
    }

    public void registerCallBack(OrderMatcherCallback callback) {
        callbacks.add(callback);
    }

    public void onEvent(NewOrderEvent order, long sequence, boolean endOfBatch) {
        OpenOrder openOrder = new OpenOrder(order.getSide(), order.getRic(), order.getQuantity(), order.getPrice(), order.getUser(), sequence);
        matchingEngines.putIfAbsent(order.getRic(), new MatchingEngine());
        Optional<OrderExecution> execution = matchingEngines.get(order.getRic()).submitOrder(openOrder);
        if (execution.isPresent()) {
            exchangeOutput.onOrderExecution(execution.get());
        }

        callbacks.forEach(callback -> callback.run(matchingEngines.get(openOrder.getRic()).statistics()));
    }

    public Statistics getStastics(String ric) {
        return matchingEngines.get(ric).statistics();
    }
}