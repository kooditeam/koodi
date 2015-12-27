package koodi.service.parameterizer.questiongenerators;

import bsh.EvalError;
import bsh.Interpreter;
import java.util.List;
import koodi.Main;
import koodi.domain.AnswerOption;
import koodi.domain.Question;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
public class AdditionerTest {

    @Autowired
    private Additioner additioner;

//    @Test
    public void parameterizingWorksAsAWhole() {
        Question question = new Question();
        question.setCode("@1+30+\"abc\"+(10+@2)");
        question.setType("addition/concatenation");
        question.setAmountOfParameters(2);

        assertNull(question.getAnswerOptions());
        additioner.parameterize(question);
        
    }
    @Test
    public void wrongAnswerOptionsAreMadeCorrectly() {
        List<AnswerOption> wrongAnswerOptions = additioner.getWrongAnswers("50abc20");
        assertNotNull(wrongAnswerOptions);
//        System.out.println("size is: " + wrongAnswerOptions.size());
    }

    @Test
    public void rightAnswerIsEvaluatedCorrectly() {
        String rightAnswerText = additioner.getRightAnswerText("20+30+\"abc\"+(10+10)");
//        System.out.println("the text is: " + rightAnswerText);
    }
}
