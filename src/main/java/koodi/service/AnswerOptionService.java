package koodi.service;

import java.util.List;
import koodi.domain.AnswerOption;
import koodi.domain.Question;
import koodi.repository.AnswerOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerOptionService extends BaseService<AnswerOption> {
    
    @Autowired
    private AnswerOptionRepository answerOptionRepository;
    
    @Autowired QuestionService questionService;
    
    public List<AnswerOption> findByQuestion(Question question) {
        return answerOptionRepository.findByRemovedFalseAndQuestion(question);
    }
    
    public AnswerOption save(AnswerOption answerOption) {
        super.save(answerOption, null);
        AnswerOption newAnswerOption = answerOptionRepository.save(answerOption);
        Question question = newAnswerOption.getQuestion();
        question.getAnswerOptions().add(newAnswerOption);
        questionService.save(question);
        return newAnswerOption;
    }
    
    public void delete(AnswerOption answerOption) {
        Question question = answerOption.getQuestion();
        question.getAnswerOptions().remove(answerOption);
        questionService.save(question);
        answerOptionRepository.delete(answerOption);
    }
    
}
