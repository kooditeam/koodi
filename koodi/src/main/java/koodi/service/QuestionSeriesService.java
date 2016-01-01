package koodi.service;

import java.util.List;
import koodi.domain.QuestionSeries;
import koodi.repository.QuestionSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionSeriesService extends BaseService<QuestionSeries> {
    
    @Autowired
    private QuestionSeriesRepository questionSeriesRepository;
    @Autowired
    private AchievementService achievementService;
    
    public List<QuestionSeries> findAll() {
        return questionSeriesRepository.findByRemovedFalseOrderByOrderNumberAsc();
    }
    
    public void delete(Long id){
        questionSeriesRepository.delete(id);
    }

    public QuestionSeries save(QuestionSeries questionSeries) {
        super.save(questionSeries, null);        
        questionSeries = questionSeriesRepository.save(questionSeries);
        if(questionSeries.getAchievements() == null){
            questionSeries.setAchievements(achievementService.createAchievements(questionSeries));
            questionSeries = questionSeriesRepository.save(questionSeries);
        }
        return questionSeries;
    }
    
    public List<QuestionSeries> findRemoved() {
        return questionSeriesRepository.findByRemovedTrueOrderByOrderNumberAsc();
    }
}
