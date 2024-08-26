package compasso.com.br.model.dao;

import compasso.com.br.model.entities.Vehicle;

import java.util.List;

public interface VehicleDao {
    void insert(Vehicle vehicle);
    void update(Vehicle vehicle, String oldPlate);
    void deleteByPlate(String plate);
    Vehicle findByPlate(String plate);
    List<Vehicle> findAll();
}
