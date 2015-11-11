
package koodi.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;
import javax.persistence.Entity;

@Entity
public class Answer extends BaseModel {
    
    private AnswerOption answerOption;
    private User user;
    
    public void setAnswerOption(AnswerOption answerOption) {
        this.answerOption = answerOption;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AnswerOption getAnswerOption() {
        return answerOption;
    }

    public User getQuestion() {
        return user;
    }
       
}
