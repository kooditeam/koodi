package koodi.service;

import java.util.ArrayList;
import koodi.Main;
import koodi.domain.Question;
import koodi.repository.AnswerOptionRepository;
import koodi.repository.QuestionRepository;
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
     
    Question question1;
    
    @Before
    public void setUp(){
        
        question1 = new Question();
        question1.setAnswerOptions(new ArrayList<>());
        question1.setTitle("Question1");
        question1.setOrderNumber(1);
        question1.setInfo("Question 1 info");
        question1.setProgrammingLanguage("java");
        question1.setCode("javakoodi");
    }
    
    @After
    public void tearDown(){
        questionRepository.delete(question1);
    }
    
    @Test
    public void defaultQuestionsExist(){
        Assert.assertEquals(2, questionRepository.findAll().size());
    }
    
    @Test
    public void newQuestionIsSaved() {
        questionService.save(question1);
        Assert.assertEquals(3, questionRepository.findAll().size());
    }
}