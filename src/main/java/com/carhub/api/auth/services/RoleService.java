package com.carhub.api.auth.services;

import com.carhub.api.auth.domain.Privilege;
import com.carhub.api.auth.domain.Role;
import com.carhub.api.auth.repositories.PrivilegeRepository;
import com.carhub.api.auth.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;


    public Role createRole(Role role){
        return roleRepository.save(role);
    }

    public Role addPrivilege(Role role, Privilege privilege){
        Role roleToUpdate = roleRepository.findById(role.getId()).get();
        Privilege privilegeToAdd = privilegeRepository.findById(role.getId()).get();
        roleToUpdate.addPrivilege(privilegeToAdd);
        return roleRepository.save(roleToUpdate);
    }

    public Role updateRoleName(Role role, String name){
        Role roleToUpdate = roleRepository.findById(role.getId()).get();
        roleToUpdate.setName(name);
        return roleRepository.save(roleToUpdate);
    }

    public Role findByName(String name){
        Role roleToUpdate = roleRepository.findByName(name);
        roleToUpdate.setName(name);
        return roleRepository.save(roleToUpdate);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

}
