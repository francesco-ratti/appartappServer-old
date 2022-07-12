package com.polimi.mrf.appartapp.entities;

import com.google.gson.annotations.Expose;
import com.polimi.mrf.appartapp.Gender;
import com.polimi.mrf.appartapp.Month;
import com.polimi.mrf.appartapp.TemporalQ;

import javax.persistence.*;
import java.util.*;

@NamedQuery(name="User.findByEmail",
        query="SELECT u FROM User u WHERE u.email=:email"
)
@NamedQuery(name = "User.findMatchedApartments", query = "SELECT m FROM Match m JOIN m.apartment a WHERE m.user=:user ORDER BY m.matchDate DESC")//JOIN a.matches m WHERE m.user=:user ORDER BY m.matchDate DESC")

//@NamedQuery(name = "User.findMatchedApartments", query = "SELECT a FROM Apartment a JOIN a.likedUsers luMap WHERE KEY(luMap).id=:userId AND a.id IN (SELECT l.id FROM User u JOIN u.likedApartments l WHERE u.id=KEY(luMap).id) ORDER BY VALUE(luMap) DESC")  //in parenthesis "useless"

//@NamedQuery(name = "User.findMatchedApartmentsFromDate", query = "SELECT a FROM Apartment a JOIN a.likedUsers luMap WHERE KEY(luMap).id=:userId AND VALUE(luMap)>=:date AND a.id IN (SELECT l.id FROM User u JOIN u.likedApartments l WHERE u.id=KEY(luMap).id) ORDER BY VALUE(luMap) DESC")  //in parenthesis "useless"

@NamedQuery(name = "User.findMatchedApartmentsFromDate", query = "SELECT m FROM Match m JOIN m.apartment a WHERE (m.user=:user AND m.matchDate > :date) ORDER BY m.matchDate ASC")  //in parenthesis "useless"

@NamedQuery(name = "User.getNewApartments", query = "SELECT h FROM Apartment h, User u WHERE u.id=:userId AND u.id <> h.owner.id AND h.id NOT IN (SELECT lh.id FROM u.likedApartments lh) AND h.id NOT IN (SELECT ih.id FROM u.ignoredApartments ih)")
//@NamedQuery(name = "User.getNewApartments", query = "SELECT h FROM Apartment h WHERE h.id NOT IN (SELECT lh.id FROM User u JOIN u.likedApartmentList lh WHERE u.id=:userId) AND h.id NOT IN (SELECT ih.id FROM User u JOIN u.ignoredApartmentList ih WHERE u.id=:userId)")
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class User {
    @Expose
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Expose
    @Column(unique = true)
    private String email;

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

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Apartment> ownedApartments;

    @Expose
    @OneToMany(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<UserImage> images =new ArrayList<>();

    @Expose
    @Column(nullable = true)
    String bio;

    @Expose
    @Column(nullable = true)
    String reason;

    @Expose
    @Column(nullable = true)
    Month month;

    @Expose
    @Column(nullable = true)
    String job;

    @Expose
    @Column(nullable = true)
    String income;

    @Expose
    @Column(nullable = true)
    TemporalQ smoker;

    @Expose
    @Column(nullable = true)
    String pets;

    private Set<UserAuthToken> userAuthTokens = new HashSet<>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    public Set<UserAuthToken> getUserAuthTokens() {
        return userAuthTokens;
    }

    public void setUserAuthTokens(Set<UserAuthToken> userAuthTokens) {
        this.userAuthTokens = userAuthTokens;
    }

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

    public void setId(Long id) {
        this.id = id;
    }

    /*
    @ManyToMany(mappedBy = "matchedUsers", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    public List<Apartment> matchedApartments;
    */

    //OPTIONALS


    public Month getMonth() {
        return month;
    }

    public String getBio() {
        return bio;
    }

    public String getIncome() {
        return income;
    }

    public String getJob() {
        return job;
    }

    public String getPets() {
        return pets;
    }

    public String getReason() {
        return reason;
    }

    public TemporalQ getSmoker() {
        return smoker;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setIgnoredApartments(List<Apartment> ignoredApartments) {
        this.ignoredApartments = ignoredApartments;
    }

    public void setImages(List<UserImage> images) {
        this.images = images;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setLikedApartments(List<Apartment> likedApartments) {
        this.likedApartments = likedApartments;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public void setOwnedApartments(List<Apartment> ownedApartments) {
        this.ownedApartments = ownedApartments;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setSmoker(TemporalQ smoker) {
        this.smoker = smoker;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof User)) {
            return false;
        }

        User a=(User) o;

        return Long.compare(a.id, this.id)==0;
    }

}
