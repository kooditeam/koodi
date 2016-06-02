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
import koodi.domain.Answer;
import koodi.domain.QuestionSeries;
import koodi.repository.AnswerRepository;
import koodi.service.AnswerService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.springframework.http.MediaType;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class QuestionAnsweringControllerTest {

    private final String API_URI = "/vastaukset";

    @Autowired
    private AnswerService answerService;
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }
    
    @After
    public void tearDown(){
        List<Answer> answers = answerRepository.findAll();
        for(Answer a : answers){
            answerRepository.delete(a);
        }
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

        assertTrue(questions.size() == 4);
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
    public void modelHasSixAttributes() throws Exception {
        mockMvc.perform(get(API_URI + "/topic/1"))
                .andExpect(status().isOk())
                .andExpect(model().size(6));
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
        JSONArray expectedResultArray = new JSONArray();
        JSONObject expectedResponseObject = new JSONObject();
        expectedResponseObject.put("successValue", 0);
        expectedResponseObject.put("comment", "test comment");
        expectedResultArray.add(expectedResponseObject);     
        JSONArray expectedAchievementArray = new JSONArray();
        expectedResultArray.add(expectedAchievementArray);
        
        MvcResult res = mockMvc.perform(post(API_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testData))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(expectedResultArray.toJSONString(), res.getResponse().getContentAsString());

    }

    @Transactional
    @Test
    public void postResponseIsCorrectWithRightAnswer() throws Exception {
        String testData = "{\"answerOptionId\":\"2\", \"questionId\":\"1\"}";
        JSONArray expectedResultArray = new JSONArray();
        JSONObject expectedResponseObject = new JSONObject();
        expectedResponseObject.put("successValue", 1);
        expectedResponseObject.put("comment", "yup");
        expectedResultArray.add(expectedResponseObject);     
        JSONArray expectedAchievementArray = new JSONArray();
        expectedResultArray.add(expectedAchievementArray);
        
        MvcResult res = mockMvc.perform(post(API_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testData))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(expectedResultArray.toJSONString(), res.getResponse().getContentAsString());

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
        assertTrue(answerService.findAll().isEmpty());
        
        String testData = "{\"answerOptionId\":\"1\", \"questionId\":\"1\"}";
        
        mockMvc.perform(post(API_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testData))
                .andExpect(status().isOk());

        assertTrue(answerService.findAll().size() == 1);       
        assertEquals("testing", answerService.findAll().get(0).getAnswerOption().getAnswerText());
        assertFalse(answerService.findAll().get(0).getAnswerOption().getIsCorrect());
       
    }
    
    @Transactional
    @Test
    public void postSavesAnswerCorrectlyWithTheRightOption() throws Exception {      
        assertTrue(answerService.findAll().isEmpty());
        
        String testData = "{\"answerOptionId\":\"3\", \"questionId\":\"2\"}";
        
        mockMvc.perform(post(API_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testData))
                .andExpect(status().isOk());
        
        assertTrue(answerService.findAll().size() == 1);       
        assertEquals("generic answer text", answerService.findAll().get(0).getAnswerOption().getAnswerText());
        assertTrue(answerService.findAll().get(0).getAnswerOption().getIsCorrect());      
    }

}

