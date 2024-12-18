package views;

import controllers.AuthController;
import controllers.ParkAreaObserver;  // ParkAreaObserver doğru paketten import edildi

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField fullNameField;
    private JTextField emailField;

    public RegisterFrame() {
        setTitle("Üye Ol");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Ad Soyad:"));
        fullNameField = new JTextField();
        add(fullNameField);

        add(new JLabel("Kullanıcı Adı:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("E-posta:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Şifre:"));
        passwordField = new JPasswordField();
        add(passwordField);

        // Park alanı gözlemcisi oluşturuluyor
        ParkAreaObserver observer1 = new ParkAreaObserver("Gözlemci 1");

        // ParkArea nesnesi null kontrolü yapılıyor
        if (AuthController.parkArea == null) {
            AuthController.parkArea = new ParkArea();  // parkArea'yı başlatıyoruz
        }

        // ParkArea nesnesine gözlemciyi ekliyoruz
        AuthController.parkArea.addObserver(observer1);

        JButton registerButton = new JButton("Kayıt Ol");
        registerButton.addActionListener(new RegisterAction());
        add(registerButton);

        setSize(300, 250);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            String role = "customer";
            String parkAreaName = "Park Alanı 1";  // Varsayılan park alanı

            if (AuthController.register(username, password, email, role, parkAreaName)) {
                JOptionPane.showMessageDialog(RegisterFrame.this, "Kayıt başarılı! Lütfen giriş yapın.");
                dispose();
                // Kayıt başarılıysa giriş ekranını açıyoruz
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new LoginFrame();
                    }
                });
            } else {
                JOptionPane.showMessageDialog(RegisterFrame.this, "Kayıt sırasında bir hata oluştu.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegisterFrame();
            }
        });
    }
}
