package com.polimi.mrf.appart.entities;

import com.google.gson.annotations.Expose;

import jakarta.persistence.*;


@NamedQuery(name="User.checkCredentials",
        query="SELECT u FROM CredentialsUser u WHERE u.email=:email AND u.password=:password"
)
@Entity
public class CredentialsUser extends User {
    @Column(nullable = false)
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
