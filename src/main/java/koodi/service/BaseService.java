package koodi.service;

import java.util.List;
import koodi.domain.BaseModel;
import koodi.domain.User;
import koodi.repository.BaseRepository;
import koodi.repository.UserRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseService<M> {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BaseRepository<M> defaultRepository;
    
    public M findById(Long id){
        return defaultRepository.findByIdAndRemovedFalse(id);
    }
    
    public M findRemovedById(Long id){
        return defaultRepository.findByIdAndRemovedTrue(id);
    }
    
    public List<M> findAll(){
        return defaultRepository.findByRemovedFalse();
    }
    
    public List<M> findAllRemoved(){
        return defaultRepository.findByRemovedTrue();
    }

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
            UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
            String username = authToken.getPrincipal().toString();
            user = userRepository.findByUsername(username);
        }
        catch (Exception exc) {
        }
        if(user == null){
            user = userRepository.findAll().get(0);
        }
        return user;
    }
}
