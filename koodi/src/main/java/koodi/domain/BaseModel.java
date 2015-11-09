package koodi.domain;

import java.util.Date;
import javax.persistence.MappedSuperclass;
import org.springframework.data.jpa.domain.AbstractPersistable;

@MappedSuperclass
public abstract class BaseModel extends AbstractPersistable<Long>{
    
    private User createdBy;

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public User getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(User editedBy) {
        this.editedBy = editedBy;
    }

    public Date getEditedOn() {
        return editedOn;
    }

    public void setEditedOn(Date editedOn) {
        this.editedOn = editedOn;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
    private Date createdOn;
    private User editedBy;
    private Date editedOn;
    private boolean removed;
    
}
