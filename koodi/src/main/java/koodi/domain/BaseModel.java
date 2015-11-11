package koodi.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.AbstractPersistable;

@MappedSuperclass
public abstract class BaseModel implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    
    private Long createdById;
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "created__on", columnDefinition = "TIMESTAMP")
    private DateTime createdOn;
    private Long editedById;
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "edited__on", columnDefinition = "TIMESTAMP")
    private DateTime editedOn;
    private boolean removed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }    
    
    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
    }

    public Long getEditedById() {
        return editedById;
    }

    public void setEditedById(Long editedById) {
        this.editedById= editedById;
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
