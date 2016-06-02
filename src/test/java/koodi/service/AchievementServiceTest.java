package koodi.service;

import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import koodi.Main;
import koodi.domain.Achievement;
import koodi.domain.QuestionSeries;
import koodi.domain.User;
import koodi.repository.AchievementRepository;
import koodi.repository.UserRepository;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.MultipleFailureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
public class AchievementServiceTest {
    
    @Autowired
    private AchievementRepository achievementRepository;    
    @Autowired
    private AchievementService achievementService;  
    @Autowired
    private AnswerService answerService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionSeriesService questionSeriesService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    User defUser; 
    
    @Before
    public void setUp(){
        defUser = userService.findByUsername("def");
        if(defUser != null)
            userRepository.delete(defUser);

        defUser = new User();
        defUser.setName("def");
        defUser.setUsername("def");
        defUser.setPassword("p");
        defUser.setIsAdmin(false);
        userService.save(defUser);
    }
    
    @After
    public void tearDown(){
        userRepository.delete(defUser);
    }
    
    @Test
    public void canCreateAchievementsForQuestionSeries(){
  
        QuestionSeries qs = new QuestionSeries();
        qs.setTitle("achievementtestqs");
        qs.setOrderNumber(1);
        qs = questionSeriesService.save(qs);
        
        int correctAchievements = 0;
        String[] achievementNames = achievementService.determineQuestionSeriesAchievementNames(qs); 
        
        assertEquals("Number of named achievements is correct", 5, achievementNames.length);
        for(String name : achievementNames){
            Achievement a = achievementRepository.findByName(name);
            if(a != null && a.getQuestionSeries().getId() == qs.getId() && a.getName().equals(name)){
                correctAchievements++;
            }
        }
        assertEquals("Number of created achievements is correct", 
                achievementNames.length, correctAchievements);
        assertNotNull("Achievement set for QuestionSeries is populated", 
                qs.getAchievements());
        assertEquals("QuestionSeries achievement set has correct number of achievements",
                5, qs.getAchievements().size());  
        
        // tear down
        qs.setAchievements(new ArrayList<Achievement>());
        qs = questionSeriesService.save(qs);
        for(String name : achievementNames){
            Achievement a = achievementRepository.findByName(name);
            achievementRepository.delete(a);
        }        
        questionSeriesService.delete(qs.getId());
    }
    
    @Test
    public void canGetAchievementsForUser(){
        QuestionSeries qs = questionSeriesService.findById(1L);
        List<Achievement> achievements = achievementService.getAchievements(defUser, qs);
        
        
    }
}