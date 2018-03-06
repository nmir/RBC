package com.rbc.exchange.model;

public class OrderExecution {

    private final String ric;
    private final String buyer;
    private final String seller;
    private final long quantity;
    private final double price;
    private final Side incomingOrderSide;

    public OrderExecution(String ric, String buyer, String seller, long quantity, double price, Side incomingOrderSide) {
        this.ric = ric;
        this.buyer = buyer;
        this.seller = seller;
        this.quantity = quantity;
        this.price = price;
        this.incomingOrderSide = incomingOrderSide;
    }

    public String getRic() {
        return ric;
    }

    public String getBuyer() {
        return buyer;
    }

    public String getSeller() {
        return seller;
    }

    public long getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public Side getIncomingOrderSide() {
        return incomingOrderSide;
    }

    @Override
    public String toString() {
        return "OrderExecution{" +
                "ric='" + ric + '\'' +
                ", buyer='" + buyer + '\'' +
                ", seller='" + seller + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}