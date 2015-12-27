
package koodi.service;

import koodi.repository.ParameterizedQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParameterizedQuestionService {
    
    @Autowired
    private ParameterizedQuestionRepository parameterizedQuestionRepository;
}
