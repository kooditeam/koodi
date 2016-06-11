
package koodi.profiles;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import koodi.domain.User;
import koodi.repository.AnswerOptionRepository;
import koodi.repository.QuestionRepository;
import koodi.repository.QuestionSeriesRepository;
import koodi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("production")
public class ProdProfile {

    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }
    
    @Autowired
    private UserRepository userRepository;
    
    @PostConstruct
    public void init(){
        User defUser = new User();
        defUser.setName("oletuskäyttäjä");
        defUser.setUsername("a");
        defUser.setPassword("a");
        defUser.setIsAdmin(true);
        defUser = userRepository.save(defUser);
        
    }

}
