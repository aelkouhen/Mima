package com.carhub.api.auth.controllers.command;

import com.carhub.api.auth.domain.Privilege;
import com.carhub.api.auth.services.PrivilegeService;
import com.carhub.api.auth.utils.exception.ElementNotCreatedException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(value = "Privilege", tags = {"Privilege Commands"}, description = "This API commands the Privilege concept.")
@RestController
@RequestMapping("/api/")
public class PrivilegeCommandController {

    @Autowired
    private PrivilegeService privilegeService;

    @ApiOperation(value = "Create an new Privilege", response = Privilege.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(value = "/privileges")
    public ResponseEntity<?> createPrivilege(@ApiParam(name = "privilege", value = "A Privilege object.", required = true) @RequestBody Privilege privilege){
        Privilege created = privilegeService.createPrivilege(privilege);
        if(created == null) throw new ElementNotCreatedException(Privilege.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @ApiOperation(value = "Delete the Privilege.", response = Privilege.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @DeleteMapping(value = "/privileges/{privilege}")
    public ResponseEntity<?> deletePrivilege(@ApiParam(name = "privilege", value = "The privilege's name.", required = true) @PathVariable(value = "privilege") String privilege){
        privilegeService.deletePrivilege(privilege);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
