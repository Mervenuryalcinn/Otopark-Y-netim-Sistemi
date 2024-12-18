package views;

import controllers.AuthController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Giriş Ekranı");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // BorderLayout daha esnek bir yerleşim sağlar

        // Başlık kısmı
        JPanel headerPanel = new JPanel();
        JLabel titleLabel = new JLabel("OTOPARK", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(0, 122, 255)); // Başlık rengi
        headerPanel.setBackground(new Color(51, 51, 51)); // Başlık paneli arka plan rengi
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH); // Başlık, üstte yer alacak

        // Ana paneli oluşturuyoruz ve ortada yerleştiriyoruz
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245)); // Soft gri arka plan
        add(panel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Alanlar arasında boşluk

        // Kullanıcı adı
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Kullanıcı Adı:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(usernameField, gbc);

        // Şifre
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Şifre:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(passwordField, gbc);

        // Butonlar için panel oluşturuluyor (Giriş ve Üye Ol butonları yan yana)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245)); // Soft gri arka plan
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Butonları ortalayarak yan yana koy

        // Giriş butonu
        JButton loginButton = new JButton("Giriş Yap");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(0, 122, 255)); // Buton rengi
        loginButton.setForeground(Color.WHITE); // Yazı rengi
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(120, 40)); // Buton boyutu
        loginButton.addActionListener(new LoginAction());

        // Üye olma butonu
        JButton registerButton = new JButton("Üye Ol");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 14));
        registerButton.setBackground(new Color(255, 102, 102)); // Buton rengi
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(120, 40)); // Buton boyutu
        registerButton.addActionListener(new RegisterAction());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        add(buttonPanel, BorderLayout.SOUTH); // Butonları altta yerleştir

        // Pencere ayarları
        setSize(400, 300); // Daha geniş bir pencere boyutu
        setLocationRelativeTo(null); // Ortada açılmasını sağlarız
        setVisible(true);
    }

    // Giriş işlemi
    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (AuthController.authenticate(username, password)) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Giriş başarılı!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                String message = AuthController.getUserMessage(username);
                if (message != null && !message.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Mesajınız: " + message, "Mesaj", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Hoşgeldiniz", "Mesaj", JOptionPane.INFORMATION_MESSAGE);
                }
                String role = AuthController.getUserRole(username); // Kullanıcı rolünü al

                dispose();
                if ("admin".equals(role)) {
                    new VehiclesManagementFrame(); // Yönetici ekranını aç
                } else {
                    new MainMenuFrame(); // Normal kullanıcı menüsünü aç
                }
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Hatalı kullanıcı adı veya şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Üye Olma işlemi
    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new RegisterFrame(); // Üye olma ekranını açıyoruz
        }
    }

    // Ana metod
    public static void main(String[] args) {
        new LoginFrame();
    }
}
