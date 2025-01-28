import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
import java.sql.*;

public class EmployeeManagementSystem {
    // Database credentials
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/EmployeeDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "psjkagap6";

    // GUI components
    private JFrame frame;
    private JTextField nameField, ageField, departmentField, salaryField;
    private JTable employeeTable;
    private DefaultTableModel tableModel;

    public EmployeeManagementSystem() {
        initialize();
        loadEmployees();
    }

    private void initialize() {
        // Frame setup
        frame = new JFrame("Employee Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel ageLabel = new JLabel("Age:");
        ageField = new JTextField();
        JLabel departmentLabel = new JLabel("Department:");
        departmentField = new JTextField();
        JLabel salaryLabel = new JLabel("Salary:");
        salaryField = new JTextField();

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(ageLabel);
        formPanel.add(ageField);
        formPanel.add(departmentLabel);
        formPanel.add(departmentField);
        formPanel.add(salaryLabel);
        formPanel.add(salaryField);
        formPanel.add(addButton);
        formPanel.add(updateButton);

        // Table panel
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Department", "Salary"}, 0);
        employeeTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(employeeTable);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Add components to frame
        frame.add(formPanel, BorderLayout.NORTH);
        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e -> addEmployee());
        updateButton.addActionListener(e -> updateEmployee());
        deleteButton.addActionListener(e -> deleteEmployee());
        clearButton.addActionListener(e -> clearFields());

        employeeTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && employeeTable.getSelectedRow() != -1) {
                populateFields();
            }
        });

        frame.setVisible(true);
    }

    private void loadEmployees() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Employees")) {

            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("department"),
                        rs.getDouble("salary")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading employees: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addEmployee() {
        String name = nameField.getText();
        String age = ageField.getText();
        String department = departmentField.getText();
        String salary = salaryField.getText();

        if (name.isEmpty() || age.isEmpty() || department.isEmpty() || salary.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Employees (name, age, department, salary) VALUES (?, ?, ?, ?)")) {

            pstmt.setString(1, name);
            pstmt.setInt(2, Integer.parseInt(age));
            pstmt.setString(3, department);
            pstmt.setDouble(4, Double.parseDouble(salary));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Employee added successfully.");
            loadEmployees();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error adding employee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an employee to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = nameField.getText();
        String age = ageField.getText();
        String department = departmentField.getText();
        String salary = salaryField.getText();

        if (name.isEmpty() || age.isEmpty() || department.isEmpty() || salary.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE Employees SET name = ?, age = ?, department = ?, salary = ? WHERE id = ?")) {

            pstmt.setString(1, name);
            pstmt.setInt(2, Integer.parseInt(age));
            pstmt.setString(3, department);
            pstmt.setDouble(4, Double.parseDouble(salary));
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Employee updated successfully.");
            loadEmployees();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error updating employee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an employee to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Employees WHERE id = ?")) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Employee deleted successfully.");
            loadEmployees();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error deleting employee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        departmentField.setText("");
        salaryField.setText("");
    }

    private void populateFields() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            ageField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            departmentField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            salaryField.setText(tableModel.getValueAt(selectedRow, 4).toString());
        }
    }

    public static void main(String[] args) {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("JDBC Driver loaded successfully.");
            
            // Example connection
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/EmployeeDB", // Replace with your DB URL
                "root",                                   // Replace with your DB username
                "password"                                // Replace with your DB password
            );
            System.out.println("Connected to the database.");
            conn.close();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Unable to load JDBC Driver.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error: Unable to connect to the database.");
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(EmployeeManagementSystem::new);
        
    }
}
