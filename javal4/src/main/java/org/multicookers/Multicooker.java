package org.multicookers;

import java.util.Objects;

public abstract class Multicooker {
    protected int id;
    protected String type;
    protected int capacity;
    protected int wattage;
    protected double price;

    public Multicooker(int id, String type, int capacity, int wattage, double price) {
        this.id = id;
        this.type = type;
        this.capacity = capacity;
        this.wattage = wattage;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int area) {
        this.capacity = area;
    }

    public int getWattage() {
        return wattage;
    }

    public void setWattage(int floors) {
        this.wattage = floors;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Multicooker{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", area=" + capacity +
                ", floors=" + wattage +
                ", price=" + price +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
        return false;
    }
    Multicooker multicooker = (Multicooker) obj;
    return id == multicooker.id &&
           Double.compare(multicooker.capacity, capacity) == 0 &&
           wattage == multicooker.wattage &&
           Double.compare(multicooker.price, price) == 0 &&
           type.equals(multicooker.type); // Проверяем ключевые поля
    }

    @Override
    public int hashCode() {
    return Objects.hash(id, capacity, wattage, price, type);
    }

}
