import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.DatabaseConnection;

public class ParkingSpot {
    private String spotNumber;
    private ParkingSpotState state;

    public ParkingSpot(String spotNumber) {
        this.spotNumber = spotNumber;
        this.state = new AvailableState(); // Varsayılan olarak park yeri boş
    }

    public String getSpotNumber() {
        return spotNumber;
    }

    public void setState(ParkingSpotState state) {
        this.state = state;
    }

    public ParkingSpotState getState() {
        return state;
    }

    public void handleRequest() {
        state.handleRequest(this);
    }

    public static ParkingSpot getParkingSpotFromDatabase(String spotNumber) {
        // Veritabanından park yeri bilgisi alınıyor
        String query = "SELECT spot_number, is_available FROM ParkingSpots WHERE spot_number = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, spotNumber); // Parametreyi sorguya ekliyoruz
            ResultSet rs = stmt.executeQuery(); // Sorguyu çalıştırıyoruz

            if (rs.next()) {
                boolean isAvailable = rs.getBoolean("is_available");
                ParkingSpot spot = new ParkingSpot(spotNumber);

                // Duruma göre State nesnesi belirleniyor
                if (isAvailable) {
                    spot.setState(new AvailableState());
                } else {
                    spot.setState(new OccupiedState());
                }

                return spot;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateAvailabilityInDatabase(boolean isAvailable) {
        String query = "UPDATE parkingspots SET is_available = ? WHERE spot_number = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, isAvailable);
            stmt.setString(2, this.spotNumber);
            stmt.executeUpdate();
            System.out.println("Park yeri durumu güncellendi: " + this.spotNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
