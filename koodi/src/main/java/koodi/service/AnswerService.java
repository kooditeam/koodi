package koodi.service;

import java.util.ArrayList;
import koodi.domain.Answer;
import koodi.domain.User;
import koodi.repository.AnswerRepository;
import koodi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import koodi.domain.Achievement;
import koodi.domain.AnswerOption;
import koodi.domain.QuestionSeries;
import koodi.domain.TentativeAnswer;
import koodi.repository.AnswerOptionRepository;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Service
public class AnswerService extends BaseService<Answer> {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AchievementService achievementService;

    public Answer save(Answer answer) {
        super.save(answer, null);
        // sets the student giving the answer as the user who created/edited the answer
        User student = null;
        if (answerWasEditedNotCreated(answer)) {
            student = findTheAnswersEditor(answer);
        } else if (answerWasJustCreated(answer)) {
            student = findTheAnswersCreator(answer);
        }
        answer.setUser(student);
        
        // if there are pre-existing answer by this user to this question, the
        // removed flag is set as true in the previous answer
        List<Answer> previousAnswers = answerRepository.findByUserIdAndQuestionId(
                student.getId(), 
                answer.getAnswerOption().getQuestion().getId());
        for(Answer a : previousAnswers){
            a.setRemoved(true);
            a.setEditedById(student.getId());
            a.setEditedOn(new DateTime());
            answerRepository.save(a);
        }
        
        answer = answerRepository.save(answer);

        return answer;
    }

    private User findTheAnswersEditor(Answer answer) {
        return userRepository.findOne(answer.getEditedById());
    }

    private User findTheAnswersCreator(Answer answer) {
        return userRepository.findOne(answer.getCreatedById());
    }

    private boolean answerWasEditedNotCreated(Answer answer) {
        return answer.getEditedById() != null;
    }

    private boolean answerWasJustCreated(Answer answer) {
        return answer.getCreatedById() != null;
    }
    
    public List<Answer> getAnswersByUserId(Long id){
        return answerRepository.findByUserId(id);
    }
    
    public List<Answer> getAnswersByQuestionId(Long id) {
        return answerRepository.findByQuestionId(id);
    }
    
    public List<Answer> getAnswersByUserIdAndQuestionSeriesId(Long userId, Long questionSeriesId){
        return answerRepository.findByUserIdAndQuestionSeriesId(userId, questionSeriesId);
    }
    
    public List<Answer> getCorrectsByUserIdAndQuestionSeriesId(Long userId, Long questionSeriesId) {
        List<Answer> allAnswers = getAnswersByUserIdAndQuestionSeriesId(userId, questionSeriesId);
        List<Answer> correctAnswers = new ArrayList<Answer>();
        for(Answer a : allAnswers){
            if(a.getAnswerOption().getIsCorrect()){
                correctAnswers.add(a);
            }
        }
        return correctAnswers;
    }


    public AnswerOption getAnswerOptionById(Long id) {
        return answerOptionRepository.findByIdAndRemovedFalse(id);
    }

    public String saveUsersAnswer(TentativeAnswer tentativeAnswer) {

        Answer answer = new Answer();
        Long answerOptionId = tentativeAnswer.getAnswerOptionId();
        setAnswerOptionToAnswer(answer, answerOptionId);
        answer = save(answer);
        
        JSONArray response = new JSONArray();
        AnswerOption chosenOption = answer.getAnswerOption();
        JSONObject resultObject = new JSONObject();
        resultObject.put("successValue", getResult(chosenOption));
        resultObject.put("comment", chosenOption.getAnswerComment());
        response.add(resultObject);
        
        // checks and retrieves users achievements
        QuestionSeries currentSeries = answer.getAnswerOption().getQuestion().getQuestionSeries();
        response.add(achievementService.getAchievementsAsJSONArray(answer.getUser(), currentSeries));
//        List<Achievement> userAchievements = achievementService.getAchievements(answer.getUser(), currentSeries);
//        JSONArray achievementArray = new JSONArray();
//        JSONObject achievementObject;
//        for(Achievement a : userAchievements){
//            achievementObject = new JSONObject();
//            achievementObject.put("Name", a.getName());
//            Long questionSeriesId = a.getQuestionSeries() != null ? a.getQuestionSeries().getId() : null;
//            achievementObject.put("QuestionSeriesId", questionSeriesId);
//            achievementArray.add(achievementObject);
//        }
//        response.add(achievementArray);
        
        return response.toJSONString();
    }

    private void setAnswerOptionToAnswer(Answer answer, Long answerOptionId) {
        AnswerOption option = getAnswerOptionById(answerOptionId);
        answer.setAnswerOption(option);
    }

    private int getResult(AnswerOption answerOption) {
        int result = 0;
        if (answerOption.getIsCorrect()) {
            result = 1;
        }
        return result;
    }

    public int getNumberOfCorrectSubsequentAnswers(User user) {
        int correctSubsequentAnswers = 0;
        List<Answer> userAnswers = answerRepository.findByUserIdOrderByCreatedOn(user.getId());
        for(int i = 0; i < userAnswers.size(); i++){
            Answer a = userAnswers.get(i);
            if(a.getAnswerOption().getIsCorrect()){
                correctSubsequentAnswers++;
            } else {
                break;
            }
        }
        return correctSubsequentAnswers;
    }

}
