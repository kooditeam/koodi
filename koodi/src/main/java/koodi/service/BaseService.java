package koodi.service;

import koodi.domain.BaseModel;
import koodi.domain.User;
import koodi.repository.UserRepository;
import org.jadira.usertype.dateandtime.joda.PersistentDateTime;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseService {
    
    @Autowired
    private UserRepository userRepository;
    
    public void save(BaseModel model, User user){
        if(user==null){
            user = userRepository.findOne(new Long(1));
        }
        if(model.getId() == null){
            model.setCreatedOn(new DateTime());
            model.setCreatedById(user.getId());
        } else {
            model.setEditedOn(new DateTime());
            model.setEditedById(user.getId());
        }       
        return;
    }    
}
