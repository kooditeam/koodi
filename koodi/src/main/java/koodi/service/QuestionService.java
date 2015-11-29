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
        return questionRepository.findAll();
    }

    public List<Question> findByQuestionSeries(QuestionSeries questionSeries) {
        return questionRepository.findByQuestionSeries(questionSeries);
    }

    public Question findById(Long id) {
        return questionRepository.findOne(id);
    }

    public void delete(Long id) {
        questionRepository.delete(id);
    }

    public void save(Question question) {
        super.save(question, null);
        questionRepository.save(question);
    }

    public void postNewExercise(Question question, String rightAnswer,
            String falseAnswers) {

        String[] falseOptionStrings = falseAnswers.split(";");

        List<AnswerOption> allOptions = parseFalseOptionStrings(falseOptionStrings);
        allOptions.add(parseCorrectAnswer(rightAnswer));

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

    private List<AnswerOption> parseFalseOptionStrings(String[] falseOptionStrings) {

        List<AnswerOption> allFalseOptions = new ArrayList<>();

        for (String falseOptionString : falseOptionStrings) {
            AnswerOption falseOption = new AnswerOption();
            falseOption.setAnswerText(falseOptionString.trim());

            allFalseOptions.add(falseOption);
        }
        return allFalseOptions;

    }

    private AnswerOption parseCorrectAnswer(String correctAnswer) {
        AnswerOption correct = new AnswerOption();
        correct.setAnswerText(correctAnswer.trim());
        correct.setIsCorrect(true);
        return correct;
    }
}
