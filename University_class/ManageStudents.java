package university;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageStudents extends JFrame {

    // Components for student details
    private JTextField txtName, txtFatherName, txtPhoneNumber, txtDOB, txtMotherName, txtEmail, txtUniversityID, txtAddress;
    private JComboBox<String> cmbDegreeProgram, cmbRoomNumber, cmbUniversityName, cmbStudentType;
    private JTextField txtPassportNumber, txtVisa; // Fields for international students
    private JRadioButton rbtnMale, rbtnFemale;
    private JButton btnSearch, btnUpdate, btnDelete, btnClear;
    private ButtonGroup genderGroup;

    // Database connection
    MyConnection myConnection = new MyConnection();

    public ManageStudents() {
        initComponents();
    }

    // Initialize components for the form
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels and Input fields
        JLabel lblName = new JLabel("Name:");
        JLabel lblFatherName = new JLabel("Father Name:");
        JLabel lblGender = new JLabel("Gender:");
        JLabel lblPhoneNumber = new JLabel("Phone Number:");
        JLabel lblUniversityName = new JLabel("University Name:");
        JLabel lblDegreeProgram = new JLabel("Degree Program:");
        JLabel lblRoomNumber = new JLabel("Room Number:");
        JLabel lblDOB = new JLabel("Date of Birth:");
        JLabel lblMotherName = new JLabel("Mother Name:");
        JLabel lblEmail = new JLabel("Email:");
        JLabel lblUniversityID = new JLabel("University ID:");
        JLabel lblAddress = new JLabel("Address:");
        JLabel lblStudentType = new JLabel("Student Type:"); // Added label for student type
        JLabel lblPassportNumber = new JLabel("Passport Number:"); // International student
        JLabel lblVisa = new JLabel("Visa:"); // International student

        // Text fields
        txtName = new JTextField(15);
        txtFatherName = new JTextField(15);
        txtPhoneNumber = new JTextField(15);
        txtDOB = new JTextField(15);
        txtMotherName = new JTextField(15);
        txtEmail = new JTextField(15);
        txtUniversityID = new JTextField(15);
        txtAddress = new JTextField(15);

        txtPassportNumber = new JTextField(15); // Added for international students
        txtVisa = new JTextField(15); // Added for international students

        // Combo box for degree programs
        cmbDegreeProgram = new JComboBox<>(new String[]{"Bachelor Program", "Master Program", "PhD Program"});

        // Combo box for room numbers (example: 1-100)
        cmbRoomNumber = new JComboBox<>();
        for (int i = 1; i <= 100; i++) {
            cmbRoomNumber.addItem(String.valueOf(i));
        }

        // Combo box for universities
        cmbUniversityName = new JComboBox<>(new String[]{"University A", "University B", "University C"});

        // Combo box for student types
        cmbStudentType = new JComboBox<>(new String[]{"Local", "International"}); // Added student type options

        // Gender radio buttons
        rbtnMale = new JRadioButton("Male");
        rbtnFemale = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(rbtnMale);
        genderGroup.add(rbtnFemale);

        // Buttons
        btnSearch = new JButton("Search");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        // Add components to the frame
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(lblPhoneNumber, gbc);
        
        gbc.gridx = 1;
        add(txtPhoneNumber, gbc);
        
        gbc.gridx = 2;
        add(btnSearch, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(lblName, gbc);
        
        gbc.gridx = 1;
        add(txtName, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        add(lblFatherName, gbc);
        
        gbc.gridx = 1;
        add(txtFatherName, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        add(lblGender, gbc);
        
        gbc.gridx = 1;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.add(rbtnMale);
        genderPanel.add(rbtnFemale);
        add(genderPanel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        add(lblUniversityName, gbc);
        
        gbc.gridx = 1;
        add(cmbUniversityName, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        add(lblDegreeProgram, gbc);
        
        gbc.gridx = 1;
        add(cmbDegreeProgram, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        add(lblRoomNumber, gbc);
        
        gbc.gridx = 1;
        add(cmbRoomNumber, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        add(lblDOB, gbc);
        
        gbc.gridx = 1;
        add(txtDOB, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        add(lblMotherName, gbc);
        
        gbc.gridx = 1;
        add(txtMotherName, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        add(lblEmail, gbc);
        
        gbc.gridx = 1;
        add(txtEmail, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        add(lblUniversityID, gbc);
        
        gbc.gridx = 1;
        add(txtUniversityID, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        add(lblAddress, gbc);
        
        gbc.gridx = 1;
        add(txtAddress, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        add(lblStudentType, gbc);
        
        gbc.gridx = 1;
        add(cmbStudentType, gbc);
        
        // For international students
        gbc.gridx = 0;
        gbc.gridy++;
        add(lblPassportNumber, gbc);
        
        gbc.gridx = 1;
        add(txtPassportNumber, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        add(lblVisa, gbc);
        
        gbc.gridx = 1;
        add(txtVisa, gbc);
        
        // Add buttons to the bottom
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        add(buttonPanel, gbc);

        // Set frame properties
        setTitle("Manage Student");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Button actions
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudent();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
    }

    // Method to search for a student by phone number
    private void searchStudent() {
        String phoneNumber = txtPhoneNumber.getText();

        if (phoneNumber.equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter a phone number.");
            return;
        }

        Connection con = myConnection.getConnection();
        String query = "SELECT * FROM students WHERE phone_number = ?";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, phoneNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Populate fields with student data
                txtName.setText(rs.getString("name"));
                txtFatherName.setText(rs.getString("father_name"));
                String gender = rs.getString("gender");
                if (gender.equals("Male")) {
                    rbtnMale.setSelected(true);
                } else {
                    rbtnFemale.setSelected(true);
                }
                cmbUniversityName.setSelectedItem(rs.getString("university_name"));
                cmbDegreeProgram.setSelectedItem(rs.getString("degree_program"));
                cmbRoomNumber.setSelectedItem(rs.getString("room_number"));
                txtDOB.setText(rs.getString("date_of_birth"));
                txtMotherName.setText(rs.getString("mother_name"));
                txtEmail.setText(rs.getString("email"));
                txtUniversityID.setText(rs.getString("university_id"));
                txtAddress.setText(rs.getString("address"));
                cmbStudentType.setSelectedItem(rs.getString("student_type"));

                // For international students
                if (rs.getString("student_type").equals("International")) {
                    txtPassportNumber.setText(rs.getString("passport_number"));
                    txtVisa.setText(rs.getString("visa"));
                } else {
                    txtPassportNumber.setText("");
                    txtVisa.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Student not found.");
            }

            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while searching student.");
        }
    }

    // Method to update student information
    private void updateStudent() {
        String phoneNumber = txtPhoneNumber.getText();
        String name = txtName.getText();
        String fatherName = txtFatherName.getText();
        String gender = rbtnMale.isSelected() ? "Male" : "Female";
        String universityName = (String) cmbUniversityName.getSelectedItem();
        String degreeProgram = (String) cmbDegreeProgram.getSelectedItem();
        String roomNumber = (String) cmbRoomNumber.getSelectedItem();
        String dob = txtDOB.getText();
        String motherName = txtMotherName.getText();
        String email = txtEmail.getText();
        String universityID = txtUniversityID.getText();
        String address = txtAddress.getText();
        String studentType = (String) cmbStudentType.getSelectedItem();
        String passportNumber = txtPassportNumber.getText();
        String visa = txtVisa.getText();

        // Validation (add more as needed)
        if (phoneNumber.equals("")) {
            JOptionPane.showMessageDialog(null, "Phone number cannot be empty.");
            return;
        }

        Connection con = myConnection.getConnection();
        String query = "UPDATE students SET name = ?, father_name = ?, gender = ?, university_name = ?, degree_program = ?, room_number = ?, date_of_birth = ?, mother_name = ?, email = ?, university_id = ?, address = ?, student_type = ?, passport_number = ?, visa = ? WHERE phone_number = ?";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, fatherName);
            ps.setString(3, gender);
            ps.setString(4, universityName);
            ps.setString(5, degreeProgram);
            ps.setString(6, roomNumber);
            ps.setString(7, dob);
            ps.setString(8, motherName);
            ps.setString(9, email);
            ps.setString(10, universityID);
            ps.setString(11, address);
            ps.setString(12, studentType);
            ps.setString(13, studentType.equals("International") ? passportNumber : null);
            ps.setString(14, studentType.equals("International") ? visa : null);
            ps.setString(15, phoneNumber);

            int updatedRows = ps.executeUpdate();
            if (updatedRows > 0) {
                JOptionPane.showMessageDialog(null, "Student information updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "No changes were made.");
            }

            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while updating student.");
        }
    }

    // Method to delete a student by phone number
    private void deleteStudent() {
        String phoneNumber = txtPhoneNumber.getText();

        if (phoneNumber.equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter a phone number.");
            return;
        }

        Connection con = myConnection.getConnection();
        String query = "DELETE FROM students WHERE phone_number = ?";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, phoneNumber);

            int deletedRows = ps.executeUpdate();
            if (deletedRows > 0) {
                JOptionPane.showMessageDialog(null, "Student deleted successfully.");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(null, "Student not found.");
            }

            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while deleting student.");
        }
    }

    // Method to clear all input fields
    private void clearFields() {
        txtName.setText("");
        txtFatherName.setText("");
        txtPhoneNumber.setText("");
        txtDOB.setText("");
        txtMotherName.setText("");
        txtEmail.setText("");
        txtUniversityID.setText("");
        txtAddress.setText("");
        txtPassportNumber.setText("");
        txtVisa.setText("");
        genderGroup.clearSelection();
        cmbUniversityName.setSelectedIndex(0);
        cmbDegreeProgram.setSelectedIndex(0);
        cmbRoomNumber.setSelectedIndex(0);
        cmbStudentType.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManageStudents frame = new ManageStudents();
            frame.setVisible(true);
        });
    }
}
