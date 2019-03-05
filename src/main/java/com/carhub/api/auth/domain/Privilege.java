package com.carhub.api.auth.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    public String getName() {
        return name;
    }

    public Privilege(String name){
        this();
        this.name = name;
    }

    public Privilege(){}

    public long getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
}
