package com.polimi.mrf.appartapp.google;

import com.google.api.services.people.v1.model.Birthday;
import com.google.api.services.people.v1.model.Gender;

public class GoogleUserInfo {
    private String id;
    private String email;
    private boolean emailVerified;
    private String name;
    private String pictureUrl;
    private String locale;
    private String familyName;
    private String givenName;
    private Birthday birthday;
    private Gender gender;

    public GoogleUserInfo(String id, String email, boolean emailVerified, String name, String pictureUrl, String locale, String familyName, String givenName, Birthday birthday, Gender gender) {
        this.id=id;
        this.email=email;
        this.emailVerified=emailVerified;
        this.name=name;
        this.pictureUrl=pictureUrl;
        this.locale=locale;
        this.familyName=familyName;
        this.givenName=givenName;
        this.birthday = birthday;
        this.gender = gender;
    }

    public Birthday getBirthday() {
        return birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getLocale() {
        return locale;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

}
