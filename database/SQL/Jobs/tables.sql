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


-- 	CREATE TABLE job_freelancer (
-- 		job_id UUID REFERENCES jobs(id),
-- 		freelancer_id UUID REFERENCES freelancers(id), -- check foreign key on which table
-- 		applied BOOLEAN DEFAULT FALSE,
-- 		viewed BOOLEAN DEFAULT FALSE,
-- 		PRIMARY KEY (job_id, freelancer_id)
-- 	);
	
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



	




