
package koodi.repository;

import java.util.List;
import koodi.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    
    public List<Answer> findAllByUserId(Long id);

    public List<Answer> findByUserId(Long userId);

//    @Query("select a "
//            + "from Question q join q.answerOptions o join o.answers a "
//            + "where a.userId = :userId and q.questionSeriesId = :questionSeriesId")
    @Query("select a from Answer a where a.user.id = ?1")
    public List<Answer> findByUserIdAndQuestionSeriesId(Long userId);    
}
