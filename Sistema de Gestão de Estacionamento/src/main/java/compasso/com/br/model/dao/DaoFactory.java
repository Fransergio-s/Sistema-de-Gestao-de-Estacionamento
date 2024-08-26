package compasso.com.br.model.dao;

import compasso.com.br.db.DB;
import compasso.com.br.model.dao.impl.VehicleDaoJDBC;
import compasso.com.br.model.entities.Vehicle;

public class DaoFactory {

    public static VehicleDao createVehicleDao() {
        return new VehicleDaoJDBC(DB.getConnection()) {
        };
    }

}
