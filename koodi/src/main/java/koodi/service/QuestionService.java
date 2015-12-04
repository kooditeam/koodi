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
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class QuestionService extends BaseService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    public List<Question> findAll() {
        return questionRepository.findByRemovedFalse();
    }

    public List<Question> findRemoved() {
        return questionRepository.findByRemovedTrue();
    }
    
    public List<Question> findByQuestionSeries(QuestionSeries questionSeries) {
        return questionRepository.findByRemovedFalseAndQuestionSeriesOrderByOrderNumberAsc(questionSeries);
    }

    public Question findById(Long id) {
        return questionRepository.findByIdAndRemovedFalse(id);
    }

    public void delete(Long id) {
        questionRepository.delete(id);
    }

    public Question save(Question question) {
        super.save(question, null);
        return questionRepository.save(question);
    }

    public void postNewExercise(Question question, String rightAnswer,
            String falseAnswers) {

        question = save(question);
        String[] falseOptionStrings = falseAnswers.split(";");

        List<AnswerOption> allOptions = parseFalseOptionStrings(falseOptionStrings, question);
        allOptions.add(parseCorrectAnswer(rightAnswer, question));

        saveOptionsInRandomOrder(question, allOptions);

        save(question);
    }

    private void saveOptionsInRandomOrder(Question question,
            List<AnswerOption> allOptions) {

        long seed = System.nanoTime();
        Collections.shuffle(allOptions, new Random(seed));
        for (AnswerOption option : allOptions) {
            answerOptionRepository.save(option);
        }
        question.setAnswerOptions(allOptions);
    }

    private List<AnswerOption> parseFalseOptionStrings(String[] falseOptionStrings, Question question) {

        List<AnswerOption> allFalseOptions = new ArrayList<>();

        for (String falseOptionString : falseOptionStrings) {
            AnswerOption falseOption = new AnswerOption();
            falseOption.setAnswerText(falseOptionString.trim());
            falseOption.setQuestion(question);
            allFalseOptions.add(falseOption);
        }
        return allFalseOptions;

    }

    private AnswerOption parseCorrectAnswer(String correctAnswer, Question question) {
        AnswerOption correct = new AnswerOption();
        correct.setAnswerText(correctAnswer.trim());
        correct.setQuestion(question);
        correct.setIsCorrect(true);
        return correct;
    }
}
