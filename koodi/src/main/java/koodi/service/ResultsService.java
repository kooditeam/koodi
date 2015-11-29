package koodi.service;

import java.util.ArrayList;
import java.util.List;
import koodi.domain.Answer;
import koodi.domain.Question;
import koodi.domain.QuestionResult;
import koodi.domain.QuestionSeries;
import koodi.domain.QuestionSeriesResult;
import koodi.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResultsService extends BaseService{
    
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
                questionResult.setOrderNumber(q.getOrderNumber());
                for(Answer a : questionSeriesAnswers){
                    if(a.getAnswerOption().getQuestion().getId().equals(q.getId())){
                        if(a.getAnswerOption().getIsCorrect()){
                            questionResult.setQuestionResult("correct");
                            numberOfCorrects++;
                        } else {
                            questionResult.setQuestionResult("incorrect");
                        }
                    }
                }
                if(questionResult.getQuestionResult() == null){
                    questionResult.setQuestionResult("unanswered");
                }                 
                questionResults.add(questionResult);
            }
            seriesResult.setNumberOfCorrects(numberOfCorrects);
            seriesResult.setQuestionResults(questionResults);
            questionSets.add(seriesResult);
        }        
        
        return questionSets;
    }
    
}
