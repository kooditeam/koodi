
package koodi.service.parameterizer.questiongenerators;

import java.util.Random;
import koodi.domain.ParameterizedQuestion;
import koodi.domain.Question;
import koodi.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public abstract class BaseGenerator extends BaseService<ParameterizedQuestion> {
    
    private Random random;
    
    public BaseGenerator() {
        random = new Random();
    }
    
    public abstract void parameterize(Question question);
    
    protected Random getRandom() {
        return random;
    }
}
