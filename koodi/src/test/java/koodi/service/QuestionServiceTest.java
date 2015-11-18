package koodi.service;

import koodi.Main;
import koodi.repository.QuestionRepository;
import org.junit.After;
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
    
    @Before
    public void setUp(){
        
    }
    
    @After
    public void tearDown(){
        
    }
    
    @Test
    public void dummyTest(){
        
    }
}