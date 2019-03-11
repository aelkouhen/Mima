package com.carhub.api.auth.domain;

import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class User implements UserDetails {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID id;

    private String email;

    @Column(name="username", unique=true)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private boolean enabled;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created = Calendar.getInstance().getTime();

    @Column(name = "account_locked")
    private boolean accountLocked;

    @Column(name = "account_expired")
    private boolean accountExpired;

    @Column(name = "credentials_expired")
    private boolean credentialsExpired;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_user", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id") })
    private List<Role> roles = new ArrayList<Role>();

    public User(String username, String password, String email){
        this();
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = true;
    }

    public User(){}

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    /*
     * Get roles and permissions and add them as a Set of GrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

        roles.forEach(r -> {
            authorities.add(new SimpleGrantedAuthority(r.getName()));
            r.getPrivileges().forEach(p -> {
                authorities.add(new SimpleGrantedAuthority(p.getName()));
            });
        });

        return authorities;
    }

    public boolean addRole(Role role){
        return roles.add(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public UUID getId() {
        return id;
    }

    public List<Role> getRoles(){
        return this.roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}