package koodi.controller;

import java.util.List;
import koodi.Main;
import koodi.domain.Answer;
import koodi.domain.User;
import koodi.repository.UserRepository;
import koodi.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExcecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@TestExecutionListeners(listeners={ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExcecutionListener.class})
public class ResultsControllerTest {
    
    private final String API_URI = "/tulokset";
    private User defUser;
    private User defAdmin;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;
    
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        
        defUser = new User();
        defUser.setName("def");
        defUser.setUsername("def");
        defUser.setPassword("p");
        defUser.setIsAdmin(false);
        defUser = userService.save(defUser);
        
        defAdmin = new User();
        defAdmin.setName("defAdmin");
        defAdmin.setUsername("defAdmin");
        defAdmin.setPassword("p");
        defAdmin.setIsAdmin(false);
        defAdmin = userService.save(defAdmin);
    }
    
    @After
    public void tearDown(){        
        userRepository.delete(defUser);
        userRepository.delete(defAdmin);
    }
    
    @Test(expected = NestedServletException.class)
    public void notBeingAuthenticatedHasNoAccess() throws Exception {
        mockMvc.perform(get(API_URI + "/kayttajat/" + defUser.getId()));
    }
    
    @WithMockUser(username = "b", roles = {"USER"})
    //@Test
    public void otherUsersDontHaveAccess() throws Exception {
        MvcResult res = mockMvc.perform(get(API_URI + "/kayttajat/" + defUser.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMsg"))
                .andReturn(); 
        
        String errorMsg = (String)res.getModelAndView().getModel().get("errorMsg");
        assertEquals("ErrorMsg must contain a valid message", 
                "Sinulla ei ole oikeuksia katsella tämän käyttäjän tuloksia.", errorMsg);
    }
    
    @WithMockUser(username = "def", roles = {"USER"})
    @Test
    public void userSelfHasAccess() throws Exception {
        mockMvc.perform(get(API_URI + "/kayttajat/" + defUser.getId()))
                .andExpect(status().isOk());  
    }
    
    @WithMockUser(username = "defAdmin", roles = {"ADMIN"})
    @Test
    public void adminHasAccess() throws Exception {
        mockMvc.perform(get(API_URI + "/kayttajat/" + defUser.getId()))
                .andExpect(status().isOk());  
    }
    
    @WithMockUser(username = "def", roles = {"USER"})
    @Test
    public void modelHasAttributeQuestionSets() throws Exception {
        mockMvc.perform(get(API_URI + "/kayttajat/" + defUser.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("questionSets"))
                .andReturn();  
    }
}
