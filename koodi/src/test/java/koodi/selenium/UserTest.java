
package koodi.selenium;

import java.util.UUID;
import koodi.Main;
import org.fluentlenium.adapter.FluentTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class UserTest extends FluentTest {

    @Value("${local.server.port}")
    private int serverPort;
    private WebDriver webDriver = new HtmlUnitDriver();

    private String getUrl() {
        return "http://localhost:" + serverPort;
    }

    @Override
    public WebDriver getDefaultDriver() {
        return webDriver;
    }

    @Test
    public void mainPageIsShownWithNoSpecifiedAddress() {
        goTo(getUrl());
        assertTrue(pageSource().contains("Koodihommia!"));
        assertEquals("Koodi", title());
    }
    
    @Test
    public void registeringWorks() {
        registerSuccessfully();
        
        assertTrue(pageSource().contains("Kirjaudu koodi-järjestelmään"));
        assertEquals("Koodi", title());
    }
    
    @Test
    public void registeringDoesNotWorkWithEmptyName() {
        registerWithEmptyName();
        
        assertTrue(pageSource().contains("Rekisteröidy"));
        assertTrue(pageSource().contains("Nimi ei saa olla tyhjä"));
    }
    
    @Test
    public void registeringDoesNotWorkWithEmptyUsername() {
        registerWithEmptyUsername();
        
        assertTrue(pageSource().contains("Rekisteröidy"));
        assertTrue(pageSource().contains("Käyttäjänimi ei saa olla tyhjä"));
    } 
    
    @Test
    public void registeringDoesNotWorkWithEmptyPassword() {
        registerWithEmptyPassword();
        
        assertTrue(pageSource().contains("Rekisteröidy"));
        assertTrue(pageSource().contains("Salasana ei saa olla tyhjä"));
    }
    
    @Test
    public void registeringDoesNotWorkWithEmptyPasswordConfirmation() {
        registerWithEmptyPasswordConfirmation();
        
        assertTrue(pageSource().contains("Rekisteröidy"));
        assertTrue(pageSource().contains("Salasanat eivät täsmää."));
    }
    
    @Test
    public void registeringDoesNotWorkWithAllValuesBeingEmpty() {
        registerWithAllEmptyValues();
        
        assertTrue(pageSource().contains("Rekisteröidy"));
        assertTrue(pageSource().contains("Nimi ei saa olla tyhjä"));
        assertTrue(pageSource().contains("Käyttäjänimi ei saa olla tyhjä"));
        assertTrue(pageSource().contains("Salasana ei saa olla tyhjä"));
    }
    
    @Test
    public void registeringDoesNotWorkTakenUsername() {
        registerWithTakenUsername();
        assertTrue(pageSource().contains("Käyttäjänimi on varattu - valitse toinen."));
    }
    
    @Test
    public void signingInAsAsdminWorks() {
        goTo(getUrl());
        click(find("a").get(2));
        
        assertTrue(pageSource().contains("Kirjaudu koodi-järjestelmään"));

        fill(find("#j_username")).with("a");
        fill(find("#j_password")).with("a");
       
        submit(find("form").first());
        
        assertTrue(pageSource().contains("Koodihommia!"));
        assertTrue(pageSource().contains("Käyttäjät"));
        assertTrue(pageSource().contains("Tehtävät"));
        assertTrue(pageSource().contains("oletusadmin"));
        assertTrue(pageSource().contains("Kirjaudu ulos"));
    }
    
    @Test
    public void signingInAfterRegisteringAsNormalUserWorks() {
        String username = registerSuccessfully(); 
        
        fill(find("#j_username")).with(username);
        fill(find("#j_password")).with("testPassword");
        
        submit(find("form").first());
        
        assertTrue(pageSource().contains("testName"));
        assertTrue(pageSource().contains("Kirjaudu ulos"));
        assertTrue(pageSource().contains("Tehtäväsarjat"));
        
        assertFalse(pageSource().contains("Käyttäjät"));
        assertFalse(pageSource().contains("Tehtavat"));      
    }
    
    private void registerWithTakenUsername() {
        register("testName", "a", "testPassword", "testPassword");
    }
    
    private void registerWithAllEmptyValues() {
        register("", "", "", "");
    }
    
    private String registerSuccessfully() {
        String username = getRandomUsername();
        register("testName", username, "testPassword", "testPassword");
        return username;
    }
    
    private void registerWithEmptyName() {
        register("", "testUsername", "testPassword", "testPassword");
    }
    
    private void registerWithEmptyUsername() {
        register("testName", "", "testPassword", "testPassword");
    }
    
    private void registerWithEmptyPassword() {
        register("testName", "testUsername", "", "testPassword");
    }
    
    private void registerWithEmptyPasswordConfirmation() {
        
        goTo(getUrl());
        click(find("a").get(1));

        fill(find("#name")).with("testName");
        fill(find("#username")).with("testUsername");
        fill(find("input[name=password]")).with("testPassword");
        fill(find("input[name=password2]")).with("");
        
        submit(find("form").first());
    }
    
    private void register(
            String name, String username, 
            String password, String password2) {
        
        goTo(getUrl());
        click(find("a").get(1));

        fill(find("#name")).with(name);
        fill(find("#username")).with(username);
        fill(find("input[name=password]")).with(password);
        fill(find("input[name=password2]")).with(password2);
        
        submit(find("form").first());
    }
    
    private String getRandomUsername() {
        String username = UUID.randomUUID().toString().substring(0, 10);
        return username;
    }
    
}
