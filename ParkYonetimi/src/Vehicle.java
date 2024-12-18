package models;

public class Vehicle {
    private int userId;
    private String licensePlate; // licensePlate'yi String yapmalısınız
    private String vehicleType;
    private String anotherValue; // Bu yeni parametreyi de constructor'a ekleyebilirsiniz

    // 5 parametreli constructor
    public Vehicle(int userId, String licensePlate, String vehicleType, String anotherValue) {
        this.userId = userId;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.anotherValue = anotherValue;
    }

    // Getter ve setter metodları
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getAnotherValue() {
        return anotherValue;
    }

    public void setAnotherValue(String anotherValue) {
        this.anotherValue = anotherValue;
    }
}
