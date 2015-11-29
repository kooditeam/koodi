
package koodi.service;

import koodi.domain.Answer;
import koodi.domain.User;
import koodi.repository.AnswerRepository;
import koodi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnswerService extends BaseService{
    
    @Autowired
    private AnswerRepository answerRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public Answer save(Answer answer){        
        super.save(answer, null);
        // sets the student giving the answer as the user who created/edited the answer
        User student = null; 
        if(answer.getEditedById() != null){
            student = userRepository.findOne(answer.getEditedById());
        } else if (answer.getCreatedById() != null){
            student = userRepository.findOne(answer.getCreatedById());
        }
        answer.setUser(student);    
        
        answer = answerRepository.save(answer);     
        return answer;
    }
    
    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }
    
    public Answer getAnswerById(Long id) {
        return answerRepository.findOne(id);
    }
    
    public List<Answer> getAnswersByUserId(Long id){
        return answerRepository.findByUserId(id);
    }
    
    public List<Answer> getAnswersByUserIdAndQuestionSeriesId(Long userId, Long questionSeriesId){
        return answerRepository.findByUserIdAndQuestionSeriesId(userId);
    }
}
