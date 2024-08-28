package compasso.com.br.model.dao;

import compasso.com.br.db.DB;
import compasso.com.br.model.dao.impl.MonthlyPayerDaoJDBC;
import compasso.com.br.model.dao.impl.TicketDaoJDBC;
import compasso.com.br.model.dao.impl.VehicleDaoJDBC;
import compasso.com.br.model.dao.impl.ParkingSpotDaoJDBC;

public class DaoFactory {

    public static VehicleDao createVehicleDao() {
        return new VehicleDaoJDBC(DB.getConnection()) {
        };
    }
    public static TicketDao createTicketDao() {
        return new TicketDaoJDBC(DB.getConnection());
    }

    public static ParkingSpotDao createParkingDao() {
        return new ParkingSpotDaoJDBC(DB.getConnection());
    }

    public static MonthlyPayerDao createMonthlyPayerDao() {
        return new MonthlyPayerDaoJDBC(DB.getConnection());
    }

}
