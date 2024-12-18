// VehicleFactory.java
package factories;

import models.Vehicle;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class VehicleFactory {
    public abstract Vehicle createVehicle(int userId, String licensePlate, String vehicleType, String  parkingSpot);
}

