
package koodi.domain;

import javax.persistence.Entity;
import java.util.List;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class AnswerOption extends BaseModel {
    
    @OneToMany
    private List<Answer> answers;
    @ManyToOne
    //@NotNull(message = "Kysymys vaaditaan")
    // creating test data is difficult if NotNull
    private Question question;
    @NotBlank(message = "Vastaus ei voi olla tyhjä")
    private String answerText;
    private String answerComment;
    @NotNull(message = "Valitse kyllä tai ei")
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

    public boolean getIsCorrect() {
        return isCorrect;
    }

    public String getAnswerComment() {
        return answerComment;
    }

    public void setAnswerComment(String answerComment) {
        this.answerComment = answerComment;
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
