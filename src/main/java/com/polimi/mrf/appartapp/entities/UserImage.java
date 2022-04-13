package com.polimi.mrf.appartapp.entities;

import javax.persistence.ManyToOne;

public class UserImage extends Image {
    @ManyToOne
    private User user;
}
