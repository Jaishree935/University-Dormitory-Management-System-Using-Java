package university;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class NewStudent extends JFrame {
    private JTextField roomNumberField;
    private JButton checkRoomButton;
    private JButton saveButton;
    private JButton clearButton;

    // Components for Student Details
    private JTextField nameField;
    private JTextField fatherNameField;
    private JTextField phoneNumberField;
    private JTextField universityField;
    private JComboBox<String> degreeProgramComboBox;
    private JTextField dateOfBirthField;
    private JTextField motherNameField;
    private JTextField emailField;
    private JTextField universityIDField;
    private JTextArea addressField;
    private JRadioButton maleRadio;
    private JRadioButton femaleRadio;
    private JRadioButton otherRadio;
    private JComboBox<String> studentTypeComboBox; // Domestic or International
    private JTextField passportField; // Passport number field
    private JTextField visaField; // Visa details field

    private static final int MAX_CAPACITY = 4; // Maximum students per room

    public NewStudent() {
        // Frame setup
        setTitle("New Student");
        setSize(650, 900);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Initialize components
        initializeComponents();
        setComponentBounds();
        addActionListeners();
        addComponentsToFrame();
    }

    private void initializeComponents() {
        roomNumberField = new JTextField();
        checkRoomButton = new JButton("Check Room Availability");

        // Student fields
        nameField = new JTextField();
        fatherNameField = new JTextField();
        phoneNumberField = new JTextField();
        universityField = new JTextField();
        degreeProgramComboBox = new JComboBox<>(new String[]{"Bachelor Program", "Master Program"});
        dateOfBirthField = new JTextField();
        motherNameField = new JTextField();
        emailField = new JTextField();
        universityIDField = new JTextField();
        addressField = new JTextArea();
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        otherRadio = new JRadioButton("Other");

        // New fields for international students
        studentTypeComboBox = new JComboBox<>(new String[]{"Domestic", "International"});
        passportField = new JTextField();
        visaField = new JTextField();

        // Initialize save and clear buttons
        saveButton = new JButton("Save");
        clearButton = new JButton("Clear");

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderGroup.add(otherRadio);
    }

    private void setComponentBounds() {
        roomNumberField.setBounds(150, 20, 200, 25);
        checkRoomButton.setBounds(50, 60, 250, 25);

        nameField.setBounds(150, 100, 200, 25);
        fatherNameField.setBounds(150, 140, 200, 25);
        phoneNumberField.setBounds(150, 180, 200, 25);
        universityField.setBounds(150, 220, 200, 25);
        degreeProgramComboBox.setBounds(150, 260, 200, 25);
        dateOfBirthField.setBounds(150, 300, 200, 25);
        motherNameField.setBounds(150, 340, 200, 25);
        emailField.setBounds(150, 380, 200, 25);
        universityIDField.setBounds(150, 420, 200, 25);
        addressField.setBounds(150, 460, 200, 60);
        maleRadio.setBounds(150, 530, 100, 25);
        femaleRadio.setBounds(250, 530, 100, 25);
        otherRadio.setBounds(350, 530, 100, 25);
        
        studentTypeComboBox.setBounds(150, 570, 200, 25);
        passportField.setBounds(150, 610, 200, 25);
        visaField.setBounds(150, 650, 200, 25);
        
        saveButton.setBounds(50, 690, 100, 25);
        clearButton.setBounds(200, 690, 100, 25);
    }

    private void addActionListeners() {
        checkRoomButton.addActionListener(e -> checkRoomAvailability());
        saveButton.addActionListener(e -> saveStudent());
        clearButton.addActionListener(e -> clearFields());

        studentTypeComboBox.addActionListener(e -> toggleInternationalFields());
    }

    private void addComponentsToFrame() {
        add(roomNumberField);
        add(checkRoomButton);

        // Labels for fields
        addLabel("Room Number:", 20, 20);
        addLabel("Name:", 20, 100);
        addLabel("Father Name:", 20, 140);
        addLabel("Phone Number:", 20, 180);
        addLabel("University Name:", 20, 220);
        addLabel("Degree Program:", 20, 260);
        addLabel("Date of Birth:", 20, 300);
        addLabel("Mother Name:", 20, 340);
        addLabel("Email:", 20, 380);
        addLabel("University ID:", 20, 420);
        addLabel("Address:", 20, 460);
        addLabel("Gender:", 20, 530);
        addLabel("Student Type:", 20, 570);
        addLabel("Passport Number:", 20, 610);
        addLabel("Visa Details:", 20, 650);

        add(nameField);
        add(fatherNameField);
        add(phoneNumberField);
        add(universityField);
        add(degreeProgramComboBox);
        add(dateOfBirthField);
        add(motherNameField);
        add(emailField);
        add(universityIDField);
        add(addressField);
        add(maleRadio);
        add(femaleRadio);
        add(otherRadio);
        add(studentTypeComboBox);
        add(passportField);
        add(visaField);
        add(saveButton);
        add(clearButton);
    }

    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 120, 25);
        add(label);
    }

    private void checkRoomAvailability() {
        String roomNumber = roomNumberField.getText();
        if (roomNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a room number.");
            return;
        }

        int availableCapacity = getAvailableRoomCapacity(roomNumber);
        if (availableCapacity < 0) {
            JOptionPane.showMessageDialog(this, "Error checking room availability.");
        } else if (availableCapacity == 0) {
            JOptionPane.showMessageDialog(this, "Room " + roomNumber + " is full.");
        } else {
            JOptionPane.showMessageDialog(this, "Room " + roomNumber + " has " + availableCapacity + " available spots.");
        }
    }

    private int getAvailableRoomCapacity(String roomNumber) {
        int currentOccupants = 0;

        String query = "SELECT COUNT(*) as current_occupants FROM students WHERE room_number = ?";
        try (Connection con = MyConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                currentOccupants = rs.getInt("current_occupants");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Error occurred
        }

        return MAX_CAPACITY - currentOccupants;
    }

    private void saveStudent() {
        String roomNumber = roomNumberField.getText();
        int availableCapacity = getAvailableRoomCapacity(roomNumber);
        
        if (availableCapacity <= 0 ) {
            JOptionPane.showMessageDialog(this, "Cannot add student: Room " + roomNumber + " is full.");
            return;
        }

        String name = nameField.getText();
        String fatherName = fatherNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String universityName = universityField.getText();
        String degreeProgram = (String) degreeProgramComboBox.getSelectedItem();
        String dateOfBirth = dateOfBirthField.getText();
        String motherName = motherNameField.getText();
        String email = emailField.getText();
        String universityID = universityIDField.getText();
        String address = addressField.getText();
        String gender = getSelectedGender();
        String studentType = (String) studentTypeComboBox.getSelectedItem();
        String passportNumber = passportField.getText();
        String visaDetails = visaField.getText();

        if (roomNumber.isEmpty() || name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Room number, name, and email are required.");
            return;
        }

        // Insert new student into database
        String insertQuery = "INSERT INTO students (name, father_name, phone_number, university_name, degree_program, date_of_birth, " +
                "mother_name, email, university_id, address, gender, room_number, student_type, passport_number, visa) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection con = MyConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setString(1, name);
            pstmt.setString(2, fatherName);
            pstmt.setString(3, phoneNumber);
            pstmt.setString(4, universityName);
            pstmt.setString(5, degreeProgram);
            pstmt.setString(6, dateOfBirth);
            pstmt.setString(7, motherName);
            pstmt.setString(8, email);
            pstmt.setString(9, universityID);
            pstmt.setString(10, address);
            pstmt.setString(11, gender);
            pstmt.setString(12, roomNumber);
            pstmt.setString(13, studentType);
            pstmt.setString(14, passportNumber);
            pstmt.setString(15, visaDetails);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student saved successfully.");
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving student data.");
        }
    }

    private String getSelectedGender() {
        if (maleRadio.isSelected()) return "Male";
        if (femaleRadio.isSelected()) return "Female";
        if (otherRadio.isSelected()) return "Other";
        return "";
    }

    private void clearFields() {
        roomNumberField.setText("");
        nameField.setText("");
        fatherNameField.setText("");
        phoneNumberField.setText("");
        universityField.setText("");
        degreeProgramComboBox.setSelectedIndex(0);
        dateOfBirthField.setText("");
        motherNameField.setText("");
        emailField.setText("");
        universityIDField.setText("");
        addressField.setText("");
        maleRadio.setSelected(false);
        femaleRadio.setSelected(false);
        otherRadio.setSelected(false);
        studentTypeComboBox.setSelectedIndex(0);
        passportField.setText("");
        visaField.setText("");
    }

    private void toggleInternationalFields() {
        boolean isInternational = studentTypeComboBox.getSelectedItem().equals("International");
        passportField.setVisible(isInternational);
        visaField.setVisible(isInternational);
        passportField.setEnabled(isInternational);
        visaField.setEnabled(isInternational);
    }

    public static void main(String[] args) {
        new NewStudent().setVisible(true);
    }
}