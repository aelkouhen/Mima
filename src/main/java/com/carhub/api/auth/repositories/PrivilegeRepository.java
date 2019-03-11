package com.carhub.api.auth.repositories;

import com.carhub.api.auth.domain.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, UUID> {
    Privilege findByName(String name);
}
