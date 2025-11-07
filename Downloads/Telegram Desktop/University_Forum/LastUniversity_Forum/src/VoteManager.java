import java.sql.*;

public class VoteManager {

    private Connection conn;

    public VoteManager(Connection conn) {
        this.conn = conn;
    }

    // Method to handle upvoting an answer
    public void upvoteAnswer(int userId, int answerId) throws SQLException {
        if (!hasUserVoted(userId, answerId)) {
            // Insert an upvote vote
            String query = "INSERT INTO votes (user_id, answer_id, vote_type) VALUES (?, ?, 'upvote')";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, answerId);
                stmt.executeUpdate();
            }

            // Update the answer's upvote count
            String updateAnswerQuery = "UPDATE answers SET upvotes = upvotes + 1 WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateAnswerQuery)) {
                stmt.setInt(1, answerId);
                stmt.executeUpdate();
            }

            System.out.println("Answer upvoted successfully!");
        } else {
            System.out.println("You have already voted on this answer.");
        }
    }

    // Method to handle downvoting an answer
    public void downvoteAnswer(int userId, int answerId) throws SQLException {
        if (!hasUserVoted(userId, answerId)) {
            // Insert a downvote vote
            String query = "INSERT INTO votes (user_id, answer_id, vote_type) VALUES (?, ?, 'downvote')";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, answerId);
                stmt.executeUpdate();
            }

            // Update the answer's downvote count
            String updateAnswerQuery = "UPDATE answers SET downvotes = downvotes + 1 WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateAnswerQuery)) {
                stmt.setInt(1, answerId);
                stmt.executeUpdate();
            }

            System.out.println("Answer downvoted successfully!");
        } else {
            System.out.println("You have already voted on this answer.");
        }
    }

    // Check if the user has already voted on the answer
    private boolean hasUserVoted(int userId, int answerId) throws SQLException {
        String query = "SELECT * FROM votes WHERE user_id = ? AND answer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, answerId);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If a record exists, user has voted
        }
    }

    // Retrieve current vote counts (upvotes, downvotes) for an answer
    public Answer getAnswerVotes(int answerId) throws SQLException {
        String query = "SELECT * FROM answers WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, answerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int upvotes = rs.getInt("upvotes");
                int downvotes = rs.getInt("downvotes");
                String professorName = rs.getString("user_id"); // Assuming professor name is stored as user_id
                String answerText = rs.getString("answer");
                return new Answer(answerId, rs.getInt("question_id"), answerText, upvotes, downvotes, professorName);
            }
        }
        return null;
    }
}
