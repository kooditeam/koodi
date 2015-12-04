package koodi.service;

import java.util.List;
import koodi.domain.User;
import koodi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService{
    
    @Autowired
    private UserRepository userRepository;
    
    public User save(User user){
        super.save(user, null);
        return userRepository.save(user);
    }
    
    public List<User> findAll(){
        return userRepository.findByRemovedIsFalse();
    }
    
    public User findById(Long id){
        return userRepository.findByIdAndRemovedFalse(id);
    }
    
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    
    public void delete(Long id){
        User user = userRepository.findOne(id);
        user = (User)super.delete(user);
        userRepository.save(user);
    }
    
    public User getAuthenticatedUser(){
        return super.getCurrentUser();
    }
}
