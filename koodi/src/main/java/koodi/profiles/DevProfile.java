
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

@Configuration
@Profile(value = {"dev", "default"})
public class DevProfile {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
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
        
        AnswerOption option1 = new AnswerOption();
        option1.setAnswerText("testing");
        answerOptionRepository.save(option1);
        
        AnswerOption option2 = new AnswerOption();
        option2.setAnswerText("testing");
        answerOptionRepository.save(option2);
        
        Question question = new Question();
        question.setAnswerOptions(new ArrayList<>());
        question.getAnswerOptions().add(option1);
        question.getAnswerOptions().add(option2);
        
        question.setTitle("test question");
        question.setInfo("test information");
        
        questionRepository.save(question);
        
        
      
        
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
