package com.rbc.exchange;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.rbc.exchange.input.ExchangeInput;
import com.rbc.exchange.input.OrderUnmarshaller;
import com.rbc.exchange.matchingengine.OrderMatcher;
import com.rbc.exchange.model.NewOrderEvent;
import com.rbc.exchange.model.OrderExecutionEvent;
import com.rbc.exchange.matchingengine.Statistics;
import com.rbc.exchange.output.ExchangeOutput;
import com.rbc.exchange.output.OrderExecutionMarshaller;
import com.rbc.exchange.matchingengine.OrderMatcherCallback;

public class Exchange {

    private final ExchangeInput exchangeInput;
    private final OrderMatcher orderMatcher;

    public Exchange() {
        // 1. Set up exchange input/output and data buffers
        Disruptor<NewOrderEvent> inputDisruptor = new Disruptor<>
                (() -> new NewOrderEvent(),
                (int) Math.pow(2, 22),
                DaemonThreadFactory.INSTANCE,
                ProducerType.MULTI,
                new BusySpinWaitStrategy());
        exchangeInput = new ExchangeInput(inputDisruptor.getRingBuffer());

        Disruptor<OrderExecutionEvent> outputDisruptor = new Disruptor<>
                (() -> new OrderExecutionEvent(),
                (int) Math.pow(2, 22),
                DaemonThreadFactory.INSTANCE,
                ProducerType.SINGLE,
                new BusySpinWaitStrategy());
        ExchangeOutput exchangeOutput = new ExchangeOutput(outputDisruptor.getRingBuffer());

        // 2. The order matching engine
        orderMatcher = new OrderMatcher(exchangeOutput);

        // 3. Link the components
        inputDisruptor.handleEventsWith(new OrderUnmarshaller()).then(orderMatcher);
        outputDisruptor.handleEventsWith(new OrderExecutionMarshaller());

        // 4. Start the data feeds
        inputDisruptor.start();
        outputDisruptor.start();
    }

    public ExchangeInput exchangeInput() {
        return exchangeInput;
    }

    public void registerMatcherCallback(OrderMatcherCallback callback) {
        orderMatcher.registerCallBack(callback);
    }

    public Statistics statistics(String ric) {
        return orderMatcher.getStastics(ric);
    }
}