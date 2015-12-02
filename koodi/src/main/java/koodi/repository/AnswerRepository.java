
package koodi.repository;

import java.util.List;
import koodi.domain.Answer;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepository extends BaseRepository<Answer> {
    
    public List<Answer> findAllByUserId(Long id);

    public List<Answer> findByUserId(Long userId);
    
    @Query("select a from Answer a join a.answerOption o join o.question q "
            + "where q.id = ?1")
    public List<Answer> findByQuestionId(Long questionId);

    @Query("select a from Answer a join a.answerOption o join o.question q "
            + "where a.user.id = ?1 "
            + "and q.questionSeries.id = ?2")
    public List<Answer> findByUserIdAndQuestionSeriesId(Long userId, Long questionSeriesId);    
}
