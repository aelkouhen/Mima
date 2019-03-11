package com.carhub.api.auth.services;

import com.carhub.api.auth.domain.Privilege;
import com.carhub.api.auth.repositories.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrivilegeService {

    @Autowired
    private PrivilegeRepository privilegeRepository;


    public Privilege createPrivilege(Privilege privilege){
        return privilegeRepository.save(privilege);
    }


    public Privilege updatePrivilegeName(Privilege privilege, String name){
        Privilege privilegeToUpdate = privilegeRepository.findById(privilege.getId()).get();
        privilegeToUpdate.setName(name);
        return privilegeRepository.save(privilegeToUpdate);
    }
}
