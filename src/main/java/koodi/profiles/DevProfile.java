
package koodi.profiles;

import javax.annotation.PostConstruct;
import koodi.domain.AnswerOption;
import koodi.domain.Question;
import koodi.domain.User;
import koodi.repository.AnswerOptionRepository;
import koodi.repository.QuestionRepository;
import koodi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import java.util.ArrayList;
import java.util.List;
import koodi.domain.Achievement;
import koodi.domain.QuestionSeries;
import koodi.repository.AchievementRepository;
import koodi.service.QuestionSeriesService;

@Configuration
@Profile(value = {"dev", "default"})
public class DevProfile {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private QuestionSeriesService questionSeriesService;
    
    @Autowired
    private AnswerOptionRepository answerOptionRepository;
    
    @Autowired
    private AchievementRepository achievementRepository;
    
    @PostConstruct
    public void init(){
        User defUser = new User();
        defUser.setName("oletusadmin");
        defUser.setUsername("a");
        defUser.setPassword("a");
        defUser.setIsAdmin(true);
        defUser = userRepository.save(defUser);
        
        User defUser2 = new User();
        defUser2.setName("oletususer");
        defUser2.setUsername("b");
        defUser2.setPassword("b");
        defUser2.setIsAdmin(false);
        defUser2 = userRepository.save(defUser2);
        
        // create general achievements
        Achievement achievement1 = new Achievement();
        achievement1.setName("5 vastausta peräkkäin oikein");
        achievementRepository.save(achievement1);
        
        Achievement achievement2 = new Achievement();
        achievement2.setName("10 vastausta peräkkäin oikein");
        achievementRepository.save(achievement2);
        
        Achievement achievement3 = new Achievement();
        achievement3.setName("20 vastausta peräkkäin oikein");
        achievementRepository.save(achievement3);
        
        // create answer options
        AnswerOption option1 = new AnswerOption();
        option1.setAnswerText("testing");
        option1.setAnswerComment("test comment");
        option1.setIsCorrect(false);
        answerOptionRepository.save(option1);
        
        AnswerOption option2 = new AnswerOption();
        option2.setAnswerText("testing");
        option2.setAnswerComment("yup");
        option2.setIsCorrect(true);
        answerOptionRepository.save(option2);
        
        AnswerOption option3 = new AnswerOption();
        option3.setAnswerText("generic answer text");
        option3.setAnswerComment("Rock on");
        option3.setIsCorrect(true);
        answerOptionRepository.save(option3);
        
        AnswerOption option4 = new AnswerOption();
        option4.setAnswerText("generic answer text#4");
        option4.setAnswerComment("Nope");
        option4.setIsCorrect(false);
        answerOptionRepository.save(option4);        
                
        AnswerOption option5 = new AnswerOption();
        option5.setAnswerText("generic answer text#5");
        option5.setAnswerComment("Rock on");
        option5.setIsCorrect(true);
        answerOptionRepository.save(option5);
        
        AnswerOption option6 = new AnswerOption();
        option6.setAnswerText("generic answer text#6");
        option6.setAnswerComment("Nope");
        option6.setIsCorrect(false);
        answerOptionRepository.save(option6);
                
        AnswerOption option7 = new AnswerOption();
        option7.setAnswerText("generic answer text#7");
        option7.setAnswerComment("Rock on");
        option7.setIsCorrect(true);
        answerOptionRepository.save(option7);
        
        AnswerOption option8 = new AnswerOption();
        option8.setAnswerText("generic answer text#8");
        option8.setAnswerComment("Nope");
        option8.setIsCorrect(false);
        answerOptionRepository.save(option8);        
                
        AnswerOption option9 = new AnswerOption();
        option9.setAnswerText("generic answer text#9");
        option9.setAnswerComment("Rock on");
        option9.setIsCorrect(true);
        answerOptionRepository.save(option9);
        
        AnswerOption option10 = new AnswerOption();
        option10.setAnswerText("generic answer text#10");
        option10.setAnswerComment("Nope");
        option10.setIsCorrect(false);
        answerOptionRepository.save(option10);
        
        // create a question series
        QuestionSeries qs1 = new QuestionSeries();
        qs1.setTitle("Sarja 1");
        qs1.setOrderNumber(1);
        questionSeriesService.save(qs1);

        QuestionSeries qs2 = new QuestionSeries();
        qs2.setTitle("Sarja 2");
        qs2.setOrderNumber(2);
        questionSeriesService.save(qs2);

        QuestionSeries qs3 = new QuestionSeries();
        qs3.setTitle("Sarja 3");
        qs3.setOrderNumber(3);
        questionSeriesService.save(qs3);
        // --
        
        // create questions
        Question question1 = new Question();
        question1.setAnswerOptions(new ArrayList<>());
        question1.getAnswerOptions().add(option1);
        question1.getAnswerOptions().add(option2);
        question1.setCode("public static void main(String[] args) {\n   System.out.prinln()\n}");
        question1.setProgrammingLanguage("java");
        question1.setTitle("test question");
        question1.setInfo("test information");
        question1.setOrderNumber(1);
        question1.setQuestionSeries(qs1);
        questionRepository.save(question1);
        
        List<AnswerOption> options = question1.getAnswerOptions();
        for(AnswerOption o : options){
            o.setQuestion(question1);
            answerOptionRepository.save(o);
        }
        
        Question question2 = new Question();
        question2.setAnswerOptions(new ArrayList<>());
        question2.getAnswerOptions().add(option3);
        question2.getAnswerOptions().add(option4);
        question2.setCode("public static void main(String[] args) {\n   System.out.prinln(\"generic code as text here\")\n}");
        question2.setProgrammingLanguage("java");
        question2.setTitle("Code reading: the generic texts");
        question2.setInfo("This code reading is about pretty much nothing");
        question2.setOrderNumber(1);
        question2.setQuestionSeries(qs2);
        questionRepository.save(question2);
        // --
        options = question2.getAnswerOptions();
        for(AnswerOption o : options){
            o.setQuestion(question2);
            answerOptionRepository.save(o);
        }
        
        Question question3 = new Question();
        question3.setAnswerOptions(new ArrayList<>());
        question3.getAnswerOptions().add(option5);
        question3.getAnswerOptions().add(option6);
        question3.setCode("public static void main(String[] args) {\n   System.out.prinln()\n}");
        question3.setProgrammingLanguage("java");
        question3.setTitle("test question #3");
        question3.setInfo("test information");
        question3.setOrderNumber(3);
        question3.setQuestionSeries(qs1);
        questionRepository.save(question3);
        
        options = question3.getAnswerOptions();
        for(AnswerOption o : options){
            o.setQuestion(question3);
            answerOptionRepository.save(o);
        }
        
        Question question4 = new Question();
        question4.setAnswerOptions(new ArrayList<>());
        question4.getAnswerOptions().add(option7);
        question4.getAnswerOptions().add(option8);
        question4.setCode("public static void main(String[] args) {\n   System.out.prinln()\n}");
        question4.setProgrammingLanguage("java");
        question4.setTitle("test question #4");
        question4.setInfo("test information");
        question4.setOrderNumber(4);
        question4.setQuestionSeries(qs1);
        questionRepository.save(question4);
        
        options = question4.getAnswerOptions();
        for(AnswerOption o : options){
            o.setQuestion(question4);
            answerOptionRepository.save(o);
        }
        
        Question question5 = new Question();
        question5.setAnswerOptions(new ArrayList<>());
        question5.getAnswerOptions().add(option9);
        question5.getAnswerOptions().add(option10);
        question5.setCode("public static void main(String[] args) {\n   System.out.prinln()\n}");
        question5.setProgrammingLanguage("java");
        question5.setTitle("test question #5");
        question5.setInfo("test information");
        question5.setOrderNumber(1);
        question5.setQuestionSeries(qs1);
        questionRepository.save(question5);
        
        options = question5.getAnswerOptions();
        for(AnswerOption o : options){
            o.setQuestion(question5);
            answerOptionRepository.save(o);
        }
        
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
