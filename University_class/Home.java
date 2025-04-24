package university;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Home extends JFrame {

    private JLabel roomsAvailableLabel;
    private JLabel roomsBookedLabel;
    private JLabel livingLocalStudentsLabel;
    private JLabel livingIntStudentsLabel;
    private JLabel leavedLocalStudentsLabel;

    public Home() {
        initComponents();
        loadDashboardValues(); // Load values from the database
    }

    // Initialize components and set up the UI
    private void initComponents() {
        // Frame settings
        setTitle("University Dormitory Management");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create the left panel for navigation with a gradient background
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(255, 140, 0); // Orange
                Color color2 = new Color(255, 69, 0);  // Red
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        leftPanel.setLayout(new GridLayout(7, 1, 10, 10));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Adding navigation buttons with vibrant colors and mouse effects
        String[] buttonLabels = {
            "Add Room", "Manage Rooms", "New Student", "Manage Students",
            "Living Students", "Leaved Students", "Logout"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setBackground(new Color(72, 209, 204)); // Medium Turquoise
            button.setForeground(Color.WHITE); // White text
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding for visual appeal

            // Add mouse listener for hover effect
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(32, 178, 170)); // Darker Turquoise on hover
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(new Color(72, 209, 204)); // Original color on exit
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    button.setBackground(new Color(0, 139, 139)); // Even darker on click
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    button.setBackground(new Color(32, 178, 170)); // Return to hover color after release
                }
            });

            button.addActionListener(new ButtonClickListener(label));
            leftPanel.add(button);
        }

        // Create the dashboard panel with a more colorful theme
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new GridLayout(3, 2, 20, 20)); // Added gaps between components
        dashboardPanel.setBackground(new Color(255, 245, 238)); // Light Coral background
        dashboardPanel.setBorder(BorderFactory.createTitledBorder("Dashboard"));

        // Dashboard titles and labels with enhanced colors
        String[] dashboardTitles = {
            "Rooms Available", "Rooms Booked", "Living Local Students",
            "Living Int Students", "Leaved Local Students"
        };

        roomsAvailableLabel = createDashboardLabel();
        roomsBookedLabel = createDashboardLabel();
        livingLocalStudentsLabel = createDashboardLabel();
        livingIntStudentsLabel = createDashboardLabel();
        leavedLocalStudentsLabel = createDashboardLabel();

        // Add colorful dashboard components
        dashboardPanel.add(createDashboardPanel(dashboardTitles[0], roomsAvailableLabel, new Color(152, 251, 152))); // Light Green
        dashboardPanel.add(createDashboardPanel(dashboardTitles[1], roomsBookedLabel, new Color(255, 182, 193)));   // Light Pink
        dashboardPanel.add(createDashboardPanel(dashboardTitles[2], livingLocalStudentsLabel, new Color(135, 206, 235))); // Sky Blue
        dashboardPanel.add(createDashboardPanel(dashboardTitles[3], livingIntStudentsLabel, new Color(255, 165, 0)));    // Orange
        dashboardPanel.add(createDashboardPanel(dashboardTitles[4], leavedLocalStudentsLabel, new Color(221, 160, 221))); // Plum

        // Add panels to the main frame
        add(leftPanel, BorderLayout.WEST);
        add(dashboardPanel, BorderLayout.CENTER);

        // Set a welcome message at the top with vibrant colors
        JLabel welcomeLabel = new JLabel("WELCOME ADMIN", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(255, 105, 180)); // Hot Pink
        add(welcomeLabel, BorderLayout.NORTH);

        setVisible(true);
    }

    // Create a colorful dashboard label
    private JLabel createDashboardLabel() {
        JLabel label = new JLabel("0", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30)); // Increased font size for better visibility
        label.setForeground(new Color(60, 179, 113)); // Medium Sea Green
        label.setPreferredSize(new Dimension(200, 80)); // Set a larger preferred size for the label
        label.setOpaque(true); // Make the label opaque to show background color
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Added border for visibility

        // Add mouse listener for dashboard label hover effect
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(new Color(0, 100, 0)); // Darker green on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(new Color(60, 179, 113)); // Original color on exit
            }
        });

        return label;
    }

    // Create a colorful panel for each dashboard item
    private JPanel createDashboardPanel(String title, JLabel valueLabel, Color bgColor) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setBackground(bgColor); // Set the background color dynamically
        panel.setLayout(new BorderLayout());
        valueLabel.setBackground(bgColor); // Set the label's background to match the panel
        valueLabel.setOpaque(true); // Ensure the label shows the background color
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    // Load dynamic dashboard values from the database
    private void loadDashboardValues() {
        try (Connection conn = MyConnection.getConnection(); // Replace with your database connection method
             Statement stmt = conn.createStatement()) {

            String roomsAvailableQuery = "SELECT COUNT(*) FROM Room WHERE room_status = 'Available'";
            String roomsBookedQuery = "SELECT COUNT(*) FROM Room WHERE room_status = 'Booked'";
            String livingLocalStudentsQuery = "SELECT COUNT(*) FROM students WHERE student_type = 'Local'";
            String livingIntStudentsQuery = "SELECT COUNT(*) FROM students WHERE student_type = 'International'";
            String leavedLocalStudentsQuery = "SELECT COUNT(*) FROM leaved_students";

            roomsAvailableLabel.setText(getCount(stmt, roomsAvailableQuery));
            roomsBookedLabel.setText(getCount(stmt, roomsBookedQuery));
            livingLocalStudentsLabel.setText(getCount(stmt, livingLocalStudentsQuery));
            livingIntStudentsLabel.setText(getCount(stmt, livingIntStudentsQuery));
            leavedLocalStudentsLabel.setText(getCount(stmt, leavedLocalStudentsQuery));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to execute a query and get the count
    private String getCount(Statement stmt, String query) throws SQLException {
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            return rs.getString(1); // Get the count from the result set
        }
        return "0"; // Default value if no result
    }

    // Button click event handler
    private class ButtonClickListener implements ActionListener {
        private String action;

        public ButtonClickListener(String action) {
            this.action = action;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (action) {
                case "Add Room":
                    // Add room and update dashboard values
                    new AddRoom().setVisible(true);
                    loadDashboardValues(); // Refresh dashboard
                    break;
                case "Manage Rooms":
                    new ManageRooms().setVisible(true);
                    loadDashboardValues(); // Refresh dashboard
                    break;
                case "New Student":
                    new NewStudent().setVisible(true);
                    loadDashboardValues(); // Refresh dashboard
                    break;
                case "Manage Students":
                    new ManageStudents().setVisible(true);
                    loadDashboardValues(); // Refresh dashboard
                    break;
                case "Living Students":
                    new LivingStudents().setVisible(true);
                    break;
                case "Leaved Students":
                    new LeavedStudents().setVisible(true);
                    break;
                case "Logout":
                    dispose();
                    new LoginForm().setVisible(true);
                    break;
                default:
                    break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Home::new);
    }
}
