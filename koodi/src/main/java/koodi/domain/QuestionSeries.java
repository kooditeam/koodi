
package koodi.domain;

import javax.persistence.Entity;


@Entity
public class QuestionSeries extends BaseModel {
    
    private String title;
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
