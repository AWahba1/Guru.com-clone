DO $$ DECLARE
    r RECORD;
BEGIN
    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP
        EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';
    END LOOP;
END $$;

	-- stubs, do not use & remove after integrating all schemas
	CREATE TABLE users (
		id uuid PRIMARY KEY,
		is_verified BOOLEAN,
		amount_spent DECIMAL(10,2)
	);

	CREATE TABLE freelancers (
		id uuid PRIMARY KEY
	);

	-- Payment Type Enum
	DO $$BEGIN
	IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'payment_type') THEN
		CREATE TYPE payment_type AS ENUM ('fixed', 'hourly', 'not sure');
	END IF;
	END$$;

	-- Fixed Price type enum
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

	-- Job Duration type enum

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

	DO $$BEGIN
	DROP TYPE IF EXISTS job_status CASCADE;
	IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'job_status') THEN
		CREATE TYPE job_status AS ENUM (
			'Under Review',
			'Open',
			'Closed',
			'Not Approved'
	);
	END IF;
	END$$;

	DROP TYPE IF EXISTS quote_status_enum CASCADE;
	CREATE TYPE quote_status_enum AS ENUM ('AWAITING_ACCEPTANCE', 'PRIORITY', 'ACCEPTED', 'ARCHIVED');

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

	CREATE TABLE timezones (
		id UUID PRIMARY KEY,
		name VARCHAR(100) NOT NULL
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
		visibility job_visibility,
		status job_status
	);

	CREATE TABLE jobs_attachments (
		id uuid PRIMARY KEY,
		job_id uuid REFERENCES jobs(id) ON DELETE CASCADE,
		url TEXT NOT NULL,
		filename VARCHAR(255) NOT NULL,
		created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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

	CREATE TABLE jobs_timezones (
		job_id UUID REFERENCES jobs(id) ON DELETE CASCADE,
		timezone_id UUID REFERENCES timezones(id) ON DELETE CASCADE,
		PRIMARY KEY (job_id, timezone_id)
	);

CREATE TABLE job_freelancer_view (
    job_id UUID REFERENCES jobs(id) ON DELETE CASCADE ON UPDATE CASCADE,
    freelancer_id UUID REFERENCES freelancers(id) ON DELETE CASCADE ON UPDATE CASCADE,
    viewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (job_id, freelancer_id)
);

CREATE TABLE quotes (
    id UUID PRIMARY KEY,
    freelancer_id UUID,
    job_id UUID,
    proposal VARCHAR(3000),
    quote_status VARCHAR(255) DEFAULT 'AWAITING_ACCEPTANCE' CHECK (quote_status IN ('AWAITING_ACCEPTANCE', 'PRIORITY', 'ACCEPTED', 'ARCHIVED')),
    bids_used int,
    bid_date TIMESTAMP,
    FOREIGN KEY (freelancer_id) REFERENCES freelancers(id) ON DELETE CASCADE,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE
);

CREATE TABLE quote_templates (
    id UUID PRIMARY KEY,
    freelancer_id UUID,
    template_name VARCHAR(255),
    template_description VARCHAR(10000),
    FOREIGN KEY (freelancer_id) REFERENCES freelancers(id) ON DELETE CASCADE
);

	CREATE TABLE quote_templates_attachments (
		id uuid PRIMARY KEY,
		quote_template_id uuid REFERENCES quote_templates(id) ON DELETE CASCADE,
		url TEXT NOT NULL,
		filename VARCHAR(255) NOT NULL,
		created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE job_watchlist (
    watchlist_id UUID PRIMARY KEY,
    freelancer_id UUID,
    job_id UUID,
    FOREIGN KEY (freelancer_id) REFERENCES freelancers(id) ON DELETE CASCADE,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE
);

CREATE TABLE job_invitations (
    invitation_id UUID PRIMARY KEY,
    freelancer_id UUID,
    client_id UUID,
    job_id UUID,
    invitation_date TIMESTAMP,
    FOREIGN KEY (freelancer_id) REFERENCES freelancers(id) ON DELETE CASCADE,
    FOREIGN KEY (client_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE
);

CREATE TABLE saved_searches (
    id UUID PRIMARY KEY,
	name VARCHAR(255),
	freelancer_id UUID references freelancers(id) ON DELETE CASCADE,
    search_query VARCHAR(255) DEFAULT NULL,
    category_id UUID references categories(id) ON DELETE CASCADE DEFAULT NULL,
    subcategory_id UUID references subcategories(id) ON DELETE CASCADE DEFAULT NULL,
    skill_id UUID references skills(id) ON DELETE CASCADE DEFAULT NULL,
    featured_only BOOLEAN DEFAULT NULL,
    payment_terms payment_type DEFAULT NULL,
    location_ids text[] DEFAULT NULL,
    sort_order VARCHAR(20) DEFAULT NULL,
    status_list text[] DEFAULT NULL,
    verified_only BOOLEAN DEFAULT NULL,
    min_employer_spend INT DEFAULT NULL,
    max_quotes_received INT DEFAULT NULL,
    not_viewed BOOLEAN DEFAULT NULL,
    not_applied BOOLEAN DEFAULT NULL
);

-- Populate tables with sample data
-- Users

INSERT INTO users (id, is_verified, amount_spent) VALUES
    ('11111111-1111-1111-1111-111111111111', true, 100),
    ('22222222-2222-2222-2222-222222222222', false, 500.55),
    ('33333333-3333-3333-3333-333333333333', false, 4000);

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

-- Jobs Timezones
INSERT INTO timezones (id, name) VALUES
	('11111111-1111-1111-1111-111111111111', 'Pacific/Midway'),
	('22222222-2222-2222-2222-222222222222', 'Pacific/Niue'),
	('33333333-3333-3333-3333-333333333333', 'Pacific/Pago_Pago'),
	('44444444-4444-4444-4444-444444444444', 'Pacific/Samoa'),
	('55555555-5555-5555-5555-555555555555', 'US/Hawaii'),
	('66666666-6666-6666-6666-666666666666', 'Pacific/Rarotonga');

-- Jobs
INSERT INTO jobs (
    id,
    title,
    description,
    category_id,
    subcategory_id,
    featured,
    client_id,
    created_at,
    payment_type,
    fixed_price_range,
    duration,
    hours_per_week,
    min_hourly_rate,
    max_hourly_rate,
    get_quotes_until,
    visibility,
	status
) VALUES (
    '11111111-1111-1111-1111-111111111111',
    'Web Developer Needed',
    'Looking for a skilled web developer to create a responsive website.',
    '77777777-7777-7777-7777-777777777777',
    '00000000-0000-0000-0000-000000000001',
    TRUE,
    '11111111-1111-1111-1111-111111111111',
    NOW(),
    'fixed',
    'Under $250',
    'Less than 1 month',
    '10-30',
    NULL,
    NULL,
    NULL,
    'Everyone',
	'Under Review'
), (
    '22222222-2222-2222-2222-222222222222',
    'Logo Design Project',
    'Need a logo design for a new startup company.',
    '88888888-8888-8888-8888-888888888888',
    '00000000-0000-0000-0000-000000000002',
    FALSE,
    '22222222-2222-2222-2222-222222222222',
    NOW(),
    'hourly',
    NULL,
    '1 to 3 months',
    '30+',
    10.00,
    50.00,
    '2024-05-10',
    'Guru Freelancers',
	'Open'
);

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

-- Jobs Timeszones
INSERT INTO jobs_timezones (job_id, timezone_id) VALUES
    ('11111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111'),
    ('11111111-1111-1111-1111-111111111111', '22222222-2222-2222-2222-222222222222'),
    ('22222222-2222-2222-2222-222222222222', '22222222-2222-2222-2222-222222222222');

INSERT INTO quotes (id, freelancer_id, job_id, proposal, quote_status, bids_used, bid_date)
VALUES
    ('11111111-1111-1111-1111-111111111111', '44444444-4444-4444-4444-444444444444', '11111111-1111-1111-1111-111111111111', 'Sample proposal 1', 'ACCEPTED', 2, NOW()),
	('22222222-2222-2222-2222-222222222222', '44444444-4444-4444-4444-444444444444', '22222222-2222-2222-2222-222222222222', 'Sample proposal 2', 'ACCEPTED', 2, NOW()),
    ('44444444-4444-4444-4444-444444444444', '55555555-5555-5555-5555-555555555555', '22222222-2222-2222-2222-222222222222', 'Sample proposal 3', 'AWAITING_ACCEPTANCE', 1, NOW());

INSERT INTO quote_templates (id, freelancer_id, template_name, template_description)
VALUES
    ('11111111-1111-1111-1111-111111111111', '44444444-4444-4444-4444-444444444444', 'Template 1', 'Description for template 1'),
    ('22222222-2222-2222-2222-222222222222', '55555555-5555-5555-5555-555555555555', 'Template 2', 'Description for template 2');


INSERT INTO job_freelancer_view (job_id, freelancer_id, viewed_at)
VALUES
    ('11111111-1111-1111-1111-111111111111', '44444444-4444-4444-4444-444444444444', NOW()),
    ('11111111-1111-1111-1111-111111111111', '55555555-5555-5555-5555-555555555555', NOW()),
    ('22222222-2222-2222-2222-222222222222', '66666666-6666-6666-6666-666666666666', NOW());


INSERT INTO saved_searches (id, name, search_query, category_id, subcategory_id, skill_id, featured_only, payment_terms, location_ids, sort_order, status_list, verified_only, min_employer_spend, max_quotes_received, not_viewed, not_applied, freelancer_id)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Saved Search 1', 'Web Developer', '77777777-7777-7777-7777-777777777777', '00000000-0000-0000-0000-000000000001', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', FALSE, 'fixed', ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'], 'newest', ARRAY['Open', 'Under Review'], TRUE, 100, 5, TRUE, FALSE, '44444444-4444-4444-4444-444444444444'),
    ('22222222-2222-2222-2222-222222222222', 'Saved Search 2', 'Logo Design', '88888888-8888-8888-8888-888888888888', '00000000-0000-0000-0000-000000000002', 'dddddddd-dddd-dddd-dddd-dddddddddddd', TRUE, 'hourly', ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'], 'oldest', ARRAY['Open'], FALSE, 200, 10, FALSE, TRUE, '55555555-5555-5555-5555-555555555555'),
    ('33333333-3333-3333-3333-333333333333', 'Saved Search 3', 'Content Writing', '99999999-9999-9999-9999-999999999999', '00000000-0000-0000-0000-000000000003', 'ffffffff-ffff-ffff-ffff-ffffffffffff', FALSE, NULL, ARRAY['cccccccc-cccc-cccc-cccc-cccccccccccc'], 'newest', ARRAY['Open', 'Under Review'], FALSE, 300, NULL, FALSE, TRUE, '66666666-6666-6666-6666-666666666666'),
    ('44444444-4444-4444-4444-444444444444', 'Saved Search 4', 'Web Developer', '77777777-7777-7777-7777-777777777777', '00000000-0000-0000-0000-000000000001', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', TRUE, 'hourly', NULL, 'newest', ARRAY['Open', 'Under Review'], TRUE, 400, 12, TRUE, TRUE, '55555555-5555-5555-5555-555555555555'),
    ('55555555-5555-5555-5555-555555555555', 'Saved Search 5', 'Logo Design', '88888888-8888-8888-8888-888888888888', '00000000-0000-0000-0000-000000000002', 'dddddddd-dddd-dddd-dddd-dddddddddddd', TRUE, NULL, ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'], 'oldest', ARRAY['Open'], FALSE, 500, NULL, FALSE, TRUE, '66666666-6666-6666-6666-666666666666');


INSERT INTO jobs_attachments (id, job_id, url, filename, created_at) VALUES
    ('11111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', 'https://example.com/attachment1.pdf', 'attachment1.pdf', NOW()),
    ('22222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', 'https://example.com/attachment2.docx', 'attachment2.docx', NOW()),
    ('33333333-3333-3333-3333-333333333333', '22222222-2222-2222-2222-222222222222', 'https://example.com/attachment3.png', 'attachment3.png', NOW());


INSERT INTO quote_templates_attachments (id, quote_template_id, url, filename, created_at) VALUES
    ('11111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', 'https://example.com/attachment1.pdf', 'attachment1.pdf', NOW()),
    ('22222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', 'https://example.com/attachment2.docx', 'attachment2.docx', NOW()),
    ('33333333-3333-3333-3333-333333333333', '22222222-2222-2222-2222-222222222222', 'https://example.com/attachment3.png', 'attachment3.png', NOW());




-- select oid::regprocedure, *
-- FROM pg_proc
-- WHERE proname = 'create_job';

-- CALL drop_procedure('create_job');
-- CREATE OR REPLACE PROCEDURE create_job(
--     _title VARCHAR(255),
--     _description TEXT,
--     _category_id UUID,
--     _subcategory_id UUID,
--     _featured BOOLEAN,
--     _client_id UUID,
--     _payment_type VARCHAR(255),
--     _fixed_price_range VARCHAR(255),
--     _duration VARCHAR(255),
--     _hours_per_week VARCHAR(255),
--     _min_hourly_rate DECIMAL(10, 2),
--     _max_hourly_rate DECIMAL(10, 2),
--     _get_quotes_until DATE,
--     _visibility VARCHAR(255),
--     _skills TEXT[],
--     _timezones TEXT[],
--     _locations TEXT[],
--     _attachments TEXT[]
-- )
-- LANGUAGE plpgsql
-- AS $$
-- DECLARE
--     new_job_id UUID;
--     skill_id UUID;
--     location_id UUID;
--     timezone_id UUID;
--     attachment_json text;
-- BEGIN
--     INSERT INTO jobs (id, title, description, category_id, subcategory_id, featured, client_id, payment_type, fixed_price_range, duration, hours_per_week, min_hourly_rate, max_hourly_rate, get_quotes_until, visibility, status)
--     VALUES (gen_random_uuid(), _title,  _description, _category_id, _subcategory_id, _featured, _client_id, _payment_type::payment_type, _fixed_price_range::price_range, _duration::job_duration, _hours_per_week::hours_per_week, _min_hourly_rate, _max_hourly_rate, _get_quotes_until, _visibility::job_visibility, 'Under Review')
--     RETURNING id INTO new_job_id;

--     -- Insert skills related to job
--     FOREACH skill_id IN ARRAY _skills
--     LOOP
--         INSERT INTO jobs_skills (job_id, skill_id)
--         VALUES (new_job_id, skill_id);
--     END LOOP;

--     -- Insert locations related to job
--     FOREACH location_id IN ARRAY _locations
--     LOOP
--         INSERT INTO jobs_locations (job_id, location_id)
--         VALUES (new_job_id, location_id);
--     END LOOP;

--     -- Insert timezones related to job
--     FOREACH timezone_id IN ARRAY _timezones
--     LOOP
--         INSERT INTO jobs_timezones (job_id, timezone_id)
--         VALUES (new_job_id, timezone_id);
--     END LOOP;

--     -- Insert attachments related to job
--     FOREACH attachment_json IN ARRAY _attachments
--     LOOP
--         INSERT INTO jobs_attachments (id, job_id, url, filename, created_at)
--         VALUES (gen_random_uuid(), new_job_id, REPLACE((attachment_json::json->'url')::text, '"', ''), REPLACE((attachment_json::json->'filename')::text, '"', '') , CURRENT_TIMESTAMP);
--     END LOOP;
-- END;
-- $$;

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
    timezones JSON ,
	attachments JSON
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
                   WHERE jt.job_id = j.id) AS timezones,
				(SELECT JSON_AGG(json_build_object('url', ja.url, 'filename', ja.filename))
                   FROM jobs_attachments ja
                   WHERE ja.job_id = j.id) AS attachments
        FROM jobs j
        INNER JOIN categories c ON j.category_id = c.id
        INNER JOIN subcategories s ON j.subcategory_id = s.id
        WHERE j.id = _job_id;
END;
$$ LANGUAGE plpgsql;
select * from get_job_by_id('11111111-1111-1111-1111-111111111111');


DROP FUNCTION IF EXISTS create_job;
CREATE OR REPLACE FUNCTION create_job(
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
    _skills TEXT[],
    _timezones TEXT[],
    _locations TEXT[],
    _attachments TEXT[]
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
    timezones JSON ,
	attachments JSON
)
LANGUAGE plpgsql
AS $$
DECLARE
    new_job_id UUID;
    skill_id UUID;
    location_id UUID;
    timezone_id UUID;
    attachment_json text;
BEGIN
	new_job_id := gen_random_uuid();
-- 	SELECT gen_random_uuid() INTO new_job_id;

    INSERT INTO jobs (id, title, description, category_id, subcategory_id, featured, client_id, payment_type, fixed_price_range, duration, hours_per_week, min_hourly_rate, max_hourly_rate, get_quotes_until, visibility, status)
    VALUES (new_job_id, _title,  _description, _category_id, _subcategory_id, _featured, _client_id, _payment_type::payment_type, _fixed_price_range::price_range, _duration::job_duration, _hours_per_week::hours_per_week, _min_hourly_rate, _max_hourly_rate, _get_quotes_until, _visibility::job_visibility, 'Under Review');
-- --     RETURNING id INTO new_job_id;

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

    -- Insert attachments related to job
    FOREACH attachment_json IN ARRAY _attachments
    LOOP
        INSERT INTO jobs_attachments (id, job_id, url, filename, created_at)
        VALUES (gen_random_uuid(), new_job_id, REPLACE((attachment_json::json->'url')::text, '"', ''), REPLACE((attachment_json::json->'filename')::text, '"', '') , CURRENT_TIMESTAMP);
    END LOOP;

	-- Fetch the newly created job
	  RETURN QUERY SELECT * FROM get_job_by_id(new_job_id);

END;
$$;

select * from create_job(
        'Test new Function',
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
        ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'cccccccc-cccc-cccc-cccc-cccccccccccc'],
		 ARRAY[
        '{"url": "https://example.com/attachment1.pdf", "filename": "attachment1.pdf"}',
        '{"url": "https://example.com/attachment2.docx", "filename": "attachment2.docx"}'
    ]
);



-- Call create_dummy_job();

-- CALL drop_procedure('create_dummy_job');
-- CREATE OR REPLACE PROCEDURE create_dummy_job(
-- )
-- LANGUAGE plpgsql
-- AS $$
-- BEGIN
--     CALL create_job(
--         'Frontend Freelancer Needed',
--         'Looking for a skilled backend developer to create a responsive website.',
--         '77777777-7777-7777-7777-777777777777',
--         '00000000-0000-0000-0000-000000000001',
--         FALSE,
--         '11111111-1111-1111-1111-111111111111',
--         'fixed',
--         'Under $250',
--         'Less than 1 month',
--         '10-30',
--         NULL,
--         NULL,
--         '2024-05-01',
--         'Everyone',
--         ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'cccccccc-cccc-cccc-cccc-cccccccccccc'],
--         ARRAY['11111111-1111-1111-1111-111111111111'],
--         ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'cccccccc-cccc-cccc-cccc-cccccccccccc'],
-- 		 ARRAY[
--         '{"url": "https://example.com/attachment1.pdf", "filename": "attachment1.pdf"}',
--         '{"url": "https://example.com/attachment2.docx", "filename": "attachment2.docx"}'
--     ]
--     );
-- END;
-- $$;

--CALL drop_procedure('update_job');
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
    _locations TEXT[] DEFAULT NULL,
	_attachments TEXT[] DEFAULT NULL
)
AS $$
DECLARE
    skill_id UUID;
    location_id UUID;
    timezone_id UUID;
	attachment_json text;
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

	IF _attachments IS NOT NULL THEN
		-- Delete existing entries from jobs_attachments
		DELETE FROM jobs_attachments WHERE job_id = _job_id;

		-- Insert attachments related to job
		FOREACH attachment_json IN ARRAY _attachments
		LOOP
			INSERT INTO jobs_attachments (id, job_id, url, filename, created_at)
			VALUES (gen_random_uuid(), _job_id, REPLACE((attachment_json::json->'url')::text, '"', ''), REPLACE((attachment_json::json->'filename')::text, '"', '') , CURRENT_TIMESTAMP);
		END LOOP;
		END IF;

END;
$$ LANGUAGE plpgsql;


CALL update_job(
    '11111111-1111-1111-1111-111111111111',  -- _job_id
    'Updated Job Title',                     -- _title
    'Updated job description goes here.',    -- _description
    '77777777-7777-7777-7777-777777777777',  -- _category_id
    '00000000-0000-0000-0000-000000000001',  -- _subcategory_id
    TRUE,                                    -- _featured
    'hourly',                                -- _payment_type
    NULL,                                    -- _fixed_price_range
    'Less than 1 month',                     -- _duration
    '10-30',                                 -- _hours_per_week
    15.00,                                   -- _min_hourly_rate
    30.00,                                   -- _max_hourly_rate
    '2024-05-20',                            -- _get_quotes_until
    'Everyone',                              -- _visibility
    'Closed',                                  -- _status
    ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'ffffffff-ffff-ffff-ffff-ffffffffffff'],      --skills
    ARRAY['44444444-4444-4444-4444-444444444444', '55555555-5555-5555-5555-555555555555'], --timezones
    ARRAY['bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'],             --locations
    ARRAY[
        '{"url": "https://example.com/attachment3.pdf", "filename": "attachment3.pdf"}',
        '{"url": "https://example.com/attachment4.docx", "filename": "attachment4.docx"}'
    ] -- file attachments
);

-- CALL create_job(
--     'Frontend Freelancer Needed',
-- 	'Looking for a skilled backend developer to create a responsive website.',
--     '77777777-7777-7777-7777-777777777777',
--     '00000000-0000-0000-0000-000000000001',
-- 	TRUE,
--     '11111111-1111-1111-1111-111111111111',
-- 	'fixed',
-- 	 'Under $250',
--     'Less than 1 month',
--     '10-30',
-- 	NULL,
--     NULL,
-- 	 '2024-05-01',
--     'Everyone',
-- 	ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'cccccccc-cccc-cccc-cccc-cccccccccccc'],
--  	ARRAY['11111111-1111-1111-1111-111111111111'],
-- 	ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'cccccccc-cccc-cccc-cccc-cccccccccccc']
-- );



-- call create_dummy_job


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
    _location_ids text[] DEFAULT NULL,
    _sort_order VARCHAR(20) DEFAULT 'newest',
    _status_list text[] DEFAULT ARRAY['Open', 'Under Review', 'Closed', 'Not Approved'],
	_verified_only BOOLEAN DEFAULT NULL,
	_min_employer_spend INT DEFAULT NULL,
	_max_quotes_received INT DEFAULT NULL,
    _not_viewed BOOLEAN DEFAULT NULL,
	_not_applied BOOLEAN DEFAULT NULL,
	_freelancer_id UUID DEFAULT NULL,
	_client_id UUID DEFAULT NULL
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
    status job_status,
    skills JSON,
    locations JSON,
    timezones JSON,
	attachments JSON
)
AS $$
BEGIN
   RETURN QUERY
        SELECT
            j.id,
            j.title,
            j.description,
            j.category_id,
            j.subcategory_id,
            j.featured,
            j.client_id,
            j.created_at,
            j.payment_type::payment_type,
            j.fixed_price_range::price_range,
            j.duration::job_duration,
            j.hours_per_week::hours_per_week,
            j.min_hourly_rate,
            j.max_hourly_rate,
            j.get_quotes_until,
            j.visibility::job_visibility,
            j.status::job_status,
            (
                SELECT JSON_AGG(json_build_object('id', sk.id, 'name', sk.name))
                FROM jobs_skills js
                INNER JOIN skills sk ON js.skill_id = sk.id
                WHERE js.job_id = j.id
            ) AS skills,
            (
                SELECT JSON_AGG(json_build_object('id', loc.id, 'name', loc.name))
                FROM jobs_locations jl
                INNER JOIN locations loc ON jl.location_id = loc.id
                WHERE jl.job_id = j.id
            ) AS locations,
            (
                SELECT JSON_AGG(json_build_object('id', tz.id, 'name', tz.name))
                FROM jobs_timezones jt
                INNER JOIN timezones tz ON jt.timezone_id = tz.id
                WHERE jt.job_id = j.id
            ) AS timezones,
				(SELECT JSON_AGG(json_build_object('url', ja.url, 'filename', ja.filename))
                   FROM jobs_attachments ja
                   WHERE ja.job_id = j.id) AS attachments
        FROM
            jobs j
        INNER JOIN users u ON j.client_id = u.id
        WHERE
            ( _verified_only IS NULL OR u.is_verified = _verified_only )
			AND ( _min_employer_spend IS NULL OR u.amount_spent >= _min_employer_spend )
			AND (_client_id IS NULL OR j.client_id = _client_id)
			AND (_max_quotes_received IS NULL OR
                  (SELECT COUNT(*) FROM quotes WHERE job_id = j.id) <= _max_quotes_received )
			AND (
                  (_not_viewed IS NULL) OR
                  (
                      (_not_viewed = TRUE) AND
                      NOT EXISTS (
                          SELECT 1 FROM job_freelancer_view WHERE job_id = j.id and freelancer_id = _freelancer_id
                      )
                  ) OR
                  (
                      (_not_viewed = FALSE) AND
                      EXISTS (
                          SELECT 1 FROM job_freelancer_view WHERE job_id = j.id and freelancer_id = _freelancer_id
                      )
                  )
            )
			AND (
                  (_not_applied IS NULL) OR
                  (
                      (_not_applied = TRUE) AND
                      NOT EXISTS (
                          SELECT 1 FROM quotes WHERE job_id = j.id AND freelancer_id = _freelancer_id
                      )
                  ) OR
                  (
                      (_not_applied = FALSE) AND
                      EXISTS (
                          SELECT 1 FROM quotes WHERE job_id = j.id AND freelancer_id = _freelancer_id
                      )
                  )
			)
            AND (_search_query IS NULL OR (j.title ILIKE '%' || _search_query || '%') OR (j.description ILIKE '%' || _search_query || '%'))
            AND (_category_id IS NULL OR j.category_id = _category_id)
            AND (_subcategory_id IS NULL OR j.subcategory_id = _subcategory_id)
            AND (_skill_id IS NULL OR j.id IN (SELECT job_id FROM jobs_skills WHERE skill_id = _skill_id))
            AND (_featured_only IS NULL OR j.featured = _featured_only)
            AND (_payment_terms IS NULL OR j.payment_type::TEXT = _payment_terms)
            AND (_location_ids IS NULL OR j.id IN (SELECT job_id FROM jobs_locations WHERE location_id::text = ANY(_location_ids))) -- Filter by locations
            AND (_status_list IS NULL OR j.status::TEXT = ANY(_status_list))
            AND (
                CASE _sort_order
                    WHEN 'closing_soon' THEN j.get_quotes_until >= CURRENT_DATE
                    ELSE TRUE
                END
            )
        ORDER BY
            CASE _sort_order
                WHEN 'newest' THEN j.created_at
            END DESC,
            CASE _sort_order
                WHEN 'oldest' THEN j.created_at
                WHEN 'closing_soon' THEN j.get_quotes_until
            END ASC
        LIMIT _page_size
        OFFSET ((_page - 1) * _page_size);
END;
$$ LANGUAGE plpgsql;


-- SELECT *
-- FROM get_all_jobs(
--     _page := 1,
--     _page_size := 10,
-- 	_location_ids := ARRAY['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'cccccccc-cccc-cccc-cccc-cccccccccccc']
-- 	_client_id := '11111111-1111-1111-1111-111111111111'
-- 	_not_viewed := false,
-- 	_not_applied := false,
-- 	_freelancer_id := '55555555-5555-5555-5555-555555555555'
--     _not_viewed BOOLEAN DEFAULT NULL
-- 	_min_employer_spend := 100
-- 	_verified_only := true,
-- 	_status_list := null
--     _search_query := 'web development', -- Search query (case-insensitive)
--     _category_id := '77777777-7777-7777-7777-777777777777', -- Category ID (optional)
--     _subcategory_id := NULL, -- Subcategory ID (optional)
--     _skill_id := NULL, -- Skill ID (optional)
--     _featured_only := TRUE, -- Featured only (optional)
--     _payment_terms := NULL, -- Payment terms (optional)
--     _location_id := NULL, -- Location ID (optional)
-- );


--CALL drop_procedure('delete_job_by_id');
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

	DELETE FROM jobs_attachments WHERE job_id = _job_id;

    DELETE FROM jobs WHERE id = _job_id;
END;
$$;

-- Call delete_job_by_id('22222222-2222-2222-2222-222222222222');


-- Categories, subcategories & skills
DROP FUNCTION IF EXISTS get_categories_with_subcategories_and_skills;
CREATE OR REPLACE FUNCTION get_categories_with_subcategories_and_skills(
    _category_id UUID DEFAULT NULL
)
RETURNS TABLE (
    id UUID,
    category_name VARCHAR(100),
    subcategories JSONB
)
AS $$
BEGIN
    RETURN QUERY
        SELECT
            c.id AS id,
            c.name AS category_name,
            jsonb_agg(
                jsonb_build_object(
                    'id', sc.id,
                    'name', sc.name,
                    'skills', (
                        SELECT jsonb_agg(jsonb_build_object('id', s.id, 'name', s.name))
                        FROM skills s
                        WHERE s.id = sc.skill_id
                    )
                )
            ) AS subcategories
        FROM
            categories c
        LEFT JOIN
            subcategories sc ON c.id = sc.category_id
        WHERE
            (_category_id IS NULL OR c.id = _category_id)
        GROUP BY
            c.id, c.name;
END;
$$ LANGUAGE plpgsql;


-- SELECT * FROM get_categories_with_subcategories_and_skills();


-- Mark Job as viewed by Freelancer
--CALL drop_procedure('view_job');
CREATE OR REPLACE PROCEDURE view_job(
    _job_id UUID,
    _freelancer_id UUID
)
AS $$
BEGIN
    -- Check if the job view already exists for the given job and freelancer
    IF NOT EXISTS (
        SELECT 1 FROM job_freelancer_view WHERE job_id = _job_id AND freelancer_id = _freelancer_id
    ) THEN
        -- If the job view doesn't exist, insert a new row into the job_views table
        INSERT INTO job_freelancer_view (job_id, freelancer_id) VALUES (_job_id, _freelancer_id);
    END IF;
END;
$$ LANGUAGE plpgsql;

CALL view_job('11111111-1111-1111-1111-111111111111', '66666666-6666-6666-6666-666666666666');