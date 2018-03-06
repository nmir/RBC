# RBC Test

This document provides a brief overview of the coding assignment for RBC. It documents the instructions on how to
run the code, the design of the application and its key components and the rationale behind the design decisions.

## Running the application
Download the code on to your machine and run the 'mvn clean test' to build and
run the junit tests. The tests are:

1. **com.rbs.exchange.ExchangeTest:** Wires up the components of the exchange application and tests the exchange order
matching by pushing test trades into the exchange input.

2. **com.rbs.exchange.matchingengine.MatchingEngineTest:** Unit test that tests the order matching component in isolation.
The buy and sell orders (from the coding test example) are pushed into the exchange and the output is verified.

## Main components

1. **Exchange input (package com.rbs.exchange.input):** Maintains a ring buffer where the incoming messages are copied to
(ExchangeInput.java). OrdOrderUnmarshaller.java is used to translate the 'raw network message' (this is just to simulate
the deserialisation of messages in wire-fromat) into exchange domain model.

2. **Order matching (package com.rbc.exchange.matchingengine):** OrderMatcher.java is the entry point into the order matching
logic of the exchange. It maintains matching engines (MatchingEngine.java) for each RIC and runs within a single thread
(all matching engines run within the same thread and process orders in sequential manner reading from the input buffer -
one order at a time).

This class provides a call back to notify the listener when the order has been processed. This is a convenience hook to
unit test the application. In a production application, this would unnecessarily slow the fast-path order processing
(as the call-back is also executed on the order matching thread).

3. **Exchange output (package com.rbs.exchange.output):** In case there are order executions within the matching engine,
this module is reponsible for sending the executions out to the participants (for this test, the order executions are just
printed to the console). ExchangeOutput.java writes the order execution message on to the output ring buffer which is
picked up and printed to console by OrderExecutionMarshaller.

The main idea of implementing the exchange in terms of input disruptor, order matcher and output disruptor is to increase
the performane of the system (reference https://martinfowler.com/articles/lmax.html)


## Potential improvements/ideas
1. The matching functions for the buy and sell orders are more or less copy paste of each other. This really needs to be
refactored to remove the duplicate code!

2. Use off heap data structures for holding the data within the matching engines. This would remove the need for GC within
the matching engine and potentially speed things up (with the obvious downside of pre-existing limitations on the number
of orders etc that can be handled).