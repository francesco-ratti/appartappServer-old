package com.polimi.mrf.appartapp.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_auth")
@NamedQueries({
        @NamedQuery(name = "UserAuthToken.findBySelector",
                query = "SELECT c FROM UserAuthToken c WHERE c.selector = :selector")
})
public class UserAuthToken implements java.io.Serializable {
    private Long id;
    private String selector;
    private String validator;
    private User user;
    private Date lastUse;

    public Date getLastUse() {
        return lastUse;
    }

    public void setLastUse(Date lastUse) {
        this.lastUse = lastUse;
    }

    public UserAuthToken() {
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSelector(String selector) {
    }

    public void setValidator(String hashedValidator) {
    }

    public String getSelector() {
        return selector;
    }

    public String getValidator() {
        return validator;
    }

    // other getters and setters are hidden for brevity

}