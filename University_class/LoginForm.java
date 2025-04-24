package university;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class LoginForm extends JFrame {

    JLabel userLabel, passLabel;
    JTextField userField;
    JPasswordField passField;
    JButton loginBtn;

    public LoginForm() {
        setLayout(null);
        setTitle("Admin Login");

        // Set the background color
        JPanel background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(200, 180, 255), 0, getHeight(), new Color(230, 200, 255));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setLayout(null);
        setContentPane(background);

        // Initialize components
        userLabel = new JLabel("Username:");
        passLabel = new JLabel("Password:");
        userField = new JTextField();
        passField = new JPasswordField();
        loginBtn = new JButton("Login");

        // Set positions and sizes
        userLabel.setBounds(50, 50, 100, 30);
        passLabel.setBounds(50, 100, 100, 30);
        userField.setBounds(150, 50, 150, 30);
        passField.setBounds(150, 100, 150, 30);
        loginBtn.setBounds(150, 150, 100, 30);

        // Set input field colors
        userField.setBackground(Color.WHITE);
        userField.setForeground(Color.BLACK);
        passField.setBackground(Color.WHITE);
        passField.setForeground(Color.BLACK);

        // Style the login button
        loginBtn.setBackground(new Color(255, 102, 102)); // Light red
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));

        // Add components to the frame
        add(userLabel);
        add(passLabel);
        add(userField);
        add(passField);
        add(loginBtn);

        // Action listener for the login button
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminLogin();
            }
        });

        // Set frame properties
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void adminLogin() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        try (Connection con = MyConnection.getConnection()) {
            String query = "SELECT * FROM admin_login WHERE username = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE, null);
                new Home().setVisible(true);
                this.dispose();
            } else {
                showMessage("Invalid credentials!", Color.RED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMessage(String message, Color color) {
        JTextArea textArea = new JTextArea(message);
        textArea.setBackground(color);
        textArea.setForeground(Color.WHITE);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JOptionPane.showMessageDialog(null, textArea, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
