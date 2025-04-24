package university;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class AddRoom extends JFrame {

    private JLabel lblRoomNumber, lblSearchRoom;
    private JTextField txtRoomNumber, txtSearchRoom;
    private JButton btnSave, btnSearch;
    private JTable tblRoomHistory;
    private DefaultTableModel tableModel;

    // Database connection
    MyConnection myConnection = new MyConnection();

    public AddRoom() {
        initComponents();
        loadRoomHistory(); // Load room history when the form opens
    }

    // Initialize GUI components
    private void initComponents() {
        // Labels
        lblRoomNumber = new JLabel("Room Number:");
        lblSearchRoom = new JLabel("Search Room:");

        // Text fields
        txtRoomNumber = new JTextField(15);
        txtSearchRoom = new JTextField(15);

        // Buttons
        btnSave = new JButton("Save");
        btnSearch = new JButton("Search");

        // Set light violet background color for buttons
        Color lightViolet = new Color(238, 220, 255); // Light violet color
        btnSave.setBackground(lightViolet);
        btnSearch.setBackground(lightViolet);

        // Room history table
        String[] columnNames = {"Room Number", "Room Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tblRoomHistory = new JTable(tableModel);

        // Set light purple background color for the main frame
        Color lightPurple = new Color(221, 160, 221); // Light purple color

        // Set light purple background color for the form panel
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(lightPurple); // Set panel background color

        // Layout for the form using GridBagLayout for better control
        setLayout(new BorderLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Add Room Number label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        panelForm.add(lblRoomNumber, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panelForm.add(txtRoomNumber, gbc);

        // Add Search Room label and text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panelForm.add(lblSearchRoom, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panelForm.add(txtSearchRoom, gbc);

        // Add Search and Save buttons
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnSave);
        panelForm.add(buttonPanel, gbc);
        buttonPanel.setBackground(lightPurple); // Set button panel background color

        // Add components to the frame
        add(panelForm, BorderLayout.NORTH);

        JScrollPane tableScrollPane = new JScrollPane(tblRoomHistory);
        tableScrollPane.getViewport().setBackground(lightPurple); // Set table background color
        add(tableScrollPane, BorderLayout.CENTER);

        // Set frame properties
        setTitle("Add Room");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Button actions
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveRoom();
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchRoom();
            }
        });
    }

    // Method to save a new room
    private void saveRoom() {
        String roomNumber = txtRoomNumber.getText();

        if (roomNumber.equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter the room number.");
            return;
        }

        Connection con = myConnection.getConnection();
        String query = "INSERT INTO Room (room_number, room_status) VALUES (?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, roomNumber);
            ps.setString(2, "Available"); // Room status default to "Available"

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Room added successfully.");
                loadRoomHistory(); // Reload room history after adding a new room
                clearFields();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add room.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    // Method to search for a room by room number
    private void searchRoom() {
        String searchRoomNumber = txtSearchRoom.getText();

        if (searchRoomNumber.equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter a room number to search.");
            return;
        }

        Connection con = myConnection.getConnection();
        String query = "SELECT * FROM Room WHERE room_number = ?";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, searchRoomNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtRoomNumber.setText(rs.getString("room_number"));
                JOptionPane.showMessageDialog(null, "Room found.");
            } else {
                JOptionPane.showMessageDialog(null, "Room not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    // Method to load room history into the table
    private void loadRoomHistory() {
        tableModel.setRowCount(0); // Clear the table before reloading data

        Connection con = myConnection.getConnection();
        String query = "SELECT * FROM Room";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String roomNumber = rs.getString("room_number");
                String roomStatus = rs.getString("room_status");

                tableModel.addRow(new Object[]{roomNumber, roomStatus});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading room history: " + ex.getMessage());
        }
    }

    // Method to clear input fields
    private void clearFields() {
        txtRoomNumber.setText("");
        txtSearchRoom.setText("");
    }

    // Main method to run the AddRoom form
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AddRoom().setVisible(true);
            }
        });
    }
}