package models;
import factories.VehicleFactory;
public class MotorcycleFactory extends VehicleFactory {
    @Override
    public Vehicle createVehicle(int userId, String licensePlate, String vehicleType, String anotherValue) {
        // Bu, motosiklet türünü yaratır
        return new Vehicle(userId, licensePlate, vehicleType, anotherValue);
    }
}
