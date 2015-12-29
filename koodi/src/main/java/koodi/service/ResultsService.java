package koodi.service;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import koodi.domain.Answer;
import koodi.domain.Question;
import koodi.domain.QuestionResult;
import koodi.domain.QuestionSeries;
import koodi.domain.QuestionSeriesResult;
import koodi.domain.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.init.ResourceReader;
import org.springframework.stereotype.Service;

@Service
public class ResultsService {
    
    @Autowired
    private UserService userService;
    @Autowired
    private QuestionSeriesService questionSeriesService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private AchievementService achievementService;

    public boolean isAllowedToView(Long id) {
        // admins are allowed to see everyone's results, norm users only their own
        User authUser = userService.getAuthenticatedUser();
        if(authUser == null){
            return false;
        }
        if(authUser.getIsAdmin() || authUser.getId().equals(id)){
            return true;
        }
        return false;        
    }
    
    public QuestionSeriesResult findResultsForUserAndQuestionSeries(Long userId, QuestionSeries qs){
            QuestionSeriesResult seriesResult = new QuestionSeriesResult();
            seriesResult.setTitle(qs.getTitle());
            seriesResult.setId(qs.getId());
            
            List<QuestionResult> questionResults = new ArrayList<>();
            List<Question> seriesQuestions = questionService.findByQuestionSeries(qs);
            List<Answer> questionSeriesAnswers = answerService.getAnswersByUserIdAndQuestionSeriesId(userId, qs.getId());
            seriesResult.setNumberOfQuestions(seriesQuestions.size());
            seriesResult.setNumberOfAnswers(questionSeriesAnswers.size());
            int numberOfCorrects = 0; 
            
            // loops through all questions in the series, looking for answers by the
            // requested user; if answer is found, result is set as "correct" or "incorrect";
            // otherwise the question is deemed "unanswered"
            for(Question q : seriesQuestions){
                QuestionResult questionResult = new QuestionResult();
                questionResult.setQuestionId(q.getId());
                questionResult.setTitle(q.getTitle());
                if(q.getOrderNumber() != null){
                    questionResult.setOrderNumber(q.getOrderNumber());
                }
                for(Answer a : questionSeriesAnswers){
                    if(a.getAnswerOption().getQuestion().getId().equals(q.getId())){
                        questionResult.setAnswerOptionId(a.getAnswerOption().getId());
                        questionResult.setComment(a.getAnswerOption().getAnswerComment());
                        if(a.getAnswerOption().getIsCorrect()){
                            questionResult.setResultText("Oikein!");
                            numberOfCorrects++;
                        } else {
                            questionResult.setResultText("Väärin...");
                        }
                    }
                }
                if(questionResult.getResultText() == null || questionResult.getResultText().length() == 0){
                    questionResult.setResultText("ei vastattu");
                }                 
                questionResults.add(questionResult);
            }
            seriesResult.setNumberOfCorrects(numberOfCorrects);
            seriesResult.setQuestionResults(questionResults);
            return seriesResult;
    }

    public List<QuestionSeriesResult> findAllResultsForUser(Long userId) {
        List<QuestionSeriesResult> questionSets = new ArrayList<>();
        List<QuestionSeries> allSeries = questionSeriesService.findAll();
        
        for(QuestionSeries qs : allSeries){            
            questionSets.add(findResultsForUserAndQuestionSeries(userId, qs));
        }        
        
        return questionSets;
    }

    public List<QuestionSeriesResult> findAllAnswersToAllQuestions() {
        List<QuestionSeriesResult> questionSets = new ArrayList<>();
        List<QuestionSeries> allSeries = questionSeriesService.findAll();
        
        for(QuestionSeries qs : allSeries){            
            QuestionSeriesResult seriesResult = new QuestionSeriesResult();
            seriesResult.setTitle(qs.getTitle());
            seriesResult.setId(qs.getId());
            
            Map<Question, List<Answer>> questionAnswers = new HashMap<>();
            List<Question> seriesQuestions = questionService.findByQuestionSeries(qs);
            for(Question q : seriesQuestions){                
                List<Answer> answers = answerService.getAnswersByQuestionId(q.getId());
                questionAnswers.put(q, answers);
            }
            seriesResult.setQuestionAnswers(questionAnswers);
            
            questionSets.add(seriesResult);
        }           
        
        return questionSets;
    }    

    public String getResultArray(Long userId, Long questionSeriesId) {
        QuestionSeries qs = questionSeriesService.findById(questionSeriesId);
        QuestionSeriesResult results = findResultsForUserAndQuestionSeries(userId, qs);
        
        JSONArray responseArray = new JSONArray();
        JSONArray resultArray = new JSONArray();
        JSONObject resultJSON;

        for(QuestionResult result : results.getQuestionResults()){
            resultJSON = new JSONObject();
            resultJSON.put("QuestionId", result.getQuestionId());
            resultJSON.put("AnswerOptionId", result.getAnswerOptionId());
            resultJSON.put("ResultText", result.getResultText());
            resultJSON.put("Comment", result.getComment());
            resultArray.add(resultJSON);
        }
        responseArray.add(resultArray);
        responseArray.add(achievementService.getAchievements(userService.findById(userId), qs));
        
        return responseArray.toJSONString();
    }
}
