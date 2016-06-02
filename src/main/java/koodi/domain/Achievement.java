/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package koodi.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Achievement extends BaseModel {
    
    @Column(unique = true)
    private String name;
    @ManyToOne
    private QuestionSeries questionSeries;
    @ManyToMany
    private List<User> achievers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QuestionSeries getQuestionSeries() {
        return questionSeries;
    }

    public void setQuestionSeries(QuestionSeries questionSeries) {
        this.questionSeries = questionSeries;
    }

    public List<User> getAchievers() {
        return achievers;
    }

    public void setAchievers(List<User> achievers) {
        this.achievers = achievers;
    }   
}
