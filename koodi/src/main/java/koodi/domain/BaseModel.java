package koodi.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.AbstractPersistable;

@MappedSuperclass
public abstract class BaseModel extends AbstractPersistable<Long>{
    
    private User createdBy;
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "created__on", columnDefinition = "TIMESTAMP")
    private DateTime createdOn;
    private User editedBy;
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "edited__on", columnDefinition = "TIMESTAMP")
    private DateTime editedOn;
    private boolean removed;

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
    }

    public User getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(User editedBy) {
        this.editedBy = editedBy;
    }

    public DateTime getEditedOn() {
        return editedOn;
    }

    public void setEditedOn(DateTime editedOn) {
        this.editedOn = editedOn;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
    
}
