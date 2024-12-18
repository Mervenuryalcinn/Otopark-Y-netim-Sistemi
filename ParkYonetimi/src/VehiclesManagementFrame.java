package views;
import payment.PaymentProcessor;
import payment.CardPaymentProcessor;
import payment.CashPaymentProcessor;
import java.sql.Timestamp;
import database.DatabaseConnection;
import strategy.DurationContext;
import strategy.MinuteDurationStrategy;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import  java.awt.Color;

import factories.VehicleFactory;
import controllers.VehicleFactoryCreator;
import models.Vehicle;

public class VehiclesManagementFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public VehiclesManagementFrame() {
        setTitle("Araç Yönetimi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Tablo sütunları ve modeli
        String[] columns = {"Araç ID", "Kullanıcı ID", "Plaka", "Araç Tipi", "Park Yeri", "Kayıt Tarihi"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table));

        // Veritabanından araç bilgilerini yükle
        loadVehicles();

        // Araç ekleme butonu
        JButton addVehicleButton = new JButton("Yeni Araç Ekle");
        addVehicleButton.setForeground(Color.PINK);
        addVehicleButton.setBackground(new Color(0, 122, 255));
        addVehicleButton.addActionListener(e -> showAddVehicleDialog());
        add(addVehicleButton);


        // Güncelleme butonu
        JButton updateButton = new JButton("Güncelle");
        updateButton.setForeground(Color.PINK);
        updateButton.setBackground(new Color(0, 122, 255));
        updateButton.addActionListener(e -> showUpdateVehicleDialog());
        add(updateButton);

        // Silme butonu
        JButton deleteButton = new JButton("Sil");
        deleteButton.setForeground(Color.PINK);
        deleteButton.setBackground(new Color(0, 122, 255));
        deleteButton.addActionListener(e -> deleteVehicle());
        add(deleteButton);
        //kalma süresi
        JButton showDurationButton = new JButton("Kalma Süresi Göster");
        showDurationButton.setForeground(Color.PINK);
        showDurationButton.setBackground(new Color(0, 122, 255));
        showDurationButton.addActionListener(e -> showVehicleDuration());
        add(showDurationButton);
        //Ödeme alma
        JButton paymentButton = new JButton("Ödeme Al ve Sil");
        paymentButton.setForeground(Color.PINK);
        paymentButton.setBackground(new Color(0, 122, 255));
        paymentButton.addActionListener(e -> handlePayment());
        add(paymentButton);



        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void showVehicleDuration() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen kalma süresi bilgisini görmek istediğiniz aracı seçin.");
            return;
        }

        int vehicleId = (int) model.getValueAt(selectedRow, 0);
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT created_at FROM Vehicles WHERE vehicle_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Veritabanından created_at değerini alıyoruz
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    // Strateji nesnesi oluşturuluyor
                    DurationContext context = new DurationContext(new MinuteDurationStrategy()); // Burada istediğiniz stratejiyi seçebilirsiniz
                    long duration = context.executeStrategy(createdAt); // Kalma süresi dakika cinsinden

                    // 5 TL ile çarpılarak ücret hesaplanıyor
                    long totalCost = duration * 5;  // Her dakika için 5 TL ücret

                    // Kalma süresi ve ücreti mesaj olarak göster
                    JOptionPane.showMessageDialog(this,
                            "Seçilen aracın kalma süresi: " + duration + " dakika.\n" +
                                    "Toplam ücret: " + totalCost + " TL.");
                } else {
                    JOptionPane.showMessageDialog(this, "Araç kaydının oluşturulma tarihi bulunamadı.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Araç verisi bulunamadı.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Araç bilgileri alınırken hata oluştu.");
        }
    }

    private void handlePayment() {    int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen ödeme yapmak istediğiniz aracı seçin.");
            return;    }
        int vehicleId = (int) model.getValueAt(selectedRow, 0);
        // Ödeme yöntemi seçimi
         String[] options = {"Nakit", "Kredi Kartı"};
        int choice = JOptionPane.showOptionDialog( this, "Ödeme yöntemi seçin:","Ödeme İşlemi",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE, null,options,options[0]    );
        PaymentProcessor processor;
        if (choice == 0) {
            processor = new CashPaymentProcessor();
        } else if (choice == 1) {
            processor = new CardPaymentProcessor();
        } else {
            return; // Hiçbir seçim yapılmadı
             }
            // Ödeme işlemini başlat
        processor.processPayment(vehicleId);    // Tabloyu güncelle
           model.removeRow(selectedRow);}

        private void loadVehicles() {
        Connection connection = DatabaseConnection.getInstance().getConnection();

        String query = " SELECT v.vehicle_id, v.user_id, v.license_plate, v.vehicle_type, ps.spot_number AS parking_spot, v.created_at FROM Vehicles v LEFT JOIN ParkingSpots ps ON v.parking_spot_id = ps.spot_id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            model.setRowCount(0);
            while (rs.next()) {
                int vehicleId = rs.getInt("vehicle_id");
                int userId = rs.getInt("user_id");
                String licensePlate = rs.getString("license_plate");
                String vehicleType = rs.getString("vehicle_type");
                String parkingSpot = rs.getString("parking_spot");
                String createdAt = rs.getString("created_at");

                model.addRow(new Object[]{vehicleId, userId, licensePlate, vehicleType, parkingSpot, createdAt});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Araç bilgileri yüklenirken bir hata oluştu.");
        }
    }

    // Araç ekleme için yeni formu gösteren metod
    private void showAddVehicleDialog() {
        JDialog addDialog = new JDialog(this, "Yeni Araç Ekle", true);
        addDialog.setLayout(new GridLayout(5, 2));

        // Kullanıcıdan alınacak bilgiler için alanlar
        JTextField userIdField = new JTextField();
        JTextField licensePlateField = new JTextField();
        JComboBox<String> vehicleTypeComboBox = new JComboBox<>(new String[]{"car", "motorcycle", "truck"});
        JTextField parkingSpotField = new JTextField();

        addDialog.add(new JLabel("Kullanıcı ID:"));
        addDialog.add(userIdField);
        addDialog.add(new JLabel("Plaka:"));
        addDialog.add(licensePlateField);
        addDialog.add(new JLabel("Araç Tipi:"));
        addDialog.add(vehicleTypeComboBox);
        addDialog.add(new JLabel("Park Yeri:"));
        addDialog.add(parkingSpotField);

        JButton saveButton = new JButton("Kaydet");
        saveButton.addActionListener(e -> {
            String userId = userIdField.getText();
            String licensePlate = licensePlateField.getText();
            String vehicleType = (String) vehicleTypeComboBox.getSelectedItem();
            String parkingSpot = parkingSpotField.getText(); // Park yerini al

            // Factory'yi kullanarak doğru aracı oluştur
            VehicleFactory factory = VehicleFactoryCreator.getFactory(vehicleType);
            Vehicle vehicle = factory.createVehicle(Integer.parseInt(userId), licensePlate, vehicleType, parkingSpot);

            addVehicleToDatabase(userId, licensePlate, vehicleType, parkingSpot); // Park yerini de ekle
            addDialog.dispose();
        });
        addDialog.add(saveButton);

        addDialog.setSize(300, 250);
        addDialog.setLocationRelativeTo(this);
        addDialog.setVisible(true);
    }

    // Veritabanına yeni araç ekleme işlemi
    private void addVehicleToDatabase(String userId, String licensePlate, String vehicleType, String parkingSpot) {
        Connection connection = DatabaseConnection.getInstance().getConnection();

        String query = "INSERT INTO Vehicles (user_id, license_plate, vehicle_type, parking_spot_id) " +
                "VALUES (?, ?, ?, (SELECT spot_id FROM ParkingSpots WHERE spot_number = ?))";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(userId));
            stmt.setString(2, licensePlate);
            stmt.setString(3, vehicleType);
            stmt.setString(4, parkingSpot); // Park yerini ekle
            stmt.executeUpdate();
            // Park yerini dolu olarak işaretle
            markParkingSpotAsOccupied(parkingSpot);
            JOptionPane.showMessageDialog(this, "Yeni araç başarıyla eklendi!");

            // Tabloyu güncelle
            loadVehicles();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Araç eklenirken hata oluştu.");
        }
    }


    // Güncelleme için araç bilgilerini gösteren dialog
    private void showUpdateVehicleDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen güncellemek istediğiniz aracı seçin.");
            return;
        }

        // Seçili aracın bilgilerini al
        int vehicleId = (int) model.getValueAt(selectedRow, 0);
        int userId = (int) model.getValueAt(selectedRow, 1);
        String licensePlate = (String) model.getValueAt(selectedRow, 2);
        String vehicleType = (String) model.getValueAt(selectedRow, 3);
        String parkingSpot = (String) model.getValueAt(selectedRow, 4); // Park yerini al

        // Güncelleme formunu oluştur
        JDialog updateDialog = new JDialog(this, "Araç Güncelle", true);
        updateDialog.setLayout(new GridLayout(5, 2));

        JTextField userIdField = new JTextField(String.valueOf(userId));
        JTextField licensePlateField = new JTextField(licensePlate);
        JComboBox<String> vehicleTypeComboBox = new JComboBox<>(new String[]{"car", "motorcycle", "truck"});
        vehicleTypeComboBox.setSelectedItem(vehicleType);
        JTextField parkingSpotField = new JTextField(parkingSpot);

        updateDialog.add(new JLabel("Kullanıcı ID:"));
        updateDialog.add(userIdField);
        updateDialog.add(new JLabel("Plaka:"));
        updateDialog.add(licensePlateField);
        updateDialog.add(new JLabel("Araç Tipi:"));
        updateDialog.add(vehicleTypeComboBox);
        updateDialog.add(new JLabel("Park Yeri:"));
        updateDialog.add(parkingSpotField);

        JButton saveButton = new JButton("Kaydet");
        saveButton.addActionListener(e -> {
            String newUserId = userIdField.getText();
            String newLicensePlate = licensePlateField.getText();
            String newVehicleType = (String) vehicleTypeComboBox.getSelectedItem();
            String newParkingSpot = parkingSpotField.getText(); // Yeni park yerini al

            updateVehicleInDatabase(vehicleId, newUserId, newLicensePlate, newVehicleType, newParkingSpot); // Park yerini de güncelle
            updateDialog.dispose();
        });
        updateDialog.add(saveButton);

        updateDialog.setSize(300, 250);
        updateDialog.setLocationRelativeTo(this);
        updateDialog.setVisible(true);
    }

    // Veritabanında araç güncelleme işlemi
    private void updateVehicleInDatabase(int vehicleId, String userId, String licensePlate, String vehicleType, String parkingSpot) {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "UPDATE Vehicles SET user_id = ?, license_plate = ?, vehicle_type = ?, parking_spot_id = (SELECT spot_id FROM ParkingSpots WHERE spot_number = ?) WHERE vehicle_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(userId));
            stmt.setString(2, licensePlate);
            stmt.setString(3, vehicleType);
            stmt.setString(4, parkingSpot); // Park yerini güncelle
            stmt.setInt(5, vehicleId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Araç başarıyla güncellendi!");

            // Güncellenmiş bilgileri tabloya yansıt
            model.setValueAt(userId, table.getSelectedRow(), 1);
            model.setValueAt(licensePlate, table.getSelectedRow(), 2);
            model.setValueAt(vehicleType, table.getSelectedRow(), 3);
            model.setValueAt(parkingSpot, table.getSelectedRow(), 4); // Park yerini de güncelle

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Araç güncellenirken hata oluştu.");
        }
    }
    // Park yerini boş olarak işaretle
    private void markParkingSpotAsAvailable(String spotNumber) {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Veritabanı bağlantısı sağlanamadı.");
            return;
        }

        // Park yerinin is_available durumunu 1 yap (boş)
        String query = "UPDATE ParkingSpots SET is_available = 0 WHERE spot_number = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, spotNumber);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, spotNumber + " numaralı park yeri boş olarak işaretlendi.");
            } else {
                JOptionPane.showMessageDialog(this, "Park yeri bulunamadı: " + spotNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Park yeri durumu güncellenirken hata oluştu: " + e.getMessage());
        }
    }

    // Park yerini dolu olarak işaretle
    private void markParkingSpotAsOccupied(String spotNumber) {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Veritabanı bağlantısı sağlanamadı.");
            return;
        }

        // Park yerinin is_available durumunu 0 yap (dolu)
        String query = "UPDATE ParkingSpots SET is_available = 1 WHERE spot_number = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, spotNumber);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, spotNumber + " numaralı park yeri dolu olarak işaretlendi.");
            } else {
                JOptionPane.showMessageDialog(this, "Park yeri bulunamadı: " + spotNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Park yeri durumu güncellenirken hata oluştu: " + e.getMessage());
        }
    }
    private void deleteVehicleFromDatabase(int vehicleId, String parkingSpot) {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Veritabanı bağlantısı sağlanamadı.");
            return;
        }

        // Araç kaydını Vehicles tablosundan silme
        String query = "DELETE FROM Vehicles WHERE vehicle_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, vehicleId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Araç başarıyla silindiyse, park yerini boş (is_available = 1) olarak işaretle
               // markParkingSpotAsAvailable(parkingSpot);

                JOptionPane.showMessageDialog(this, "Araç başarıyla silindi!");
                loadVehicles(); // Tabloyu güncelle
            } else {
                JOptionPane.showMessageDialog(this, "Araç silinirken bir hata oluştu.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Araç silinirken hata oluştu: " + e.getMessage());
        }
    }


    private void deleteVehicle() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen silmek istediğiniz aracı seçin.");
            return;
        }

        int vehicleId = (int) model.getValueAt(selectedRow, 0); // Seçilen aracın ID'si

        int confirmation = JOptionPane.showConfirmDialog(this, "Bu aracı silmek istediğinizden emin misiniz?", "Araç Silme", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "DELETE FROM Vehicles WHERE vehicle_id = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, vehicleId);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Araç başarıyla silindi!");

                // Tabloyu güncelle
                model.removeRow(selectedRow);

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Araç silinirken hata oluştu.");
            }
        }
    }

}