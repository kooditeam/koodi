package koodi.service;

import java.util.List;
import koodi.Main;
import koodi.domain.Answer;
import koodi.domain.AnswerOption;
import koodi.domain.TentativeAnswer;
import koodi.domain.User;
import koodi.repository.AnswerOptionRepository;
import koodi.repository.AnswerRepository;
import koodi.repository.UserRepository;
import static org.junit.Assert.*;
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
    @Autowired
    private UserService userService;
    private Answer answer1;
    private Answer answer2;
    private AnswerOption answerOption1;
    private AnswerOption answerOption2;
    private int existingAnswers;
    private User defUser;

    @Before
    public void setUp() {
        answerOption1 = answerOptionRepository.findOne(1L);
        answerOption2 = answerOptionRepository.findOne(2L);

        answer1 = new Answer();
        answer1.setAnswerOption(answerOption1);

        answer2 = new Answer();
        answer2.setAnswerOption(answerOption2);

        existingAnswers = answerRepository.findAll().size();
        defUser = userRepository.findOne(1L);

    }

    @Test
    public void newAnswerIsSavedToRepository() {
        Answer savedAnswer = answerService.save(answer1);
        assertEquals(existingAnswers + 1, answerRepository.findAll().size());
        assertEquals(answerOption1.getId(), savedAnswer.getAnswerOption().getId());
        assertNotNull(savedAnswer.getUser());
        assertEquals(defUser.getId(), savedAnswer.getUser().getId());

        Answer savedAnswer2 = answerService.save(answer2);
        assertEquals(existingAnswers + 2, answerRepository.findAll().size());
        assertEquals(answerOption2.getId(), savedAnswer2.getAnswerOption().getId());
        assertNotNull(savedAnswer2.getUser());
        assertEquals(defUser.getId(), savedAnswer2.getUser().getId());
    }
    
    @Test
    public void canGetAnswersByUserIdAndQuestionSeriesId(){
        defUser = new User();
        defUser.setName("def");
        defUser.setUsername("def");
        defUser.setPassword("p");
        defUser.setIsAdmin(false);
        defUser = userService.save(defUser);
        
        User defUser2 = new User();
        defUser2.setName("def2");
        defUser2.setUsername("def2");
        defUser2.setPassword("p2");
        defUser2.setIsAdmin(false);
        defUser2 = userService.save(defUser2);
        
        Answer answer1 = new Answer();
        answer1.setAnswerOption(answerOptionRepository.findOne(1L));
        answer1.setUser(defUser);
        answer1.setCreatedById(defUser.getId());
        answer1 = answerRepository.save(answer1);
        
        Answer answer2 = new Answer();
        answer2.setAnswerOption(answerOptionRepository.findOne(3L));
        answer2.setUser(defUser);
        answer2.setCreatedById(defUser.getId());
        answer2 = answerRepository.save(answer2);
        
        Answer answer3 = new Answer();
        answer3.setAnswerOption(answerOptionRepository.findOne(3L));
        answer3.setUser(defUser2);
        answer3.setCreatedById(defUser2.getId());
        answer3 = answerRepository.save(answer3);
        
        List<Answer> answers = answerService
                .getAnswersByUserIdAndQuestionSeriesId(defUser.getId(), 
                        answerOptionRepository.findOne(3L).getQuestion().getQuestionSeries().getId());
        
        assertEquals("There should be only 1 Question in this QuestionSeries for this User", 1, answers.size());
        assertEquals("User information should match", defUser.getId(), answers.get(0).getUser().getId());
        assertEquals("AnswerOption should match", (Long)3L, answers.get(0).getAnswerOption().getId());
        
        userService.delete(defUser);
        userService.delete(defUser2);
        userService.delete(answer1);
        userService.delete(answer2);
        userService.delete(answer3);
    }

//    @Test
//    public void savingUsersAnswerWorksWithWrongAnswer() {
//        assertTrue(answerRepository.count() == existingAnswers);
//
//        TentativeAnswer tentativeAnswer = new TentativeAnswer();
//        tentativeAnswer.setAnswerOptionId(1L);
//        
//        String result = answerService.saveUsersAnswer(tentativeAnswer);
//
//        assertTrue(answerRepository.count() == existingAnswers + 1);
//
//        assertEquals("testing", answerRepository.findOne(new Long(existingAnswers + 1)).getAnswerOption()
//                .getAnswerText());
//        assertFalse(answerRepository.findOne(new Long(existingAnswers + 1)).getAnswerOption().getIsCorrect());
//
//        assertEquals("{\"result\": \"0\"}", result);
//    }
//    
//    @Test
//    public void savingUsersAnswerWorksWithRightAnswer() {
//        assertTrue(answerRepository.count() == existingAnswers);
//
//        TentativeAnswer tentativeAnswer = new TentativeAnswer();
//        tentativeAnswer.setAnswerOptionId(2L);
//        
//        String result = answerService.saveUsersAnswer(tentativeAnswer);
//
//        assertTrue(answerRepository.count() == existingAnswers + 1);
//        assertEquals("testing", answerRepository.findOne(new Long(existingAnswers + 1)).getAnswerOption()
//                .getAnswerText());
//        assertTrue(answerRepository.findOne(new Long(existingAnswers + 1)).getAnswerOption().getIsCorrect());
//        
//        assertEquals("{\"result\": \"1\"}", result);
//    }

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