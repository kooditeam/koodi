
package koodi.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class QuestionSeries extends BaseModel {
    
    @NotBlank(message = "Otsikko ei saa olla tyhjä")
    private String title;
    
    @NotNull(message = "Järjestys vaaditaan")
    private Integer orderNumber;
    
    @OneToMany(fetch = FetchType.EAGER)
    private List<Achievement> achievements;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }  
    
}
