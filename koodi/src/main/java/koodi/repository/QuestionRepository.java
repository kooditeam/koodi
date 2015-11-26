
package koodi.repository;

import java.util.List;
import koodi.domain.Question;
import koodi.domain.QuestionSeries;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long>{
    List<Question> findByQuestionSeries(QuestionSeries questionSeries);
}
