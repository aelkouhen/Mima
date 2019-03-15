package com.carhub.api.auth.controllers.query;

import com.carhub.api.auth.domain.Privilege;
import com.carhub.api.auth.domain.Role;
import com.carhub.api.auth.services.PrivilegeService;
import com.carhub.api.auth.services.RoleService;
import com.carhub.api.auth.utils.exception.ElementNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Role", tags = {"Role Queries"}, description = "This API queries the Role concept.")
@RestController
@RequestMapping("/api/")
public class RoleQueryController {

    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "List the Roles.", responseContainer = "List")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/roles")
    @ResponseBody
    public ResponseEntity<?> getRoles(){

        List<Role> results = roleService.getRoles();
        if (results == null || results.isEmpty()) throw new ElementNotFoundException(Role.class);

        return ResponseEntity.ok(results);
    }

    @ApiOperation(value = "Find Role by name", response = Role.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/roles/find", params = "name")
    @ResponseBody
    public ResponseEntity<Role> findRoleByname(@ApiParam(name = "name", value = "The role's name.", required = true) @RequestParam(name = "name") String name){
        Role result = roleService.findByName(name);
        if (result == null) throw new ElementNotFoundException(Role.class);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Get Role's privileges.", responseContainer = "List")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/roles/{name}/privileges")
    @ResponseBody
    public ResponseEntity<List<Privilege>> getPrivileges(@ApiParam(name = "name", value = "The role's name.", required = true) @PathVariable(name = "name") String name){
        List<Privilege> results = roleService.findByName(name).getPrivileges();
        if (results == null || results.isEmpty()) throw new ElementNotFoundException(Privilege.class);
        return ResponseEntity.ok(results);
    }
}
