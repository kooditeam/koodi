package koodi.controller;

import koodi.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class UserControllerTest {
    
    private final String API_URI = "/kayttajat";
    
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
    public void kayttajatStatusIsOk() throws Exception {
        
    }
    
}
