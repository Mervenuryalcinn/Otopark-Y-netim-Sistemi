package controllers;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParkAreaObserver implements Observer {
    private String observerName;

    public ParkAreaObserver(String observerName) {
        this.observerName = observerName;
    }

    @Override
    public void update(String name, String email, String parkArea) {
        System.out.println(observerName + " gözlemcisi: ");
        System.out.println("Park Adı: " + name);
        System.out.println("Email: " + email);
        System.out.println("Park Alanı: " + parkArea);

        // Mesajı veritabanına kaydet
        saveMessageToDatabase(email, "Yeni park alanı: " + parkArea);
    }

    private void saveMessageToDatabase(String email, String message) {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "UPDATE Users SET message = ? WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, message);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
