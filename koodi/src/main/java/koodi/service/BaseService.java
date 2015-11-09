package koodi.service;

import java.util.Date;
import koodi.domain.BaseModel;
import koodi.domain.User;

public abstract class BaseService {
    
    public void save(BaseModel model, User user){
        if(model.getId() == null){
            model.setCreatedOn(new Date());
            model.setCreatedBy(user);
        } else {
            model.setEditedOn(new Date());
            model.setEditedBy(user);
        }       
        return;
    }    
}
