package koodi.service.parameterizer.questiongenerators;

import bsh.EvalError;
import bsh.Interpreter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import koodi.domain.Question;
import koodi.domain.AnswerOption;
import koodi.domain.ParameterizedQuestion;
import koodi.repository.ParameterizedQuestionRepository;
import koodi.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Additioner extends BaseGenerator {
    
    @Autowired
    private ParameterizedQuestionRepository parameterizedQuestionRepository;
    
    @Autowired
    private QuestionService questionService;

    @Override
    public void parameterize(Question question) {
        int amountOfParameters = question.getAmountOfParameters();
        System.out.println("the code at start is: " + question.getCode());

        for (int i = 1; i <= amountOfParameters; i++) {
            boolean parameterIsString = parameterIsToBeStringOrNot();
            String newCode = "";
            if (parameterIsString) {
                newCode = replaceParameterWithRandomString(question.getCode(), i);
            } else {
                newCode = replaceParameterWithRandomInt(question.getCode(), i);
            }
            question.setCode(newCode);
            System.out.println("the code is: " + question.getCode());
        }
        
        String correctAnswerText = getRightAnswerText(question.getCode());
        System.out.println("correctAnswerText at para method is: " + correctAnswerText);
        List<AnswerOption> answers = getWrongAnswers(correctAnswerText);
        
        answers.add(createRightAnswerOption(correctAnswerText));
        question.setAnswerOptions(answers);
        System.out.println("----------------------------------------------");
        System.out.println("repo is: " + parameterizedQuestionRepository);
        
        questionService.saveOptionsInRandomOrder(question, answers);
        parameterizedQuestionRepository.save(new ParameterizedQuestion(question));
        
    }
    
    private String replaceParameterWithRandomString(String code, int i) {
        return code.replace("@" + i, getRandomString());
    }
    
    private String replaceParameterWithRandomInt(String code, int i) {
        return code.replace("@" + i, "" + getRandomInt());
    }
    
    private AnswerOption createRightAnswerOption(String correctAnswerText) {
        return new AnswerOption(correctAnswerText, true);
    }

    public String getRightAnswerText(String code) {

        System.out.println("creating the right answer text, the code is: " + code);
        Object result = "";
        try {
            Interpreter interpreter = new Interpreter();
            result = interpreter.eval(code);
            System.out.println("res is: " + result.toString());
        } catch (EvalError e) {
            System.out.println("ran into an evalerror");
            return null;
        }
        System.out.println("returning: " + result.toString());
        return result.toString();
    }
    
    public List<AnswerOption> getWrongAnswers(String correctAnswerOption) {
        System.out.println("The correctAnswerOption is: " + correctAnswerOption);
        List<AnswerOption> wrongAnswerOptions = new ArrayList<>();
        try {
            int correctOption = Integer.parseInt(correctAnswerOption);
            int wrongOption1 = correctOption - 1;
            int wrongOption2 = correctOption - 2;
            int wrongOption3 = correctOption + 1;
            int wrongOption4 = correctOption + 2;
            int wrongOption5 = correctOption * 2;
            
            return createAnswerOptions(wrongOption1, wrongOption2, wrongOption3,
                    wrongOption4, wrongOption4, wrongOption5);
            
        } catch (NumberFormatException e) {
            String wrongOption1 = getStringMinusTheLastCharacter(correctAnswerOption);
            String wrongOption2 = getStringMinusTheFirstCharacter(correctAnswerOption);
            String wrongOption3 = getStringMinusFirstAndLastCharacter(correctAnswerOption);
            
            return createAnswerOptions(wrongOption1, wrongOption2, wrongOption3);
        }
        
    }
    
    private String getStringMinusTheLastCharacter(String word) {
        System.out.println("word is: " + word);
        return getStringWithoutWantedLetter(word, 0, word.length() - 2);
    }
    
    private String getStringMinusTheFirstCharacter(String word) {
        return getStringWithoutWantedLetter(word, 1, word.length() - 1);
    }
    
    private String getStringMinusFirstAndLastCharacter(String word) {
        return getStringWithoutWantedLetter(word, 1, word.length() - 2);
    }
    
    private String getStringWithoutWantedLetter(String word, int index1, int index2) {
        return word.substring(index1, index2);
    }
    
    public List<AnswerOption> createAnswerOptions(int... wrongOptions) {
        List<AnswerOption> wrongAnswerOptions = new ArrayList<>();
        
        for (int option : wrongOptions) {
            AnswerOption answerOption = new AnswerOption("" + option, false);
            
            wrongAnswerOptions.add(answerOption);
        }
        return wrongAnswerOptions;
    }
    
    private List<AnswerOption> createAnswerOptions(String... wrongOptions) {
        List<AnswerOption> wrongAnswerOptions = new ArrayList<>();
        
        for (String option : wrongOptions) {
            AnswerOption answerOption = new AnswerOption(option, false);
            
            wrongAnswerOptions.add(answerOption);
        }
        return wrongAnswerOptions;
    }

    private int getRandomInt() {
        return this.getRandom().nextInt(101);
    }

    private String getRandomString() {
        String string = "\"" +  UUID.randomUUID().toString().substring(0, 7) + "\"";
        return string;
    }

    private boolean parameterIsToBeStringOrNot() {
        int stringOrInt = this.getRandom().nextInt(2);
        if (parameterIsString(stringOrInt)) {
            return true;
        }
        return false;
    }

    private boolean parameterIsString(int parameterRandomizerValue) {
        if (parameterRandomizerValue == 1) {
            return true;
        }
        return false;
    }
}
