DO $$ DECLARE
    r RECORD;
BEGIN
    -- if the schema you operate on is not "current", you will want to
    -- replace current_schema() in query with 'schematodeletetablesfrom'
    -- *and* update the generate 'DROP...' accordingly.
    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP
        EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';
    END LOOP;
END $$;

	-- stubs, do not use & remove after integrating all schemas
	CREATE TABLE users (
		id uuid PRIMARY KEY
	);

	CREATE TABLE freelancers (
		id uuid PRIMARY KEY
	);

	-- CREATE TABLE payments (
	--     id UUID PRIMARY KEY,
	--     client_id UUID REFERENCES users(id), -- check referenced table
	--     amount DECIMAL(10, 2) NOT NULL,
	--     transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	-- );

	-- CREATE TYPE payment_type AS ENUM ('fixed', 'hourly');

	-- Payment Type Enum
	DO $$BEGIN
	IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'payment_type') THEN
		CREATE TYPE payment_type AS ENUM ('fixed', 'hourly', 'not sure');
	END IF;
	END$$;
	
	DO $$BEGIN
	IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'price_range') THEN
		CREATE TYPE price_range AS ENUM (
			'Under $250',
			'$250 to $500',
			'$500 to $1,000',
			'$1,000 to $2,500',
			'$2,500 to $5,000'
	);
	END IF;
	END$$;
	
	DO $$BEGIN
	IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'job_duration') THEN
		CREATE TYPE job_duration AS ENUM (
			'Less than 1 week',
			'Less than 1 month',
			'1 to 3 months',
			'3 to 6 months',
			'More than 6 months / ongoing'
	);
	END IF;
	END$$;
	
	DO $$BEGIN
	IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'hours_per_week') THEN
		CREATE TYPE hours_per_week AS ENUM (
			'1-10',
			'10-30',
			'30+'
	);
	END IF;
	END$$;
	
	DO $$BEGIN
	IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'job_visibility') THEN
		CREATE TYPE job_visibility AS ENUM (
			'Everyone',
			'Guru Freelancers',
			'Invite Only (on Guru)'
	);
	END IF;
	END$$;

	CREATE TABLE categories (
		id UUID PRIMARY KEY,
		name VARCHAR(100) NOT NULL
	);
	
	CREATE TABLE skills (
		id UUID PRIMARY KEY,
		name VARCHAR(100)
	);

	CREATE TABLE subcategories (
		id UUID PRIMARY KEY,
		name VARCHAR(100) NOT NULL,
		skill_id UUID REFERENCES skills(id),
		category_id UUID REFERENCES categories(id)
	);

	CREATE TABLE locations (
		id UUID PRIMARY KEY,
		name VARCHAR(255) NOT NULL --handle populating table?
	);
	

	CREATE TABLE jobs (
		id uuid PRIMARY KEY,
		title VARCHAR(255) NOT NULL,
		description TEXT NOT NULL,
		category_id UUID references categories(id) ON DELETE CASCADE,
		subcategory_id UUID references subcategories(id) ON DELETE CASCADE,
		featured BOOLEAN DEFAULT FALSE,
		client_id UUID references users(id) ON DELETE CASCADE,  -- check foreign key on which table
		created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		payment_type payment_type DEFAULT NULL,
		fixed_price_range price_range,
		duration job_duration,
		hours_per_week hours_per_week,
		min_hourly_rate DECIMAL(10, 2),
    	max_hourly_rate DECIMAL(10, 2),
		get_quotes_until DATE,
		visibility job_visibility
		
	);
	
	CREATE TABLE jobs_skills (
		job_id UUID REFERENCES jobs(id),
		skill_id UUID REFERENCES skills(id),
		PRIMARY KEY (job_id, skill_id)
	);

	CREATE TABLE jobs_locations (
		job_id UUID REFERENCES jobs(id) ON DELETE CASCADE,
		location_id UUID REFERENCES locations(id) ON DELETE CASCADE,
		PRIMARY KEY (job_id, location_id)
	);


	CREATE TABLE job_freelancer (
		job_id UUID REFERENCES jobs(id),
		freelancer_id UUID REFERENCES freelancers(id), -- check foreign key on which table
		applied BOOLEAN DEFAULT FALSE,
		viewed BOOLEAN DEFAULT FALSE,
		PRIMARY KEY (job_id, freelancer_id)
	);
	
-- Populate tables with sample data
-- Users
INSERT INTO users (id) VALUES
    ('11111111-1111-1111-1111-111111111111'),
    ('22222222-2222-2222-2222-222222222222'),
    ('33333333-3333-3333-3333-333333333333');

-- Freelancers
INSERT INTO freelancers (id) VALUES
    ('44444444-4444-4444-4444-444444444444'),
    ('55555555-5555-5555-5555-555555555555'),
    ('66666666-6666-6666-6666-666666666666');

-- Categories
INSERT INTO categories (id, name) VALUES
    ('77777777-7777-7777-7777-777777777777', 'Software Development'),
    ('88888888-8888-8888-8888-888888888888', 'Graphic Design'),
    ('99999999-9999-9999-9999-999999999999', 'Writing');

-- Skills
INSERT INTO skills (id, name) VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'HTML'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'CSS'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'JavaScript'),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Adobe Illustrator'),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Photoshop'),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'Content Writing');

-- Subcategories
INSERT INTO subcategories (id, name, skill_id, category_id) VALUES
    ('00000000-0000-0000-0000-000000000001', 'Web Development', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '77777777-7777-7777-7777-777777777777'),
    ('00000000-0000-0000-0000-000000000002', 'Logo Design', 'dddddddd-dddd-dddd-dddd-dddddddddddd', '88888888-8888-8888-8888-888888888888'),
    ('00000000-0000-0000-0000-000000000003', 'Copywriting', 'ffffffff-ffff-ffff-ffff-ffffffffffff', '99999999-9999-9999-9999-999999999999');

-- Locations
INSERT INTO locations (id, name) VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'New York'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'London'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Tokyo');
	
-- Jobs
INSERT INTO jobs (id, title, description, category_id, subcategory_id, featured, client_id, created_at, payment_type, fixed_price_range, duration, hours_per_week, min_hourly_rate, max_hourly_rate, get_quotes_until, visibility) VALUES
    ('11111111-1111-1111-1111-111111111111', 'Web Developer Needed', 'Looking for a skilled web developer to create a responsive website.', '77777777-7777-7777-7777-777777777777', '00000000-0000-0000-0000-000000000001', TRUE, '11111111-1111-1111-1111-111111111111', NOW(), 'fixed', 'Under $250', 'Less than 1 month', '10-30', NULL, NULL, '2024-05-01', 'Everyone'),
    ('22222222-2222-2222-2222-222222222222', 'Logo Design Project', 'Need a logo design for a new startup company.', '88888888-8888-8888-8888-888888888888', '00000000-0000-0000-0000-000000000002', FALSE, '22222222-2222-2222-2222-222222222222', NOW(), 'hourly', NULL, '1 to 3 months', '30+', 10.00, 50.00, '2024-05-10', 'Guru Freelancers');

-- Jobs Skills
INSERT INTO jobs_skills (job_id, skill_id) VALUES
    ('11111111-1111-1111-1111-111111111111', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    ('11111111-1111-1111-1111-111111111111', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
    ('11111111-1111-1111-1111-111111111111', 'cccccccc-cccc-cccc-cccc-cccccccccccc'),
    ('22222222-2222-2222-2222-222222222222', 'dddddddd-dddd-dddd-dddd-dddddddddddd'),
    ('22222222-2222-2222-2222-222222222222', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee');

-- Jobs Locations
INSERT INTO jobs_locations (job_id, location_id) VALUES
    ('11111111-1111-1111-1111-111111111111', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    ('11111111-1111-1111-1111-111111111111', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
    ('22222222-2222-2222-2222-222222222222', 'cccccccc-cccc-cccc-cccc-cccccccccccc');
	


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
	_skills uuid[]

)
LANGUAGE plpgsql
AS $$
-- DECLARE
--     new_job_id UUID;
--     skill_id UUID;
BEGIN
    INSERT INTO jobs (id, title, description, category_id, subcategory_id, featured, client_id, payment_type, fixed_price_range, duration, hours_per_week, min_hourly_rate, max_hourly_rate, get_quotes_until, visibility)
    VALUES (gen_random_uuid(), _title,  _description, _category_id, _subcategory_id, _featured, _client_id, _payment_type, _fixed_price_range, _duration, _hours_per_week, _min_hourly_rate, _max_hourly_rate, _get_quotes_until, _visibility);
--     RETURNING id INTO new_job_id;

--     -- Insert into jobs_skills table
--     FOREACH skill_id IN ARRAY _skills
--     LOOP
--         INSERT INTO jobs_skills (job_id, skill_id)
--         VALUES (new_job_id, skill_id);
--     END LOOP;
END;
$$;

CALL create_job(
    'Backend Freelancer Needed',
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
	ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'cccccccc-cccc-cccc-cccc-cccccccccccc']
);
select * from jobs;

INSERT INTO jobs (id, title, description, category_id, subcategory_id, featured, client_id, payment_type, fixed_price_range, duration, hours_per_week, min_hourly_rate, max_hourly_rate, get_quotes_until, visibility)
    VALUES (
	gen_random_uuid(),
	'Backend Freelancer Needed',
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
    'Everyone');
select * from jobs;
-- 	ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'cccccccc-cccc-cccc-cccc-cccccccccccc']);
-- CREATE OR REPLACE PROCEDURE add_skill(
-- _skill_name uuid[]
-- )
-- LANGUAGE plpgsql
-- AS $$
-- BEGIN
--     -- Insert into skills table
-- --    INSERT INTO skills (id, name) VALUES (gen_random_uuid(), _skill_name);
-- END;
-- $$;

-- CALL add_skill(ARRAY['1','2','3']);
-- select * from skills;

-- 	CREATE TABLE quotes ( -- handle the different types of quotes
-- 		id UUID PRIMARY KEY,
-- 		job_id UUID REFERENCES jobs(id),
-- 		freelancer_id UUID REFERENCES freelancers(id),
-- 		proposal TEXT,
-- 		submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- 	);

	-- CREATE TABLE employer_spend (
	--     id UUID PRIMARY KEY,
	--     client_id UUID REFERENCES users(id),
	--     spend_amount DECIMAL(10, 2) DEFAULT 0,
	--     transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	-- );


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
	




