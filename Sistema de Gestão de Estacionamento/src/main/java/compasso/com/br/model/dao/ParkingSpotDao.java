package compasso.com.br.model.dao;



import compasso.com.br.model.entities.ParkingSpot;

import java.util.List;

public interface ParkingSpotDao {
    void insert(ParkingSpot parkingSpot);
    void update(ParkingSpot parkingSpot);
    void deleteById(Integer id);
    ParkingSpot findByNumber(int number);
    List<ParkingSpot> findAvailableSpots();
    int getTotalSpots();
    List<ParkingSpot> findAvailableSpotsMonthlyPayers();
    List<ParkingSpot> allocateConsecutiveSpots(int requiredSpots);
    ParkingSpot createAdditionalSpotForPublicService();
    public List<ParkingSpot> findUnavailableSpots();
}
