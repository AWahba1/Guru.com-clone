package com.guru.jobservice.repositories;

import com.guru.jobservice.model.SavedSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SavedSearchRepository extends JpaRepository<SavedSearch, UUID> {

    @Query(value = "select * from get_saved_search_by_id(?1)", nativeQuery = true)
    SavedSearch getSavedSearchById(UUID savedSearchId);

    @Query(value = "select * from get_freelancer_saved_searches(?1)", nativeQuery = true)
    List<SavedSearch> getFreelancerSavedSearches(UUID freelancerId);

    @Procedure("create_saved_search")
    void createSavedSearch(
            String _name, UUID _freelancer_id, String _search_query,
            UUID _category_id, UUID _subcategory_id, UUID _skill_id, Boolean _featured_only,
            String _payment_terms, String[] _location_ids, String _sort_order, String[] _status_list,
            Boolean _verified_only, Integer _min_employer_spend,
            Integer _max_quotes_received, Boolean _not_viewed, Boolean _not_applied
    );

    @Procedure("update_saved_search")
    void updateSavedSearch(
            UUID _id, String _name, String _search_query,
            UUID _category_id, UUID _subcategory_id, UUID _skill_id, Boolean _featured_only,
            String _payment_terms, String[] _location_ids, String _sort_order, String[] _status_list,
            Boolean _verified_only, Integer _min_employer_spend,
            Integer _max_quotes_received, Boolean _not_viewed, Boolean _not_applied
    );

    @Procedure("delete_saved_search")
    void deleteSavedSearch(UUID savedSearchId);
}

