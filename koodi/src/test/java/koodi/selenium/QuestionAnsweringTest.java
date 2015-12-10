
package koodi.selenium;

import koodi.Main;
import org.fluentlenium.adapter.FluentTest;
import org.fluentlenium.core.domain.FluentWebElement;
import org.hibernate.cfg.AvailableSettings;
import static org.junit.Assert.assertEquals;
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
public class QuestionAnsweringTest extends FluentTest {

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
    public void answeringQuestionWorks() {
        loginAsJustRegisteredUser();
        assertTrue(pageSource().contains("oletususer"));
        
        find("a[href='/vastaukset/topic/1']").click();
        assertTrue(pageSource().contains("Vastaa tehtäviin"));
        assertTrue(pageSource().contains("Sarja 1"));
        assertTrue(pageSource().contains("test question"));
        
        assertEquals(2, find("input[type='radio']").size());
        assertEquals(1, find("input[type='submit']").size());
        findFirst("input[type='radio']").click();
        
        find("input[type='submit']").get(0).click();
//        click("button");
        
//        assertTrue(pageSource().contains("undefined"));
//        assertTrue(pageSource().contains("Väärin..."));
        assertTrue(pageSource().contains("test comment"));
    }
    
    private void login(String username, String password) {
        goTo(getUrl());
        click(find("a").get(2));

        assertTrue(pageSource().contains("Kirjaudu koodi-järjestelmään"));

        fill(find("#j_username")).with(username);
        fill(find("#j_password")).with(password);
        submit(find("form").first());
    }
    
    private void loginAsJustRegisteredUser() {
        login("b", "b");
    }

    private void loginAsAdmin() {
        login("a", "a");
    }
}
