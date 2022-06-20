package com.polimi.mrf.appartapp.google;

public class GoogleUserInfo {
    private String id;
    private String email;
    private boolean emailVerified;
    private String name;
    private String pictureUrl;
    private String locale;
    private String familyName;
    private String givenName;

    public GoogleUserInfo(String id, String email, boolean emailVerified, String name, String pictureUrl, String locale, String familyName, String givenName) {
        this.id=id;
        this.email=email;
        this.emailVerified=emailVerified;
        this.name=name;
        this.pictureUrl=pictureUrl;
        this.locale=locale;
        this.familyName=familyName;
        this.givenName=givenName;
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
