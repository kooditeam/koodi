package koodi.service;

import koodi.Main;
import koodi.domain.Answer;
import koodi.domain.AnswerOption;
import koodi.domain.User;
import koodi.repository.AnswerOptionRepository;
import koodi.repository.AnswerRepository;
import koodi.repository.UserRepository;
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
public class AnswerServiceTest {
    
    @Autowired
    private AnswerService answerService;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private AnswerOptionRepository answerOptionRepository;
    @Autowired
    private UserRepository userRepository;
    Answer answer1;
    Answer answer2;
    AnswerOption answerOption1;
    AnswerOption answerOption2;
    int existingAnswers;
    User defUser;
    
    @Before
    public void setUp(){
        answerOption1 = answerOptionRepository.findOne(1L);
        answerOption2 = answerOptionRepository.findOne(2L);
        
        answer1 = new Answer();
        answer1.setAnswerOption(answerOption1);
        
        answer2 = new Answer();
        answer2.setAnswerOption(answerOption2);
        
        existingAnswers = answerRepository.findAll().size();
        defUser = userRepository.findOne(1L);
        
    }
    
    @After
    public void tearDown(){
        while(answerRepository.findAll().size() > existingAnswers){
            answerRepository.delete(answerRepository.findAll().get(answerRepository.findAll().size() - 1));
        }
    }
    
    @Test
    public void newAnswerIsSaved(){
        Answer savedAnswer = answerService.save(answer1);
        Assert.assertEquals(existingAnswers + 1, answerRepository.findAll().size());
        Assert.assertEquals(answerOption1.getId(), savedAnswer.getAnswerOption().getId());
        Assert.assertNotNull(savedAnswer.getUser());
        Assert.assertEquals(defUser.getId(), savedAnswer.getUser().getId());
        
        Answer savedAnswer2 = answerService.save(answer2);
        Assert.assertEquals(existingAnswers + 2, answerRepository.findAll().size());
        Assert.assertEquals(answerOption2.getId(), savedAnswer2.getAnswerOption().getId());
        Assert.assertNotNull(savedAnswer2.getUser());
        Assert.assertEquals(defUser.getId(), savedAnswer2.getUser().getId());
    }
    
//    @Test
//    public void existingAnswerIsUpdated(){
//        Answer savedAnswer = answerService.save(answer1);
//        savedAnswer.setAnswerOption(answerOption2);
//        savedAnswer = answerService.save(savedAnswer);
//        
//        Assert.assertEquals(existingAnswers, answerRepository.findAll().size());
//        Assert.assertEquals(answerOption2.getId(), savedAnswer.getAnswerOption().getId());
//        Assert.assertNotNull(savedAnswer.getUser());
//        Assert.assertEquals(defUser.getId(), savedAnswer.getUser().getId());
//    }
}
