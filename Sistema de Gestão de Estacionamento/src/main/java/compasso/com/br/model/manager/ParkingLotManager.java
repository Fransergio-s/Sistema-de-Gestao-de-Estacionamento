package compasso.com.br.model.manager;

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

    public boolean parkVehicle(Type type, boolean isMonthly) {
        int requiredSpots = getRequiredSpots(type);

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

    public void leaveVehicle(Type type, boolean isMonthly) {
        int requiredSpots = getRequiredSpots(type);

        if (isMonthly) {
            availableMonthlySpots += requiredSpots;
        } else {
            availableSpots += requiredSpots;
        }
    }

    private int getRequiredSpots(Type type) {
        switch (type) {
            case Motorcycles:
                return 1;
            case PassengerCars:
                return 2;
            case Trucks:
                return 4;
            default:
                throw new IllegalArgumentException("Tipo de veículo desconhecido: " + type);
        }
    }

    public int getAvailableSpots() {
        return availableSpots;
    }

    public int getAvailableMonthlySpots() {
        return availableMonthlySpots;
    }
}
