package com.carhub.api.auth.services;

import com.carhub.api.auth.domain.ClientDetails;
import com.carhub.api.auth.repositories.ClientDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;


@Service
@Transactional
public class CustomClientDetailsService implements ClientDetailsService {

    @Autowired
    private ClientDetailsRepository clientDetailsRepository;

    @Override
    public org.springframework.security.oauth2.provider.ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDetails client = clientDetailsRepository.findByClientId(clientId);

        BaseClientDetails base = new BaseClientDetails(client.getClientId(), client.getResourceIds(), client.getScope(), client.getAuthorizedGrantTypes(), client.getAuthorities());
        base.setClientSecret(client.getClientSecret());
        base.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
        base.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
        base.setAutoApproveScopes(Arrays.stream(client.getScope().split("\\s*,\\s*")).collect(Collectors.toList()));
        return base;
    }

    public ClientDetails createClient(ClientDetails user) {
        return clientDetailsRepository.save(user);
    }
}