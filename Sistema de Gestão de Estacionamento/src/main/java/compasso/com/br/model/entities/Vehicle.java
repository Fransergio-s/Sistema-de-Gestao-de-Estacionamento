package compasso.com.br.model.entities;

import compasso.com.br.model.entities.enums.Category;
import compasso.com.br.model.entities.enums.Type;

import java.util.Objects;

public class Vehicle {
    private int id;
    private String licensePlate;
    private Type type;
    private Category category;

    public Vehicle() {
    }

    public Vehicle(int id, String plate, Type type, Category category) {
        this.id = id;
        this.licensePlate = plate;
        this.type = type;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String plate) {
        this.licensePlate = plate;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", licensePlate='" + licensePlate + '\'' +
                ", type=" + type +
                ", category=" + category +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return id == vehicle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


}
