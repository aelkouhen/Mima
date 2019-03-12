package com.carhub.api.auth.controllers.query;

import com.carhub.api.auth.domain.User;
import com.carhub.api.auth.services.CustomUserDetailsService;
import com.carhub.api.auth.services.RoleService;
import com.carhub.api.auth.utils.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class UserQueryController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private RoleService roleService;

    @PreAuthorize("#oauth2.isUser()")
    @GetMapping(value = "/users/iam")
    @ResponseBody
    public ResponseEntity<?> aboutMe(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails result = userDetailsService.loadUserByUsername(authentication.getPrincipal().toString());
        if (result == null) throw new ElementNotFoundException(User.class);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/users")
    @ResponseBody
    public ResponseEntity<?> getUsers( @RequestParam int page,
                                       @RequestParam int size,
                                       @RequestParam(name = "order") String sortDirection,
                                       @RequestParam(name = "field") String sort){
        List<User> results;
        if(sortDirection.toLowerCase().equals("asc"))
            results = userDetailsService.getUsersListAsc(page, size, sort);
        else
            results = userDetailsService.getUsersListDesc(page, size, sort);

        if (results == null || results.isEmpty()) throw new ElementNotFoundException(User.class);
        return ResponseEntity.ok(results);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/users/count")
    @ResponseBody
    public long countAllUsers(){
        return userDetailsService.countAllUsers();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/users/find", params = "username")
    @ResponseBody
    public ResponseEntity<UserDetails> findUserByUsername(@RequestParam(name = "username") String username){
        UserDetails result = userDetailsService.loadUserByUsername(username);
        if (result == null) throw new ElementNotFoundException(User.class);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/users/{username}/roles")
    @ResponseBody
    public ResponseEntity<Collection<? extends GrantedAuthority>> getAuthorities(@PathVariable(name = "username") String username){
        Collection<? extends GrantedAuthority> results = userDetailsService.loadUserByUsername(username).getAuthorities();
        if (results == null || results.isEmpty()) throw new ElementNotFoundException(User.class);
        return ResponseEntity.ok(results);
    }
}