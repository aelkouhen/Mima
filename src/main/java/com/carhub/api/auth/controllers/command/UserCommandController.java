package com.carhub.api.auth.controllers.command;

import com.carhub.api.auth.domain.Role;
import com.carhub.api.auth.domain.User;
import com.carhub.api.auth.services.CustomUserDetailsService;
import com.carhub.api.auth.services.RoleService;
import com.carhub.api.auth.utils.exception.ElementNotCreatedException;
import com.carhub.api.auth.utils.exception.ElementNotDeletedException;
import com.carhub.api.auth.utils.exception.ElementNotUpdatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class UserCommandController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(value = "/users")
    public ResponseEntity<?> signup(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User created = userDetailsService.createUser(user);
        if(created == null) throw new ElementNotCreatedException(User.class);

        Role role = roleService.findByName("ROLE_USER");
        if(role == null) throw new ElementNotCreatedException(Role.class);

        userDetailsService.addRole(created, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @PutMapping(value = "/users/{username}")
    public ResponseEntity<?> updateUser(@PathVariable(value = "username") String username, @RequestBody User user){
        User updated = userDetailsService.updateUser(username, user);
        if(updated == null) throw new ElementNotUpdatedException(User.class);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @ResponseBody
    @PatchMapping(value = "/users/{username}", params = "password")
    public ResponseEntity<?> updateUserPassword(@PathVariable(value = "username") String username, @RequestParam(name = "password")  String password){
        User updated = userDetailsService.updatePassword(username, passwordEncoder.encode(password));
        if(updated == null) throw new ElementNotUpdatedException(User.class);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @ResponseBody
    @PatchMapping(value = "/users/{username}", params = "email")
    public ResponseEntity<?> updateUserEmail(@PathVariable(value = "username") String username, @RequestParam(name = "email") String email){
        User updated = userDetailsService.updateEmail(username, email);
        if(updated == null) throw new ElementNotUpdatedException(User.class);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @PatchMapping(value = "/users/{username}/grant")
    public ResponseEntity<?> updateUserGrantRole(@PathVariable(value = "username") String username, @RequestParam(name = "role") String role){
        User updated = userDetailsService.grantRole(username, role);
        if(updated == null) throw new ElementNotUpdatedException(User.class);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @PatchMapping(value = "/users/{username}/revoke")
    public ResponseEntity<?> updateUserRevokeRole(@PathVariable(value = "username") String username, @RequestParam(name = "role") String role){
        User updated = userDetailsService.revokeRole(username, role);
        if(updated == null) throw new ElementNotUpdatedException(User.class);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @DeleteMapping(value = "/users/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "username") String username){
        if(username.equals("admin"))
            throw new ElementNotDeletedException(User.class);

        userDetailsService.deleteUser(username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
