
package koodi.domain;

import javax.persistence.Entity;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Question extends BaseModel {
    
    @NotNull(message = "Anna tehtäväsarja")
    @ManyToOne(targetEntity = QuestionSeries.class)
    private QuestionSeries questionSeries;
    @OneToMany(fetch=FetchType.EAGER)
    private List<AnswerOption> answerOptions;
    @NotNull(message = "Järjestys vaaditaan")
    private Integer orderNumber;
    @NotBlank(message = "Otsikko ei saa olla tyhjä")
    private String title;
    @Column(columnDefinition="varchar(10000)")
    private String info;
    @NotBlank(message = "Valitse ohjelmointikieli")
    private String programmingLanguage;
    @Column(columnDefinition="varchar(10000)")
    @NotBlank(message = "Koodi tarvitaan")
    private String code;

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
     
    public QuestionSeries getQuestionSeries() {
        return questionSeries;
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

    public void setQuestionSeries(QuestionSeries questionSeries) {
        this.questionSeries = questionSeries;
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
