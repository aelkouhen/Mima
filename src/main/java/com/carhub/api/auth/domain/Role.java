package com.carhub.api.auth.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

@Entity
public class Role {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name="name", unique=true)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "privilege_role", joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id") }, inverseJoinColumns = {
            @JoinColumn(name = "privilege_id", referencedColumnName = "id") })
    private List<Privilege> privileges = new ArrayList<Privilege>();

    public Role(String name){
        this();
        this.name = name;
    }

    public Role(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean addPrivilege(Privilege privilege) {
        return privileges.add(privilege);
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }
}