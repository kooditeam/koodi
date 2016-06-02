package koodi.service;

import java.util.List;
import koodi.Main;
import koodi.domain.AnswerOption;
import koodi.domain.Question;
import koodi.domain.QuestionSeries;
import koodi.repository.AnswerOptionRepository;
import koodi.repository.QuestionRepository;
import koodi.repository.QuestionSeriesRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
        question3.setQuestionSeries(qs2);
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
        question3 = new Question();
        question3.setTitle("Testitehtävä");
        question3.setOrderNumber(3);
        question3.setCode("public void jotain(String[] args){\n   koodi();\n}");
        question3.setInfo("info");
        question3.setProgrammingLanguage("Cobol");
        question3.setQuestionSeries(qs1);
        JSONArray optionsArray = new JSONArray();
        JSONObject option1 = new JSONObject();
        option1.put("answerText", "public void jotain(String[] args){\n   vastaa();\n}");
        option1.put("answerComment", "tää on kommentti");
        option1.put("isCorrectAnswer", false);
        optionsArray.add(option1);
        JSONObject option2 = new JSONObject();
        option2.put("answerText", "public void jotain(String[] args){\n   vastaaOikein();\n}");
        option2.put("answerComment", "tää on paras");
        option2.put("isCorrectAnswer", true);
        optionsArray.add(option2);        
        
        questionService.postNewExercise(question3, optionsArray);

        assertEquals(existingQuestions + 1, questionRepository.count());
        List<Question> questions = questionRepository.findAll();
        Question lastQuestion = questions.get(questions.size() - 1);

        assertEquals("Testitehtävä", lastQuestion.getTitle());
        assertEquals("info", lastQuestion.getInfo());
        assertEquals("Cobol", lastQuestion.getProgrammingLanguage());
        assertEquals("public void jotain(String[] args){\n   koodi();\n}", lastQuestion.getCode());
        assertEquals(3, (int)lastQuestion.getOrderNumber());
        assertEquals("Testisarja 1", lastQuestion.getQuestionSeries().getTitle());
        
        assertEquals(2, lastQuestion.getAnswerOptions().size());
        int matchingAnswerTexts = 0;
        int matchingComments = 0;
        int matchingCorrects = 0;
        for(AnswerOption o : lastQuestion.getAnswerOptions()){
            if(o.getAnswerText().equals("public void jotain(String[] args){\n   vastaa();\n}") ||
                    o.getAnswerText().equals("public void jotain(String[] args){\n   vastaaOikein();\n}")){
                matchingAnswerTexts++;
            }
            if(o.getAnswerComment().equals("tää on kommentti") ||
                    o.getAnswerComment().equals("tää on paras")){
                matchingComments++;
            }
            if(o.getIsCorrect())
                matchingCorrects++;
        }
        assertEquals(2, matchingAnswerTexts);
        assertEquals(2, matchingComments);
        assertEquals(1, matchingCorrects);
        assertEquals(true, !lastQuestion.getAnswerOptions().get(0).getAnswerText().equals(lastQuestion.getAnswerOptions().get(1).getAnswerText()));
        assertEquals(true, !lastQuestion.getAnswerOptions().get(0).getAnswerComment().equals(lastQuestion.getAnswerOptions().get(1).getAnswerComment()));
        assertEquals(true, lastQuestion.getAnswerOptions().get(0).getIsCorrect() != (lastQuestion.getAnswerOptions().get(1).getIsCorrect()));
        
    }

}
