package koodi.service;

import java.util.ArrayList;
import java.util.Collections;
import koodi.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;
import koodi.domain.AnswerOption;
import koodi.domain.Question;
import koodi.domain.QuestionSeries;
import koodi.repository.AnswerOptionRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class QuestionService extends BaseService<Question> {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;
    
    public List<Question> findByQuestionSeries(QuestionSeries questionSeries) {
        return questionRepository.findByRemovedFalseAndQuestionSeriesOrderByOrderNumberAsc(questionSeries);
    }

    public void delete(Long id) {
        detachQuestion(id);
        questionRepository.delete(id);
    }

    public Question save(Question question) {
        super.save(question, null);
        return questionRepository.save(question);
    }

    public void postNewExercise(Question question, JSONArray answerOptionsData) {

        question = save(question);
        String[] elementNames;
        AnswerOption newOption;
        
        List<AnswerOption> allOptions = new ArrayList<AnswerOption>();
        for(int i = 0; i < answerOptionsData.size(); i++){
            newOption = new AnswerOption();
            JSONObject optionData = (JSONObject)answerOptionsData.get(i);
            newOption.setAnswerText((String)optionData.get("answerText"));
            newOption.setAnswerComment((String)optionData.get("answerComment"));
            newOption.setIsCorrect((boolean)optionData.get("isCorrectAnswer"));
            newOption.setQuestion(question);
            allOptions.add(newOption);
        }
        
        saveOptionsInRandomOrder(question, allOptions);

        save(question);
    }

    public void saveOptionsInRandomOrder(Question question,
            List<AnswerOption> allOptions) {

        long seed = System.nanoTime();
        Collections.shuffle(allOptions, new Random(seed));
        for (AnswerOption option : allOptions) {
            answerOptionRepository.save(option);
        }
        question.setAnswerOptions(allOptions);
    }

    private void detachQuestion(Long id) {
        Question question = super.findById(id);
        
        List<AnswerOption> answerOptions = question.getAnswerOptions();
        for(AnswerOption option : answerOptions){
            option.setQuestion(null);
            answerOptionRepository.save(option);
        }
    }
}
