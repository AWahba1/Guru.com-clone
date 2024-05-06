package com.guru.jobservice.controllers;

import com.guru.jobservice.dtos.SavedSearchRequestDTO;
import com.guru.jobservice.model.SavedSearch;
import com.guru.jobservice.services.SavedSearchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/saved-searches")
public class SavedSearchController {
    private final SavedSearchService savedSearchService;

    @Autowired
    public SavedSearchController(SavedSearchService savedSearchService) {
        this.savedSearchService = savedSearchService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavedSearch> getSavedSearchById(@PathVariable UUID id) {
        SavedSearch savedSearch = savedSearchService.getSavedSearchById(id);
        return new ResponseEntity<>(savedSearch, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createSavedSearch(@Valid @RequestBody SavedSearchRequestDTO savedSearchRequest) {
        savedSearchService.createSavedSearch(savedSearchRequest);
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateSavedSearch(@PathVariable UUID id, @Valid @RequestBody SavedSearchRequestDTO savedSearchRequest) {
        savedSearchService.updateSavedSearch(id, savedSearchRequest);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSavedSearch(@PathVariable UUID id) {
        savedSearchService.deleteSavedSearch(id);
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }

}

