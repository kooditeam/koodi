package koodi;

import koodi.profiles.DevProfile;
import koodi.profiles.ProdProfile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({DevProfile.class, ProdProfile.class})
public class Main {
    
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }
}
 
