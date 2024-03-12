	drop table IF exists jobs_locations;
	drop table IF exists job_freelancer;
	drop table IF exists quotes;
	drop table IF exists locations;
	drop table IF exists jobs;
	drop table IF exists users;
	drop table IF exists freelancers;
	drop table IF exists subcategories;
	drop table IF exists categories;


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

	DO $$BEGIN
	IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'payment_type') THEN
		CREATE TYPE payment_type AS ENUM ('fixed', 'hourly');
	END IF;
	END$$;

	CREATE TABLE categories (
		id UUID PRIMARY KEY,
		name VARCHAR(100) NOT NULL
	);

	CREATE TABLE subcategories (
		id UUID PRIMARY KEY,
		name VARCHAR(100) NOT NULL,
		skills TEXT[],
		category_id UUID REFERENCES categories(id)
	);

	CREATE TABLE locations (
		id UUID PRIMARY KEY,
		name VARCHAR(255) NOT NULL --handle populating table?
	);

	CREATE TABLE jobs (
		id uuid PRIMARY KEY,
		title VARCHAR(255) NOT NULL,
		category_id UUID references categories(id) ON DELETE CASCADE,
		payment_terms payment_type DEFAULT NULL,
		featured BOOLEAN DEFAULT FALSE,
		client_id UUID references users(id) ON DELETE CASCADE,  -- check foreign key on which table
		created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

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

	CREATE TABLE quotes ( -- handle the different types of quotes
		id UUID PRIMARY KEY,
		job_id UUID REFERENCES jobs(id),
		freelancer_id UUID REFERENCES freelancers(id),
		proposal TEXT,
		submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	);

	-- CREATE TABLE employer_spend (
	--     id UUID PRIMARY KEY,
	--     client_id UUID REFERENCES users(id),
	--     spend_amount DECIMAL(10, 2) DEFAULT 0,
	--     transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	-- );



	INSERT INTO users (id) VALUES
	('11111111-1111-1111-1111-111111111111'), -- User 1
	('22222222-2222-2222-2222-222222222222'), -- User 2
	('33333333-3333-3333-3333-333333333333'); -- User 3

	-- Inserting freelancers
	INSERT INTO freelancers (id) VALUES
	('44444444-4444-4444-4444-444444444444'), -- Freelancer 1
	('55555555-5555-5555-5555-555555555555'), -- Freelancer 2
	('66666666-6666-6666-6666-666666666666'); -- Freelancer 3


	INSERT INTO categories (id, name) VALUES
	('77777777-7777-7777-7777-777777777777', 'Software Development'),
	('88888888-8888-8888-8888-888888888888', 'Graphic Design'),
	('99999999-9999-9999-9999-999999999999', 'Writing');

	INSERT INTO subcategories (id, name, category_id, skills) VALUES
	('00000000-0000-0000-0000-000000000001', 'Web Development', '77777777-7777-7777-7777-777777777777', '{"HTML", "CSS", "JavaScript"}'),
	('00000000-0000-0000-0000-000000000002', 'Logo Design', '88888888-8888-8888-8888-888888888888', '{"Adobe Illustrator", "Photoshop"}'),
	('00000000-0000-0000-0000-000000000003', 'Copywriting', '99999999-9999-9999-9999-999999999999', '{"Content Writing", "Copy Editing"}');


	INSERT INTO locations (id, name) VALUES
	('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'New York'),
	('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'London'),
	('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Tokyo');


	INSERT INTO jobs (id, title, category_id, payment_terms, featured, client_id, created_at) VALUES
	('11111111-1111-1111-1111-111111111111', 'Web Developer Needed', '77777777-7777-7777-7777-777777777777', 'fixed', TRUE, '11111111-1111-1111-1111-111111111111', NOW()),
	('22222222-2222-2222-2222-222222222222', 'Logo Design Project', '88888888-8888-8888-8888-888888888888', 'hourly', FALSE, '22222222-2222-2222-2222-222222222222', NOW());


	INSERT INTO jobs_locations (job_id, location_id) VALUES
	('11111111-1111-1111-1111-111111111111', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
	('11111111-1111-1111-1111-111111111111', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
	('22222222-2222-2222-2222-222222222222', 'cccccccc-cccc-cccc-cccc-cccccccccccc');

	INSERT INTO job_freelancer (job_id, freelancer_id, applied, viewed) VALUES
	('11111111-1111-1111-1111-111111111111', '44444444-4444-4444-4444-444444444444', TRUE, TRUE),
	('11111111-1111-1111-1111-111111111111', '55555555-5555-5555-5555-555555555555', FALSE, TRUE),
	('22222222-2222-2222-2222-222222222222', '66666666-6666-6666-6666-666666666666', TRUE, FALSE);

	INSERT INTO quotes (id, job_id, freelancer_id, proposal, submitted_at) VALUES
	('33333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111', '44444444-4444-4444-4444-444444444444', 'Proposal for Web Developer Needed', NOW()),
	('44444444-4444-4444-4444-444444444444', '22222222-2222-2222-2222-222222222222', '55555555-5555-5555-5555-555555555555', 'Proposal for Logo Design Project', NOW());

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


