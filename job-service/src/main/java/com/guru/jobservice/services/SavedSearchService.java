package com.guru.jobservice.services;

import com.guru.jobservice.dtos.SavedSearchRequestDTO;
import com.guru.jobservice.exceptions.ResourceNotFoundException;
import com.guru.jobservice.model.SavedSearch;
import com.guru.jobservice.repositories.SavedSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SavedSearchService {
    private final SavedSearchRepository savedSearchRepository;

    @Autowired
    public SavedSearchService(SavedSearchRepository savedSearchRepository) {
        this.savedSearchRepository = savedSearchRepository;
    }

    public void createSavedSearch(SavedSearchRequestDTO savedSearchRequest) {
        savedSearchRepository.createSavedSearch(
                savedSearchRequest.getName(),
                savedSearchRequest.getFreelancerId(),
                savedSearchRequest.getSearchQuery(),
                savedSearchRequest.getCategoryId(),
                savedSearchRequest.getSubcategoryId(),
                savedSearchRequest.getSkillId(),
                savedSearchRequest.getFeaturedOnly(),
                savedSearchRequest.getPaymentTerms(),
                savedSearchRequest.getLocationIds(),
                savedSearchRequest.getSortOrder(),
                savedSearchRequest.getStatusList(),
                savedSearchRequest.getVerifiedOnly(),
                savedSearchRequest.getMinEmployerSpend(),
                savedSearchRequest.getMaxQuotesReceived(),
                savedSearchRequest.getNotViewed(),
                savedSearchRequest.getNotApplied()
        );
    }

    public SavedSearch getSavedSearchById(UUID savedSearchId)
    {
        SavedSearch savedSearch = savedSearchRepository.getSavedSearchById(savedSearchId);
        if (savedSearch == null) {
            throw new ResourceNotFoundException();
        }
        return savedSearch;
    }

    public void deleteSavedSearch(UUID savedSearchId)
    {
        SavedSearch savedSearch = savedSearchRepository.getSavedSearchById(savedSearchId);
        if (savedSearch == null) {
            throw new ResourceNotFoundException();
        }

        savedSearchRepository.deleteSavedSearch(savedSearchId);
    }

    public void updateSavedSearch(UUID savedSearchId, SavedSearchRequestDTO savedSearchRequest) {

        SavedSearch savedSearch = savedSearchRepository.getSavedSearchById(savedSearchId);
        if (savedSearch == null) {
            throw new ResourceNotFoundException();
        }

        savedSearchRepository.updateSavedSearch(
                savedSearchId,
                savedSearchRequest.getName(),
                savedSearchRequest.getSearchQuery(),
                savedSearchRequest.getCategoryId(),
                savedSearchRequest.getSubcategoryId(),
                savedSearchRequest.getSkillId(),
                savedSearchRequest.getFeaturedOnly(),
                savedSearchRequest.getPaymentTerms(),
                savedSearchRequest.getLocationIds(),
                savedSearchRequest.getSortOrder(),
                savedSearchRequest.getStatusList(),
                savedSearchRequest.getVerifiedOnly(),
                savedSearchRequest.getMinEmployerSpend(),
                savedSearchRequest.getMaxQuotesReceived(),
                savedSearchRequest.getNotViewed(),
                savedSearchRequest.getNotApplied()
        );
    }


}

