package com.carhub.api.auth.services;

import com.carhub.api.auth.domain.Role;
import com.carhub.api.auth.domain.User;
import com.carhub.api.auth.repositories.RoleRepository;
import com.carhub.api.auth.repositories.UserRepository;
import com.carhub.api.auth.utils.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service(value = "userDetailsService")
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null)
            throw new BadCredentialsException("Bad credentials");

        new AccountStatusUserDetailsChecker().check(user);

        return user;
    }

    public Page<User> getUsersPage(int page, int size, Sort.Direction sortDirection, String sort) {
        return userRepository.findAll(PageRequest.of(page, size, sortDirection, sort));
    }

    public List<User> getUsersListAsc(int page, int size, String sort) {
        return getUsersPage(page, size, Sort.Direction.ASC, sort).getContent();
    }

    public List<User> getUsersListDesc(int page, int size, String sort) {
        return getUsersPage(page, size, Sort.Direction.DESC, sort).getContent();
    }

    public long countAllUsers() {
        return userRepository.count();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }


    public User updateUser(String username, User user) {
        User userToUpdate = userRepository.findByUsername(username);
        if (user == null)
            throw new ElementNotFoundException(User.class);

        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setEmail(user.getEmail());
        user.getRoles().forEach(r -> {
            userToUpdate.addRole(r);
        });
        return userRepository.save(userToUpdate);
    }

    public User grantRole(String username, String role) {
        User userToUpdate = userRepository.findByUsername(username);
        if (userToUpdate == null)
            throw new ElementNotFoundException(User.class);

        Role roleToGrant = roleRepository.findByName(role);
        if (roleToGrant == null)
            throw new ElementNotFoundException(Role.class);

        return addRole(userToUpdate, roleToGrant);
    }

    public User revokeRole(String username, String role) {
        User userToUpdate = userRepository.findByUsername(username);
        if (userToUpdate == null)
            throw new ElementNotFoundException(User.class);

        Role roleToRevoke = roleRepository.findByName(role);
        if (roleToRevoke == null)
            throw new ElementNotFoundException(Role.class);

        return revokeRole(userToUpdate, roleToRevoke);
    }

    public User addRole(User user, Role role) {
        if (!user.getRoles().contains(role))
            user.addRole(role);

        return userRepository.save(user);
    }

    public User revokeRole(User user, Role role) {
        if (user.getRoles().contains(role))
            user.getRoles().remove(role);

        return userRepository.save(user);
    }


    public User updatePassword(String username, String password) {
        User userToUpdate = userRepository.findByUsername(username);
        if (userToUpdate == null)
            throw new ElementNotFoundException(User.class);

        return updatePassword(userToUpdate, password);
    }

    private User updatePassword(User user, String password) {
        User userToUpdate = userRepository.findByUsername(user.getUsername());
        if (userToUpdate == null)
            throw new ElementNotFoundException(User.class);

        userToUpdate.setPassword(password);
        return userRepository.save(userToUpdate);
    }

    public User updateEmail(String username, String email) {
        User userToUpdate = userRepository.findByUsername(username);
        if (userToUpdate == null)
            throw new ElementNotFoundException(User.class);

        return updateEmail(userToUpdate, email);
    }

    private User updateEmail(User user, String email) {
        User userToUpdate = userRepository.findByUsername(user.getUsername());
        if (userToUpdate == null)
            throw new ElementNotFoundException(User.class);

        userToUpdate.setEmail(email);
        return userRepository.save(userToUpdate);
    }

    public void deleteUser(String username) {
        User userToDelete = userRepository.findByUsername(username);
        if (userToDelete == null)
            throw new ElementNotFoundException(User.class);

        userRepository.delete(userToDelete);
    }
}