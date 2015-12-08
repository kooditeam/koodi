package koodi.service;

import koodi.domain.Answer;
import koodi.domain.User;
import koodi.repository.AnswerRepository;
import koodi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import koodi.domain.AnswerOption;
import koodi.domain.TentativeAnswer;
import koodi.repository.AnswerOptionRepository;
import org.json.simple.JSONObject;

@Service
public class AnswerService extends BaseService<Answer> {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Autowired
    private UserRepository userRepository;

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

    public AnswerOption getAnswerOptionById(Long id) {
        return answerOptionRepository.findByIdAndRemovedFalse(id);
    }

    public String saveUsersAnswer(TentativeAnswer tentativeAnswer) {

        Answer answer = new Answer();
        Long answerOptionId = tentativeAnswer.getAnswerOptionId();

        setAnswerOptionToAnswer(answer, answerOptionId);

        answer = save(answer);
        AnswerOption chosenOption = answer.getAnswerOption();
        JSONObject resultObject = new JSONObject();
        resultObject.put("successValue", getResult(chosenOption));
        resultObject.put("comment", chosenOption.getAnswerComment());
        return resultObject.toJSONString();
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

}
