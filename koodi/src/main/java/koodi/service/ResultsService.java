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
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<QuestionSeriesResult> findAllResultsForUser(Long id) {
        List<QuestionSeriesResult> questionSets = new ArrayList<>();
        List<QuestionSeries> allSeries = questionSeriesService.findAll();
        
        for(QuestionSeries qs : allSeries){            
            QuestionSeriesResult seriesResult = new QuestionSeriesResult();
            seriesResult.setTitle(qs.getTitle());
            seriesResult.setId(qs.getId());
            
            List<QuestionResult> questionResults = new ArrayList<>();
            List<Question> seriesQuestions = questionService.findByQuestionSeries(qs);
            List<Answer> questionSeriesAnswers = answerService.getAnswersByUserIdAndQuestionSeriesId(id, qs.getId());
            seriesResult.setNumberOfQuestions(seriesQuestions.size());
            seriesResult.setNumberOfAnswers(questionSeriesAnswers.size());
            int numberOfCorrects = 0; 
            
            // loops through all questions in the series, looking for answers by the
            // requested user; if answer is found, result is set as "correct" or "incorrect";
            // otherwise the question is deemed "unanswered"
            for(Question q : seriesQuestions){
                QuestionResult questionResult = new QuestionResult();
                questionResult.setTitle(q.getTitle());
                if(q.getOrderNumber() != null){
                    questionResult.setOrderNumber(q.getOrderNumber());
                }
                for(Answer a : questionSeriesAnswers){
                    if(a.getAnswerOption().getQuestion().getId().equals(q.getId())){
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
            questionSets.add(seriesResult);
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
}
