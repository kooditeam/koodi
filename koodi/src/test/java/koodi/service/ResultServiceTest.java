package koodi.service;

import java.util.List;
import static org.junit.Assert.*;
import koodi.Main;
import koodi.domain.Answer;
import koodi.domain.Question;
import koodi.domain.QuestionSeries;
import koodi.domain.QuestionSeriesResult;
import koodi.domain.User;
import koodi.repository.AnswerOptionRepository;
import koodi.repository.AnswerRepository;
import koodi.repository.QuestionRepository;
import koodi.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
public class ResultServiceTest {
    
    @Autowired
    private ResultsService resultsService;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    User defUser;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionSeriesService questionSeriesService;
    @Autowired
    private AnswerOptionRepository answerOptionRepository;
    Answer answer1;
    Answer answer2;
    
    @Before
    public void setUp(){
        for(Answer a : answerRepository.findAll()){
            answerRepository.delete(a);
        }
        defUser = userService.findByUsername("def");
        if(defUser != null)
            userRepository.delete(defUser);

        defUser = new User();
        defUser.setName("def");
        defUser.setUsername("def");
        defUser.setPassword("p");
        defUser.setIsAdmin(false);
        userService.save(defUser);

        answer1 = new Answer();
        answer1.setAnswerOption(answerOptionRepository.findOne(1L));
        answer1.setUser(defUser);
        answer1.setCreatedById(defUser.getId());
        answer1 = answerRepository.save(answer1);
        
        answer2 = new Answer();
        answer2.setAnswerOption(answerOptionRepository.findOne(3L));
        answer2.setUser(defUser);
        answer2.setCreatedById(defUser.getId());
        answer2 = answerRepository.save(answer2);
        
    }
    
    @After
    public void tearDown(){
        answerRepository.delete(answer1);
        answerRepository.delete(answer2);
        userRepository.delete(defUser);
    }
    
    @Test
    public void canGetAllResultsForUser(){
        List<Answer> allAnswers = answerRepository.findAll();
        List<QuestionSeries> allQuestionSeries = questionSeriesService.findAll();
        List<Question> seriesQuestions;       
                
        List<QuestionSeriesResult> questionSets = resultsService.findAllResultsForUser(defUser.getId());
        assertEquals("Size of QuestionSeriesResult List should match number of QuestionSeries.", allQuestionSeries.size(), questionSets.size());
        for(QuestionSeries qs : allQuestionSeries){
            int indexInSet = -1;
            for (int i = 0; i < questionSets.size(); i++){
                if(questionSets.get(i).getId() == qs.getId()){
                    indexInSet = i;
                    break;
                }
            }
            assertTrue("All QuestionSeries should be included in the results.", indexInSet > -1); 
            assertEquals("QuestionSeries titles should match", qs.getTitle(), questionSets.get(indexInSet).getTitle());            

            seriesQuestions = questionService.findByQuestionSeries(qs);
            assertEquals("Number of Questions in QuestionSeries should match number of QuestionResults in QuestionSeriesResult", 
                    seriesQuestions.size(), questionSets.get(indexInSet).getQuestionResults().size());
            assertEquals("NumberOfQuestions in QuestionSeries should match number of QuestionResults in QuestionSeriesResult", 
                    seriesQuestions.size(), questionSets.get(indexInSet).getNumberOfQuestions());
            for(Question q : seriesQuestions){
                int indexInResults = -1;
                for(int j = 0; j < questionSets.get(indexInSet).getQuestionResults().size(); j++){
                    if(questionSets.get(indexInSet).getQuestionResults().get(j).getTitle().equals(q.getTitle())){
                        indexInResults = j;
                        break;
                    }
                }                
                assertTrue("All Questions should be included in the results.", indexInResults > -1); 
            }
            
            
        }
    }
    
}
