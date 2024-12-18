package payment;

import database.DatabaseConnection;


import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class PaymentProcessor {
    public final void processPayment(int vehicleId) {
        // Ortak işlem adımları
        selectPaymentMethod();
        calculateAmount(vehicleId);
        processTransaction();
        finalizePayment(vehicleId); // Araç silinir
    }

    protected abstract void selectPaymentMethod();

    protected abstract void calculateAmount(int vehicleId);

    protected abstract void processTransaction();

    private void finalizePayment(int vehicleId) {
        // Araç silme işlemi
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String deleteQuery = "DELETE FROM Vehicles WHERE vehicle_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
            stmt.setInt(1, vehicleId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Ödeme başarılı ve araç silindi!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Araç silinirken hata oluştu.");
        }
    }
}