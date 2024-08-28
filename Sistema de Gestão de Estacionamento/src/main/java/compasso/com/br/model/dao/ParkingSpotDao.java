package compasso.com.br.model.dao;



import compasso.com.br.model.entities.ParkingSpot;

import java.util.List;

public interface ParkingSpotDao {
    void insert(ParkingSpot parkingSpot);
    void update(ParkingSpot parkingSpot);
    void deleteById(Integer id);
    ParkingSpot findById(Integer id);
    List<ParkingSpot> findAll();
}
