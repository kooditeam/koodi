package koodi.service;

import java.util.List;
import koodi.Main;
import koodi.domain.User;
import koodi.repository.UserRepository;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
public class UserServiceTest {
    
    @Autowired
    private UserService userService;    
    @Autowired
    private UserRepository userRepository;
    User user1;
    User user2;
    User user3;
    
    @Before
    public void setUp(){
        user1 = new User();
        user1.setName("user1");
        user1.setUsername("u1@u.com");
        user1.setPassword("aaa");
        
        user2 = new User();
        user2.setName("user2");
        user2.setUsername("u2@u.com");
        user2.setPassword("bbb");
        
        user3 = new User();
        user3.setName("user3");
        user3.setUsername("u3@u.com");
        user3.setPassword("ccc");
    }
    
    @After
    public void tearDown(){
        while(userRepository.findAll().size() > 1){
            userRepository.delete(userRepository.findAll().get(userRepository.findAll().size() - 1));
        }
    }    
    
    @Test
    public void defaultUserExists(){
        Assert.assertEquals(1, userRepository.findAll().size());
    }

    @Test
    public void newUserIsSaved(){
        User defUser = userRepository.findAll().get(0);
        User savedUser = userService.save(user1, defUser);
        Assert.assertEquals(2, userRepository.findAll().size());
        Assert.assertEquals(user1.getName(), savedUser.getName());
        Assert.assertEquals(user1.getUsername(), savedUser.getUsername());
        Assert.assertEquals(user1.getPassword(), savedUser.getPassword());
        Assert.assertEquals(user1.getCreatedById(), defUser.getId());
        Assert.assertNotNull(savedUser.getCreatedOn());
    }
    
    @Test
    public void userIsUpdated(){
        User defUser = userRepository.findAll().get(0);
        user1 = userService.save(user1, defUser);
        user1.setName("user1changed");
        user1.setUsername("u1changed@u.com");
        user1.setPassword("zzz");
        Long createdBy = user1.getCreatedById();
        DateTime createdOn = user1.getCreatedOn();
        User changedUser = userService.save(user1, defUser);
        Assert.assertEquals(2, userRepository.findAll().size());
        Assert.assertEquals(user1.getName(), changedUser.getName());
        Assert.assertEquals(user1.getUsername(), changedUser.getUsername());
        Assert.assertEquals(user1.getPassword(), changedUser.getPassword());
        Assert.assertEquals(user1.getCreatedById(), createdBy);
        Assert.assertEquals(user1.getCreatedOn(), createdOn);
        Assert.assertEquals(user1.getEditedById(), defUser.getId());
        Assert.assertNotNull(changedUser.getEditedOn());
    }
    
    @Test
    public void allUsersAreListed(){
        User defUser = userRepository.findAll().get(0);
        user1 = userService.save(user1, defUser);
        user2 = userService.save(user2, defUser);
        user3 = userService.save(user3, defUser);
        User[] users = new User[]{defUser, user1, user2, user3};
        
        List<User> foundUsers = userRepository.findAll();
        Assert.assertEquals(4, foundUsers.size());
        int matches = 0;        
        for(User u : users){
            for(User fu : foundUsers){
                if(u.getId().equals(fu.getId())){
                    matches++;
                    continue;
                }
            }
        }
        Assert.assertEquals(4, matches);
    }
    
    @Test
    public void userIsFoundById(){
        User defUser = userRepository.findAll().get(0);
        user1 = userService.save(user1, defUser);
        user2 = userService.save(user2, defUser);
        user3 = userService.save(user3, defUser);
        
        User foundUser = userService.findById(user2.getId());
        Assert.assertEquals(foundUser.getId(), user2.getId());
    }
    
    @Test
    public void userIsDeleted(){
        User defUser = userRepository.findAll().get(0);
        user1 = userService.save(user1, defUser);
        user2 = userService.save(user2, defUser);
        user3 = userService.save(user3, defUser);
        
        userService.delete(user2.getId());
        List<User> foundUsers = userRepository.findAll();
        Assert.assertEquals(3, foundUsers.size());
        int matches = 0;       

        for(User fu : foundUsers){
            if(user2.getId().equals(fu.getId())){
                matches++;
            }
        }
        
        Assert.assertEquals(0, matches);
    }
}
