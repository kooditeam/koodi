
package koodi.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Answer extends BaseModel {
    
    @ManyToOne
    private AnswerOption answerOption;
    @ManyToOne
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

    public User getUser() {
        return user;
    }
       
}
