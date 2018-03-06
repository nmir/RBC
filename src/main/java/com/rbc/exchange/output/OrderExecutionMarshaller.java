package com.rbc.exchange.output;

import com.lmax.disruptor.EventHandler;
import com.rbc.exchange.model.OrderExecutionEvent;

public class OrderExecutionMarshaller implements EventHandler<OrderExecutionEvent> {

    public void onEvent(OrderExecutionEvent executionEvent, long sequence, boolean endOfBatch) {
        System.out.println(executionEvent.getOrderExecution());
    }
}