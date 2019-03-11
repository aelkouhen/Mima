package com.carhub.api.auth.services;

import com.carhub.api.auth.domain.Privilege;
import com.carhub.api.auth.domain.Role;
import com.carhub.api.auth.repositories.PrivilegeRepository;
import com.carhub.api.auth.repositories.RoleRepository;
import com.carhub.api.auth.utils.exception.ElementNotFoundException;
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

    public Role grantPrivilege(String role, String privilege){
        Role roleToUpdate = roleRepository.findByName(role);
        if(roleToUpdate == null)
            throw new ElementNotFoundException(Role.class);

        Privilege privilegeToGrant = privilegeRepository.findByName(privilege);
        if(privilegeToGrant == null)
            throw new ElementNotFoundException(Privilege.class);

        return grantPrivilege(roleToUpdate, privilegeToGrant);
    }

    public Role grantPrivilege(Role role, Privilege privilege){
        if (!role.getPrivileges().contains(privilege))
            role.addPrivilege(privilege);

        return roleRepository.save(role);
    }

    public Role revokePrivilege(String role, String privilege){
        Role roleToUpdate = roleRepository.findByName(role);
        if(roleToUpdate == null)
            throw new ElementNotFoundException(Role.class);

        Privilege privilegeToRevoke = privilegeRepository.findByName(privilege);
        if(privilegeToRevoke == null)
            throw new ElementNotFoundException(Privilege.class);

        return revokePrivilege(roleToUpdate, privilegeToRevoke);
    }

    public Role revokePrivilege(Role role, Privilege privilege){
        if (role.getPrivileges().contains(privilege))
            role.getPrivileges().remove(privilege);

        return roleRepository.save(role);
    }

    public Role findByName(String name){
        Role roleToUpdate = roleRepository.findByName(name);
        if(roleToUpdate == null)
            throw new ElementNotFoundException(Role.class);

        return roleRepository.save(roleToUpdate);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public void deleteRole(String role) {
        Role roleToDelete = roleRepository.findByName(role);
        if (roleToDelete == null)
            throw new ElementNotFoundException(Role.class);

        roleRepository.delete(roleToDelete);
    }
}
