package compasso.com.br.model.dao.impl;

import compasso.com.br.db.DB;
import compasso.com.br.db.DbException;
import compasso.com.br.model.dao.ParkingSpotDao;
import compasso.com.br.model.entities.ParkingSpot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingSpotDaoJDBC implements ParkingSpotDao {

    private Connection conn;

    public ParkingSpotDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(ParkingSpot parkingSpot) {
        if (getTotalSpots() >= 500) {
            throw new DbException("Limite máximo de 500 vagas atingido.");
        }

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO parking_spot (number, occupied) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            st.setInt(1, parkingSpot.getNumber());
            st.setBoolean(2, parkingSpot.isOccupied());
            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    parkingSpot.setId(rs.getInt(1));
                }
                rs.close();
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(ParkingSpot parkingSpot) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE parking_spot SET occupied = ? WHERE number = ?"
            );
            st.setBoolean(1, parkingSpot.isOccupied());
            st.setInt(2, parkingSpot.getNumber());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Error updating parking spot: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        // Implementar se necessário
    }

    @Override
    public ParkingSpot findByNumber(int number) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * FROM parking_spot WHERE number = ?"
            );
            st.setInt(1, number);
            rs = st.executeQuery();

            if (rs.next()) {
                ParkingSpot spot = new ParkingSpot();
                spot.setId(rs.getInt("id"));
                spot.setNumber(rs.getInt("number"));
                spot.setOccupied(rs.getBoolean("occupied"));
                spot.setReserved(rs.getBoolean("reserved"));
                return spot;
            }
            return null; // Retorna null se a vaga não for encontrada
        } catch (SQLException e) {
            throw new DbException("Error finding parking spot by number: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }


    @Override
    public List<ParkingSpot> findAvailableSpots() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM parking_spot WHERE occupied = FALSE");
            rs = st.executeQuery();

            List<ParkingSpot> list = new ArrayList<>();
            while (rs.next()) {
                ParkingSpot spot = new ParkingSpot();
                spot.setId(rs.getInt("id"));
                spot.setNumber(rs.getInt("number"));
                spot.setOccupied(rs.getBoolean("occupied"));
                spot.setReserved(rs.getBoolean("reserved"));
                list.add(spot);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<ParkingSpot> findUnavailableSpots() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM parking_spot WHERE occupied = TRUE");
            rs = st.executeQuery();

            List<ParkingSpot> list = new ArrayList<>();
            while (rs.next()) {
                ParkingSpot spot = new ParkingSpot();
                spot.setId(rs.getInt("id"));
                spot.setNumber(rs.getInt("number"));
                spot.setOccupied(rs.getBoolean("occupied"));
                spot.setReserved(rs.getBoolean("reserved"));
                list.add(spot);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<ParkingSpot> findAvailableSpotsMonthlyPayers() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM parking_spot WHERE occupied = FALSE AND reserved = TRUE");
            rs = st.executeQuery();

            List<ParkingSpot> list = new ArrayList<>();
            while (rs.next()) {
                ParkingSpot spot = new ParkingSpot();
                spot.setId(rs.getInt("id"));
                spot.setNumber(rs.getInt("number"));
                spot.setOccupied(rs.getBoolean("occupied"));
                spot.setReserved(rs.getBoolean("reserved"));
                list.add(spot);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public int getTotalSpots() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT COUNT(*) FROM parking_spot");
            rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    // Novo método para alocar vagas consecutivas
    public List<ParkingSpot> allocateConsecutiveSpots(int requiredSpots) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * FROM parking_spot WHERE occupied = FALSE ORDER BY number LIMIT ?");
            st.setInt(1, requiredSpots);
            rs = st.executeQuery();

            List<ParkingSpot> allocatedSpots = new ArrayList<>();
            while (rs.next()) {
                ParkingSpot spot = new ParkingSpot();
                spot.setId(rs.getInt("id"));
                spot.setNumber(rs.getInt("number"));
                spot.setOccupied(true); // Marcando a vaga como ocupada
                allocatedSpots.add(spot);
            }

            // Verificar se foi possível alocar a quantidade necessária de vagas
            if (allocatedSpots.size() == requiredSpots) {
                for (ParkingSpot spot : allocatedSpots) {
                    // Atualiza o status de cada vaga no banco de dados
                    updateParkingSpotStatus(spot.getId(), true);
                }
                return allocatedSpots;
            } else {
                // Se não conseguir todas as vagas necessárias, liberar as que já foram alocadas
                for (ParkingSpot spot : allocatedSpots) {
                    updateParkingSpotStatus(spot.getId(), false);
                }
                return null; // Não foi possível alocar as vagas
            }
        } catch (SQLException e) {
            throw new DbException("Database error: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    // Novo método para criar uma vaga adicional para veículos de serviço público
    public ParkingSpot createAdditionalSpotForPublicService() {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO parking_spot (number, occupied) VALUES (?, FALSE)",
                    Statement.RETURN_GENERATED_KEYS);

            // Definindo o número da nova vaga
            st.setInt(1, getHighestSpotNumber() + 1);
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                ParkingSpot spot = new ParkingSpot();
                spot.setId(id);
                spot.setNumber(getHighestSpotNumber());
                spot.setOccupied(true); // Marcando a vaga como ocupada

                updateParkingSpotStatus(spot.getId(), true); // Atualizando o status para ocupado
                return spot;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException("Database error: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    // Método auxiliar para obter o maior número de vaga existente
    private int getHighestSpotNumber() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT MAX(number) FROM parking_spot");
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 500; // Caso não tenha nenhuma vaga, retorna 500
        } catch (SQLException e) {
            throw new DbException("Database error: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    // Método auxiliar para atualizar o status de uma vaga
    private void updateParkingSpotStatus(int spotId, boolean occupied) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE parking_spot SET occupied = ? WHERE id = ?");
            st.setBoolean(1, occupied);
            st.setInt(2, spotId);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Database error: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }


}
