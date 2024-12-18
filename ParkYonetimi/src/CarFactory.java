// CarFactory.java
package controllers;
import models.Vehicle;          // Vehicle sınıfını import et
import factories.VehicleFactory;

public class CarFactory extends VehicleFactory {
    @Override
    public Vehicle createVehicle(int userId, String licensePlate, String vehicleType, String anotherValue) {
        // Bu, araba türünü yaratır
        return new Vehicle(userId, licensePlate, vehicleType, anotherValue);
    }
}
