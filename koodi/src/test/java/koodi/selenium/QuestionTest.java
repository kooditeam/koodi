
package koodi.selenium;

import koodi.Main;
import org.fluentlenium.adapter.FluentTest;
import org.fluentlenium.core.domain.FluentWebElement;
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
public class QuestionTest extends FluentTest {

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

    //@Test
    public void addingQuestionWorks() {
        loginAsAdmin();
        click(find("a", 2));

        assertTrue(pageSource().contains("Lisää tehtävä"));
        assertTrue(pageSource().contains("Kaikki tehtävät"));

        fillSelect("#series").withIndex(0);
        fill("#order").with("1");
        fill("#questionTitle").with("new question series header test");
        fill("#questionInfo").with("new question series description test");
        fillSelect("#selectLanguage").withValue("java");
        fill("#questionCode").with("public static void main(String[] args) {\n   "
                + "System.out.println(\new question series code test\");\n}");
        fill("#correctAnswer").with("new question series right answer option test");
        fill("#falseAnswers").with("\"falseAnswer1\";\"falseAnswer2\";\"falseAnswer3\"");

        submit(find("form").first());

        assertTrue(pageSource().contains("new question series header test"));
        assertTrue(pageSource().contains("new question series description test"));
        assertTrue(pageSource().contains("public static void main(String[] args) {"));
        assertTrue(pageSource().contains("System.out.println(\new question series code test\");"));
        assertTrue(pageSource().contains("}"));
        assertTrue(pageSource().contains("new question series right answer option test"));
        assertTrue(pageSource().contains("\"falseAnswer1\""));
        assertTrue(pageSource().contains("\"falseAnswer2\""));
        assertTrue(pageSource().contains("falseAnswer3"));       
    }
    
    @Test
    public void seeingQuestionSeriesWorks() {
        registerSuccessfully();
        loginAsJustRegisteredUser();

        click(find("a", 5));
        
        assertTrue(pageSource().contains("Vastaa tehtäviin"));
        assertTrue(pageSource().contains("Sarja 1"));
        assertTrue(pageSource().contains("test question"));
        assertTrue(pageSource().contains("test information"));
        assertTrue(pageSource().contains("public static void main(String[] args) {"));
        assertTrue(pageSource().contains("System.out.prinln()"));
        assertTrue(pageSource().contains("}"));
        assertTrue(pageSource().contains("testing"));
    }
    
    // create a test to check that the user can answer the questions and see the results
    // at the moment not possible since the system doesn't find the css and javascript files
    
    private void registerSuccessfully() {
        register("testName", "testUsername", "testPassword", "testPassword");
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
    
    private void login(String username, String password) {
        goTo(getUrl());
        click(find("a").get(2));

        assertTrue(pageSource().contains("Kirjaudu koodi-järjestelmään"));

        fill(find("#j_username")).with("a");
        fill(find("#j_password")).with("a");
        submit(find("form").first());
    }
    
    private void loginAsJustRegisteredUser() {
        login("testUsername", "testPassword");
    }

    private void loginAsAdmin() {
        login("a", "a");
    }
}
