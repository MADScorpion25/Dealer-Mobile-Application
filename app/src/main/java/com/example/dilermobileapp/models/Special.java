package com.example.dilermobileapp.models;

import com.example.dilermobileapp.models.enums.CarClass;
import com.example.dilermobileapp.models.enums.DriveType;

import java.util.Objects;

public class Special {
    private int id;
    private CarClass carClass;
    private DriveType driveType;
    private String description;

    public Special() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CarClass getCarClass() {
        return carClass;
    }

    public void setCarClass(CarClass carClass) {
        this.carClass = carClass;
    }

    public DriveType getDriveType() {
        return driveType;
    }

    public void setDriveType(DriveType driveType) {
        this.driveType = driveType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return id + ": "
                + carClass + " "
                + driveType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Special special = (Special) o;
        return id == special.id && carClass == special.carClass && driveType == special.driveType && description.equals(special.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carClass, driveType, description);
    }
}
