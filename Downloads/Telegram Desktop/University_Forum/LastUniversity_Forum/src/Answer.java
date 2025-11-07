public class Answer {
    private int answerId;
    private int questionId;
    private String answerText;
    private int upvotes;
    private int downvotes;
    private String professorName;

    // Constructor, getters, and setters

    public Answer(int answerId, int questionId, String answerText, int upvotes, int downvotes, String professorName) {
        this.answerId = answerId;
        this.questionId = questionId;
        this.answerText = answerText;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.professorName = professorName;
    }

    public int getAnswerId() {
        return answerId;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public String getProfessorName() {
        return professorName;
    }

    public String getAnswerText() {
        return answerText;
    }
}
