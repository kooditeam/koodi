
package koodi.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class ParameterizedQuestion extends BaseModel {

    @ManyToOne(targetEntity = QuestionSeries.class)
    private QuestionSeries questionSeries;
    
    @OneToMany(fetch=FetchType.EAGER)
    private List<AnswerOption> answerOptions;
    
    @Column(columnDefinition="varchar(10000)")
    private String info;
    
    @Column(columnDefinition="varchar(10000)")
    private String code;
    
    @ManyToOne(targetEntity=Question.class)
    private Question question;

    private Integer orderNumber;
    private String title;  
    private String programmingLanguage;
       
    private String type;
    private Integer amountOfParameters;

    public ParameterizedQuestion() {       
    }
    
    public ParameterizedQuestion(Question question) {
        questionSeries = question.getQuestionSeries();
        answerOptions = question.getAnswerOptions();
        info = question.getInfo();
        code = question.getCode();
        this.question = question;
        orderNumber = question.getOrderNumber();
        title = question.getTitle();
        programmingLanguage = question.getProgrammingLanguage();
        type = question.getType();
        amountOfParameters = question.getAmountOfParameters();
    }
    
    public QuestionSeries getQuestionSeries() {
        return questionSeries;
    }

    public List<AnswerOption> getAnswerOptions() {
        return answerOptions;
    }

    public String getInfo() {
        return info;
    }

    public String getCode() {
        return code;
    }

    public Question getQuestion() {
        return question;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public String getType() {
        return type;
    }

    public Integer getAmountOfParameters() {
        return amountOfParameters;
    }

    public void setQuestionSeries(QuestionSeries questionSeries) {
        this.questionSeries = questionSeries;
    }

    public void setAnswerOptions(List<AnswerOption> answerOptions) {
        this.answerOptions = answerOptions;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmountOfParameters(Integer amountOfParameters) {
        this.amountOfParameters = amountOfParameters;
    }   
    
}
