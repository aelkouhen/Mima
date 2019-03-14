package com.carhub.api.auth.controllers.query;

import com.carhub.api.auth.domain.User;
import com.carhub.api.auth.services.CustomUserDetailsService;
import com.carhub.api.auth.services.RoleService;
import com.carhub.api.auth.utils.exception.ElementNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

@Api(value = "User", tags = {"User Queries"}, description = "This API queries the User concept.")
@RestController
@RequestMapping("/api/")
public class UserQueryController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "Get current User", notes = "Returns details on the current user.", response = UserDetails.class)
    @PreAuthorize("#oauth2.isUser()")
    @GetMapping(value = "/users/iam")
    @ResponseBody
    public ResponseEntity<?> aboutMe(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails result = userDetailsService.loadUserByUsername(authentication.getPrincipal().toString());
        if (result == null) throw new ElementNotFoundException(User.class);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "List the Users : Retrieve the Users list paged and sorted by field.", notes = "It takes the page number, a size for each page, a sorting order and the field on which the list is sorted.", responseContainer = "List")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/users")
    @ResponseBody
    public ResponseEntity<?> getUsers(@ApiParam(name = "page", example="0", value = "The page number.", required = true) @RequestParam int page,
                                      @ApiParam(name = "size", example="10", value = "The size of the page.", required = true) @RequestParam int size,
                                      @ApiParam(name = "order", example="DESC", value = "The sorting order (DESC or ASC).", required = true) @RequestParam(name = "order") String sortDirection,
                                      @ApiParam(name = "field", example="username", value = "The sort field name.", required = true) @RequestParam(name = "field") String sort){
        List<User> results;
        if(sortDirection.toLowerCase().equals("asc"))
            results = userDetailsService.getUsersListAsc(page, size, sort);
        else
            results = userDetailsService.getUsersListDesc(page, size, sort);

        if (results == null || results.isEmpty()) throw new ElementNotFoundException(User.class);
        return ResponseEntity.ok(results);
    }

    @ApiOperation(value = "Count the Users.", response = long.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/users/count")
    @ResponseBody
    public long countAllUsers(){
        return userDetailsService.countAllUsers();
    }

    @ApiOperation(value = "Find User by username", responseContainer = "List")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/users/find", params = "username")
    @ResponseBody
    public ResponseEntity<UserDetails> findUserByUsername(@ApiParam(name = "username", value = "The filtering expression.", required = true) @RequestParam(name = "username") String username){
        UserDetails result = userDetailsService.loadUserByUsername(username);
        if (result == null) throw new ElementNotFoundException(User.class);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Get User's roles.", responseContainer = "List")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/users/{username}/roles")
    @ResponseBody
    public ResponseEntity<Collection<? extends GrantedAuthority>> getAuthorities(@ApiParam(name = "username", value = "The username.", required = true) @PathVariable(name = "username") String username){
        Collection<? extends GrantedAuthority> results = userDetailsService.loadUserByUsername(username).getAuthorities();
        if (results == null || results.isEmpty()) throw new ElementNotFoundException(User.class);
        return ResponseEntity.ok(results);
    }
}