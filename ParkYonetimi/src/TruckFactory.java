package models;
import models.Vehicle;          // Vehicle sınıfını import et
import factories.VehicleFactory;
public class TruckFactory extends factories.VehicleFactory {
    @Override
    public models.Vehicle createVehicle(int userId, String licensePlate, String vehicleType, String anotherValue) {
        // Bu, araba türünü yaratır
        return new models.Vehicle(userId, licensePlate, vehicleType, anotherValue);
    }
}
