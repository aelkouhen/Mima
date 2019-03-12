package com.carhub.api.auth.repositories;

import com.carhub.api.auth.domain.ClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientDetailsRepository extends JpaRepository<ClientDetails, UUID> {
    ClientDetails findByClientId(String clientId);
}
