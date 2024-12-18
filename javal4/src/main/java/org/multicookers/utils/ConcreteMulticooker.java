package org.multicookers.utils;

import org.multicookers.Multicooker;

public class ConcreteMulticooker extends Multicooker {

    public ConcreteMulticooker(int id, String type, int capacity, int wattage, double price) {
        super(id, type, capacity, wattage, price);
    }

    @Override
    public String toString() {
        return "Multicooker ID: " + getId() + ", Type: " + getType() + ", Capacity: " + getCapacity() +
               ", Wattage: " + getWattage() + ", Price: " + getPrice();
    }
}
