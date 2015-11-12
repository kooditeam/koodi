package koodi.service;

import koodi.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import koodi.domain.Question;

@Service
public class QuestionService extends BaseService {
    
    private QuestionRepository questionRepository;
    
    public List<Question> findAll() {
        return questionRepository.findAll();
    }
    
    public Question findById(Long id){
        return questionRepository.findOne(id);
    }
    
    public void delete(Long id){
        questionRepository.delete(id);
    }
}
