package university;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class LivingStudents extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    // Database connection
    MyConnection myConnection = new MyConnection();

    public LivingStudents() {
        setTitle("Living Students");
        setSize(700, 400); // Increased width to accommodate the additional column
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create table model and JTable
        String[] columnNames = {"Name", "Phone Number", "Gender", "Email", "University ID", "Room Number"}; // Added Room Number
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        
        // Load data from database
        loadLivingStudents();

        // Set up the layout
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadLivingStudents() {
        String query = "SELECT name, phone_number, gender, email, university_id, room_number FROM students"; // Updated query to include room_number

        try (Connection con = myConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Clear previous data
            tableModel.setRowCount(0);

            // Populate table model with data
            while (rs.next()) {
                String name = rs.getString("name");
                String phoneNumber = rs.getString("phone_number");
                String gender = rs.getString("gender");
                String email = rs.getString("email");
                String universityId = rs.getString("university_id");
                String roomNumber = rs.getString("room_number"); // Fetch room number

                // Add a row to the table model
                tableModel.addRow(new Object[]{name, phoneNumber, gender, email, universityId, roomNumber}); // Added room number
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LivingStudents().setVisible(true));
    }
}
