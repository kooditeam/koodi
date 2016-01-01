package koodi.service;

import java.util.List;
import koodi.domain.Achievement;
import koodi.domain.User;
import koodi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService<User> {
    
    @Autowired
    private UserRepository userRepository;
    
    public User save(User user){
        super.save(user, null);
        return userRepository.save(user);
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

    public Object getAchievementsAsHtmlList(User user) {
        List<Achievement> achievements = user.getAchievements();
        String achievementHtml = "";
        if(achievements != null){
            achievementHtml = "<div><ul id='achievement-list' style='list-style-type:none'>";
            for(Achievement a : achievements){
                achievementHtml += "<li>" + a.getName() + "</li>";
            }
            achievementHtml += "</ul></div>";
        }        
        return achievementHtml;
    }
}
