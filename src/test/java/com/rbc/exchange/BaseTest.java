package com.rbc.exchange;

import com.rbc.exchange.model.Side;
import com.rbc.exchange.model.OpenOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseTest {

    private static final String ORDER_FIELD_SEPARATOR = "|";
    
    protected List<String> rawTestOrders() {
        return testOrders()
                .stream()
                .map(openOrder -> openOrder.getSide().name() + ORDER_FIELD_SEPARATOR
                        + openOrder.getRic() + ORDER_FIELD_SEPARATOR
                        + openOrder.getQuantity() + ORDER_FIELD_SEPARATOR
                        + openOrder.getPrice() + ORDER_FIELD_SEPARATOR
                        + openOrder.getUser())
                .collect(Collectors.toList());
    }

    protected List<OpenOrder> testOrders() {
        List<OpenOrder> testOrders = new ArrayList<>();
        testOrders.add(createOrder(Side.ASK, "VOD.L", 1000, 100.2, "User1", 0));
        testOrders.add(createOrder(Side.BID, "VOD.L", 1000, 100.2, "User2", 1));
        testOrders.add(createOrder(Side.BID, "VOD.L", 1000, 99, "User1", 2));
        testOrders.add(createOrder(Side.BID, "VOD.L", 1000, 101, "User1", 3));
        testOrders.add(createOrder(Side.ASK, "VOD.L", 500, 102, "User2", 4));
        testOrders.add(createOrder(Side.BID, "VOD.L", 500, 103, "User1", 5));
        testOrders.add(createOrder(Side.ASK, "VOD.L", 1000, 98, "User2", 6));
        return testOrders;
    }

    private OpenOrder createOrder(Side side, String ric, long quantity, double price, String user, long sequence) {
        return new OpenOrder(side, ric, quantity, price, user, sequence);
    }
}
