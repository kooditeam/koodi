package koodi.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotBlank;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
@Table(name = "KoodiUser")
public class User extends BaseModel {
    
    @NotBlank(message = "Nimi ei saa olla tyhjä")
    private String name;
    @NotBlank(message = "Käyttäjänimi ei saa olla tyhjä")
    @Column(unique = true)
    private String username;
    @NotBlank(message = "Salasana ei saa olla tyhjä")
    private String password;
    private String salt;
    private boolean isAdmin;
    @OneToMany
    private List<Answer> answers;
    @ManyToMany(mappedBy = "achievers", fetch = FetchType.EAGER)
    private List<Achievement> achievements;

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }    

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }   

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }    
}
