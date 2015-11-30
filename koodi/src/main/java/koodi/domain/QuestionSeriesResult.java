package koodi.domain;

import java.util.List;

public class QuestionSeriesResult {
    
    private Long id;
    private String title;
    private List<QuestionResult> questionResults;
    private int numberOfQuestions;
    private int numberOfAnswers;
    private int numberOfCorrects;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<QuestionResult> getQuestionResults() {
        return questionResults;
    }

    public void setQuestionResults(List<QuestionResult> questionResults) {
        this.questionResults = questionResults;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public int getNumberOfAnswers() {
        return numberOfAnswers;
    }

    public void setNumberOfAnswers(int numberOfAnswers) {
        this.numberOfAnswers = numberOfAnswers;
    }

    public int getNumberOfCorrects() {
        return numberOfCorrects;
    }

    public void setNumberOfCorrects(int numberOfCorrects) {
        this.numberOfCorrects = numberOfCorrects;
    }
    
}
