package com.polimi.mrf.appartapp.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

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
