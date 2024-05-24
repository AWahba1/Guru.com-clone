-- CALL drop_procedure('create_saved_search');
CREATE OR REPLACE PROCEDURE create_saved_search(
	_name VARCHAR(255),
	_freelancer_id UUID, 
    _search_query VARCHAR(255) DEFAULT NULL,
    _category_id UUID DEFAULT NULL,
    _subcategory_id UUID DEFAULT NULL,
    _skill_id UUID DEFAULT NULL,
    _featured_only BOOLEAN DEFAULT NULL,
    _payment_terms text DEFAULT NULL,
    _location_ids text[] DEFAULT NULL,
    _sort_order VARCHAR(20) DEFAULT NULL,
    _status_list text[] DEFAULT NULL,
    _verified_only BOOLEAN DEFAULT NULL,
    _min_employer_spend INT DEFAULT NULL,
    _max_quotes_received INT DEFAULT NULL,
    _not_viewed BOOLEAN DEFAULT NULL,
    _not_applied BOOLEAN DEFAULT NULL
--     _client_id UUID DEFAULT NULL
)
AS $$
BEGIN
    INSERT INTO saved_searches (id, name, search_query, category_id, subcategory_id, skill_id, featured_only, payment_terms, location_ids, sort_order, status_list, verified_only, min_employer_spend, max_quotes_received, not_viewed, not_applied, freelancer_id)
    VALUES (gen_random_uuid(), _name, _search_query, _category_id, _subcategory_id, _skill_id, _featured_only, _payment_terms::payment_type, _location_ids, _sort_order, _status_list, _verified_only, _min_employer_spend, _max_quotes_received, _not_viewed, _not_applied, _freelancer_id);
END;
$$ LANGUAGE plpgsql;


CALL create_saved_search(
	_name := 'My saved search',
	_freelancer_id := '44444444-4444-4444-4444-444444444444',
    _search_query := 'Web Development Jobs',
    _category_id := '77777777-7777-7777-7777-777777777777', -- Software Development
    _subcategory_id := '00000000-0000-0000-0000-000000000001', -- Web Development
    _skill_id := 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', -- HTML
    _featured_only := TRUE,
    _payment_terms := 'fixed',
    _location_ids := ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'], -- New York
    _sort_order := 'newest',
    _status_list := ARRAY['Open', 'Under Review'],
    _verified_only := TRUE,
    _min_employer_spend := 500,
    _max_quotes_received := 10,
    _not_viewed := NULL,
    _not_applied := NULL
);

CALL create_saved_search(
	_name := 'My saved search 2',
	_freelancer_id := '44444444-4444-4444-4444-444444444444',
    _search_query := NULL,
    _category_id := NULL,
    _subcategory_id := NULL,
    _skill_id := NULL, 
    _featured_only := NULL,
    _payment_terms := NULL,
    _location_ids := NULL, 
    _sort_order := NULL,
    _status_list := NULL,
    _verified_only := NULL,
    _min_employer_spend := NULL,
    _max_quotes_received := NULL,
    _not_viewed := NULL,
    _not_applied := NULL
);

DROP FUNCTION IF EXISTS get_saved_search_by_id;
CREATE OR REPLACE FUNCTION get_saved_search_by_id(
    _search_id UUID
)
RETURNS TABLE (
    id UUID,
    name VARCHAR(255),
	freelancer_id UUID,
    search_query VARCHAR(255),
    category_id UUID,
    subcategory_id UUID,
    skill_id UUID,
    featured_only BOOLEAN,
    payment_terms text,
    location_ids text[],
    sort_order VARCHAR(20),
    status_list text[],
    verified_only BOOLEAN,
    min_employer_spend INT,
    max_quotes_received INT,
    not_viewed BOOLEAN,
    not_applied BOOLEAN
)
AS $$
BEGIN
    RETURN QUERY 
        SELECT 
            ss.id,
            ss.name,
			ss.freelancer_id,
            ss.search_query,
            ss.category_id,
            ss.subcategory_id,
            ss.skill_id,
            ss.featured_only,
            ss.payment_terms::text,
            ss.location_ids,
            ss.sort_order,
            ss.status_list,
            ss.verified_only,
            ss.min_employer_spend,
            ss.max_quotes_received,
            ss.not_viewed,
            ss.not_applied
        FROM 
            saved_searches ss
        WHERE 
            ss.id = _search_id;
END;
$$ LANGUAGE plpgsql;

SELECT * FROM get_saved_search_by_id('55555555-5555-5555-5555-555555555555');


DROP FUNCTION IF EXISTS get_freelancer_saved_searches;
CREATE OR REPLACE FUNCTION get_freelancer_saved_searches(
    _freelancer_id UUID
)
RETURNS TABLE (
    id UUID,
    name VARCHAR(255),
    search_query VARCHAR(255),
    category_id UUID,
    subcategory_id UUID,
    skill_id UUID,
    featured_only BOOLEAN,
    payment_terms payment_type,
    location_ids TEXT[],
    sort_order VARCHAR(20),
    status_list TEXT[],
    verified_only BOOLEAN,
    min_employer_spend INT,
    max_quotes_received INT,
    not_viewed BOOLEAN,
    not_applied BOOLEAN,
	freelancer_id UUID
)
AS $$
BEGIN
    RETURN QUERY
        SELECT
            ss.id,
            ss.name,
            ss.search_query,
            ss.category_id,
            ss.subcategory_id,
            ss.skill_id,
            ss.featured_only,
            ss.payment_terms,
            ss.location_ids,
            ss.sort_order,
            ss.status_list,
            ss.verified_only,
            ss.min_employer_spend,
            ss.max_quotes_received,
            ss.not_viewed,
            ss.not_applied,
            ss.freelancer_id
        FROM
            saved_searches ss
        WHERE
            ss.freelancer_id = _freelancer_id;
END;
$$ LANGUAGE plpgsql;

-- SELECT * FROM get_freelancer_saved_searches('44444444-4444-4444-4444-444444444444');

-- CALL drop_procedure('update_saved_search');
CREATE OR REPLACE PROCEDURE update_saved_search(
    _id UUID,
    _name VARCHAR(255),
    _search_query VARCHAR(255) DEFAULT NULL,
    _category_id UUID DEFAULT NULL,
    _subcategory_id UUID DEFAULT NULL,
    _skill_id UUID DEFAULT NULL,
    _featured_only BOOLEAN DEFAULT NULL,
    _payment_terms text DEFAULT NULL,
    _location_ids text[] DEFAULT NULL,
    _sort_order VARCHAR(20) DEFAULT NULL,
    _status_list text[] DEFAULT NULL,
    _verified_only BOOLEAN DEFAULT NULL,
    _min_employer_spend INT DEFAULT NULL,
    _max_quotes_received INT DEFAULT NULL,
    _not_viewed BOOLEAN DEFAULT NULL,
    _not_applied BOOLEAN DEFAULT NULL
)
AS $$
BEGIN
    UPDATE saved_searches ss
    SET
        name = _name,
        search_query = _search_query,
       	category_id = _category_id,
        subcategory_id = _subcategory_id,
        skill_id = _skill_id,
        featured_only = _featured_only,
        payment_terms = _payment_terms::payment_type,
        location_ids = _location_ids,
        sort_order = _sort_order,
        status_list = _status_list,
        verified_only = _verified_only,
        min_employer_spend = _min_employer_spend,
        max_quotes_received = _max_quotes_received,
        not_viewed = _not_viewed,
        not_applied = _not_applied
    WHERE
        id = _id;
END;
$$ LANGUAGE plpgsql;

-- CALL update_saved_search(
-- 	_id := '82399122-79a9-47b7-ba4a-f7c1ac76cfe4',
-- 	_name := 'My Updated search',
--     _search_query := 'New Search Query',
--     _category_id := '77777777-7777-7777-7777-777777777777', -- Software Development
--     _subcategory_id := '00000000-0000-0000-0000-000000000001', -- Web Development
--     _skill_id := 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', -- HTML
--     _featured_only := TRUE,
--     _payment_terms := 'fixed',
--     _location_ids := ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'], -- New York
--     _sort_order := 'newest',
--     _status_list := ARRAY['Open', 'Under Review'],
--     _verified_only := TRUE,
--     _min_employer_spend := 500,
--     _max_quotes_received := 10,
--     _not_viewed := NULL,
--     _not_applied := NULL
-- );


-- CALL drop_procedure('delete_saved_search');
CREATE OR REPLACE PROCEDURE delete_saved_search(
    _search_id UUID
)
AS $$
BEGIN
    DELETE FROM saved_searches
    WHERE id = _search_id;
END;
$$ LANGUAGE plpgsql;

-- CALL delete_saved_search('11111111-1111-1111-1111-111111111111');

