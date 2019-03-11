package com.carhub.api.auth.controllers.query;

import com.carhub.api.auth.domain.Privilege;
import com.carhub.api.auth.services.PrivilegeService;
import com.carhub.api.auth.utils.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class PrivilegeQueryController {

    @Autowired
    private PrivilegeService privilegeService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/privileges")
    @ResponseBody
    public ResponseEntity<?> getPrivileges(){

        List<Privilege> results = privilegeService.getPrivileges();
        if (results == null || results.isEmpty()) throw new ElementNotFoundException(Privilege.class);

        return ResponseEntity.ok(results);
    }
}
