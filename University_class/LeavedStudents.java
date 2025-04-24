package university;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class LeavedStudents extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    // Database connection class
    MyConnection myConnection = new MyConnection();

    public LeavedStudents() {
        setTitle("Leaved Students");
        setSize(700, 400); // Set size for the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create table model and JTable
        String[] columnNames = {"Name", "Phone Number", "Gender", "Email", "Universityname", "Room Number"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        
        // Load data from the database
        loadLeavedStudents();

        // Set up the layout
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    // Method to load leaved students from the database
    private void loadLeavedStudents() {
        // SQL query to fetch leaved students with their room numbers
        String query = "SELECT name, phone_number, gender, email, university_name, room_number FROM leaved_students";

        try (Connection con = myConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Clear previous table data
            tableModel.setRowCount(0);

            // Loop through result set and populate table
            while (rs.next()) {
                String name = rs.getString("name");
                String phoneNumber = rs.getString("phone_number");
                String gender = rs.getString("gender");
                String email = rs.getString("email");
                String universityname = rs.getString("university_name");
                String roomNumber = rs.getString("room_number");  

                // Add row to table model
                tableModel.addRow(new Object[]{name, phoneNumber, gender, email, universityname, roomNumber});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LeavedStudents().setVisible(true));
    }
}

 
 
