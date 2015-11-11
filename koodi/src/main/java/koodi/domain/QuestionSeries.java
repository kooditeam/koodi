
package koodi.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;


@Entity
public class QuestionSeries extends BaseModel {
    
    @ManyToMany(mappedBy = "questionSeries")
    private List<Question> questions;
    private String title;
    private Integer order;

    public String getTitle() {
        return title;
    }

    public Integer getOrder() {
        return order;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
    
    
}
