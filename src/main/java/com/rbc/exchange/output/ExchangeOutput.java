package com.rbc.exchange.output;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.rbc.exchange.model.OrderExecution;
import com.rbc.exchange.model.OrderExecutionEvent;

public class ExchangeOutput {

    private final RingBuffer<OrderExecutionEvent> orderExecutionsBuffer;
    private final OrderExecutionTranslator orderExecutionTranslator;

    public ExchangeOutput(RingBuffer<OrderExecutionEvent> outgoingBuffer) {
        this.orderExecutionsBuffer = outgoingBuffer;
        this.orderExecutionTranslator = new OrderExecutionTranslator();
    }

    public void onOrderExecution(OrderExecution execution) {
        this.orderExecutionsBuffer.publishEvent(orderExecutionTranslator, execution);
    }

    private class OrderExecutionTranslator implements EventTranslatorOneArg<OrderExecutionEvent, OrderExecution> {

        @Override
        public void translateTo(OrderExecutionEvent orderExecutionEvent, long sequence, OrderExecution execution) {
            orderExecutionEvent.setOrderExecution(execution);
        }
    }
}