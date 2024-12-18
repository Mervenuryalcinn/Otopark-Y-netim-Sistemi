import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import controllers.Observer;
import database.DatabaseConnection;  // doğru paketi import ettik
import java.sql.DriverManager;

public class CustomerObserver implements Observer {
    private String observerName;

    public CustomerObserver(String observerName) {
        this.observerName = observerName;
    }

    @Override
    public void update(String name, String email, String parkArea) {
        System.out.println(observerName + " gözlemcisi: " + parkArea);
        saveMessageToCustomers("Yeni park alanı: " + parkArea);
    }

    private void saveMessageToCustomers(String message) {
        // DatabaseConnection sınıfını kullanarak bağlantı alıyoruz
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String query = "UPDATE Users SET message = ? WHERE role = 'customer'";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, message);
                stmt.executeUpdate();
                System.out.println("Mesaj customer rolündeki kullanıcılara başarıyla gönderildi.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
