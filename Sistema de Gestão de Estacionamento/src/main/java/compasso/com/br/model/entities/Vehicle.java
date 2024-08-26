package compasso.com.br.model.entities;

import compasso.com.br.model.entities.enums.Type;

import java.util.Objects;

public class Vehicle {
    private int id;
    private String licensePlate;
    private Type type;

    public Vehicle() {
    }

    public Vehicle(int id, String plate, String type) {
        this.id = id;
        this.licensePlate = plate;
        this.type = Type.valueOf(type);
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

    public void setType(String type) {
        this.type = Type.valueOf(type);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", plate='" + licensePlate + '\'' +
                ", type='" + type + '\'' +
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
