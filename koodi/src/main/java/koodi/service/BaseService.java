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

    public void save(BaseModel model, User user) {
        if (user == null) {
            user = getCurrentUser();
        }
        if (modelWasJustCreated(model)) {
            setCreationTimeForModel(model);
            setModelsCreator(model, user);
        } else {
            setEditingTimeForModel(model);
            setModelsEditor(model, user);
        }
    }
    
    private void setEditingTimeForModel(BaseModel model) {
        model.setEditedOn(new DateTime());
    }
    
    private void setModelsEditor(BaseModel model, User user) {
        model.setEditedById(user.getId());
    }
    
    private boolean modelWasJustCreated(BaseModel model) {
        return model.getId() == null;
    }
    
    private void setCreationTimeForModel(BaseModel model) {
        model.setCreatedOn(new DateTime());
    }
    
    private void setModelsCreator(BaseModel model, User user) {
        model.setCreatedById(user.getId());
    }

    public BaseModel delete(BaseModel model) {
        model.setEditedById(getCurrentUser().getId());
        model.setEditedOn(new DateTime());
        model.setRemoved(true);
        return model;
    }

    public User getCurrentUser() {
        User user = null;
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            user = userRepository.findByUsername(username);
        } // poistettava kun autentikointi oikeasti käytössä!!!
        catch (Exception exc) {
        }
        if (user == null) {
            user = userRepository.findAll().get(0);
        }
        return user;
    }
}
