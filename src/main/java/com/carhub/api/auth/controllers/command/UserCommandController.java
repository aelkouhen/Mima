package com.carhub.api.auth.controllers.command;

import com.carhub.api.auth.domain.User;
import com.carhub.api.auth.services.CustomUserDetailsService;
import com.carhub.api.auth.services.PrivilegeService;
import com.carhub.api.auth.services.RoleService;
import com.carhub.api.auth.utils.exception.ElementNotCreatedException;
import com.carhub.api.auth.utils.exception.ElementNotDeletedException;
import com.carhub.api.auth.utils.exception.ElementNotUpdatedException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Api(value = "User", tags = {"User Commands"}, description = "This API commands the User concept.")
@RestController
@RequestMapping("/v1")
public class UserCommandController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation(value = "Create an new User", response = User.class)
    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(value = "/users")
    public ResponseEntity<?> signup(@ApiParam(name = "user", value = "A User object.", required = true) @RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User created = userDetailsService.createUser(user);
        if(created == null) throw new ElementNotCreatedException(User.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @ApiOperation(value = "Update a User.", response = User.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @PutMapping(value = "/users/{username}")
    public ResponseEntity<?> updateUser(@ApiParam(name = "username", value = "The username.", required = true) @PathVariable(value = "username") String username, @RequestBody User user){
        User updated = userDetailsService.updateUser(username, user);
        if(updated == null) throw new ElementNotUpdatedException(User.class);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @ApiOperation(value = "Update the Users's password.", response = User.class)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @ResponseBody
    @PatchMapping(value = "/users/{username}", params = "password")
    public ResponseEntity<?> updateUserPassword(@ApiParam(name = "username", value = "The username.", required = true) @PathVariable(value = "username") String username, @ApiParam(name = "role", value = "The role name.", required = true) @RequestParam(name = "password")  String password){
        User updated = userDetailsService.updatePassword(username, passwordEncoder.encode(password));
        if(updated == null) throw new ElementNotUpdatedException(User.class);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @ApiOperation(value = "Update the Users's email.", response = User.class)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @ResponseBody
    @PatchMapping(value = "/users/{username}", params = "email")
    public ResponseEntity<?> updateUserEmail(@ApiParam(name = "username", value = "The username.", required = true) @PathVariable(value = "username") String username, @ApiParam(name = "email", value = "The email.", required = true) @RequestParam(name = "email") String email){
        User updated = userDetailsService.updateEmail(username, email);
        if(updated == null) throw new ElementNotUpdatedException(User.class);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @ApiOperation(value = "Grant a role to the User.", response = User.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @PatchMapping(value = "/users/{username}/grant")
    public ResponseEntity<?> updateUserGrantRole(@ApiParam(name = "username", value = "The username.", required = true) @PathVariable(value = "username") String username, @ApiParam(name = "role", value = "The role name.", required = true) @RequestParam(name = "role") String role){
        User updated = userDetailsService.grantRole(username, role);
        if(updated == null) throw new ElementNotUpdatedException(User.class);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @ApiOperation(value = "Revoke a role from the User.", response = User.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @PatchMapping(value = "/users/{username}/revoke")
    public ResponseEntity<?> updateUserRevokeRole(@ApiParam(name = "username", value = "The username.", required = true) @PathVariable(value = "username") String username, @ApiParam(name = "role", value = "The role name.", required = true) @RequestParam(name = "role") String role){
        User updated = userDetailsService.revokeRole(username, role);
        if(updated == null) throw new ElementNotUpdatedException(User.class);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @ApiOperation(value = "Delete the User.", response = User.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @DeleteMapping(value = "/users/{username}")
    public ResponseEntity<?> deleteUser(@ApiParam(name = "username", value = "The username.", required = true) @PathVariable(value = "username") String username){
        if(username.equals("admin"))
            throw new ElementNotDeletedException(User.class);

        userDetailsService.deleteUser(username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
