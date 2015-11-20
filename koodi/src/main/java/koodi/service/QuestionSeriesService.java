package koodi.service;

import java.util.List;
import koodi.domain.QuestionSeries;
import koodi.repository.QuestionSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionSeriesService extends BaseService {
    
    @Autowired
    private QuestionSeriesRepository questionSeriesRepository;
    
    public List<QuestionSeries> findAll() {
        return questionSeriesRepository.findAll();
    }
    
    public QuestionSeries findById(Long id){
        return questionSeriesRepository.findOne(id);
    }
    
    public void delete(Long id){
        questionSeriesRepository.delete(id);
    }

    public void save(QuestionSeries questionSeries) {
        super.save(questionSeries, null);
        questionSeriesRepository.save(questionSeries);
    }
}
