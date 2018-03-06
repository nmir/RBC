package com.rbc.exchange.input;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.rbc.exchange.model.NewOrderEvent;

/**
 * Receives raw order messages from the 'network' and puts them on the input ring buffer.
 */

public class ExchangeInput {

    private final RingBuffer<NewOrderEvent> incomingOrdersBuffer;
    private final OrderTranslator eventTranslator;

    public ExchangeInput(RingBuffer<NewOrderEvent> incomingBuffer) {
        this.incomingOrdersBuffer = incomingBuffer;
        this.eventTranslator = new OrderTranslator();
    }

    public void onOrderReceived(String rawOrder) {
        this.incomingOrdersBuffer.publishEvent(eventTranslator, rawOrder);
    }

    private class OrderTranslator implements EventTranslatorOneArg<NewOrderEvent, String> {
        public void translateTo(NewOrderEvent order, long sequence, String raw) {
            order.setRawOrder(raw);
        }
    }
}