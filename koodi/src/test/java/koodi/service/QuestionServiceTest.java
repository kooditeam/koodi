package koodi.service;

import java.util.ArrayList;
import koodi.Main;
import koodi.domain.Question;
import koodi.domain.QuestionSeries;
import koodi.repository.AnswerOptionRepository;
import koodi.repository.QuestionRepository;
import koodi.repository.QuestionSeriesRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
public class QuestionServiceTest {
    
    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private AnswerOptionRepository answerOptionRepository;
     
    @Autowired
    private QuestionSeriesRepository questionSeriesRepository;
    
    Question question1;
    Question question2;
    Question question3;
    QuestionSeries qs1;
    QuestionSeries qs2;
    
    @Before
    public void setUp(){
        
        qs1 = new QuestionSeries();
        qs1.setTitle("Testisarja 1");
        qs1.setOrderNumber(1);
        questionSeriesRepository.save(qs1);
        
        qs2 = new QuestionSeries();
        qs2.setTitle("Testisarja 2");
        qs2.setOrderNumber(2);
        questionSeriesRepository.save(qs2);
        
        question1 = new Question();
        question1.setAnswerOptions(new ArrayList<>());
        question1.setTitle("Question1");
        question1.setOrderNumber(1);
        question1.setInfo("Question 1 info");
        question1.setProgrammingLanguage("java");
        question1.setCode("javakoodi");
        question1.setQuestionSeries(qs1);
        questionRepository.save(question1);
        
        question2 = new Question();
        question2.setAnswerOptions(new ArrayList<>());
        question2.setTitle("Question2");
        question2.setOrderNumber(2);
        question2.setInfo("Question 2 info");
        question2.setProgrammingLanguage("java2");
        question2.setCode("javakoodi2");
        question2.setQuestionSeries(qs2);
        questionRepository.save(question2);
        
        question3 = new Question();
        question3.setAnswerOptions(new ArrayList<>());
        question3.setTitle("Question2");
        question3.setOrderNumber(2);
        question3.setInfo("Question 2 info");
        question3.setProgrammingLanguage("java2");
        question3.setCode("javakoodi2");
        //question3.setQuestionSeries(qs2);
        //questionRepository.save(question3);
    }
    
    @After
    public void tearDown(){
        questionRepository.delete(question1);
        questionRepository.delete(question2);
        questionRepository.delete(question3);
        questionSeriesRepository.delete(qs1);
        questionSeriesRepository.delete(qs2);
    }
    
    @Test
    public void defaultQuestionsExist(){
        Assert.assertEquals(4, questionRepository.findAll().size());
    }
    
    @Test
    public void newQuestionIsSaved() {
        questionService.save(question3);
        Assert.assertEquals(5, questionRepository.findAll().size());
    }
    
    @Test
    public void findQuestionSeries() {
        Assert.assertEquals(1, questionService.findByQuestionSeries(qs1).size());
    }
    
}