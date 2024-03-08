package com.polimi.mrf.appart.entities;

import com.google.gson.annotations.Expose;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "apartment_user_liked",
        uniqueConstraints={
        @UniqueConstraint(columnNames = {"apartment_id", "user_id"})
})
public class Match {
    public Match(User user, Date matchDate) {
        this.user=user;
        this.matchDate=matchDate;
    }

    @Expose
    @ManyToOne()
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Expose
    @Column(columnDefinition = "TIMESTAMP (3)")
    private Date matchDate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Match() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchTime) {
        this.matchDate = matchTime;
    }
}
