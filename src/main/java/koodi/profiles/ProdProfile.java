
package koodi.profiles;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import koodi.domain.User;
import koodi.repository.AnswerOptionRepository;
import koodi.repository.QuestionRepository;
import koodi.repository.QuestionSeriesRepository;
import koodi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("production")
public class ProdProfile {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
    
    /*
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
    */

}
