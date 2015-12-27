
package koodi.service.parameterizer;

import koodi.domain.Question;
import koodi.service.parameterizer.questiongenerators.Additioner;
import koodi.service.parameterizer.questiongenerators.BaseGenerator;

public class Parameterizer {
    
    private BaseGenerator additioner;
    
    public Parameterizer() {
        this.additioner = new Additioner();
    }
    
    public void parameterize(Question question) {
        
        if (question.getType().equals("addition/concatenation")) {
            additioner.parameterize(question);
        }
    }
}
