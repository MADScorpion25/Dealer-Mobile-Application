package com.example.dilermobileapp.models;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Car {
    private int id;
    private String brandName;
    private String modelName;
    private short productionYear;
    private Set<Config> configs;

    public Car(String brandName, String modelName, short productionYear) {
        this.brandName = brandName;
        this.modelName = modelName;
        this.productionYear = productionYear;
    }

    public Car(){
        brandName = "";
        modelName = "";
    }
    @Override
    public String toString(){
        return id + ": " + brandName + " " + modelName + " " + productionYear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public short getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(short productionYear) {
        this.productionYear = productionYear;
    }

    public Set<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(Set<Config> configs) {
        this.configs = configs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return productionYear == car.productionYear && Objects.equals(brandName, car.brandName) && Objects.equals(modelName, car.modelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brandName, modelName, productionYear);
    }
}
