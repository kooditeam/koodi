
package koodi.profiles;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import koodi.domain.AnswerOption;
import koodi.domain.Question;
import koodi.domain.QuestionSeries;
import koodi.domain.User;
import koodi.repository.AnswerOptionRepository;
import koodi.repository.QuestionRepository;
import koodi.repository.QuestionSeriesRepository;
import koodi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@Configuration
@Profile("prod")
public class ProdProfile {
//    
//        @Bean
//    public BasicDataSource dataSource() throws URISyntaxException {
//        URI dbUri = new URI(System.getenv("DATABASE_URL"));
//
//        String username = dbUri.getUserInfo().split(":")[0];
//        String password = dbUri.getUserInfo().split(":")[1];
//        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
//
//        BasicDataSource basicDataSource = new BasicDataSource();
//        basicDataSource.setUrl(dbUrl);
//        basicDataSource.setUsername(username);
//        basicDataSource.setPassword(password);
//
//        return basicDataSource;
//    }
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private QuestionSeriesRepository questionSeriesRepository;
    
    @Autowired
    private AnswerOptionRepository answerOptionRepository;
    
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
