package com.polimi.mrf.appart.entities;

import com.google.gson.annotations.Expose;

import jakarta.persistence.*;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public abstract class Image {
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Column(nullable = true)
    //public String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private byte[] imageBytes;

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Image)) {
            return false;
        }

        Image i=(Image) o;

        return Long.compare(i.id, this.id)==0;
    }
}
