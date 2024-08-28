package compasso.com.br.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParkingSpot {

    private int id;
    private int number;
    private boolean occupied;

    public ParkingSpot() {
    }

    public ParkingSpot(int id, int number, boolean occupied) {
        this.id = id;
        this.number = number;
        this.occupied = occupied;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }


    public void parkingSpacesAvailable(){
        List<ParkingSpot> parkingSpots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            if (!occupied) {
                parkingSpots.add(new ParkingSpot(id, i, true));
            }
        }
        System.out.println(parkingSpots);
    }

    @Override
    public String toString() {
        return "ParkingSpot{" +
                "id=" + id +
                ", number=" + number +
                ", occupied=" + occupied +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
