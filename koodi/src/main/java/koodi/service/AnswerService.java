
package koodi.service;

import koodi.domain.Answer;
import koodi.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerService extends BaseService{
    
    @Autowired
    private AnswerRepository answerRepository;
    
    public Answer save(Answer answer){
        super.save(answer, null);
        answer = answerRepository.save(answer);
     
        return answer;
    }            
}
