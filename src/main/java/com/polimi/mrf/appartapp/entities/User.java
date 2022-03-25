package com.polimi.mrf.appartapp.entities;

import com.polimi.mrf.appartapp.Gender;

import javax.persistence.*;
import java.util.Date;

@NamedQuery(name="User.checkCredentials",
        query="SELECT u FROM User u WHERE u.email=:email AND u.password=:password"
)
@Entity
public class User {
    @Id
    private String email;
    @Column(nullable = false)
    private String password;
    private String name;
    private String surname;
    private Date birthday;
    private Gender gender;

    public Date getBirthday() {
        return birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
