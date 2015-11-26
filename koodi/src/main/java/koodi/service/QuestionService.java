package koodi.service;

import koodi.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import koodi.domain.Question;
import koodi.domain.QuestionSeries;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class QuestionService extends BaseService {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    public List<Question> findAll() {
        return questionRepository.findAll();
    }
    
    public List<Question> findByQuestionSeries(QuestionSeries questionSeries) {
        return questionRepository.findByQuestionSeries(questionSeries);
    }
    
    public Question findById(Long id){
        return questionRepository.findOne(id);
    }
    
    public void delete(Long id){
        questionRepository.delete(id);
    }

    public void save(Question question) {
        super.save(question, null);
        questionRepository.save(question);
    }
}
