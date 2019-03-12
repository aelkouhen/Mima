package com.carhub.api.auth.services;

import com.carhub.api.auth.domain.CustomClientDetails;
import com.carhub.api.auth.repositories.ClientDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class CustomClientDetailsService implements ClientDetailsService {

    @Autowired
    private ClientDetailsRepository clientDetailsRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDetails client = clientDetailsRepository.findByClientId(clientId);

        return client;
    }

    public CustomClientDetails createClient(CustomClientDetails user) {
        return clientDetailsRepository.save(user);
    }
}