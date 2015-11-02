
package koodi.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;
import javax.persistence.Entity;

@Entity
public class Answer extends AbstractPersistable<Long> {
    
    private Integer userId;
    private Integer answerOptionId;
    private String answerTime;

    public Integer getUserId() {
        return userId;
    }

    public Integer getAnswerOptionId() {
        return answerOptionId;
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setAnswerOptionId(Integer answerOptionId) {
        this.answerOptionId = answerOptionId;
    }

    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }
       
}
