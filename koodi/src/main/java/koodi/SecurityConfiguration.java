package koodi;
    
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.authorizeRequests()
                .anyRequest().permitAll();
//                .antMatchers("/login", "/rekisteroityminen", "index").permitAll()
//                .anyRequest().authenticated();
        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/j_spring_security_check")
                .defaultSuccessUrl("/index")
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .permitAll();
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
                .invalidateHttpSession(true);
    }

    @Configuration
    protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        private JpaAuthenticationProvider jpaAuthenticationProvider;

        public void init(AuthenticationManagerBuilder auth) throws Exception{
            auth.authenticationProvider(jpaAuthenticationProvider); 
        }
    }
}
