package compasso.com.br.model.manager;

import compasso.com.br.model.entities.enums.Category;

public class ParkingLotManager {

    private int totalSpots; //
    private int availableSpots;
    private int reservedMonthlySpots;
    private int availableMonthlySpots;

    public ParkingLotManager() {
        this.totalSpots = 500;
        this.reservedMonthlySpots = 200;
        this.availableSpots = totalSpots - reservedMonthlySpots;
        this.availableMonthlySpots = reservedMonthlySpots;
    }

    public boolean parkVehicle(Category category, boolean isMonthly) {
        int requiredSpots = getRequiredSpots(category);

        if (isMonthly) {
            if (availableMonthlySpots >= requiredSpots) {
                availableMonthlySpots -= requiredSpots;
                return true; // Veículo estacionado com sucesso
            } else {
                return false; // Sem vagas suficientes para mensalistas
            }
        } else {
            if (availableSpots >= requiredSpots) {
                availableSpots -= requiredSpots;
                return true; // Veículo estacionado com sucesso
            } else {
                return false; // Sem vagas suficientes para avulsos
            }
        }
    }

    public void leaveVehicle(Category category, boolean isMonthly) {
        int requiredSpots = getRequiredSpots(category);

        if (isMonthly) {
            availableMonthlySpots += requiredSpots;
        } else {
            availableSpots += requiredSpots;
        }
    }

    private int getRequiredSpots(Category category) {
        switch (category) {
            case Motorcycles:
                return 1;
            case PassengerCars:
                return 2;
            case DeliveryTrucks:
                return 4;
            default:
                throw new IllegalArgumentException("Tipo de veículo desconhecido: " + category);
        }
    }

    public int getAvailableSpots() {
        return availableSpots;
    }

    public int getAvailableMonthlySpots() {
        return availableMonthlySpots;
    }
}
