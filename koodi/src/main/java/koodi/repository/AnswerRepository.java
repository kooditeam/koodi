
package koodi.repository;

import java.util.List;
import koodi.domain.Answer;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepository extends BaseRepository<Answer> {
    
    public List<Answer> findAllByUserId(Long id);

    public List<Answer> findByUserId(Long userId);
    
    @Query("select a from Answer a join a.answerOption o join o.question q "
            + "where q.id = ?1 "
            + "and a.removed = false")
    public List<Answer> findByQuestionId(Long questionId);

    @Query("select a from Answer a join a.answerOption o join o.question q "
            + "where a.user.id = ?1 "
            + "and q.questionSeries.id = ?2 "
            + "and a.removed = false")
    public List<Answer> findByUserIdAndQuestionSeriesId(Long userId, Long questionSeriesId);    
    
    @Query("select a from Answer a join a.answerOption o join o.question q "
            + "where a.user.id = ?1 "
            + "and q.id = ?2 "
            + "and a.removed = false")
    public List<Answer> findByUserIdAndQuestionId(Long userId, Long questionSeriesId);
    
    @Query("select a from Answer a "
            + "where a.user.id = ?1 "
            + "and a.removed = false "
            + "order by a.createdOn desc")
    public List<Answer> findByUserIdOrderByCreatedOn(Long userId);
}
