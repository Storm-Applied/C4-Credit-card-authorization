package stormapplied.creditcard.services;

import stormapplied.creditcard.Order;

import java.io.Serializable;

public class NotificationService implements Serializable {
    public void notifyOrderHasBeenProcessed(Order order) {
        // In a real scenario, this would notify any downstream systems that the order has been processed.
    }
}
