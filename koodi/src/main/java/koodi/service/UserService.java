package koodi.service;

import java.util.List;
import koodi.domain.User;
import koodi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService{
    
    @Autowired
    private UserRepository userRepository;
    
    public User save(User user, User admin){
        super.save(user, admin);
        return userRepository.save(user);
    }
    
    public List<User> findAll(){
        return userRepository.findAll();
    }
    
    public User findById(Long id){
        return userRepository.findOne(id);
    }
    
    public void delete(Long id){
        userRepository.delete(id);
    }
    
    public User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName());
    }
}
