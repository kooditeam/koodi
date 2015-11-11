
package koodi.profiles;

import javax.annotation.PostConstruct;
import koodi.domain.User;
import koodi.repository.UserRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = {"dev", "default"})
public class DevProfile {
    
    @Autowired
    private UserRepository userRepository;
    
    @PostConstruct
    public void init(){
        User defUser = new User();
        defUser.setName("oletuskäyttäjä");
        defUser.setEmail("a@a.com");
        defUser = userRepository.save(defUser);
//        defUser.setCreatedOn(DateTime.now());
//        defUser.setCreatedBy(defUser);
//        userRepository.save(defUser);
    }
}
