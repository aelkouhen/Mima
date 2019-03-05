package com.carhub.api.auth;

import com.carhub.api.auth.domain.Privilege;
import com.carhub.api.auth.domain.Role;
import com.carhub.api.auth.domain.User;
import com.carhub.api.auth.services.CustomUserDetailsService;
import com.carhub.api.auth.services.PrivilegeService;
import com.carhub.api.auth.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class AuthInitialDataLoader implements ApplicationRunner {

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    RoleService roleService;

    @Autowired
    PrivilegeService privilegeService;

    public void run(ApplicationArguments args) {

        Privilege p1 = new Privilege("READ_PRIVILEGE");
        privilegeService.CreatePrivilege(p1);

        Privilege p2 = new Privilege("WRITE_PRIVILEGE");
        privilegeService.CreatePrivilege(p2);

        Role adminRole = new Role("ROLE_ADMIN");
        adminRole.addPrivilege(p1);
        adminRole.addPrivilege(p2);
        roleService.CreateRole(adminRole);

        Role userRole = new Role("ROLE_USER");
        userRole.addPrivilege(p1);
        roleService.CreateRole(userRole);

        User simple = new User("user", PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("password"), "user@user.io");
        simple.addRole(userRole);
        userDetailsService.CreateUser(simple);

        User admin = new User("admin", PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("password"), "admin@user.io");
        admin.addRole(adminRole);
        userDetailsService.CreateUser(admin);

        BaseClientDetails client = new BaseClientDetails("USER_CLIENT_APP", "USER_CLIENT_RESOURCE,USER_ADMIN_RESOURCE",
                "ROLE_ADMIN,ROLE_USER", "authorization_code,password,refresh_token,implicit", null);

        client.setClientSecret(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("password"));
        client.setAccessTokenValiditySeconds(900);
        client.setRefreshTokenValiditySeconds(3600);
        client.setRegisteredRedirectUri(null);

        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.addClientDetails(client);
    }
}
