package com.rbc.exchange.model;

import java.util.Objects;

public class OpenOrder {

    private Side side;
    private String ric;
    private long quantity;
    private double price;
    private String user;
    private long sequence;

    public OpenOrder(Side side, String ric, long quantity, double price, String user, long sequence) {
        this.side = side;
        this.ric = ric;
        this.quantity = quantity;
        this.price = price;
        this.user = user;
        this.sequence = sequence;
    }

    public Side getSide() {
        return side;
    }

    public String getRic() {
        return ric;
    }

    public long getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getUser() {
        return user;
    }

    public long getSequence() {
        return sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenOrder openOrder = (OpenOrder) o;
        return quantity == openOrder.quantity &&
                Double.compare(openOrder.price, price) == 0 &&
                sequence == openOrder.sequence &&
                side == openOrder.side &&
                Objects.equals(ric, openOrder.ric) &&
                Objects.equals(user, openOrder.user);
    }

    @Override
    public int hashCode() {

        return Objects.hash(side, ric, quantity, price, user, sequence);
    }
}
