package com.guru.jobservice.services;

import com.guru.jobservice.repositories.ClientFavouritesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ClientFavouritesService {

    private final ClientFavouritesRepository clientFavouritesRepository;

    @Autowired
    public ClientFavouritesService(ClientFavouritesRepository clientFavouritesRepository) {
        this.clientFavouritesRepository = clientFavouritesRepository;
    }

    public void addToFavourites(UUID clientId, UUID freelancerId) {
        clientFavouritesRepository.addToFavourites(clientId, freelancerId);
    }

    public void removeFromFavourites(UUID clientId, UUID freelancerId) {
        clientFavouritesRepository.removeFromFavourites(clientId, freelancerId);
    }

}