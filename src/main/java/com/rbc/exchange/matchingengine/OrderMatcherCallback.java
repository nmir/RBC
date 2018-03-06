package com.rbc.exchange.matchingengine;

@FunctionalInterface
public interface OrderMatcherCallback {
    void run(Statistics statistics);
}
