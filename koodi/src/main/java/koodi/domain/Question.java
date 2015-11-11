
package koodi.domain;

import javax.persistence.Entity;
import java.util.List;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Question extends BaseModel {
    
    @ManyToMany
    private List<QuestionSeries> questionSeries;
    @OneToMany
    private List<AnswerOption> answerOptions;
    private Integer order;
    private String title;
    private String info;
    private String programmingLanguage;
    private String code;

    public List<QuestionSeries> getQuestionSeries() {
        return questionSeries;
    }

    public Integer getOrder() {
        return order;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public String getCode() {
        return code;
    }

    public void setQuestionSeries(List<QuestionSeries> questionSeries) {
        this.questionSeries = questionSeries;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<AnswerOption> getAnswerOptions() {
        return answerOptions;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setAnswerOptions(List<AnswerOption> answerOptions) {
        this.answerOptions = answerOptions;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }
       
}
