import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminUI extends JFrame {
    private JTextArea displayArea;
    private JButton viewUsersButton, deleteUserButton, viewQuestionsButton, deleteQuestionButton, deleteAnswerButton;
    private JTextField usernameField, questionIdField, answerIdField;

    public AdminUI() {
        setTitle("Admin - University Forum");
        setSize(600, 600);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Admin Label
        JLabel adminLabel = new JLabel("Admin Panel", SwingConstants.CENTER);
        adminLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(adminLabel, gbc);

        // Display Area
        displayArea = new JTextArea(15, 40);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        add(scrollPane, gbc);

        // Buttons & Fields
        gbc.gridwidth = 1;
        viewUsersButton = new JButton("View All Users");
        gbc.gridx = 0; gbc.gridy = 2;
        add(viewUsersButton, gbc);

        deleteUserButton = new JButton("Delete User");
        gbc.gridx = 1;
        add(deleteUserButton, gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Username to delete:"), gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);

        viewQuestionsButton = new JButton("View All Questions and Answers");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(viewQuestionsButton, gbc);

        gbc.gridwidth = 1;
        deleteQuestionButton = new JButton("Delete Question");
        gbc.gridx = 0; gbc.gridy = 5;
        add(deleteQuestionButton, gbc);

        questionIdField = new JTextField(15);
        gbc.gridx = 1;
        add(new JLabel("Question ID to delete:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6;
        add(questionIdField, gbc);

        deleteAnswerButton = new JButton("Delete Answer");
        gbc.gridx = 0; gbc.gridy = 7;
        add(deleteAnswerButton, gbc);

        answerIdField = new JTextField(15);
        gbc.gridx = 1;
        add(new JLabel("Answer ID to delete:"), gbc);
        gbc.gridx = 1; gbc.gridy = 8;
        add(answerIdField, gbc);

        // Action Listeners
        viewUsersButton.addActionListener(e -> viewUsers());
        deleteUserButton.addActionListener(e -> deleteUser());
        viewQuestionsButton.addActionListener(e -> viewQuestionsAndAnswers());
        deleteQuestionButton.addActionListener(e -> deleteQuestion());
        deleteAnswerButton.addActionListener(e -> deleteAnswer());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void viewUsers() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM users";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                displayArea.setText("");
                while (rs.next()) {
                    displayArea.append("ID: " + rs.getInt("id") + ", ");
                    displayArea.append("Name: " + rs.getString("first_name") + " " + rs.getString("last_name") + ", ");
                    displayArea.append("Username: " + rs.getString("username") + ", ");
                    displayArea.append("Role: " + rs.getString("role") + "\n");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteUser() {
        String username = usernameField.getText();
        try (Connection conn = DBConnection.getConnection()) {
            String query = "DELETE FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "User not found.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void viewQuestionsAndAnswers() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT q.id, q.username AS student, q.question, a.username AS professor, a.answer, a.id "
                    + "FROM questions q LEFT JOIN answers a ON q.id = a.question_id";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                displayArea.setText("");  // Clear the display area before updating
                while (rs.next()) {
                    displayArea.append("Question ID: " + rs.getInt("q.id") + "\n");
                    displayArea.append("Question by: " + rs.getString("student") + "\n");
                    displayArea.append("Question: " + rs.getString("question") + "\n");
                    String professorAnswer = rs.getString("answer");
                    if (professorAnswer != null) {
                        displayArea.append("Answer ID: " + rs.getInt("a.id") + "\n");
                        displayArea.append("Answered by Professor: " + rs.getString("professor") + "\n");
                        displayArea.append("Answer: " + professorAnswer + "\n");
                    } else {
                        displayArea.append("No answer yet.\n");
                    }
                    displayArea.append("------------------------------------------------------\n");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteQuestion() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "DELETE FROM questions WHERE id = ?";
            int questionId = Integer.parseInt(questionIdField.getText());
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, questionId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Question deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Question not found.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteAnswer() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "DELETE FROM answers WHERE id = ?";
            int answerId = Integer.parseInt(answerIdField.getText());
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, answerId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Answer deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Answer not found.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminUI::new);
    }
}
