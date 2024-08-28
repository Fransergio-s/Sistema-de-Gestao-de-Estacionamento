package compasso.com.br.model.dao;

import compasso.com.br.model.entities.MonthlyPayer;
import compasso.com.br.model.entities.ParkingSpot;

import java.util.List;

public interface MonthlyPayerDao {
    void insert(MonthlyPayer monthlyPayer);
    void update(MonthlyPayer monthlyPayer);
    void deleteById(Integer id));
    MonthlyPayer findById(Integer id);
    List<MonthlyPayer> findAll();

}
