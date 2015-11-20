
package koodi.profiles;

import java.util.List;
import javax.annotation.PostConstruct;
import koodi.domain.AnswerOption;
import koodi.domain.Question;
import koodi.domain.User;
import koodi.repository.AnswerOptionRepository;
import koodi.repository.QuestionRepository;
import koodi.repository.UserRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import java.util.ArrayList;
import koodi.domain.QuestionSeries;
import koodi.repository.QuestionSeriesRepository;

@Configuration
@Profile(value = {"dev", "default"})
public class DevProfile {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private QuestionSeriesRepository questionSeriesRepository;
    
    @Autowired
    private AnswerOptionRepository answerOptionRepository;
    
    @PostConstruct
    public void init(){
        User defUser = new User();
        defUser.setName("oletuskäyttäjä");
        defUser.setEmail("a@a.com");
        defUser = userRepository.save(defUser);
//        defUser.setCreatedOn(DateTime.now());
//        defUser.setCreatedBy(defUser);
//        userRepository.save(defUser);
        
        // create a question
        AnswerOption option1 = new AnswerOption();
        option1.setAnswerText("testing");
        answerOptionRepository.save(option1);
        
        AnswerOption option2 = new AnswerOption();
        option2.setAnswerText("testing");
        option2.setIsCorrect(true);
        answerOptionRepository.save(option2);
        
        Question question1 = new Question();
        question1.setAnswerOptions(new ArrayList<>());
        question1.getAnswerOptions().add(option1);
        question1.getAnswerOptions().add(option2);
        question1.setCode("public static void main(String[] args) {\n   System.out.prinln()\n}");
        
        question1.setTitle("test question");
        question1.setInfo("test information");
        
        questionRepository.save(question1);
        // --
        
        // create another question
        AnswerOption option3 = new AnswerOption();
        option3.setAnswerText("generic answer text");
        option3.setIsCorrect(true);
        answerOptionRepository.save(option3);
        
        AnswerOption option4 = new AnswerOption();
        option4.setAnswerText("generic answer text2");
        answerOptionRepository.save(option4);
        
        Question question2 = new Question();
        question2.setAnswerOptions(new ArrayList<>());
        question2.getAnswerOptions().add(option3);
        question2.getAnswerOptions().add(option4);
        question2.setCode("public static void main(String[] args) {\n   System.out.prinln(\"generic code as text here\")\n}");
        
        question2.setTitle("Code reading: the generic texts");
        question2.setInfo("This code reading is about pretty much nothing");
        
        questionRepository.save(question2);
        // --
        
        // create a question series
        QuestionSeries qs1 = new QuestionSeries();
        qs1.setTitle("Sarja 1");
        qs1.setOrderNumber(1);
        questionSeriesRepository.save(qs1);
        // --
        
        // create another question series
        QuestionSeries qs2 = new QuestionSeries();
        qs2.setTitle("Sarja 2");
        qs2.setOrderNumber(2);
        questionSeriesRepository.save(qs2);
        // --
    }
}
/*
private Question question;
    private String answerText;

private List<AnswerOption> answerOptions;
    private Integer order;
    private String title;
    private String info;
    private String programmingLanguage;
    private String code;
*/
