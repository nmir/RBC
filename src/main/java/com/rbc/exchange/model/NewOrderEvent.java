package com.rbc.exchange.model;

public class NewOrderEvent {

    private Side side;
    private String ric;
    private long quantity;
    private double price;
    private String user;

    private String rawOrder;

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public String getRic() {
        return ric;
    }

    public void setRic(String ric) {
        this.ric = ric;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRawOrder() {
        return rawOrder;
    }

    public void setRawOrder(String rawOrder) {
        this.rawOrder = rawOrder;
    }
}
