import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginSignup extends JFrame {
    private JTextField usernameField, firstNameField, lastNameField, emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton loginButton, signupButton;

    public LoginSignup() {
        setTitle("University Forum - Login/Signup");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        signupButton = new JButton("Signup");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(usernameLabel, gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(loginButton, gbc);
        gbc.gridy = 3;
        add(signupButton, gbc);

        loginButton.addActionListener(e -> loginUser());
        signupButton.addActionListener(e -> showSignupForm());

        setVisible(true);
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String role = rs.getString("role");
                    JOptionPane.showMessageDialog(this, "Login successful! Role: " + role);
                    if (role.equals("Student")) {
                        new StudentUI(username);
                    } else if (role.equals("Professor")) {
                        new ProfessorUI(username);
                    }

                 else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.");
                }}
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void showSignupForm() {
        JDialog signupDialog = new JDialog(this, "Signup", true);
        signupDialog.setSize(400, 400);
        signupDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        emailField = new JTextField(20);
        roleComboBox = new JComboBox<>(new String[] { "Student", "Professor" });
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton submitButton = new JButton("Submit");

        gbc.gridx = 0; gbc.gridy = 0; signupDialog.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1; signupDialog.add(firstNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; signupDialog.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1; signupDialog.add(lastNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; signupDialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; signupDialog.add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; signupDialog.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1; signupDialog.add(roleComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 4; signupDialog.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; signupDialog.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; signupDialog.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; signupDialog.add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        signupDialog.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            signupUser();
            signupDialog.dispose();
        });

        signupDialog.setLocationRelativeTo(this);
        signupDialog.setVisible(true);
    }

    private void signupUser() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String role = (String) roleComboBox.getSelectedItem();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO users (first_name, last_name, email, role, username, password) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setString(3, email);
                stmt.setString(4, role);
                stmt.setString(5, username);
                stmt.setString(6, password);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Signup successful! You can now log in.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginSignup::new);
    }
}
