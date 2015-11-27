
package koodi.domain;

public class TentativeAnswer {
    private Long answerOptionId;
    private Long questionId;

    public Long getAnswerOptionId() {
        return answerOptionId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setAnswerOptionId(Long answerOptionId) {
        this.answerOptionId = answerOptionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
  
}
