
package koodi.domain;

import javax.persistence.Entity;
import java.util.List;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class AnswerOption extends BaseModel {
    
    @OneToMany
    private List<Answer> answers;
    @ManyToOne
    private Question question;
    private String answerText;
    private boolean isCorrect;

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getAnswerText() {
        return answerText;
    }

    public boolean isIsCorrect() {
        return isCorrect;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Answer> getAnswers() {
        return answers;
    }
    
}
