package com.polimi.mrf.appartapp.entities;

import com.google.gson.annotations.Expose;
import com.polimi.mrf.appartapp.Gender;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NamedQuery(name="User.checkCredentials",
        query="SELECT u FROM User u WHERE u.email=:email AND u.password=:password"
)
@NamedQuery(name="User.findByEmail",
        query="SELECT u FROM User u WHERE u.email=:email"
)

@NamedQuery(name = "User.getNewApartments", query = "SELECT h FROM Apartment h, User u WHERE u.id=:userId AND u.id <> h.owner.id AND h.id NOT IN (SELECT lh.id FROM u.likedApartments lh) AND h.id NOT IN (SELECT ih.id FROM u.ignoredApartments ih)")
//@NamedQuery(name = "User.getNewApartments", query = "SELECT h FROM Apartment h WHERE h.id NOT IN (SELECT lh.id FROM User u JOIN u.likedApartmentList lh WHERE u.id=:userId) AND h.id NOT IN (SELECT ih.id FROM User u JOIN u.ignoredApartmentList ih WHERE u.id=:userId)")
@Entity
public class User {
    @Expose
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Expose
    @Column(unique = true)
    private String email;

    @Expose
    @Column(nullable = false)
    private String password;

    @Expose
    private String name;

    @Expose
    private String surname;

    @Expose
    private Date birthday;

    @Expose
    private Gender gender;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name="user_apartment_ignored",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="apartment_id"))
    private List<Apartment> ignoredApartments;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name="user_apartment_liked",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="apartment_id"))
    private List<Apartment> likedApartments;

    @Expose
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Apartment> ownedApartments;

    @Expose
    @OneToMany(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<UserImage> images =new ArrayList<>();

    public List<UserImage> getImages() {
        return images;
    }

    public void addImage(UserImage userImage) {
        userImage.setUser(this);
        images.add(userImage);
    }


    public Date getBirthday() {
        return birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public Long getId() {
        return id;
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

    public List<Apartment> getIgnoredApartments() {
        return ignoredApartments;
    }

    public List<Apartment> getLikedApartments() {
        return likedApartments;
    }

    public List<Apartment> getOwnedApartments() {
        return ownedApartments;
    }

    public void addIgnoredApartment(Apartment apartment) {
        this.ignoredApartments.add(apartment);
    }

    public void addLikedApartment(Apartment apartment) {
        this.likedApartments.add(apartment);
    }

    public void addOwnedApartment(Apartment apartment) {
        this.ownedApartments.add(apartment);
    }

    public boolean removeImage (long imageId) {
        //avoiding useless lookup , since equals method of Image class has been overridden, returns true if ids are the same
        UserImage dummyImg=new UserImage();
        dummyImg.setId(imageId);
        return this.images.remove(dummyImg);
    }

    /*
    @ManyToMany(mappedBy = "matchedUsers", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    public List<Apartment> matchedApartments;
    */

}
