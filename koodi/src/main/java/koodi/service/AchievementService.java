package koodi.service;

import java.util.ArrayList;
import java.util.List;
import koodi.domain.Achievement;
import koodi.domain.QuestionSeries;
import koodi.domain.User;
import koodi.repository.AchievementRepository;
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
        List<Achievement> userAchievements = user.getAchievements();
        Achievement entitledAchievement = checkShareOfCorrectsInSeries(currentSeries, user);
        if(entitledAchievement != null){
            List<User> entitledUsers = entitledAchievement.getAchievers();
            entitledUsers.add(user);
            entitledAchievement.setAchievers(entitledUsers);
            entitledAchievement = achievementRepository.save(entitledAchievement);
            userAchievements.add(entitledAchievement);
            user.setAchievements(userAchievements);
            user = userService.save(user);
        }
        // TODO: kun kysymys- ja vastausvaihtoehtoehdottaminen on mahdollista, tarkistetaan nämäkin
        
        return userAchievements;
    }
    
    private Achievement checkCorrectAnswersStreak(User user){
        return null;
    }
    
    private Achievement checkShareOfCorrectsInSeries(QuestionSeries currentSeries, User user){
        Achievement newAchievement = null;
	int numberOfQuestions = questionService.findByQuestionSeries(currentSeries).size();
	int numberOfCorrects = answerService.getCorrectsByUserIdAndQuestionSeriesId(user.getId(), currentSeries.getId()).size();
	float shareOfCorrects = numberOfCorrects / numberOfQuestions;
        String[] achievementNames = determineQuestionSeriesAchievementNames(currentSeries);
	if(!userHasAchievement(achievementNames[2], user) 
		&& Math.abs(shareOfCorrects - 1.0) < 0.001) {
		newAchievement = achievementRepository.findByName(achievementNames[2]);
	} else if(!userHasAchievement(achievementNames[1], user)
		&& shareOfCorrects >= 0.75) {
		newAchievement = achievementRepository.findByName(achievementNames[1]);
	} else if(!userHasAchievement(achievementNames[0], user)
		&& shareOfCorrects >= 0.5){
		newAchievement = achievementRepository.findByName(achievementNames[0]);
	}	
	return newAchievement;
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
    private String[] determineQuestionSeriesAchievementNames(QuestionSeries qs) {
	String[] names = new String[5];
	names[0] = qs.getTitle() + ": 50% oikein";
	names[1] = qs.getTitle() + ": 75% oikein";
	names[2] = qs.getTitle() + ": 100% oikein";
	names[3] = qs.getTitle() + ": ehdotettu kysymys";
	names[4] = qs.getTitle() + ": ehdotettu vastaus";
	return names;
    }
}
