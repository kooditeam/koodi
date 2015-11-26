package koodi.controller;

import java.util.List;
import javax.transaction.Transactional;
import koodi.Main;
import koodi.domain.QuestionSeries;
import koodi.domain.User;
import koodi.repository.UserRepository;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.context.support.WithSecurityContextTestExcecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@TestExecutionListeners(listeners={ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExcecutionListener.class})
public class UserControllerTest {

    private final String API_URI = "/kayttajat";

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webAppContext)
                .build();
    }

    @Test
    @WithMockUser(username = "a", roles = {"ADMIN"})
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
    @WithMockUser(username = "a", roles = {"ADMIN"})
    public void modelHasAttributeUsers() throws Exception {
        MvcResult res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andReturn();

        List<User> users = (List<User>) res.getModelAndView().getModel()
                .get("users");

        assertTrue(users.size() == 1);
        System.out.println("user is: " + users.get(0).getId());
    }

    @WithMockUser(username = "a", roles = {"ADMIN"})
    @Transactional
    @Test
    public void deletingUserWorks() throws Exception {

        assertTrue(userRepository.count() == 1);
        assertFalse(userRepository.findAll().get(0).isRemoved());

        mockMvc.perform(post(API_URI + "/1/poista"))
                .andExpect(redirectedUrl(API_URI))
                .andExpect(flash().attribute("message", "Käyttäjä poistettu."));

        assertTrue(userRepository.count() == 1);
        assertTrue(userRepository.findAll().get(0).isRemoved());
    }

    @WithMockUser(username = "a", roles = {"ADMIN"})
    @Transactional
    @Test
    public void addingUserWorks() throws Exception {
        
        assertTrue(userRepository.count() == 1);
        mockMvc.perform(post(API_URI + "/lisaa")
                .param("name", "testName")
                .param("username", "testUsername")
                .param("password", "testPassword")
                .param("password2", "testPassword"))
                .andExpect(flash().attribute("message", "Uusi käyttäjä tallennettu!"))
                .andExpect(redirectedUrl(API_URI));
        
        assertTrue(userRepository.count() == 2);
        
        assertEquals("testName", userRepository.findAll().get(1).getName());
        assertEquals("testUsername", userRepository.findAll().get(1).getUsername());
        assertEquals("testPassword", userRepository.findAll().get(1).getPassword());
        
    }
    
    @WithMockUser(username = "a", roles = {"ADMIN"})
    @Test
    public void addingUserDoesNotWorkIfPassWordsDontMatch() throws Exception {
        
        assertTrue(userRepository.count() == 1);
        mockMvc.perform(post(API_URI + "/lisaa")
                .param("name", "testName")
                .param("username", "testUsername")
                .param("password", "doesNotMatch")
                .param("password2", "testPassword"));
        assertTrue(userRepository.count() == 1);
    }
    
    @WithMockUser(username = "a", roles = {"ADMIN"})
    @Test
    public void addingUserWithTakenUsernameDoesNotWork() throws Exception {
        
        assertTrue(userRepository.count() == 1);
        mockMvc.perform(post(API_URI + "/lisaa")
                .param("name", "testName")
                .param("username", "a")
                .param("password", "testPassword")
                .param("password2", "testPassword"));
        assertTrue(userRepository.count() == 1);
    }
    
    @WithMockUser(username = "a", roles = {"ADMIN"})
    @Test
    public void addingUserWithEmptyNameDoesNotWork() throws Exception {
        
        assertTrue(userRepository.count() == 1);
        mockMvc.perform(post(API_URI + "/lisaa")
                .param("name", "")
                .param("username", "testUsername")
                .param("password", "testPassword")
                .param("password2", "testPassword"));
        assertTrue(userRepository.count() == 1);
    }
    
    @WithMockUser(username = "a", roles = {"ADMIN"})
    @Test
    public void addingUserWithEmptyUsernameDoesNotWork() throws Exception {
        
        assertTrue(userRepository.count() == 1);
        mockMvc.perform(post(API_URI + "/lisaa")
                .param("name", "testName")
                .param("username", "")
                .param("password", "testPassword")
                .param("password2", "testPassword"));
        assertTrue(userRepository.count() == 1);
    }
    
    @WithMockUser(username = "a", roles = {"ADMIN"})
    @Test
    public void addingUserWithEmptyPasswordDoesNotWork() throws Exception {
        
        assertTrue(userRepository.count() == 1);
        mockMvc.perform(post(API_URI + "/lisaa")
                .param("name", "testName")
                .param("username", "testUsername")
                .param("password", "")
                .param("password2", ""));
        assertTrue(userRepository.count() == 1);
    }
    
    @WithMockUser(username = "a", roles = {"ADMIN"})
    @Test
    public void addingUserWithAllValuesBeingEmptyDoesNotWork() throws Exception {
        
        assertTrue(userRepository.count() == 1);
        mockMvc.perform(post(API_URI + "/lisaa")
                .param("name", "")
                .param("username", "")
                .param("password", "")
                .param("password2", ""));
        assertTrue(userRepository.count() == 1);
    }
}
