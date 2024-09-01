package compasso.com.br.model.entities;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Ticket {
    private int id;
    private String plate;
    private LocalDateTime entryHour;
    private LocalDateTime exitHour;
    private int entryGate;
    private int exitGate;
    private String parkingSpot;
    private Double amount;

    public Ticket() {
    }

    public Ticket(String plate, LocalDateTime entryHour, LocalDateTime exitHour, int entryGate, int exitGate, String parkingSpot, Double amount) {
        this.plate = plate;
        this.entryHour = entryHour;
        this.exitHour = exitHour;
        this.entryGate = entryGate;
        this.exitGate = exitGate;
        this.parkingSpot = parkingSpot;
        this.amount = amount;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public LocalDateTime getEntryHour() {
        return entryHour;
    }

    public void setEntryHour(LocalDateTime entryHour) {
        this.entryHour = entryHour;
    }

    public LocalDateTime getExitHour() {
        return exitHour;
    }

    public void setExitHour(LocalDateTime exitHour) {
        this.exitHour = exitHour;
    }

    public int getEntryGate() {
        return entryGate;
    }

    public void setEntryGate(int entryGate) {
        this.entryGate = entryGate;
    }

    public int getExitGate() {
        return exitGate;
    }

    public void setExitGate(int exitGate) {
        this.exitGate = exitGate;
    }

    public String getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(String parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double amountToBePaid(){
        double valerPerMinute = 0.10;
        this.amount = valerPerMinute * (Duration.between(entryHour, exitHour).toMinutes());
        return amount;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", plate='" + plate + '\'' +
                ", entryHour=" + entryHour +
                ", exitHour=" + exitHour +
                ", entryGate=" + entryGate +
                ", exitGate=" + exitGate +
                ", parkingSpot=" + parkingSpot +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
