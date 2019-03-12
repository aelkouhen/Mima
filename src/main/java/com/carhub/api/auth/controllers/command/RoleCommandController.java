package com.carhub.api.auth.controllers.command;

import com.carhub.api.auth.domain.Role;
import com.carhub.api.auth.services.RoleService;
import com.carhub.api.auth.utils.exception.ElementNotCreatedException;
import com.carhub.api.auth.utils.exception.ElementNotDeletedException;
import com.carhub.api.auth.utils.exception.ElementNotUpdatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class RoleCommandController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(value = "/roles")
    public ResponseEntity<?> createRole(@RequestBody Role role){
        Role created = roleService.createRole(role);
        if(created == null) throw new ElementNotCreatedException(Role.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @PatchMapping(value = "/roles/{role}/grant")
    public ResponseEntity<?> updateRoleGrantPrivilege(@PathVariable(value = "role") String role, @RequestParam(name = "privilege") String privilege){
        Role updated = roleService.grantPrivilege(role, privilege);
        if(updated == null) throw new ElementNotUpdatedException(Role.class);

        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @PatchMapping(value = "/roles/{role}/revoke")
    public ResponseEntity<?> updateRoleRevokeProvilege(@PathVariable(value = "role") String role, @RequestParam(name = "privilege") String privilege){
        Role updated = roleService.revokePrivilege(role, privilege);
        if(updated == null) throw new ElementNotUpdatedException(Role.class);

        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @DeleteMapping(value = "/roles/{role}")
    public ResponseEntity<?> deleteRole(@PathVariable(value = "role") String role){
        if(role.equals("ROLE_ADMIN"))
            throw new ElementNotDeletedException(Role.class);

        roleService.deleteRole(role);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
