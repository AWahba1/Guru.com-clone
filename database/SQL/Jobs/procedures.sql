CREATE OR REPLACE PROCEDURE create_job(
    _title VARCHAR(255),
	_description TEXT,
	_category_id UUID,
    _subcategory_id UUID,
	_featured BOOLEAN,
    _client_id UUID,
    _payment_type payment_type,
	_fixed_price_range price_range,
    _duration job_duration,
    _hours_per_week hours_per_week,
	_min_hourly_rate DECIMAL(10, 2),
    _max_hourly_rate DECIMAL(10, 2),
	_get_quotes_until DATE,
    _visibility job_visibility,
	_skills text[],
	_timezones text[],
	_locations text[]
)
LANGUAGE plpgsql
AS $$
DECLARE
    new_job_id UUID;
    skill_id UUID;
	location_id UUID;
	timezone_id UUID;
BEGIN
    INSERT INTO jobs (id, title, description, category_id, subcategory_id, featured, client_id, payment_type, fixed_price_range, duration, hours_per_week, min_hourly_rate, max_hourly_rate, get_quotes_until, visibility)
    VALUES (gen_random_uuid(), _title,  _description, _category_id, _subcategory_id, _featured, _client_id, _payment_type, _fixed_price_range, _duration, _hours_per_week, _min_hourly_rate, _max_hourly_rate, _get_quotes_until, _visibility)
    RETURNING id INTO new_job_id;

    -- Insert skills related to job
    FOREACH skill_id IN ARRAY _skills
    LOOP
        INSERT INTO jobs_skills (job_id, skill_id)
        VALUES (new_job_id, skill_id);
    END LOOP;
	
	-- Insert locations related to job
    FOREACH location_id IN ARRAY _locations
    LOOP
        INSERT INTO jobs_locations (job_id, location_id)
        VALUES (new_job_id, location_id);
    END LOOP;
	
	-- Insert timezones related to job
    FOREACH timezone_id IN ARRAY _timezones
    LOOP
        INSERT INTO jobs_timezones (job_id, timezone_id)
        VALUES (new_job_id, timezone_id);
    END LOOP;
END;
$$;

CREATE OR REPLACE PROCEDURE create_dummy_job(
)
LANGUAGE plpgsql
AS $$
BEGIN
    call create_job(
        'Frontend Freelancer Needed',
        'Looking for a skilled backend developer to create a responsive website.',
        '77777777-7777-7777-7777-777777777777',
        '00000000-0000-0000-0000-000000000001',
        FALSE,
        '11111111-1111-1111-1111-111111111111',
        'fixed',
        'Under $250',
        'Less than 1 month',
        '10-30',
        NULL,
        NULL,
        '2024-05-01',
        'Everyone',
        ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'cccccccc-cccc-cccc-cccc-cccccccccccc'],
        ARRAY['11111111-1111-1111-1111-111111111111'],
        ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'cccccccc-cccc-cccc-cccc-cccccccccccc']
    );
END;
$$;


CALL create_job(
    'Frontend Freelancer Needed',
	'Looking for a skilled backend developer to create a responsive website.',
    '77777777-7777-7777-7777-777777777777',
    '00000000-0000-0000-0000-000000000001',
	TRUE,
    '11111111-1111-1111-1111-111111111111',
	'fixed',
	 'Under $250',
    'Less than 1 month',
    '10-30',
	NULL,
    NULL,
	 '2024-05-01',
    'Everyone',
	ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'cccccccc-cccc-cccc-cccc-cccccccccccc'],
 	ARRAY['11111111-1111-1111-1111-111111111111'],
	ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'cccccccc-cccc-cccc-cccc-cccccccccccc']
);

DROP FUNCTION IF EXISTS get_job_by_id;
CREATE OR REPLACE FUNCTION get_job_by_id(
    _job_id UUID
)
RETURNS TABLE (
    id UUID,
    title VARCHAR(255),
    description TEXT,
    category_id UUID,
    subcategory_id UUID,
    featured BOOLEAN,
    client_id UUID,
    created_at TIMESTAMP,
    payment_type payment_type,
    fixed_price_range price_range,
    duration job_duration,
    hours_per_week hours_per_week,
    min_hourly_rate DECIMAL(10, 2),
    max_hourly_rate DECIMAL(10, 2),
    get_quotes_until DATE,
    visibility job_visibility
)
AS $$
BEGIN
    RETURN QUERY SELECT * FROM jobs WHERE jobs.id = _job_id;
END;
$$ LANGUAGE plpgsql;
select * from get_job_by_id('11111111-1111-1111-1111-111111111111');

-- call create_dummy_job();

DROP FUNCTION IF EXISTS get_all_jobs;
CREATE OR REPLACE FUNCTION get_all_jobs(
    _page INT,
    _page_size INT,
    _search_query VARCHAR(255) DEFAULT NULL,
    _category_id UUID DEFAULT NULL,
    _subcategory_id UUID DEFAULT NULL,
    _skill_id UUID DEFAULT NULL,
    _featured_only BOOLEAN DEFAULT NULL,
    _payment_terms VARCHAR(20) DEFAULT NULL,
    _location_id UUID DEFAULT NULL,
	_sort_order VARCHAR(20) DEFAULT 'newest'
)
RETURNS TABLE (
    id UUID,
    title VARCHAR(255),
    description TEXT,
    category_id UUID,
    subcategory_id UUID,
    featured BOOLEAN,
    client_id UUID,
    created_at TIMESTAMP,
    payment_type payment_type,
    fixed_price_range price_range,
    duration job_duration,
    hours_per_week hours_per_week,
    min_hourly_rate DECIMAL(10, 2),
    max_hourly_rate DECIMAL(10, 2),
    get_quotes_until DATE,
    visibility job_visibility
)
AS $$
BEGIN
    RETURN QUERY 
        SELECT *
        FROM jobs
		WHERE (_search_query IS NULL OR (jobs.title ILIKE '%' || _search_query || '%') OR (jobs.description ILIKE '%' || _search_query || '%'))
		AND (_category_id IS NULL OR jobs.category_id = _category_id)
		AND (_subcategory_id IS NULL OR jobs.subcategory_id = _subcategory_id)
		AND (_skill_id IS NULL OR jobs.id IN (SELECT job_id FROM jobs_skills WHERE skill_id = _skill_id))
		AND (_featured_only IS NULL OR jobs.featured = _featured_only)
		AND (_payment_terms IS NULL OR jobs.payment_type::TEXT = _payment_terms)
		AND (_location_id IS NULL OR jobs.id IN (SELECT job_id FROM jobs_locations WHERE location_id = _location_id))
		AND (
                CASE _sort_order
                    WHEN 'closing_soon' THEN jobs.get_quotes_until >= CURRENT_DATE
                    ELSE TRUE
                END
              )
		ORDER BY
            CASE _sort_order
                WHEN 'newest' THEN jobs.created_at  
            END DESC,     
			CASE _sort_order
                WHEN 'oldest' THEN jobs.created_at  
                WHEN 'closing_soon' THEN jobs.get_quotes_until         
		END ASC
		LIMIT _page_size
        OFFSET ((_page - 1) * _page_size);
END;
$$ LANGUAGE plpgsql;



SELECT *
FROM get_all_jobs(
    _page := 1,
    _page_size := 10,
    _search_query := 'web development', -- Search query (case-insensitive)
    _category_id := '77777777-7777-7777-7777-777777777777', -- Category ID (optional)
    _subcategory_id := NULL, -- Subcategory ID (optional)
    _skill_id := NULL, -- Skill ID (optional)
    _featured_only := TRUE, -- Featured only (optional)
    _payment_terms := NULL, -- Payment terms (optional)
    _location_id := NULL, -- Location ID (optional)
	_sort_order := 'oldest'
);

DROP FUNCTION IF EXISTS delete_job_by_id;
CREATE OR REPLACE PROCEDURE delete_job_by_id(
    _job_id UUID
)
LANGUAGE plpgsql
AS $$
BEGIN
	DELETE FROM jobs_timezones WHERE job_id = _job_id;
	
    DELETE FROM jobs_skills WHERE job_id = _job_id;
	
	DELETE FROM jobs_locations WHERE job_id = _job_id;

    DELETE FROM jobs WHERE id = _job_id;
END;
$$;

Call delete_job_by_id('22222222-2222-2222-2222-222222222222');
