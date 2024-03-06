package com.polimi.mrf.appart.entities;

import jakarta.persistence.*;

@NamedQuery(name="GoogleUser.findByGoogleId",
        query="SELECT g FROM GoogleUser g WHERE g.googleId=:googleId"
)
@Entity
public class GoogleUser extends User {
    @Column(nullable = false)
    private String googleId;


    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getGoogleId() {
        return googleId;
    }
}
