import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentUI extends JFrame {
    private JTextArea questionArea;
    private JButton postQuestionButton;
    private JTextArea displayArea;
    private String username;

    public StudentUI(String username) {
        this.username = username;
        setTitle("Student - University Forum");
        setLayout(new FlowLayout());
        setSize(600, 400);

        // Components for posting a question
        JLabel questionLabel = new JLabel("Post a Question:");
        questionArea = new JTextArea(5, 40);
        postQuestionButton = new JButton("Post Question");

        // Display area for questions and answers
        displayArea = new JTextArea(15, 40);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        add(questionLabel);
        add(questionArea);
        add(postQuestionButton);
        add(scrollPane);

        postQuestionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                postQuestion();
            }
        });

        loadQuestionsAndAnswers();  // Load questions and answers when the UI is shown

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void postQuestion() {
        String question = questionArea.getText();
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO questions (username, question) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, question);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Question posted successfully!");
                loadQuestionsAndAnswers();  // Reload questions and answers after posting
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadQuestionsAndAnswers() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT q.id, q.username, q.question, a.username AS professor, a.answer "
                    + "FROM questions q LEFT JOIN answers a ON q.id = a.question_id";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                displayArea.setText("");  // Clear the display area before updating
                while (rs.next()) {
                    displayArea.append("Question ID: " + rs.getInt("q.id") + "\n");
                    displayArea.append("Question by: " + rs.getString("q.username") + "\n");
                    displayArea.append("Question: " + rs.getString("question") + "\n");
                    String professorAnswer = rs.getString("answer");
                    if (professorAnswer != null) {
                        displayArea.append("Answered by Professor: " + rs.getString("professor") + "\n");
                        displayArea.append("Answer: " + professorAnswer + "\n");
                    }
                    displayArea.append("------------------------------------------------------\n");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
