DO $$
DECLARE
    rec record;
BEGIN
    FOR rec IN
        SELECT proname, oidvectortypes(proargtypes) AS arg_types
        FROM pg_proc
        WHERE proname = 'create_job'  -- procedure name
    LOOP
        EXECUTE format('DROP PROCEDURE IF EXISTS %I(%s);', rec.proname, rec.arg_types);
    END LOOP;
END$$;

select oid::regprocedure, *
FROM pg_proc
WHERE proname = 'create_job';

CREATE OR REPLACE PROCEDURE create_job(
    _title VARCHAR(255),
	_description TEXT,
	_category_id UUID,
    _subcategory_id UUID,
	_featured BOOLEAN,
    _client_id UUID,
    _payment_type VARCHAR(255),
	_fixed_price_range VARCHAR(255),
    _duration VARCHAR(255),
    _hours_per_week VARCHAR(255),
	_min_hourly_rate DECIMAL(10, 2),
    _max_hourly_rate DECIMAL(10, 2),
	_get_quotes_until DATE,
    _visibility VARCHAR(255),
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
    INSERT INTO jobs (id, title, description, category_id, subcategory_id, featured, client_id, payment_type, fixed_price_range, duration, hours_per_week, min_hourly_rate, max_hourly_rate, get_quotes_until, visibility, status)
    VALUES (gen_random_uuid(), _title,  _description, _category_id, _subcategory_id, _featured, _client_id, _payment_type::payment_type, _fixed_price_range::price_range, _duration::job_duration, _hours_per_week::hours_per_week, _min_hourly_rate, _max_hourly_rate, _get_quotes_until, _visibility::job_visibility, 'Under Review')
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

Call create_dummy_job();

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
    category_name VARCHAR(100),
    subcategory_id UUID,
    subcategory_name VARCHAR(100),
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
    status job_status,
    skills JSON,
    locations JSON,
    timezones JSON 
)
AS $$
BEGIN
    RETURN QUERY 
        SELECT j.id, j.title, j.description, j.category_id, c.name AS category_name, 
               j.subcategory_id, s.name AS subcategory_name, j.featured, 
               j.client_id, j.created_at, j.payment_type, j.fixed_price_range, 
               j.duration, j.hours_per_week, j.min_hourly_rate, j.max_hourly_rate, 
               j.get_quotes_until, j.visibility, j.status,
               (SELECT JSON_AGG(json_build_object('id', sk.id, 'name', sk.name)) 
                   FROM jobs_skills js 
                   INNER JOIN skills sk ON js.skill_id = sk.id 
                   WHERE js.job_id = j.id) AS skills,
               (SELECT JSON_AGG(json_build_object('id', loc.id, 'name', loc.name)) 
                   FROM jobs_locations jl 
                   INNER JOIN locations loc ON jl.location_id = loc.id 
                   WHERE jl.job_id = j.id) AS locations,
               (SELECT JSON_AGG(json_build_object('id', tz.id, 'name', tz.name)) 
                   FROM jobs_timezones jt 
                   INNER JOIN timezones tz ON jt.timezone_id = tz.id 
                   WHERE jt.job_id = j.id) AS timezones
        FROM jobs j
        INNER JOIN categories c ON j.category_id = c.id
        INNER JOIN subcategories s ON j.subcategory_id = s.id
        WHERE j.id = _job_id;
END;
$$ LANGUAGE plpgsql;
-- select * from get_job_by_id('11111111-1111-1111-1111-111111111111');

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
	_sort_order VARCHAR(20) DEFAULT 'newest',
	_status_list text[] DEFAULT ARRAY['Open']
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
    visibility job_visibility,
	status job_status
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
		AND (_status_list IS NULL OR jobs.status::TEXT = ANY(_status_list))
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



-- SELECT *
-- FROM get_all_jobs(
--     _page := 1,
--     _page_size := 10,
-- 	_status_list := ARRAY['Under Review', 'Open'],
--     _search_query := 'web development', -- Search query (case-insensitive)
--     _category_id := '77777777-7777-7777-7777-777777777777', -- Category ID (optional)
--     _subcategory_id := NULL, -- Subcategory ID (optional)
--     _skill_id := NULL, -- Skill ID (optional)
--     _featured_only := TRUE, -- Featured only (optional)
--     _payment_terms := NULL, -- Payment terms (optional)
--     _location_id := NULL, -- Location ID (optional)
-- 	_sort_order := 'oldest'
-- );


select oid::regprocedure, *
FROM pg_proc
WHERE proname = 'update_job';

DO $$
DECLARE
    rec record;
BEGIN
    FOR rec IN
        SELECT proname, oidvectortypes(proargtypes) AS arg_types
        FROM pg_proc
        WHERE proname = 'update_job'  -- procedure name
    LOOP
        EXECUTE format('DROP PROCEDURE IF EXISTS %I(%s);', rec.proname, rec.arg_types);
    END LOOP;
END$$;





-- DROP PROCEDURE IF EXISTS update_job;
CREATE OR REPLACE PROCEDURE update_job(
    _job_id UUID,
    _title VARCHAR(255) DEFAULT NULL,
    _description TEXT DEFAULT NULL,
    _category_id UUID DEFAULT NULL,
    _subcategory_id UUID DEFAULT NULL,
    _featured BOOLEAN DEFAULT NULL,
    _payment_type VARCHAR(20) DEFAULT NULL,
    _fixed_price_range VARCHAR(20) DEFAULT NULL,
    _duration VARCHAR(100) DEFAULT NULL,
    _hours_per_week VARCHAR(20) DEFAULT NULL,
    _min_hourly_rate DECIMAL(10, 2) DEFAULT NULL,
    _max_hourly_rate DECIMAL(10, 2) DEFAULT NULL,
    _get_quotes_until DATE DEFAULT NULL,
    _visibility VARCHAR(50) DEFAULT NULL,
    _status VARCHAR(50) DEFAULT NULL,
    _skills TEXT[] DEFAULT NULL,
    _timezones TEXT[] DEFAULT NULL,
    _locations TEXT[] DEFAULT NULL
)
AS $$
DECLARE
    skill_id UUID;
    location_id UUID;
    timezone_id UUID;
BEGIN
    UPDATE jobs
    SET 
        title = COALESCE(_title, title),
        description = COALESCE(_description, description),
        category_id = COALESCE(_category_id, category_id),
        subcategory_id = COALESCE(_subcategory_id, subcategory_id),
        featured = COALESCE(_featured, featured),
        payment_type = COALESCE(_payment_type::payment_type, payment_type::payment_type),
        fixed_price_range = COALESCE(_fixed_price_range::price_range, fixed_price_range::price_range),
        duration = COALESCE(_duration::job_duration, duration::job_duration),
        hours_per_week = COALESCE(_hours_per_week::hours_per_week, hours_per_week::hours_per_week),
        min_hourly_rate = COALESCE(_min_hourly_rate, min_hourly_rate),
        max_hourly_rate = COALESCE(_max_hourly_rate, max_hourly_rate),
        get_quotes_until = COALESCE(_get_quotes_until, get_quotes_until),
        visibility = COALESCE(_visibility::job_visibility, visibility::job_visibility),
        status = COALESCE(_status::job_status, status::job_status)
    WHERE id = _job_id;

 

    -- Insert new entries into jobs_skills
    IF _skills IS NOT NULL THEN
	   -- Delete existing entries from jobs_skills
    	DELETE FROM jobs_skills WHERE job_id = _job_id;
		
        FOREACH skill_id IN ARRAY _skills
        LOOP
            INSERT INTO jobs_skills (job_id, skill_id) VALUES (_job_id, skill_id);
        END LOOP;
    END IF;



    -- Insert new entries into jobs_timezones
    IF _timezones IS NOT NULL THEN
	    -- Delete existing entries from jobs_timezones
   		DELETE FROM jobs_timezones WHERE job_id = _job_id;
		 
        FOREACH timezone_id IN ARRAY _timezones
        LOOP
            INSERT INTO jobs_timezones (job_id, timezone_id) VALUES (_job_id, timezone_id);
        END LOOP;
    END IF;



    -- Insert new entries into jobs_locations
    IF _locations IS NOT NULL THEN
	    -- Delete existing entries from jobs_locations
   		DELETE FROM jobs_locations WHERE job_id = _job_id;
		
        FOREACH location_id IN ARRAY _locations
        LOOP
            INSERT INTO jobs_locations (job_id, location_id) VALUES (_job_id, location_id);
        END LOOP;
    END IF;

END;
$$ LANGUAGE plpgsql;


-- CALL update_job(
--     '11111111-1111-1111-1111-111111111111',  -- _job_id
--     'Updated Job Title',                     -- _title
--     'Updated job description goes here.',    -- _description
--     '77777777-7777-7777-7777-777777777777',  -- _category_id
--     '00000000-0000-0000-0000-000000000001',  -- _subcategory_id
--     TRUE,                                    -- _featured
--     'hourly',                                -- _payment_type
--     NULL,                                    -- _fixed_price_range
--     'Less than 1 month',                     -- _duration
--     '10-30',                                 -- _hours_per_week
--     15.00,                                   -- _min_hourly_rate
--     30.00,                                   -- _max_hourly_rate
--     '2024-05-20',                            -- _get_quotes_until
--     'Everyone',                              -- _visibility
--     'Closed',                                  -- _status
--     ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'ffffffff-ffff-ffff-ffff-ffffffffffff'],      --skills
--     ARRAY['44444444-4444-4444-4444-444444444444', '55555555-5555-5555-5555-555555555555'], --timezones
--     ARRAY['bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb']              --locations
-- );



DROP FUNCTION IF EXISTS delete_job_by_id;
CREATE OR REPLACE PROCEDURE delete_job_by_id(
    _job_id UUID
)
LANGUAGE plpgsql
AS $$
BEGIN

    -- Check if the job exists for the given ID
    IF NOT EXISTS (SELECT 1 FROM jobs WHERE jobs.id = _job_id) THEN
        RAISE EXCEPTION 'Job with ID % does not exist', _job_id;
    END IF;

	DELETE FROM jobs_timezones WHERE job_id = _job_id;
	
    DELETE FROM jobs_skills WHERE job_id = _job_id;
	
	DELETE FROM jobs_locations WHERE job_id = _job_id;

    DELETE FROM jobs WHERE id = _job_id;
END;
$$;

-- Call delete_job_by_id('22222222-2222-2222-2222-222222222222');
