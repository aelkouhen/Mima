package com.carhub.api.auth;

import com.carhub.api.auth.domain.CustomClientDetails;
import com.carhub.api.auth.domain.Privilege;
import com.carhub.api.auth.domain.Role;
import com.carhub.api.auth.domain.User;
import com.carhub.api.auth.services.CustomClientDetailsService;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AuthInitialDataLoader implements ApplicationRunner {

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    RoleService roleService;

    @Autowired
    CustomClientDetailsService customClientDetailsService;

    @Autowired
    PrivilegeService privilegeService;

    static final int ONE_DAY= 60 * 60 * 24;
    static final int ONE_MONTH = ONE_DAY * 30;
    static final String PASSWORD = "password";
    static final String AUTHORIZATION_CODE = "authorization_code";
    static final String REFRESH_TOKEN = "refresh_token";
    static final String IMPLICIT = "implicit";
    static final String SCOPE_READ = "READ_PRIVILEGE";
    static final String SCOPE_CREATE = "CREATE_PRIVILEGE";
    static final String SCOPE_UPDATE = "UPDATE_PRIVILEGE";
    static final String SCOPE_DELETE = "DELETE_PRIVILEGE";


    public void run(ApplicationArguments args) {

        CustomClientDetails clientApp = new CustomClientDetails();

        clientApp.setResourceIds(Stream.of("CLIENT_RESOURCE").collect(Collectors.toSet()));
        clientApp.setClientId("CLIENT_APP");
        clientApp.setClientSecret(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("password"));
        clientApp.setAuthorizedGrantTypes(Stream.of(PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN, IMPLICIT).collect(Collectors.toSet()));
        clientApp.setScope(Stream.of(SCOPE_READ, SCOPE_CREATE, SCOPE_UPDATE, SCOPE_DELETE).collect(Collectors.toSet()));
        clientApp.setAccessTokenValiditySeconds(ONE_DAY);
        clientApp.setRefreshTokenValiditySeconds(ONE_MONTH);

        customClientDetailsService.createClient(clientApp);

        CustomClientDetails adminApp = new CustomClientDetails();

        adminApp.setResourceIds(Stream.of("CLIENT_RESOURCE", "ADMIN_RESOURCE").collect(Collectors.toSet()));
        adminApp.setClientId("ADMIN_APP");
        adminApp.setClientSecret(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("admin"));
        adminApp.setAuthorizedGrantTypes(Stream.of(PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN, IMPLICIT).collect(Collectors.toSet()));
        adminApp.setScope(Stream.of(SCOPE_READ, SCOPE_CREATE, SCOPE_UPDATE, SCOPE_DELETE).collect(Collectors.toSet()));
        adminApp.setAccessTokenValiditySeconds(ONE_DAY);
        adminApp.setRefreshTokenValiditySeconds(ONE_MONTH);

        customClientDetailsService.createClient(adminApp);


        Privilege p1 = new Privilege("READ_PRIVILEGE");
        privilegeService.createPrivilege(p1);

        Privilege p2 = new Privilege("UPDATE_PRIVILEGE");
        privilegeService.createPrivilege(p2);

        Privilege p3 = new Privilege("DELETE_PRIVILEGE");
        privilegeService.createPrivilege(p3);

        Privilege p4 = new Privilege("CREATE_PRIVILEGE");
        privilegeService.createPrivilege(p4);


        Role adminRole = new Role("ROLE_ADMIN");
        adminRole.setPrivileges(Arrays.asList(p1, p2, p3, p4));
        roleService.createRole(adminRole);

        Role userRole = new Role("ROLE_USER");
        userRole.setPrivileges(Arrays.asList(p1, p4));
        roleService.createRole(userRole);

        Role guestRole = new Role("ROLE_GUEST");
        guestRole.setPrivileges(Arrays.asList(p1));
        roleService.createRole(guestRole);

        User guest = new User("guest", PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("password"), "guest@user.io");
        guest.addRole(guestRole);
        userDetailsService.createUser(guest);

        User simple = new User("user", PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("password"), "user@user.io");
        simple.addRole(userRole);
        userDetailsService.createUser(simple);

        User admin = new User("admin", PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("password"), "admin@user.io");
        admin.addRole(adminRole);
        userDetailsService.createUser(admin);
    }
}
