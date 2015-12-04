
package koodi.domain;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;


@Entity
public class QuestionSeries extends BaseModel {
    
    @NotBlank(message = "Otsikko ei saa olla tyhjä")
    private String title;
    
    @NotNull(message = "Järjestys vaaditaan")
    private Integer orderNumber;

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
    
    
}
