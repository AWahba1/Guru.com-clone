package com.guru.jobservice.controllers;

import com.guru.jobservice.services.ClientFavouritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/client-favourites")
public class ClientFavouritesController {

    private final ClientFavouritesService clientFavouritesService;

    @Autowired
    public ClientFavouritesController(ClientFavouritesService clientFavouritesService) {
        this.clientFavouritesService = clientFavouritesService;
    }

    @PostMapping("/{clientId}/add/{freelancerId}")
    public void addToFavourites(@PathVariable UUID clientId, @PathVariable UUID freelancerId) {
        clientFavouritesService.addToFavourites(clientId, freelancerId);
    }

    @DeleteMapping("/{clientId}/remove/{freelancerId}")
    public void removeFromFavourites(@PathVariable UUID clientId, @PathVariable UUID freelancerId) {
        clientFavouritesService.removeFromFavourites(clientId, freelancerId);
    }
}
