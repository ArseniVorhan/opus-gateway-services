package com.adeo.connector.opus.service.models;

/**
 * Created by stievena on 01/03/2017.
 */
public class FamilySort {
    public enum Order {
        ASC, DESC
    }

    public FamilySort(String name, Order order) {
        this.name = name;
        this.order = order;
    }

    private String name;
    private Order order;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
