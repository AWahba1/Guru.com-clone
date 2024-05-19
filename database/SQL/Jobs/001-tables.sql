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
    id UUID PRIMARY KEY,
    freelancer_name VARCHAR(50),
    image_url VARCHAR(255),
    visibility BOOLEAN,
    profile_views INT DEFAULT 0,
    job_invitations_num INT,
    available_bids INT,
    all_time_earnings DECIMAL,
    employers_num INT,
    highest_paid DECIMAL,
    membership_date TIMESTAMP,
    tagline VARCHAR(190),
    bio VARCHAR(3000),
    work_terms VARCHAR(2000),
    attachments VARCHAR(255) ARRAY,
    user_type varchar(255) CHECK (user_type IN ('INDIVIDUAL','COMPANY')),
    website_link VARCHAR(255),
    facebook_link VARCHAR(255),
    linkedin_link VARCHAR(255),
    professional_video_link VARCHAR(255),
    company_history VARCHAR(3000),
    operating_since TIMESTAMP
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

CREATE TYPE team_member_role_client AS ENUM ('ADMINISTRATOR', 'MANAGER', 'COORDINATOR');

CREATE TABLE team_members_client (
    owner_id UUID REFERENCES users(id) ON DELETE CASCADE,
    team_member_id UUID REFERENCES users(id) ON DELETE CASCADE,
    role team_member_role_client NOT NULL,
    email VARCHAR(255) NOT NULL,
    PRIMARY KEY (owner_id, team_member_id)
);
	
-- Populate tables with sample data
-- Users
	
INSERT INTO users (id, is_verified, amount_spent) VALUES
    ('11111111-1111-1111-1111-111111111111', true, 100),
    ('22222222-2222-2222-2222-222222222222', false, 500.55),
    ('33333333-3333-3333-3333-333333333333', false, 4000);

INSERT INTO freelancers (
    id,
    freelancer_name,
    image_url,
    visibility,
    profile_views,
    job_invitations_num,
    available_bids,
    all_time_earnings,
    employers_num,
    highest_paid,
    membership_date,
    tagline,
    bio,
    work_terms,
    attachments,
    user_type,
    website_link,
    facebook_link,
    linkedin_link,
    professional_video_link,
    company_history,
    operating_since
) VALUES
    ('44444444-4444-4444-4444-444444444444', 'freelancer2', 'image_url2', true, 200, 20, 30, 2000, 10, 30000, '2021-01-01 00:00:00', 'tagline2', 'bio2', 'work_terms2', ARRAY['attachment2'], 'INDIVIDUAL', 'website_link2', 'facebook_link2', 'linkedin_link2', 'professional_video_link2', 'company_history2', '2021-01-01 00:00:00'),
    ('55555555-5555-5555-5555-555555555555', 'freelancer3', 'image_url3', true, 300, 30, 40, 3000, 20, 40000, '2021-01-01 00:00:00', 'tagline3', 'bio3', 'work_terms3', ARRAY['attachment3'], 'COMPANY', 'website_link3', 'facebook_link3', 'linkedin_link3', 'professional_video_link3', 'company_history3', '2021-01-01 00:00:00'),
    ('66666666-6666-6666-6666-666666666666', 'freelancer4', 'image_url4', true, 400, 40, 50, 4000, 30, 50000, '2021-01-01 00:00:00', 'tagline4', 'bio4', 'work_terms4', ARRAY['attachment4'], 'INDIVIDUAL', 'website_link4', 'facebook_link4', 'linkedin_link4', 'professional_video_link4', 'company_history4', '2021-01-01 00:00:00');

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