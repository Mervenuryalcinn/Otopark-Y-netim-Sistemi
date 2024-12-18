package controllers;

import database.DatabaseConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import controllers.Observer;
import views.ParkArea;  // Doğru paketten ParkArea'yı import ediyoruz
import controllers.ParkAreaObserver;  // Doğru paketten ParkAreaObserver'ı import ediyoruz

public class AuthController {
    //private ParkArea parkArea;
    // ParkArea nesnesini burada tanımlıyoruz
    public static ParkArea parkArea;  // ParkArea nesnesi

    // Kullanıcıyı kimlik doğrulama
    public static boolean authenticate(String username, String password) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM Users WHERE username = ? AND password_hash = ?"
            );
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kullanıcı kaydı
    public static boolean register(String username, String password, String email, String role, String parkAreaName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getInstance().getConnection();

            // Şifreyi güvenli bir şekilde hash'liyoruz (SHA-256)
            String passwordHash = hashPassword(password);

            // Kullanıcıyı veritabanına ekliyoruz
            String sql = "INSERT INTO Users (username, password_hash, email, role) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, passwordHash);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, role);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Park alanı nesnesini oluşturuyoruz
                parkArea = new ParkArea(); // ParkArea nesnesi oluşturuluyor
                ParkAreaObserver observer1 = new ParkAreaObserver("Gözlemci 1");
                parkArea.addObserver(observer1);  // Gözlemci ekleniyor

                // Kullanıcıyı veritabanına eklemeden önce gözlemciler bilgilendirilecek
                parkArea.setParkArea("Park Alanı 1", email, "Park Alanı");
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Şifreyi güvenli bir şekilde hash'leme
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getUserMessage(String username) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT message FROM Users WHERE username = ?"
            );
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("message");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Kullanıcı rolünü almak
    public static String getUserRole(String username) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT role FROM Users WHERE username = ?"
            );
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

