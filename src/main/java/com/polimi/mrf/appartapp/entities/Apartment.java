package com.polimi.mrf.appartapp.entities;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Apartment {
    @Expose
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @ManyToOne
    private User owner;

    @Expose
    private String listingTitle;
    @Expose
    private String description;
    @Expose
    private int price;
    @Expose
    private String address;
    @Expose
    private String additionalExpenseDetail;

    @Expose
    @OneToMany(mappedBy="apartment", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<ApartmentImage> apartmentImageList=new ArrayList<>();

    public String getAdditionalExpenseDetail() {
        return additionalExpenseDetail;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getListingTitle() {
        return listingTitle;
    }

    public int getPrice() {
        return price;
    }

    public void setAdditionalExpenseDetail(String additionalExpenseDetail) {
        this.additionalExpenseDetail = additionalExpenseDetail;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setListingTitle(String listingTitle) {
        this.listingTitle = listingTitle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
        owner.addOwnedApartment(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Apartment)) {
            return false;
        }

        Apartment a=(Apartment) o;

        return Long.compare(a.id, this.id)==0;
    }

    public void addImage(ApartmentImage apartmentImage) {
        apartmentImage.setApartment(this);

        apartmentImageList.add(apartmentImage);
    }
}