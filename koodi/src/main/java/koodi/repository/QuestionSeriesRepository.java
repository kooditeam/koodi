
package koodi.repository;

import java.util.List;
import koodi.domain.QuestionSeries;


public interface QuestionSeriesRepository extends BaseRepository<QuestionSeries> {
    public List<QuestionSeries> findByRemovedFalseOrderByOrderNumberAsc();
    public List<QuestionSeries> findByRemovedTrueOrderByOrderNumberAsc();
}
