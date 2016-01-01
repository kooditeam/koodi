package koodi.service;

import java.util.ArrayList;
import java.util.List;
import koodi.domain.Achievement;
import koodi.domain.Answer;
import koodi.domain.QuestionSeries;
import koodi.domain.User;
import koodi.repository.AchievementRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AchievementService extends BaseService<Achievement> {
    
    @Autowired
    private AchievementRepository achievementRepository;    
    @Autowired
    private AnswerService answerService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionSeriesService questionSeriesService;
    @Autowired
    private UserService userService;
    
    public List<Achievement> createAchievements(QuestionSeries qs){
	String[] names = determineQuestionSeriesAchievementNames(qs);
	List<Achievement> achievements = new ArrayList<Achievement>();	
	for(int i = 0; i < names.length; i++) {
		Achievement newAchievement = new Achievement();
		newAchievement.setName(names[i]);
		newAchievement.setQuestionSeries(qs);
		newAchievement = achievementRepository.save(newAchievement);
		achievements.add(newAchievement);
	}
	return achievements;
    }
    
    public List<Achievement> getAchievements(User user, QuestionSeries currentSeries){
        checkForNewAchievements(user, currentSeries);
        return user.getAchievements();
    }
    
    public JSONArray getAchievementsAsJSONArray(User user, QuestionSeries currentSeries){
        List<Achievement> userAchievements = getAchievements(user, currentSeries);
        JSONArray achievementArray = new JSONArray();
        JSONObject achievementObject;
        for(Achievement a : userAchievements){
            achievementObject = new JSONObject();
            achievementObject.put("Name", a.getName());
            Long questionSeriesId = a.getQuestionSeries() != null ? a.getQuestionSeries().getId() : null;
            achievementObject.put("QuestionSeriesId", questionSeriesId);
            achievementArray.add(achievementObject);
        }
        return achievementArray;
    } 
    
    private void checkForNewAchievements(User user, QuestionSeries currentSeries){
        checkShareOfCorrectsInSeries(currentSeries, user);
        checkCorrectAnswersStreak(user);        
        // TODO: kun kysymys- ja vastausvaihtoehtoehdottaminen on mahdollista, tarkistetaan nämäkin
    }
    
    private void addNewAchievement(User user, Achievement achievement){
        if(achievement == null)
            return;
            
        List<User> entitledUsers = achievement.getAchievers();
        entitledUsers.add(user);
        achievement.setAchievers(entitledUsers);
        achievement = achievementRepository.save(achievement);
        
        List<Achievement> userAchievements = user.getAchievements();
        userAchievements.add(achievement);
        user.setAchievements(userAchievements);
        user = userService.save(user);
    }
    
    private void checkCorrectAnswersStreak(User user){
        Achievement newAchievement = null;
        int numberOfCorrectSubsequentAnswers = answerService.getNumberOfCorrectSubsequentAnswers(user);
        String fiveStreakName = "5 vastausta peräkkäin oikein";
        String tenStreakName = "10 vastausta peräkkäin oikein";
        String twentyStreakName = "20 vastausta peräkkäin oikein";
        if(numberOfCorrectSubsequentAnswers >= 20 && !userHasAchievement(twentyStreakName, user)){
            addNewAchievement(user, achievementRepository.findByName(twentyStreakName));
        } else if(numberOfCorrectSubsequentAnswers >= 10 && !userHasAchievement(tenStreakName, user)) {
            addNewAchievement(user, achievementRepository.findByName(tenStreakName));
        } else if(numberOfCorrectSubsequentAnswers >= 5 && !userHasAchievement(fiveStreakName, user)) {
            addNewAchievement(user, achievementRepository.findByName(fiveStreakName));
        }        
    }
    
    private void checkShareOfCorrectsInSeries(QuestionSeries currentSeries, User user){
        Achievement newAchievement = null;
	int numberOfQuestions = questionService.findByQuestionSeries(currentSeries).size();
	int numberOfCorrects = answerService.getCorrectsByUserIdAndQuestionSeriesId(user.getId(), currentSeries.getId()).size();
	float shareOfCorrects = numberOfCorrects / numberOfQuestions;
        String[] achievementNames = determineQuestionSeriesAchievementNames(currentSeries);
        List<Achievement> allAchievements =  achievementRepository.findAll();
	if(!userHasAchievement(achievementNames[2], user) 
		&& Math.abs(shareOfCorrects - 1.0) < 0.001) {
            addNewAchievement(user, achievementRepository.findByName(achievementNames[2]));
	} else if(!userHasAchievement(achievementNames[1], user)
		&& (Math.abs(shareOfCorrects - 0.75) < 0.001 || shareOfCorrects > 0.75)) {
            addNewAchievement(user, achievementRepository.findByName(achievementNames[1]));
	} else if(!userHasAchievement(achievementNames[0], user)
		&& (Math.abs(shareOfCorrects - 0.5) < 0.001 || shareOfCorrects > 0.5)){
            addNewAchievement(user, achievementRepository.findByName(achievementNames[0]));
	}	
    }
    
    private Achievement checkSuggestedQuestion(QuestionSeries qs, User user){
        return null;
    }
    
    private Achievement checkSuggestedAnswerOption(QuestionSeries qs, User user){
        return null;
    }
    
    private boolean userHasAchievement(String achievementName, User user){
	boolean hasAchievement = false;
        if(user.getAchievements() != null){
            for(Achievement a : user.getAchievements()) {
                if(a.getName().equals(achievementName)) {
                        hasAchievement = true;
                        break;
                }
            }	
        }
	return hasAchievement;
    }
    
    public String[] determineQuestionSeriesAchievementNames(QuestionSeries qs) {
	String[] names = new String[5];
	names[0] = qs.getTitle() + ": 50% oikein";
	names[1] = qs.getTitle() + ": 75% oikein";
	names[2] = qs.getTitle() + ": 100% oikein";
	names[3] = qs.getTitle() + ": ehdotettu kysymys";
	names[4] = qs.getTitle() + ": ehdotettu vastaus";
	return names;
    }
}
