package com.polimi.mrf.appart.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;

@NamedQuery(name="UserImage.findMaxId",
        query="SELECT MAX(ui.id) FROM UserImage ui"
)
@Entity
public class UserImage extends Image {
    @ManyToOne
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
