package compasso.com.br.model.dao.impl;

import compasso.com.br.db.DbException;
import compasso.com.br.db.DB;
import compasso.com.br.model.entities.Vehicle;
import compasso.com.br.model.entities.enums.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDaoJDBC implements compasso.com.br.model.dao.VehicleDao {

    private Connection conn;

    public VehicleDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Vehicle vehicle) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO vehicle (plate, model, category) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, vehicle.getPlate());
            st.setString(2, vehicle.getModel());
            st.setString(3, String.valueOf(vehicle.getCategory()));

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    vehicle.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected error! No rows affected.");
            }
        } catch (SQLException e) {
            throw new DbException("Database error: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Vehicle vehicle, String oldPlate) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE vehicle SET plate = ?, model = ?, category = ? WHERE plate = ?");

            st.setString(1, vehicle.getPlate());
            st.setString(2, vehicle.getModel());
            st.setString(3, String.valueOf(vehicle.getCategory()));
            st.setString(4, oldPlate);

            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("No rows affected. Check if the plate exists.");
            }
        } catch (SQLException e) {
            throw new DbException("Database error: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteByPlate(String plate) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM vehicle WHERE plate = ?");
            st.setString(1, plate);

            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("No rows affected. Check if the plate exists.");
            }
        } catch (SQLException e) {
            throw new DbException("Database error: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Vehicle findByPlate(String plate) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM vehicle WHERE plate = ?");
            st.setString(1, plate);
            rs = st.executeQuery();
            if (rs.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(rs.getInt("id"));
                vehicle.setPlate(rs.getString("plate"));
                vehicle.setModel(rs.getString("model"));
                vehicle.setCategory(Category.valueOf(rs.getString("category")));
                return vehicle;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException("Database error: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Vehicle> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM vehicle ORDER BY id");
            rs = st.executeQuery();

            List<Vehicle> list = new ArrayList<>();
            while (rs.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(rs.getInt("id"));
                vehicle.setPlate(rs.getString("plate"));
                vehicle.setModel(rs.getString("model"));
                vehicle.setCategory(Category.valueOf(rs.getString("category")));
                list.add(vehicle);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException("Database error: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
