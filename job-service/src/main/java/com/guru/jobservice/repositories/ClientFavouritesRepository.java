package com.guru.jobservice.repositories;

import com.guru.jobservice.model.ClientFavourites;
import com.guru.jobservice.model.ClientFavourites.ClientFavouriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientFavouritesRepository extends JpaRepository<ClientFavourites, ClientFavouriteId> {

    @Procedure(name = "add_to_favourites")
    void addToFavourites(@Param("_client_id") UUID clientId, @Param("_freelancer_id") UUID freelancerId);

    @Procedure(name = "remove_from_favourites")
    void removeFromFavourites(@Param("_client_id") UUID clientId, @Param("_freelancer_id") UUID freelancerId);
}