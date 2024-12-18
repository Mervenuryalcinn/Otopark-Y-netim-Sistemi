// VehicleFactory.java
package factories;

import models.Vehicle;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VehicleFactory {

    // 4 parametre ile araç oluşturma metodu
    public static Vehicle createVehicle(int userId, String licensePlate, String vehicleType, String anotherValue) {
        Vehicle vehicle = new Vehicle(userId, licensePlate, vehicleType, anotherValue);
        addVehicleToDatabase(vehicle);
        return vehicle;

    }

    private static void addVehicleToDatabase(Vehicle vehicle) {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "INSERT INTO Vehicles (user_id, license_plate, vehicle_type) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, vehicle.getUserId());
            stmt.setString(2, vehicle.getLicensePlate());
            stmt.setString(3, vehicle.getVehicleType());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Araç veritabanına eklenemedi!");
        }
    }
}
