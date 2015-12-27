
package koodi.repository;

import java.util.List;
import koodi.domain.AnswerOption;
import koodi.domain.Question;

public interface AnswerOptionRepository extends BaseRepository<AnswerOption> {
    
    public List<AnswerOption> findByRemovedFalseAndQuestion(Question question);
}
