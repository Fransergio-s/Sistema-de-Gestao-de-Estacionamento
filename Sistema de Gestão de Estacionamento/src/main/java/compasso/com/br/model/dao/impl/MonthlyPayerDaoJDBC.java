package compasso.com.br.model.dao.impl;

import compasso.com.br.db.DB;
import compasso.com.br.db.DbException;
import compasso.com.br.model.entities.MonthlyPayer;
import compasso.com.br.model.entities.ParkingSpot;
import compasso.com.br.model.entities.Ticket;
import compasso.com.br.model.entities.Vehicle;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class MonthlyPayerDaoJDBC implements compasso.com.br.model.dao.MonthlyPayerDao {

    private Connection conn;

    public MonthlyPayerDaoJDBC(Connection conn) {
        this.conn = conn;
    }


    @Override
    public void insert(MonthlyPayer monthlyPayer) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO monthly_payers (payment_month, plate ) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setDate(1, Date.valueOf(monthlyPayer.getPaymentMonth()));
            st.setString(2, monthlyPayer.getPlate());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    monthlyPayer.setId(id);
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
    public void update(MonthlyPayer monthlyPayer) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE monthly_payers SET payment_month = ?, plate = ? WHERE id = ?");

            st.setDate(1, Date.valueOf(monthlyPayer.getPaymentMonth()));
            st.setString(2, monthlyPayer.getPlate());
            st.setInt(3, monthlyPayer.getId());

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
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM monthly_payers WHERE id = ?");

            st.setInt(1, id);
            int rowsAffected = st.executeUpdate();
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
    public MonthlyPayer findByPlate(String plate) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM monthly_payers WHERE plate = ?");
            st.setString(1, plate);
            rs = st.executeQuery();
            if (rs.next()) {
                MonthlyPayer monthlyPayer = new MonthlyPayer();
                monthlyPayer.setId(rs.getInt("id"));
                monthlyPayer.setPaymentMonth(rs.getDate("payment_month").toLocalDate());
                monthlyPayer.setPlate(rs.getString("plate"));

                return monthlyPayer;
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
    public List<MonthlyPayer> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM monthly_payers ORDER BY id");
            rs = st.executeQuery();

            List<MonthlyPayer> list = new ArrayList<>();
            while (rs.next()) {
                MonthlyPayer monthlyPayer = new MonthlyPayer();
                monthlyPayer.setId(rs.getInt("id"));
                monthlyPayer.setPaymentMonth(rs.getDate("payment_month").toLocalDate());
                monthlyPayer.setPlate(rs.getString("plate"));
                list.add(monthlyPayer);
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
