package controllers;


import factories.VehicleFactory;  // VehicleFactory importu
import controllers.CarFactory;  // CarFactory importu
import models.MotorcycleFactory;
import models.TruckFactory;
// MotorcycleFactory importu

public class VehicleFactoryCreator {
    public static VehicleFactory getFactory(String vehicleType) {
        switch (vehicleType) {
            case "car":
                return new CarFactory();
            case "motorcycle":
                return new MotorcycleFactory();
            case "truck":
                return new TruckFactory();
            // Diğer türler için fabrikalar eklenebilir
            default:
                throw new IllegalArgumentException("Geçersiz araç tipi: " + vehicleType);
        }
    }
}
