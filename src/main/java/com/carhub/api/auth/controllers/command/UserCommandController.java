package com.carhub.api.auth.controllers.command;

import com.carhub.api.auth.domain.Role;
import com.carhub.api.auth.domain.User;
import com.carhub.api.auth.services.CustomUserDetailsService;
import com.carhub.api.auth.services.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Api(value = "User", tags = "User Commands", description = "This API commands the User concept.")
@RestController
@RequestMapping("/api/")
public class UserCommandController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation(value = "Create a Car.", response = User.class)
    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_ADMIN', 'ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(value = "/users")
    public ResponseEntity<?> signup(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User created = userDetailsService.createUser(user);
        Role role = roleService.findByName("ROLE_USER");
        userDetailsService.addRole(created, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
