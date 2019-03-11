package com.carhub.api.auth.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Privilege {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name="name", unique=true)
    private String name;

    public String getName() {
        return name;
    }

    public Privilege(String name){
        this();
        this.name = name;
    }

    public Privilege(){}

    public UUID getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
}
