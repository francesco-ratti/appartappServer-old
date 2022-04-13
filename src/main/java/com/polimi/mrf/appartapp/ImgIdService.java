package com.polimi.mrf.appartapp;

import com.polimi.mrf.appartapp.beans.ImgIdServiceBean;

import javax.ejb.EJB;

//SINGLETON in charge of assigning ids to images

/*
if it's the first run retrive MAX IDs from data store;
next calls wont't require query to data store since id will be handed by this singleton
 */

public class ImgIdService {
    @EJB(name = "com.polimi.mrf.appartapp.beans/ImgIdServiceBean")
    ImgIdServiceBean imgIdServiceBean;

    private long currentUserImageId;
    private long currentApartmentImageId;

    private static ImgIdService instance=null;

    public static ImgIdService getInstance() {
        if (instance==null)
            instance=new ImgIdService();
        return instance;
    }

    //first run, retrieve max ids from data store
    private ImgIdService() {
        currentUserImageId=imgIdServiceBean.getCurrentUserImageId();
        currentApartmentImageId=imgIdServiceBean.getCurrentApartmentImageId();
    }

    public long getCurrentApartmentImageId() {
        return currentApartmentImageId;
    }

    public long getCurrentUserImageId() {
        return currentUserImageId;
    }

    public long getNewApartmentImageId() {
        currentApartmentImageId++;
        return currentApartmentImageId;
    }

    public long getNewUserImageId() {
        currentUserImageId++;
        return currentUserImageId;
    }
}
