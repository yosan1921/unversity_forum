import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ProfessorUI extends JFrame {
    private JComboBox<String> questionComboBox;
    private JTextArea answerArea;
    private JButton postAnswerButton;
    private String username;

    public ProfessorUI(String username) {
        this.username = username;
        setTitle("Professor - University Forum");
        setLayout(new FlowLayout());
        setSize(600, 400);

        // Components for selecting a question and posting an answer
        JLabel questionLabel = new JLabel("Select a Question to Answer:");
        questionComboBox = new JComboBox<>();
        JLabel answerLabel = new JLabel("Post an Answer:");
        answerArea = new JTextArea(5, 40);
        postAnswerButton = new JButton("Post Answer");

        // Displaying the components
        add(questionLabel);
        add(questionComboBox);
        add(answerLabel);
        add(answerArea);
        add(postAnswerButton);

        postAnswerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                postAnswer();
            }
        });

        loadQuestions();  // Load questions when the UI is shown

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadQuestions() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM questions";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                questionComboBox.removeAllItems();  // Clear existing items
                while (rs.next()) {
                    questionComboBox.addItem(rs.getString("question"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void postAnswer() {
        String question = (String) questionComboBox.getSelectedItem();
        String answer = answerArea.getText();

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT id FROM questions WHERE question = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, question);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int questionId = rs.getInt("id");

                    query = "INSERT INTO answers (question_id, username, answer) VALUES (?, ?, ?)";
                    try (PreparedStatement stmtInsert = conn.prepareStatement(query)) {
                        stmtInsert.setInt(1, questionId);
                        stmtInsert.setString(2, username);
                        stmtInsert.setString(3, answer);
                        stmtInsert.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Answer posted successfully!");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
