package compasso.com.br.model.dao.impl;

import compasso.com.br.db.DbException;
import compasso.com.br.model.dao.VehicleDao;
import compasso.com.br.model.entities.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import compasso.com.br.db.DB;
import compasso.com.br.model.entities.enums.Category;
import compasso.com.br.model.entities.enums.Type;

public class VehicleDaoJDBC implements VehicleDao {

    // variável que permitirá todas as funções sincarem com bd
    private Connection conn;

    // faz conexão com o banco
    public VehicleDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Vehicle vehicle) {

        PreparedStatement st = null;

        try{
            st = conn.prepareStatement(
                    "INSERT INTO vehicle "
                            + "(licensePlate, type, category) "
                            + "VALUES "
                            + "(?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, vehicle.getLicensePlate());
            st.setString(2, String.valueOf(vehicle.getType()));
            st.setString(3, String.valueOf(vehicle.getCategory()));


            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()){
                    int id = rs.getInt(1);
                    vehicle.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else {
                throw new DbException("Unexpected error ! No rows affected!");
            }
        }
        catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Vehicle plate, String oldLicensePlate) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "UPDATE vehicle "
                            + "SET licensePlate = ?, type = ?, category = ? "
                            + "WHERE licensePlate = ?"
            );

            st.setString(1, plate.getLicensePlate());
            st.setString(2, String.valueOf(plate.getType()));
            st.setString(3, String.valueOf(plate.getCategory()));
            st.setString(4, oldLicensePlate); // Placa antiga passada como parâmetro

            int rowsAffected = st.executeUpdate(); // Executa a query uma única vez
            if (rowsAffected == 0) {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteByPlate(String plate) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("DELETE FROM vehicle WHERE licensePlate = ?");

            st.setString(1, plate);
            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("Unexpected error ! No rows affected!");
            }
            st.executeUpdate();
        }
        catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Vehicle findByPlate(String plate) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * FROM vehicle WHERE licensePlate = ?");

            st.setString(1, plate);
            rs = st.executeQuery();
            if (rs.next()) {
                Vehicle obj = new Vehicle();
                obj.setId(rs.getInt("id"));
                obj.setLicensePlate(rs.getString("licensePlate"));
                obj.setType(Type.valueOf(rs.getString("type")));
                obj.setCategory(Category.valueOf(rs.getString("category")));
                return obj;
            }
            return null;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Vehicle> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * FROM vehicle ORDER BY id");
            rs = st.executeQuery();

            List<Vehicle> list = new ArrayList<>();

            while (rs.next()) {
                Vehicle obj = new Vehicle();
                obj.setId(rs.getInt("Id"));
                obj.setLicensePlate(rs.getString("licensePlate"));
                obj.setType(Type.valueOf(rs.getString("type")));
                obj.setCategory(Category.valueOf(rs.getString("category")));
                list.add(obj);
            }
            return list;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
