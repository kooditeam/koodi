package koodi.controller;

import koodi.Main;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import javax.transaction.Transactional;
import koodi.domain.QuestionSeries;
import koodi.service.AnswerService;
import org.springframework.http.MediaType;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class QuestionAnsweringControllerTest {

    private final String API_URI = "/vastaukset";

    @Autowired
    private AnswerService answerService;

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void statusOk() throws Exception {
        mockMvc.perform(get(API_URI + "/topic/1")).andExpect(status().isOk());
    }

    @Test
    public void modelHasAttributeAllQuestionSeries() throws Exception {
        MvcResult res = mockMvc.perform(get(API_URI + "/topic/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allQuestionSeries"))
                .andReturn();

        List<QuestionSeries> questionSeries = (List<QuestionSeries>) res.getModelAndView().getModel()
                .get("allQuestionSeries");

        assertTrue(questionSeries.size() == 3);
    }

    @Test
    public void modelHasAttributeAllQuestions() throws Exception {
        MvcResult res = mockMvc.perform(get(API_URI + "/topic/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("questions"))
                .andReturn();

        List<QuestionSeries> questions = (List<QuestionSeries>) res.getModelAndView().getModel()
                .get("questions");

        assertTrue(questions.size() == 1);
    }
    
    @Test
    public void modelHasAttributeQuestionSeries() throws Exception {
        MvcResult res = mockMvc.perform(get(API_URI + "/topic/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("questionSeries"))
                .andReturn();

        QuestionSeries questionSeries = (QuestionSeries) res.getModelAndView().getModel()
                .get("questionSeries");       

        assertEquals("Sarja 1", questionSeries.getTitle());
    }

    @Test
    public void modelHasAttributeDoesNotContainRandomAttributes() throws Exception {
        MvcResult res = mockMvc.perform(get(API_URI + "/topic/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("sjsj"))
                .andReturn();

        List<QuestionSeries> questionSeries = (List<QuestionSeries>) res.getModelAndView().getModel()
                .get("sjsj");

        assertNull(questionSeries);
    }

    @Test
    public void modelHasThreeAttributes() throws Exception {
        mockMvc.perform(get(API_URI + "/topic/1"))
                .andExpect(status().isOk())
                .andExpect(model().size(3));
    }

    @Transactional
    @Test
    public void postStatusOk() throws Exception {
        String testData = "{\"answerOptionId\":\"1\", \"questionId\":\"1\"}";
        mockMvc.perform(post(API_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testData))
                .andExpect(status().isOk());

    }

    @Transactional
    @Test
    public void postResponseTypeIsApplicationJson() throws Exception {
        String testData = "{\"answerOptionId\":\"1\", \"questionId\":\"1\"}";
        MvcResult res = mockMvc.perform(post(API_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testData))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("application/json", res.getResponse().getContentType());
    }

    @Transactional
    @Test
    public void postResponseTypeIsNotTextHtml() throws Exception {
        String testData = "{\"answerOptionId\":\"1\", \"questionId\":\"1\"}";
        MvcResult res = mockMvc.perform(post(API_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testData))
                .andExpect(status().isOk())
                .andReturn();
        assertNotEquals("text/html", res.getResponse().getContentType());

    }

    @Transactional
    @Test
    public void postResponseIsCorrectWithWrongAnswer() throws Exception {
        String testData = "{\"answerOptionId\":\"1\", \"questionId\":\"1\"}";
        MvcResult res = mockMvc.perform(post(API_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testData))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("{\"result\": \"0\"}", res.getResponse().getContentAsString());

    }

    @Transactional
    @Test
    public void postResponseIsCorrectWithRightAnswer() throws Exception {
        String testData = "{\"answerOptionId\":\"2\", \"questionId\":\"1\"}";
        MvcResult res = mockMvc.perform(post(API_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testData))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("{\"result\": \"1\"}", res.getResponse().getContentAsString());

    }

    @Test
    public void postingNothingReturnsBadRequestState() throws Exception {
        mockMvc.perform(post(API_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postingWithNoSpecifiedTypeReturnsUnsupportedMediaTypeState() throws Exception {
        mockMvc.perform(post(API_URI))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Transactional
    @Test
    public void postSavesAnswerCorrectlyWithTheWrongOption() throws Exception {      
        assertTrue(answerService.getAllAnswers().isEmpty());
        
        String testData = "{\"answerOptionId\":\"1\", \"questionId\":\"1\"}";
        
        mockMvc.perform(post(API_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testData))
                .andExpect(status().isOk());

        assertTrue(answerService.getAllAnswers().size() == 1);       
        assertEquals("testing", answerService.getAllAnswers().get(0).getAnswerOption().getAnswerText());
        assertFalse(answerService.getAllAnswers().get(0).getAnswerOption().getIsCorrect());
       
    }
    
    @Transactional
    @Test
    public void postSavesAnswerCorrectlyWithTheRightOption() throws Exception {      
        assertTrue(answerService.getAllAnswers().isEmpty());
        
        String testData = "{\"answerOptionId\":\"3\", \"questionId\":\"2\"}";
        
        mockMvc.perform(post(API_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testData))
                .andExpect(status().isOk());
        
        assertTrue(answerService.getAllAnswers().size() == 1);       
        assertEquals("generic answer text", answerService.getAllAnswers().get(0).getAnswerOption().getAnswerText());
        assertTrue(answerService.getAllAnswers().get(0).getAnswerOption().getIsCorrect());      
    }

}

