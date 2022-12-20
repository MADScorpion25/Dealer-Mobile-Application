package com.example.dilermobileapp.models;

import com.example.dilermobileapp.models.enums.CarClass;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Config {
    private int id;
    private String configurationName;
    private int price;
    private short power;
    private Set<Car> cars;
    private Special special;

    public Config() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public short getPower() {
        return power;
    }

    public void setPower(short power) {
        this.power = power;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    public Special getSpecial() {
        return special;
    }

    public void setSpecial(Special special) {
        this.special = special;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return id == config.id && price == config.price && power == config.power && configurationName.equals(config.configurationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, configurationName, price, power);
    }

    @Override
    public String toString() {
        int sId = 0;
        if(special != null) sId = special.getId();
        return id + ": "
                + configurationName
                + " " + price
                + " " + power
                + " " + sId;
    }
}
