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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Arrays;

@Component
public class AuthInitialDataLoader implements ApplicationRunner {

    //public static final int ONE_DAY= 60 * 60 * 24;
    //public static final int ONE_MONTH = ONE_DAY * 30;

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

        Privilege p2 = new Privilege("UPDATE_PRIVILEGE");
        privilegeService.CreatePrivilege(p2);

        Privilege p3 = new Privilege("DELETE_PRIVILEGE");
        privilegeService.CreatePrivilege(p3);

        Privilege p4 = new Privilege("CREATE_PRIVILEGE");
        privilegeService.CreatePrivilege(p4);


        Role adminRole = new Role("ROLE_ADMIN");
        adminRole.setPrivileges(Arrays.asList(p1, p2, p3, p4));
        roleService.CreateRole(adminRole);

        Role userRole = new Role("ROLE_USER");
        userRole.setPrivileges(Arrays.asList(p1, p4));
        roleService.CreateRole(userRole);

        Role guestRole = new Role("ROLE_GUEST");
        guestRole.setPrivileges(Arrays.asList(p1));
        roleService.CreateRole(guestRole);

        User guest = new User("guest", PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("password"), "guest@user.io");
        guest.addRole(guestRole);
        userDetailsService.CreateUser(guest);

        User simple = new User("user", PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("password"), "user@user.io");
        simple.addRole(userRole);
        userDetailsService.CreateUser(simple);

        User admin = new User("admin", PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("password"), "admin@user.io");
        admin.addRole(adminRole);
        userDetailsService.CreateUser(admin);

        /*
        BaseClientDetails client = new BaseClientDetails("USER_CLIENT_APP", "USER_CLIENT_RESOURCE,USER_ADMIN_RESOURCE",
                userRole.getName() + "," + adminRole.getName(), "authorization_code,password,refresh_token,implicit", null);

        client.setClientSecret(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("password"));
        client.setAccessTokenValiditySeconds(ONE_DAY);
        client.setRefreshTokenValiditySeconds(ONE_MONTH);
        client.setRegisteredRedirectUri(null);

        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.addClientDetails(client);
        */
    }
}
