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
import org.springframework.web.util.NestedServletException;

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
    
    @Test(expected = NestedServletException.class)
    public void notBeingAuthenticatedHasNoAccess() throws Exception {
        mockMvc.perform(get(API_URI));
    }
    
    @Test(expected = NestedServletException.class)
    public void notBeingAuthenticatedHasNoAccessToDeleteting() throws Exception {
        mockMvc.perform(post(API_URI + "/1/poista"));
    }
    
    @WithMockUser(username = "a", roles = {"USER"})
    @Test(expected = NestedServletException.class)
    public void nonAdminUsersDontHaveAccess() throws Exception {
        mockMvc.perform(get(API_URI));   
    }
    
    @WithMockUser(username = "a", roles = {"USER"})
    @Test(expected = NestedServletException.class)
    public void nonAdminUsersDontHaveAccessToDeleting() throws Exception {
        mockMvc.perform(post(API_URI + "/1/poista"));
    }
    
    @WithMockUser(username = "a", roles = {"USER"})
    @Test(expected = NestedServletException.class)
    public void nonAdminUsersDontHaveAccessToAddingUsers() throws Exception {
        mockMvc.perform(post(API_URI + "/lisaa")
                .param("name", "testName")
                .param("username", "testUsername")
                .param("password", "testPassword")
                .param("password2", "testPassword"))
                .andExpect(flash().attribute("message", "Uusi käyttäjä tallennettu!"))
                .andExpect(redirectedUrl(API_URI));     
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

        assertTrue(users.size() == userRepository.findAll().size());
    }
   
    @WithMockUser(username = "a", roles = {"ADMIN"})
    @Transactional
    @Test
    public void deletingUserWorks() throws Exception {

        int origUserCount = userRepository.findAll().size();
        assertFalse(userRepository.findAll().get(0).isRemoved());

        mockMvc.perform(post(API_URI + "/1/poista"))
                .andExpect(redirectedUrl(API_URI))
                .andExpect(flash().attribute("message", "Käyttäjä poistettu."));

        assertTrue(userRepository.count() == origUserCount);
        assertTrue(userRepository.findAll().get(0).isRemoved());
    }
    
    @WithMockUser(username = "a", roles = {"ADMIN"})
    @Transactional
    @Test
    public void addingUserWorks() throws Exception {
        
        int origUserCount = userRepository.findAll().size();
        
        mockMvc.perform(post(API_URI + "/lisaa")
                .param("name", "testName")
                .param("username", "testUsername")
                .param("password", "testPassword")
                .param("password2", "testPassword"))
                .andExpect(flash().attribute("message", "Uusi käyttäjä tallennettu!"))
                .andExpect(redirectedUrl(API_URI));
        
        assertTrue(userRepository.count() == origUserCount + 1);
        
        List<User> users = userRepository.findAll();
        assertEquals("testName", users.get(users.size() - 1).getName());
        assertEquals("testUsername", users.get(users.size() - 1).getUsername());
        assertEquals("testPassword", users.get(users.size() - 1).getPassword());
        
    }
    
    @WithMockUser(username = "a", roles = {"ADMIN"})
    @Test
    public void addingUserDoesNotWorkIfPassWordsDontMatch() throws Exception {
        
        assertNull(userRepository.findByUsername("testUserName"));
        int origUserCount = userRepository.findAll().size();
        mockMvc.perform(post(API_URI + "/lisaa")
                .param("name", "testName")
                .param("username", "testUsername")
                .param("password", "doesNotMatch")
                .param("password2", "testPassword"));
        assertTrue(userRepository.count() == origUserCount);
        assertNull(userRepository.findByUsername("testUserName"));
    }
    
    @WithMockUser(username = "a", roles = {"ADMIN"})
    @Test
    public void addingUserDoesNotWorkIfPassWordsDontMatchOtherWayAround() throws Exception {
        
        assertNull(userRepository.findByUsername("testUserName"));
        int origUserCount = userRepository.findAll().size();
        
        mockMvc.perform(post(API_URI + "/lisaa")
                .param("name", "testName")
                .param("username", "testUsername")
                .param("password", "testPassWord")
                .param("password2", "doesNotMatch"));
        
        assertNull(userRepository.findByUsername("testUserName"));
        assertTrue(userRepository.count() == origUserCount);
    }
    
    @WithMockUser(username = "a", roles = {"ADMIN"})
    @Test
    public void addingUserWithTakenUsernameDoesNotWork() throws Exception {
        
        assertNotEquals("a", userRepository.findByUsername("a"));
        int origUserCount = userRepository.findAll().size();
        
        mockMvc.perform(post(API_URI + "/lisaa")
                .param("name", "testName")
                .param("username", "a")
                .param("password", "testPassword")
                .param("password2", "testPassword"));
        
        assertNotEquals("a", userRepository.findByUsername("a"));
        assertTrue(userRepository.count() == origUserCount);
    }
    
    @WithMockUser(username = "a", roles = {"ADMIN"})
    @Test
    public void addingUserWithEmptyNameDoesNotWork() throws Exception {
        
        assertNull(userRepository.findByUsername("testUsername"));
        int origUserCount = userRepository.findAll().size();
        mockMvc.perform(post(API_URI + "/lisaa")
                .param("name", "")
                .param("username", "testUsername")
                .param("password", "testPassword")
                .param("password2", "testPassword"));
        
        assertNull(userRepository.findByUsername("testUsername"));
        assertTrue(userRepository.count() == origUserCount);
    }
    
    @WithMockUser(username = "a", roles = {"ADMIN"})
    @Test
    public void addingUserWithEmptyUsernameDoesNotWork() throws Exception {
        
        assertNull(userRepository.findByUsername(""));
        int origUserCount = userRepository.findAll().size();
        mockMvc.perform(post(API_URI + "/lisaa")
                .param("name", "testName")
                .param("username", "")
                .param("password", "testPassword")
                .param("password2", "testPassword"));
        
        assertNull(userRepository.findByUsername(""));
        assertTrue(userRepository.count() == origUserCount);
    }
    
    @WithMockUser(username = "a", roles = {"ADMIN"})
    @Test
    public void addingUserWithEmptyPasswordDoesNotWork() throws Exception {
        
        assertNull(userRepository.findByUsername("testUsername"));
        int origUserCount = userRepository.findAll().size();
        mockMvc.perform(post(API_URI + "/lisaa")
                .param("name", "testName")
                .param("username", "testUsername")
                .param("password", "")
                .param("password2", ""));
        
        assertNull(userRepository.findByUsername("testUsername"));
        assertTrue(userRepository.count() == origUserCount);
    }
    
    @WithMockUser(username = "a", roles = {"ADMIN"})
    @Test
    public void addingUserWithAllValuesBeingEmptyDoesNotWork() throws Exception {
        
        assertNull(userRepository.findByUsername(""));
        int origUserCount = userRepository.findAll().size();
        mockMvc.perform(post(API_URI + "/lisaa")
                .param("name", "")
                .param("username", "")
                .param("password", "")
                .param("password2", ""));
        
        assertNull(userRepository.findByUsername(""));
        assertTrue(userRepository.count() == origUserCount);
    }
    
    // registering tests
    
    @Transactional
    @Test
    public void registeringUserWorks() throws Exception {
        
        assertNull(userRepository.findByUsername("testUsername"));
        int origUserCount = userRepository.findAll().size();
        
        mockMvc.perform(post("/rekisteroidy")
                .param("name", "testName")
                .param("username", "testUsername")
                .param("password", "testPassword")
                .param("password2", "testPassword"))
                .andExpect(flash().attribute("message", "Tervetuloa käyttäjäksi!"))
                .andExpect(redirectedUrl("/login"));
        
        assertTrue(userRepository.count() == origUserCount + 1);
        
        assertNotNull(userRepository.findByUsername("testUsername"));
        assertEquals("testName", userRepository.findByUsername("testUsername").getName());
        assertEquals("testPassword", userRepository.findByUsername("testUsername").getPassword()); 
    }
    
    @Test
    public void registeringWithAllValuesBeingEmptyDoesNotWork() throws Exception {
        
        assertNull(userRepository.findByUsername(""));
        int origUserCount = userRepository.findAll().size();
        
        mockMvc.perform(post("/rekisteroidy")
                .param("name", "")
                .param("username", "")
                .param("password", "")
                .param("password2", ""));
        
        assertNull(userRepository.findByUsername(""));
        assertTrue(userRepository.count() == origUserCount);
    }
    
    @Test
    public void registeringWithEmptyUsernameDoesNotWork() throws Exception {
        
        assertNull(userRepository.findByUsername(""));
        int origUserCount = userRepository.findAll().size();
        
        mockMvc.perform(post("/rekisteroidy")
                .param("name", "testName")
                .param("username", "")
                .param("password", "testPassword")
                .param("password2", "testPassword"));
        
        assertNull(userRepository.findByUsername(""));
        assertTrue(userRepository.count() == origUserCount);
    }
    
    @Test
    public void registeringWithEmptyNameDoesNotWork() throws Exception {
        
        assertNull(userRepository.findByUsername("testUsername"));
        int origUserCount = userRepository.findAll().size();
        mockMvc.perform(post("/rekisteroidy")
                .param("name", "")
                .param("username", "testUsername")
                .param("password", "testPassword")
                .param("password2", "testPassword"));
        
        assertNull(userRepository.findByUsername("testUsername"));
        assertTrue(userRepository.count() == origUserCount);
    }
    
    @Test
    public void registeringWithEmptyPasswordDoesNotWork() throws Exception {
        
        assertNull(userRepository.findByUsername("testUsername"));
        int origUserCount = userRepository.findAll().size();
        
        mockMvc.perform(post("/rekisteroidy")
                .param("name", "testName")
                .param("username", "testUsername")
                .param("password", "")
                .param("password2", ""));
        
        assertNull(userRepository.findByUsername("testUsername"));
        assertTrue(userRepository.count() == origUserCount);
    }
    
    @Test
    public void registeringDoesNotWorkIfPassWordsDontMatch() throws Exception {
        
        assertNull(userRepository.findByUsername("testUsername"));
        int origUserCount = userRepository.findAll().size();
        
        mockMvc.perform(post("/rekisteroidy")
                .param("name", "testName")
                .param("username", "testUsername")
                .param("password", "doesNotMatch")
                .param("password2", "testPassword"));
        
        assertNull(userRepository.findByUsername("testUsername"));
        assertTrue(userRepository.count() == origUserCount);
    }
    
    @Test
    public void registeringDoesNotWorkIfPassWordsDontMatchOtherWayAround() throws Exception {
        
        assertNull(userRepository.findByUsername("testUsername"));
        int origUserCount = userRepository.findAll().size();
        
        mockMvc.perform(post("/rekisteroidy")
                .param("name", "testName")
                .param("username", "testUsername")
                .param("password", "testPassWord")
                .param("password2", "doesNotMatch"));
        
        assertNull(userRepository.findByUsername("testUsername"));
        assertTrue(userRepository.count() == origUserCount);
    }
    
    @Test
    public void registeringWithTakenUsernameDoesNotWork() throws Exception {
        
        assertNotNull(userRepository.findByUsername("a"));
        assertNotEquals("testName", userRepository.findByUsername("a"));
        int origUserCount = userRepository.findAll().size();
        mockMvc.perform(post("/rekisteroidy")
                .param("name", "testName")
                .param("username", "a")
                .param("password", "testPassword")
                .param("password2", "testPassword"));
        
        assertNotNull(userRepository.findByUsername("a"));
        assertNotEquals("testName", userRepository.findByUsername("a"));
        assertTrue(userRepository.count() == origUserCount);
    }

}
