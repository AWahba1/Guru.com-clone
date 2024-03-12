CREATE KEYSPACE client
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE saved_searches (
    search_id UUID,
    search_name TEXT,
    user_id UUID,
    category TEXT,
    subcategory TEXT,
    skill SET<TEXT>,
    payment_terms TEXT,
    quotes_received INT,
    location SET<TEXT>,
    featured BOOLEAN,
    applied BOOLEAN,
    not_viewed BOOLEAN,
    verified_client BOOLEAN,
    min_employer_spend int,
    PRIMARY KEY((search_name, user_id))
);

-- Get User Saved Searches
-- @user_id uuid
SELECT * FROM saved_searches WHERE user_id = '123';

-- Insert Saved Search query
/* @user_id uuid 
-- @category text (optional)
-- @subcategory text (optional)
-- @skill text (optional)
-- @payment_terms text (optional)
-- @quotes_received int (optional)
-- @location set<text> (optional)
-- @featured boolean (optional)
-- @applied boolean (optional)
-- @not_viewed boolean (optional)
-- @verified_client boolean (optional)
-- @min_employer_spend int (optional) */
INSERT INTO saved_searches (search_id, user_id, category, subcategory, skill, payment_terms, quotes_received, location, featured, applied, not_viewed, verified_client, employer_spend)
VALUES (uuid(), '123', 'Programming', 'Web Development', 'JavaScript', 'Fixed Price', 5, {'New York', 'London'}, true, false, true, true, 1000);






