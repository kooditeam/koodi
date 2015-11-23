package koodi.controller;

import koodi.Main;
import koodi.domain.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import koodi.domain.QuestionSeries;
import org.springframework.http.MediaType;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class QuestionAnsweringControllerTest {
    
    private final String API_URI = "/vastaukset";
    
    @Autowired
    private WebApplicationContext webAppContext;
    
    private MockMvc mockMvc;
    
    @Before
    public void setUp(){
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
        
        List<QuestionSeries> questionSeries = (List) res.getModelAndView().getModel()
                .get("allQuestionSeries");
        
        assertTrue(questionSeries.size() == 3);
    }     
    
    @Test
    public void modelHasAttributeAllQuestions() throws Exception {
        MvcResult res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("questions"))
                .andReturn();
        
        List<QuestionSeries> questionSeries = (List) res.getModelAndView().getModel()
                .get("questions");
        
        assertTrue(questionSeries.size() == 2);
    }
    
    @Test
    public void modelHasAttributeDoesNotContainRandomAttributes() throws Exception {
        MvcResult res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("sjsj"))
                .andReturn();
        
        List<QuestionSeries> questionSeries = (List) res.getModelAndView().getModel()
                .get("sjsj");
        
        assertNull(questionSeries);
    }
    
    @Test
    public void modelHasTwoAttributes() throws Exception {
        mockMvc.perform(get(API_URI))
                .andExpect(status().isOk())
                .andExpect(model().size(2));
    }
    
//    @Test
//    public void modelHasAttribute() throws Exception {
//        MvcResult res = mockMvc.perform(post(API_URI))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andReturn();
//
//        // oletetaan, että kontrolleri asettaa listan Message-tyyppisiä olioita
//        // modeliin
//
////        List<Message> messages = (List) res.getModelAndView().getModel().get("messages");
//
//        // tarkista lista
//    }
    
    
}
