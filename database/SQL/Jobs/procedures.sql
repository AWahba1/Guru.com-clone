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
	_timezone_id UUID,
	_locations text[]
)
LANGUAGE plpgsql
AS $$
DECLARE
    new_job_id UUID;
    skill_id UUID;
	location_id UUID;
BEGIN
    INSERT INTO jobs (id, title, description, category_id, subcategory_id, featured, client_id, payment_type, fixed_price_range, duration, hours_per_week, min_hourly_rate, max_hourly_rate, get_quotes_until, visibility, timezone_id)
    VALUES (gen_random_uuid(), _title,  _description, _category_id, _subcategory_id, _featured, _client_id, _payment_type, _fixed_price_range, _duration, _hours_per_week, _min_hourly_rate, _max_hourly_rate, _get_quotes_until, _visibility, _timezone_id)
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
	'11111111-1111-1111-1111-111111111111',
	ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'cccccccc-cccc-cccc-cccc-cccccccccccc']
);


CREATE OR REPLACE FUNCTION get_job_by_id(
    _job_id UUID
)
RETURNS TABLE (
    job_id UUID,
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
    visibility job_visibility,
    timezone_id UUID
)
AS $$
BEGIN
    RETURN QUERY SELECT * FROM jobs WHERE id = _job_id;
END;
$$ LANGUAGE plpgsql;

select * from get_job_by_id('11111111-1111-1111-1111-111111111111');


	-- Get All Jobs
	-- CREATE OR REPLACE FUNCTION get_all_jobs(
	--     _category_id UUID DEFAULT NULL,
	--     _subcategory_id UUID DEFAULT NULL,
	--     _skill TEXT DEFAULT NULL,
	--     _payment_terms payment_type DEFAULT NULL,
	--     _location_id UUID DEFAULT NULL,
	--     _quotes_received INT DEFAULT NULL,
	--     _featured BOOLEAN DEFAULT NULL,
	--     _applied BOOLEAN DEFAULT NULL,
	--     _viewed BOOLEAN DEFAULT NULL,
	--     _verified_client BOOLEAN DEFAULT NULL,
	--     _employer_spend DECIMAL DEFAULT NULL,
	--     _page INT DEFAULT 1,
	--     _page_size INT DEFAULT 10
	-- )
	-- RETURNS TABLE (
	--     id UUID,
	--     title VARCHAR(255),
	--     category_id UUID,
	--     payment_terms payment_type,
	--     featured BOOLEAN,
	--     client_id UUID,
	--     created_at TIMESTAMP,
	--     location_id UUID,
	--     applied BOOLEAN,
	--     viewed BOOLEAN,
	--     quotes_received INT,
	--     verified_client BOOLEAN,
	--     employer_spend DECIMAL
	-- ) AS
	-- $$
	-- BEGIN
	--     RETURN QUERY
	--     SELECT
	--         jobs.id,
	--         jobs.title,
	--         jobs.category_id,
	--         jobs.payment_terms,
	--         jobs.featured,
	--         jobs.client_id,
	--         jobs.created_at,
	--         jobs_locations.location_id,
	--         job_freelancer.applied,
	--         job_freelancer.viewed,
	--         COALESCE(qr.quotes_received, 0) AS quotes_received,
	--         CASE
	--             WHEN job_freelancer.applied AND job_freelancer.viewed THEN true
	--             ELSE false
	--         END AS verified_client,
	--         COALESCE(jobs.employer_spend, 0) AS employer_spend
	--     FROM
	--         jobs
	--     LEFT JOIN
	--         jobs_locations ON jobs.id = jobs_locations.job_id
	--     LEFT JOIN
	--         job_freelancer ON jobs.id = job_freelancer.job_id
	--     LEFT JOIN LATERAL (
	--         SELECT
	--             COUNT(*) AS quotes_received
	--         FROM
	--             quotes q
	--         WHERE
	--             q.job_id = jobs.id
	--     ) qr ON true
	--     WHERE
	--         (_category_id IS NULL OR jobs.category_id = _category_id)
	--         AND (_subcategory_id IS NULL OR EXISTS (
	--             SELECT 1 FROM subcategories WHERE id = _subcategory_id AND category_id = jobs.category_id
	--         ))
	--         AND (_skill IS NULL OR EXISTS (
	--             SELECT 1 FROM subcategories WHERE category_id = jobs.category_id AND _skill = ANY (skills)
	--         ))
	--         AND (_payment_terms IS NULL OR jobs.payment_terms = _payment_terms)
	--         AND (_location_id IS NULL OR EXISTS (
	--             SELECT 1 FROM jobs_locations WHERE job_id = jobs.id AND location_id = _location_id
	--         ))
	--         AND (_quotes_received IS NULL OR qr.quotes_received >= _quotes_received)
	--         AND (_featured IS NULL OR jobs.featured = _featured)
	--         AND (_applied IS NULL OR job_freelancer.applied = _applied)
	--         AND (_viewed IS NULL OR job_freelancer.viewed = _viewed)
	--         AND (_verified_client IS NULL OR (job_freelancer.applied AND job_freelancer.viewed))
	--         AND (_employer_spend IS NULL OR jobs.employer_spend >= _employer_spend)
	--     ORDER BY
	--         jobs.created_at DESC
	--     LIMIT
	--         _page_size
	--     OFFSET
	--         ((_page - 1) * _page_size);
	-- END;
	-- $$
	-- LANGUAGE plpgsql;



	-- SELECT * FROM get_all_jobs(
	--     _category_id := '77777777-7777-7777-7777-777777777777', -- Category ID for Software Development
	--     _location_id := 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', -- Location ID for New York
	--     _payment_terms := 'fixed', -- Payment terms fixed
	--     _featured := TRUE, -- Featured jobs
	--     _applied := TRUE, -- Jobs where freelancers have applied
	--     _viewed := TRUE, -- Jobs that have been viewed
	--     _page := 1, -- Page number
	--     _page_size := 10 -- Number of jobs per page
	-- );