package views;

import database.DatabaseConnection;
import factories.VehicleFactory;
import java.sql.Timestamp;
import strategy.DurationContext;
import strategy.MinuteDurationStrategy;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ParkingManagementFrame extends JFrame {

    private JTable table;
    private int currentUserId; // Giriş yapan kullanıcının ID'si
    private DefaultTableModel model;
    public ParkingManagementFrame(int userId) {
        this.currentUserId = userId;
        setTitle("Park Yerleri Yönetimi");
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
        addVehicleButton.addActionListener(e -> showAddVehicleDialog());
        add(addVehicleButton);


        // Boş Park Yerlerini Listeleme Butonu
        JButton listEmptySpotsButton = new JButton("Boş Park Yerlerini Listele");
        listEmptySpotsButton.addActionListener(e -> listEmptyParkingSpots());
        add(listEmptySpotsButton);

        JButton showDurationButton = new JButton("Kalma Süresi Göster");
        showDurationButton.addActionListener(e -> showVehicleDuration());
        add(showDurationButton);

        loadVehicles(); // Kullanıcıya ait araçları yükle

        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void showVehicleDuration() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Lütfen kalma süresi bilgisini görmek istediğiniz aracı seçin.");
        return;     }
        int vehicleId = (int) model.getValueAt(selectedRow, 0);
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT created_at FROM Vehicles WHERE vehicle_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {             // Veritabanından created_at değerini alıyoruz
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {                 // Strateji nesnesi oluşturuluyor
                    DurationContext context = new DurationContext(new MinuteDurationStrategy()); // Burada istediğiniz stratejiyi seçebilirsiniz
                    long duration = context.executeStrategy(createdAt); // Kalma süresi dakika cinsinden//
                    // 5 TL ile çarpılarak ücret hesaplanıyor
                    long totalCost = duration * 20;  // Her dakika için 20 TL ücret
                    // Kalma süresi ve ücreti mesaj olarak göster
                    JOptionPane.showMessageDialog(this,"Seçilen aracın kalma süresi: " + duration + " dakika.\n" + "Toplam ücret: " + totalCost + " TL."); }
                else { JOptionPane.showMessageDialog(this, "Araç kaydının oluşturulma tarihi bulunamadı."); } }
            else { JOptionPane.showMessageDialog(this, "Araç verisi bulunamadı."); } }
        catch (SQLException e) { e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Araç bilgileri alınırken hata oluştu."); } }
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
            int userId = Integer.parseInt(userIdField.getText()); // S
            String licensePlate = licensePlateField.getText();
            String vehicleType = (String) vehicleTypeComboBox.getSelectedItem();
            String parkingSpot = parkingSpotField.getText(); // Park yerini al

            addVehicleToDatabase(userId, licensePlate, vehicleType, parkingSpot); // Park yerini de ekle
            addDialog.dispose();
        });
        addDialog.add(saveButton);

        addDialog.setSize(300, 250);
        addDialog.setLocationRelativeTo(this);
        addDialog.setVisible(true);
    }

    private void addVehicleToDatabase(int userId, String licensePlate, String vehicleType, String parkingSpot) {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Veritabanı bağlantısı sağlanamadı.");
            return;
        }

        String query = "INSERT INTO Vehicles (user_id, license_plate, vehicle_type, parking_spot_id) " +
                "VALUES (?, ?, ?, (SELECT spot_id FROM ParkingSpots WHERE spot_number = ?))";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, licensePlate);
            stmt.setString(3, vehicleType);
            stmt.setString(4, parkingSpot);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                markParkingSpotAsOccupied(parkingSpot);
                JOptionPane.showMessageDialog(this, "Yeni araç başarıyla eklendi!");
                loadVehicles(); // Tabloyu güncelle
            } else {
                JOptionPane.showMessageDialog(this, "Araç eklenirken bir hata oluştu.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Araç eklenirken hata oluştu: " + e.getMessage());
        }
    }


    // Boş park yerlerini listeleyen fonksiyon
    private void listEmptyParkingSpots() {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Veritabanı bağlantısı sağlanamadı.");
            return;
        }

        // Boş park yerlerini sorgulayan SQL ifadesi
        String query = "SELECT spot_number FROM ParkingSpots WHERE is_available = 1"; // is_available = 0 boş park yerini ifade eder

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            StringBuilder emptySpots = new StringBuilder("Boş Park Yerleri:\n");

            // Her bir boş park yerini yazdır
            while (rs.next()) {
                emptySpots.append(rs.getString("spot_number")).append("\n");
            }

            // Boş park yeri varsa listeyi göster, yoksa bilgilendirme yap
            JOptionPane.showMessageDialog(this, emptySpots.length() > 0 ? emptySpots.toString() : "Boş park yeri bulunamadı.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Boş park yerlerini listelerken hata oluştu.");
        }
    }

    private void loadVehicles() {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Veritabanı bağlantısı sağlanamadı.");
            return;
        }

        String query = "SELECT v.vehicle_id, v.user_id, v.license_plate, v.vehicle_type, ps.spot_number AS parking_spot, v.created_at " +
                "FROM Vehicles v LEFT JOIN ParkingSpots ps ON v.parking_spot_id = ps.spot_id WHERE v.user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, currentUserId); // Giriş yapan kullanıcının ID'sini kullan
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0); // Tabloyu temizle

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
    private void markParkingSpotAsAvailable(String spotNumber) {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Veritabanı bağlantısı sağlanamadı.");
            return;
        }

        // Park yerinin is_available durumunu 1
        String query = "UPDATE ParkingSpots SET is_available = 1 WHERE spot_number = ?";

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


    private void markParkingSpotAsOccupied(String spotNumber) {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Veritabanı bağlantısı sağlanamadı.");
            return;
        }

        // Park yerinin is_available durumunu 0 yap (dolu)
        String query = "UPDATE ParkingSpots SET is_available = 0 WHERE spot_number = ?";

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



    public static void main(String[] args) {
        int currentUserId = 1; // Örnek kullanıcı ID'si
        new ParkingManagementFrame(currentUserId);
    }
}

