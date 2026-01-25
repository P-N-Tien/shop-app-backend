package com.shop_app.shipment.enums;

public enum ShipmentStatus {

    // the order just has been created, no delivery request has been made yet
    WAITING_FOR_PICKUP,
    // shipper has picked up the product
    PICKED_UP,
    // currently in transit
    IN_TRANSIT,
    // delivering to recipient
    OUT_FOR_DELIVERY,
    // delivery successful
    DELIVERED,
    // delivery failed
    DELIVERY_FAILED,
    // cancelled
    CANCELLED;
}