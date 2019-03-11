package com.carhub.api.auth.controllers.command;

import com.carhub.api.auth.domain.Privilege;
import com.carhub.api.auth.services.PrivilegeService;
import com.carhub.api.auth.utils.exception.ElementNotCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class PrivilegeCommandController {

    @Autowired
    private PrivilegeService privilegeService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(value = "/privileges")
    public ResponseEntity<?> createPrivilege(@RequestBody Privilege privilege){
        Privilege created = privilegeService.createPrivilege(privilege);
        if(created == null) throw new ElementNotCreatedException(Privilege.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @DeleteMapping(value = "/privileges/{privilege}")
    public ResponseEntity<?> deletePrivilege(@PathVariable(value = "privilege") String privilege){
        privilegeService.deletePrivilege(privilege);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
