package compasso.com.br.model.dao.impl;

import compasso.com.br.db.DB;
import compasso.com.br.db.DbException;
import compasso.com.br.model.dao.ParkingSpotDao;
import compasso.com.br.model.entities.ParkingSpot;

import java.sql.*;
import java.util.List;

public class ParkingSpotDaoJDBC implements ParkingSpotDao {

    private Connection conn;

    public ParkingSpotDaoJDBC(Connection conn) {
        this.conn = conn;
    }


    @Override
    public void insert(ParkingSpot parkingSpot) {

    }

    @Override
    public void update(ParkingSpot parkingSpot) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public ParkingSpot findById(Integer id) {
        return null;
    }

    @Override
    public List<ParkingSpot> findAll() {
        return List.of();
    }
}
