package compasso.com.br.model.dao.impl;

import compasso.com.br.db.DB;
import compasso.com.br.db.DbException;
import compasso.com.br.model.dao.TicketDao;
import compasso.com.br.model.entities.Ticket;
import compasso.com.br.model.entities.ParkingSpot;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.valueOf;

public class TicketDaoJDBC implements TicketDao {

    private Connection conn;

    public TicketDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Ticket ticket) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO ticket " +
                            "(plate, entry_hour, exit_hour, entry_gate, exit_gate, parking_spot, amount) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, ticket.getPlate());
            st.setString(2, String.valueOf(ticket.getEntryHour()));
            st.setString(3, String.valueOf(ticket.getExitHour()));
            st.setInt(4, ticket.getEntryGate());
            st.setInt(5, ticket.getExitGate());
            st.setString(6, ticket.getParkingSpot());
            st.setDouble(7, ticket.getAmount());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    ticket.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Ticket ticket) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE ticket " +
                            "SET plate = ?, entry_hour = ?, exit_hour = ?, entry_gate = ?, exit_gate = ?, parking_spot = ?, amount = ? " +
                            "WHERE id = ?");

            st.setString(1, ticket.getPlate());
            st.setString(2, String.valueOf(ticket.getEntryHour()));
            st.setString(3, String.valueOf(ticket.getExitHour()));
            st.setInt(4, ticket.getEntryGate());
            st.setInt(5, ticket.getExitGate());
            st.setString(6, ticket.getParkingSpot());
            st.setDouble(7, ticket.getAmount());
            st.setInt(8, ticket.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM ticket WHERE id = ?");

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
    public Ticket findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM ticket WHERE id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getInt("id"));
                ticket.setPlate(rs.getString("plate"));
                ticket.setEntryHour(rs.getTimestamp("entry_hour").toLocalDateTime());
                ticket.setExitHour(rs.getTimestamp("exit_hour").toLocalDateTime());
                ticket.setEntryGate(rs.getInt("entry_gate"));
                ticket.setExitGate(rs.getInt("exit_gate"));
                ticket.setParkingSpot(rs.getString("parking_spot"));


                ticket.setAmount(rs.getDouble("amount"));
                return ticket;
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
    public List<Ticket> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM ticket ORDER BY id");
            rs = st.executeQuery();

            List<Ticket> list = new ArrayList<>();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getInt("id"));
                ticket.setPlate(rs.getString("plate"));
                ticket.setEntryHour(LocalDateTime.parse(String.valueOf(rs.getDate("entry_hour"))));
                ticket.setExitHour(LocalDateTime.parse(String.valueOf(rs.getDate("exit_hour"))));
                ticket.setEntryGate(rs.getInt("entry_gate"));
                ticket.setExitGate(rs.getInt("exit_gate"));
                ticket.setParkingSpot(rs.getString("parking_spot"));
                ticket.setAmount(rs.getDouble("amount"));
                list.add(ticket);
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
