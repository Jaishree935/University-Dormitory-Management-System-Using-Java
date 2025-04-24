package university;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManageRooms extends JFrame {

    private JTextField txtRoomNumber, txtSearchRoom, txtUniversityName; // Replace drop-down with text field
    private JCheckBox checkRoomActive;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch, btnShowAll;
    private JTable tableRooms;
    private DefaultTableModel tableModel;

    public ManageRooms() {
        setTitle("Manage Rooms");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        JLabel lblRoomNumber = new JLabel("Room Number:");
        txtRoomNumber = new JTextField(10);
        JLabel lblUniversityName = new JLabel("University Name:"); // Added University Name label
        txtUniversityName = new JTextField(10); // Added University Name text field
        checkRoomActive = new JCheckBox("Room Active");

        btnAdd = new JButton("Add Room");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        btnSearch = new JButton("Search Room");
        btnShowAll = new JButton("Show All Rooms");

        // Set button colors to light red
        Color lightRed = new Color(255, 102, 102); // Light red color
        btnAdd.setBackground(lightRed);
        btnUpdate.setBackground(lightRed);
        btnDelete.setBackground(lightRed);
        btnClear.setBackground(lightRed);
        btnSearch.setBackground(lightRed);
        btnShowAll.setBackground(lightRed);

        txtSearchRoom = new JTextField(10);

        // Table to display rooms
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new Object[]{"Room Number", "Status"});
        tableRooms = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(tableRooms);

        // Layout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 1: Room Number
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblRoomNumber, gbc);

        gbc.gridx = 1;
        panel.add(txtRoomNumber, gbc);

        gbc.gridx = 2;
        panel.add(checkRoomActive, gbc);

        // Row 2: University Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblUniversityName, gbc);

        gbc.gridx = 1;
        panel.add(txtUniversityName, gbc); // Add text field for University Name

        // Row 3: Buttons (Add, Update, Delete, Clear)
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(btnAdd, gbc);

        gbc.gridx = 1;
        panel.add(btnUpdate, gbc);

        gbc.gridx = 2;
        panel.add(btnDelete, gbc);

        gbc.gridx = 3;
        panel.add(btnClear, gbc);

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search Room:"));
        searchPanel.add(txtSearchRoom);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);

        // Add components to frame
        add(panel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);

        // Set background color for the panel
        panel.setBackground(new Color(238, 224, 229)); // Light purple color

        // Event listeners
        btnAdd.addActionListener(e -> addRoom());
        btnUpdate.addActionListener(e -> updateRoom());
        btnDelete.addActionListener(e -> deleteRoom());
        btnClear.addActionListener(e -> clearFields());
        btnSearch.addActionListener(e -> searchRoom());
        btnShowAll.addActionListener(e -> showAllRooms());

        // Load all rooms initially
        showAllRooms();
    }

    private void addRoom() {
        String roomNumber = txtRoomNumber.getText();
        String universityName = txtUniversityName.getText(); // Added for University Name
        String status = checkRoomActive.isSelected() ? "Booked" : "Available";

        if (roomNumber.isEmpty() || universityName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter room number and university name.");
            return;
        }

        try (Connection con = MyConnection.getConnection()) {
            String query = "INSERT INTO Room (room_number, room_status, university_name) VALUES (?, ?, ?)"; // Adjusted query
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, roomNumber);
            ps.setString(2, status);
            ps.setString(3, universityName); // Added for University Name

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Room added successfully!");
            showAllRooms();
            clearFields();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateRoom() {
        int selectedRow = tableRooms.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to update.");
            return;
        }

        String roomNumber = (String) tableModel.getValueAt(selectedRow, 0);
        String newRoomNumber = txtRoomNumber.getText();
        String universityName = txtUniversityName.getText(); // Added for University Name
        String status = checkRoomActive.isSelected() ? "Booked" : "Available";

        if (newRoomNumber.isEmpty() || universityName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter room number and university name.");
            return;
        }

        try (Connection con = MyConnection.getConnection()) {
            String query = "UPDATE Room SET room_number = ?, room_status = ?, university_name = ? WHERE room_number = ?"; // Adjusted query
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, newRoomNumber);
            ps.setString(2, status);
            ps.setString(3, universityName); // Added for University Name
            ps.setString(4, roomNumber);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Room updated successfully!");
            showAllRooms();
            clearFields();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteRoom() {
        int selectedRow = tableRooms.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to delete.");
            return;
        }

        String roomNumber = (String) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 1);

        if (status.equals("Booked")) {
            JOptionPane.showMessageDialog(this, "Cannot delete a booked room.");
            return;
        }

        try (Connection con = MyConnection.getConnection()) {
            String query = "DELETE FROM Room WHERE room_number = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, roomNumber);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Room deleted successfully!");
            showAllRooms();
            clearFields();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void searchRoom() {
        String roomNumber = txtSearchRoom.getText();

        if (roomNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a room number to search.");
            return;
        }

        try (Connection con = MyConnection.getConnection()) {
            String query = "SELECT * FROM Room WHERE room_number = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, roomNumber);

            ResultSet rs = ps.executeQuery();
            tableModel.setRowCount(0);

            if (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("room_number"),
                        rs.getString("room_status")
                });
            } else {
                JOptionPane.showMessageDialog(this, "Room not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void showAllRooms() {
        try (Connection con = MyConnection.getConnection()) {
            String query = "SELECT * FROM Room";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("room_number"),
                        rs.getString("room_status")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        txtRoomNumber.setText("");
        txtUniversityName.setText(""); // Clear University Name
        checkRoomActive.setSelected(false);
        txtSearchRoom.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageRooms().setVisible(true));
    }
}