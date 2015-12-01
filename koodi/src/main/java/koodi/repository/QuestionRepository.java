
package koodi.repository;

import java.util.List;
import koodi.domain.Question;
import koodi.domain.QuestionSeries;

public interface QuestionRepository extends BaseRepository<Question>{
    List<Question> findByQuestionSeries(QuestionSeries questionSeries);
}
