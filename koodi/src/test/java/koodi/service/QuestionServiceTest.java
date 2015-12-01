package koodi.service;

import java.util.List;
import koodi.Main;
import koodi.domain.AnswerOption;
import koodi.domain.Question;
import koodi.domain.QuestionSeries;
import koodi.repository.AnswerOptionRepository;
import koodi.repository.QuestionRepository;
import koodi.repository.QuestionSeriesRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class QuestionServiceTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionSeriesRepository questionSeriesRepository;
    
    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    private Question question1;
    private Question question2;
    private Question question3;
    private QuestionSeries qs1;
    private QuestionSeries qs2;

    private int existingQuestions;

    @Before
    public void setUp() {
        
        if(questionRepository.findAll() == null)
            existingQuestions = 0;
        else
            existingQuestions = questionRepository.findAll().size();

        qs1 = new QuestionSeries();
        qs1.setTitle("Testisarja 1");
        qs1.setOrderNumber(1);
        questionSeriesRepository.save(qs1);

        qs2 = new QuestionSeries();
        qs2.setTitle("Testisarja 2");
        qs2.setOrderNumber(2);
        questionSeriesRepository.save(qs2);

        question1 = new Question();
        question1.setTitle("Question1");
        question1.setOrderNumber(1);
        question1.setInfo("Question 1 info");
        question1.setProgrammingLanguage("java");
        question1.setCode("javakoodi");
        question1.setQuestionSeries(qs1);
        questionRepository.save(question1);
        existingQuestions += 1;

        question2 = new Question();
        question2.setTitle("Question2");
        question2.setOrderNumber(2);
        question2.setInfo("Question 2 info");
        question2.setProgrammingLanguage("java2");
        question2.setCode("javakoodi2");
        question2.setQuestionSeries(qs2);
        questionRepository.save(question2);
        existingQuestions += 1;

        question3 = new Question();
        question3.setTitle("Question3");
        question3.setOrderNumber(2);
        question3.setInfo("Question 3 info");
        question3.setProgrammingLanguage("java3");
        question3.setCode("javakoodi3");
        //question3.setQuestionSeries(qs2);
        //questionRepository.save(question3);
    }

    @After
    public void tearDown() {
        Question[] questions = {question1, question2, question3};
        for(Question q : questions){
            List<AnswerOption> options = q.getAnswerOptions();
            if(options != null){
                for(AnswerOption option : options){
                    option.setQuestion(null);
                    answerOptionRepository.save(option);
                }
            }
        }        
        questionRepository.delete(question1);
        questionRepository.delete(question2);
        questionRepository.delete(question3);
        questionSeriesRepository.delete(qs1);
        questionSeriesRepository.delete(qs2);
    }

//    @Test
//    public void defaultQuestionsExist() {
//        assertEquals(existingQuestions, questionRepository.count());
//    }

    @Test
    public void newQuestionIsSaved() {
        questionService.save(question3);
        assertEquals(existingQuestions + 1, questionRepository.count());
    }

    @Test
    public void findQuestionSeries() {
        assertEquals(1, questionService.findByQuestionSeries(qs1).size());
    }

    @Test
    public void postingNewQuestionWorks() {
        assertEquals(existingQuestions, questionRepository.count());

        question3.setQuestionSeries(qs1);
        String rightAnswer = "correctOne";
        String wrongsAnswers = "wrong1;wrong2;wrong3;wrong4";

        questionService.postNewExercise(question3, rightAnswer, wrongsAnswers);

        assertEquals(existingQuestions + 1, questionRepository.count());

        assertEquals("Question3", questionRepository.findAll().get(existingQuestions).getTitle());
        assertEquals("Question 3 info", questionRepository.findAll().get(existingQuestions).getInfo());
        assertEquals("java3", questionRepository.findAll().get(existingQuestions).getProgrammingLanguage());
        assertEquals("javakoodi3", questionRepository.findAll().get(existingQuestions).getCode());
        
        for (AnswerOption option : questionRepository.findAll().get(existingQuestions).getAnswerOptions()) {
            if (option.getIsCorrect()) {
                assertEquals("correctOne", option.getAnswerText());
            } else {
                assertTrue(option.getAnswerText().matches("wrong[1-4]"));
            }
        }
    }

}
