package com.irahavoi.jaxb;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

@XmlRootElement
public class Skier {
    private Person person;
    private String team;
    private Collection achievements;

    public Skier() { }

    public Skier(Person person, String team, Collection achievements) {
        this.person = person;
        this.team = team;
        this.achievements = achievements;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Collection getAchievements() {
        return achievements;
    }

    public void setAchievements(Collection achievements) {
        this.achievements = achievements;
    }
}
