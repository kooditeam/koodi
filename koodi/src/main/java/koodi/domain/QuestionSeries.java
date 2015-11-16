
package koodi.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;


@Entity
public class QuestionSeries extends BaseModel {
    
    @ManyToMany(mappedBy="questionSeries")
    private List<Question> questions;
    private String title;
    private Integer orderNumber;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }
    
    
}
