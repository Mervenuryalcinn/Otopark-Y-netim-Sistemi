package views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class MainMenuFrame extends JFrame {
    public MainMenuFrame() {
        setTitle("Ana Menü");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Park yeri yönetimi butonu
        JButton parkingButton = new JButton("Park Yerleri Yönetimi");
        parkingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int someIntValue = 1; // You can replace this with the appropriate value
                new ParkingManagementFrame(someIntValue); // Park yeri yönetim ekranını aç
            }
        });
        add(parkingButton);

        // Çıkış butonu
        JButton logoutButton = new JButton("Çıkış Yap");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Ana menüyü kapat
                new LoginFrame(); // Giriş ekranına dön
            }
        });
        add(logoutButton);

        setSize(300, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
