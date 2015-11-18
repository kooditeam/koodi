
package koodi.profiles;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@Configuration
@Profile("prod")
public class ProdProfile {
    
    @Autowired
    private SpringTemplateEngine templateEngine;
    
    @PostConstruct
    public void extendTemplateResolvers(){
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();        
        resolver.setPrefix("/koodi/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(templateEngine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        templateEngine.addTemplateResolver(resolver);
    }
}
