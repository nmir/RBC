package com.rbc.exchange.input;

import com.lmax.disruptor.EventHandler;
import com.rbc.exchange.model.Side;
import com.rbc.exchange.model.NewOrderEvent;

/**
 * Once a new order arrives on the input ring buffer, deserializes the order and populates the data fields
 * of the new order at the given sequence in the ring buffer.
 */
public class OrderUnmarshaller implements EventHandler<NewOrderEvent> {

    private static final int SIDE_INDEX     = 0;
    private static final int RIC_INDEX      = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final int PRICE_INDEX    = 3;
    private static final int USER_INDEX     = 4;

    public void onEvent(NewOrderEvent order, long sequence, boolean endOfBatch) throws Exception {
        unmarshall(order, order.getRawOrder());
    }

    public void unmarshall(NewOrderEvent order, String rawData) {
        String[] rawDataArray = rawData.split("\\|");
        order.setSide(Side.valueOf(rawDataArray[SIDE_INDEX]));
        order.setRic(rawDataArray[RIC_INDEX]);
        order.setQuantity(Long.parseLong(rawDataArray[QUANTITY_INDEX]));
        order.setPrice(Double.parseDouble(rawDataArray[PRICE_INDEX]));
        order.setUser(rawDataArray[USER_INDEX]);
    }
}