package koodi.service;

import koodi.domain.BaseModel;
import koodi.domain.User;
import koodi.repository.UserRepository;
import org.jadira.usertype.dateandtime.joda.PersistentDateTime;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseService {
    
    @Autowired
    private UserRepository userRepository;
    
    public void save(BaseModel model, User user){
        if(user==null){
            user = getCurrentUser();
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
    
    public BaseModel delete(BaseModel model){
        model.setEditedById(getCurrentUser().getId());
        model.setEditedOn(new DateTime());
        model.setRemoved(true);    
        return model;
    }
    
    public User getCurrentUser(){
        User user = null;
        try{
            String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            user = userRepository.findByUsername(username);
        }
        // poistettava kun autentikointi oikeasti käytössä!!!
        catch(Exception exc){ }       
        if(user == null){
             user = userRepository.findAll().get(0);
        }
        return user;
    }
}
