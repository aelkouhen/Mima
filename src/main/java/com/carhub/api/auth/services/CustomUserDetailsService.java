package com.carhub.api.auth.services;

import com.carhub.api.auth.domain.Role;
import com.carhub.api.auth.domain.User;
import com.carhub.api.auth.repositories.RoleRepository;
import com.carhub.api.auth.repositories.UserRepository;
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
import java.util.UUID;


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

    public UserDetails loadUserById(UUID id) {
        User user = userRepository.findById(id).get();

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

    public long countAllUsers(){
        return userRepository.count();
    }

    public User createUser(User user){
        return userRepository.save(user);
    }


    public User addRole(User user, Role role){
        User userToUpdate = userRepository.findById(user.getId()).get();
        Role roleToAdd = roleRepository.findById(role.getId()).get();
        userToUpdate.addRole(roleToAdd);
        return userRepository.save(userToUpdate);
    }

    public User updateUsername(User user, String username){
        User userToUpdate = userRepository.findById(user.getId()).get();
        userToUpdate.setUsername(username);
        return userRepository.save(userToUpdate);
    }

    public User updatePassword(User user, String password){
        User userToUpdate = userRepository.findById(user.getId()).get();
        userToUpdate.setPassword(password);
        return userRepository.save(userToUpdate);
    }

    public User updateEmail(User user, String email){
        User userToUpdate = userRepository.findById(user.getId()).get();
        userToUpdate.setEmail(email);
        return userRepository.save(userToUpdate);
    }
}
