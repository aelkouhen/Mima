package com.carhub.api.auth.services;

import com.carhub.api.auth.domain.Privilege;
import com.carhub.api.auth.repositories.PrivilegeRepository;
import com.carhub.api.auth.utils.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PrivilegeService {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    public Privilege createPrivilege(Privilege privilege){
        return privilegeRepository.save(privilege);
    }

    public void deletePrivilege(String privilege){
        Privilege privilegeToDelete = privilegeRepository.findByName(privilege);
        if (privilegeToDelete == null)
            throw new ElementNotFoundException(Privilege.class);

        privilegeRepository.delete(privilegeToDelete);
    }

    public List<Privilege> getPrivileges() {
        return privilegeRepository.findAll();
    }
}
