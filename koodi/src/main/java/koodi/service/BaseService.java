package koodi.service;

import koodi.domain.BaseModel;
import koodi.domain.User;
import org.jadira.usertype.dateandtime.joda.PersistentDateTime;
import org.joda.time.DateTime;

public abstract class BaseService {
    
    public void save(BaseModel model, User user){
        if(model.getId() == null){
            model.setCreatedOn(new DateTime());
            model.setCreatedBy(user);
        } else {
            model.setEditedOn(new DateTime());
            model.setEditedBy(user);
        }       
        return;
    }    
}
