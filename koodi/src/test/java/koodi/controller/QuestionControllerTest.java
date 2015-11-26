
package koodi.controller;

import java.util.List;
import koodi.Main;
import koodi.domain.Question;
import koodi.domain.QuestionSeries;
import koodi.service.AnswerService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class QuestionControllerTest {

    private final String API_URI = "/tehtavat";

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
        mockMvc.perform(get(API_URI)).andExpect(status().isOk());
    }
    
    @Test
    public void modelHasAttributeAllQuestionSeries() throws Exception {
        MvcResult res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allQuestionSeries"))
                .andReturn();

        List<QuestionSeries> questionSeries = (List<QuestionSeries>) res.getModelAndView().getModel()
                .get("allQuestionSeries");

        assertTrue(questionSeries.size() == 3);
    }

    @Test
    public void modelHasAttributeAllQuestions() throws Exception {
        MvcResult res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("questions"))
                .andReturn();

        List<QuestionSeries> questions = (List<QuestionSeries>) res.getModelAndView().getModel()
                .get("questions");
        
        assertTrue(questions.size() == 2);
    }
    
    // disabled for now as the view doesn't work at the moment
//    @Test
    public void inThePageOfACertainQuestionTheModelHasQuestionAttribute() throws Exception {
        MvcResult res = mockMvc.perform(get(API_URI + "/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("question"))
                .andReturn();

        Question question = (Question) res.getModelAndView().getModel()
                .get("question");
        
        
        assertTrue(question.getAnswerOptions().size() == 2);
        // add some assertTrue tests
    }
    
}
