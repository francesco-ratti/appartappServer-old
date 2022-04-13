package com.polimi.mrf.appartapp.entities;

import javax.persistence.*;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public abstract class Image {
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column(nullable = true)
    public String description;
}
