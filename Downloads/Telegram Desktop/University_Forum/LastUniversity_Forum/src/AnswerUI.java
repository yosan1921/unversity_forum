import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

class AnswerUI extends JFrame {
    private JTextArea answerDisplay;
    private JButton upvoteButton, downvoteButton;
    private JLabel voteLabel;
    private int userId;
    private int answerId;
    private VoteManager voteManager;

    public AnswerUI(int userId, int answerId, VoteManager voteManager) {
        this.userId = userId;
        this.answerId = answerId;
        this.voteManager = voteManager;

        setTitle("Answer - Upvote/Downvote");
        setLayout(new FlowLayout());
        setSize(400, 300);

        answerDisplay = new JTextArea(5, 30);
        answerDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(answerDisplay);

        upvoteButton = new JButton("Upvote");
        downvoteButton = new JButton("Downvote");
        voteLabel = new JLabel("Votes: 0 Upvotes, 0 Downvotes");

        add(scrollPane);
        add(upvoteButton);
        add(downvoteButton);
        add(voteLabel);

        // Load answer and initial vote counts
        loadAnswerDetails();

        // Action Listeners for Upvote and Downvote
        upvoteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    voteManager.upvoteAnswer(userId, answerId);
                    loadAnswerDetails();  // Reload updated vote count
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        downvoteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    voteManager.downvoteAnswer(userId, answerId);
                    loadAnswerDetails();  // Reload updated vote count
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadAnswerDetails() {
        try {
            Answer answer = voteManager.getAnswerVotes(answerId);
            if (answer != null) {
                answerDisplay.setText(answer.getAnswerText());
                voteLabel.setText("Votes: " + answer.getUpvotes() + " Upvotes, " + answer.getDownvotes() + " Downvotes");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        // Assuming userId and answerId are passed from somewhere in the program
        int userId = 1;  // Example user
        int answerId = 1;  // Example answer
        VoteManager voteManager = new VoteManager(DBConnection.getConnection());
        new AnswerUI(userId, answerId, voteManager);
    }
}
