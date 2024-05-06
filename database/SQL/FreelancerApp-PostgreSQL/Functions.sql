CREATE OR REPLACE FUNCTION get_freelancer_profile (_freelancer_id UUID)
RETURNS TABLE (
freelancer_id UUID,
    freelancer_name VARCHAR(50),
    image_url VARCHAR(255),
    visibility BOOLEAN,
    profile_views INT,
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
    user_type varchar(255),
    website_link VARCHAR(255),
    facebook_link VARCHAR(255),
    linkedin_link VARCHAR(255),
    professional_video_link VARCHAR(255),
    company_history VARCHAR(3000),
    operating_since TIMESTAMP,
    service_skills varchar(100) ARRAY,
    resource_skills varchar(100) ARRAY,
    portfolio_service_skills varchar(100) ARRAY,
    portfolio_resource_skills varchar(100) ARRAY
) AS $$
DECLARE
BEGIN
RETURN QUERY
    SELECT
        f.*,
        ARRAY(SELECT sk.name FROM service_skills ss JOIN services s ON ss.service_id = s.service_id JOIN skills sk ON sk.id = ss.skill_id WHERE s.freelancer_id = _freelancer_id AND s.is_draft = false),
        ARRAY(SELECT sk.name FROM resource_skills rs JOIN dedicated_resource r ON rs.resource_id = r.resource_id JOIN skills sk ON sk.id =rs.skill_id WHERE r.freelancer_id = _freelancer_id AND r.is_draft = false),
        ARRAY(SELECT sk.name FROM portfolio_skills ps JOIN portfolios p ON ps.portfolio_id = p.portfolio_id JOIN skills sk ON sk.id = ps.skill_id JOIN portfolio_service p_s ON p_s.portfolio_id = ps.portfolio_id WHERE p.freelancer_id = _freelancer_id),
        ARRAY(SELECT sk.name FROM portfolio_skills ps JOIN portfolios p ON ps.portfolio_id = p.portfolio_id JOIN skills sk ON sk.id = ps.skill_id JOIN portfolio_resource p_r ON p_r.portfolio_id = ps.portfolio_id WHERE p.freelancer_id = _freelancer_id)
    FROM freelancers f
    WHERE f.freelancer_id = _freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_portfolio (_portfolio_id uuid)
RETURNS TABLE (
portfolio_id uuid,
freelancer_id uuid,
title varchar(255),
cover_image_url varchar(255),
portfolio_skills varchar(255) ARRAY,
attachments varchar(255) ARRAY,
is_draft boolean
)
AS $$
BEGIN
    RETURN QUERY
    SELECT p.portfolio_id, p.freelancer_id, p.title, p.cover_image_url,
    ARRAY(SELECT sk.name FROM portfolio_skills ps JOIN skills sk ON ps.skill_id = sk.id WHERE ps.portfolio_id = p.portfolio_id),
    p.attachments, p.is_draft
    FROM portfolios p WHERE p.portfolio_id = _portfolio_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_all_freelancer_portfolios (_freelancer_id uuid)
RETURNS TABLE (
portfolio_id uuid,
title varchar(255),
cover_image_url varchar(255)
)
AS $$
BEGIN
    RETURN QUERY
    SELECT p.portfolio_id, p.title, p.cover_image_url FROM portfolios p WHERE freelancer_id = _freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_all_freelancer_services (_freelancer_id uuid)
RETURNS TABLE (
service_id uuid,
service_title varchar(255),
service_description varchar(5000),
service_skills varchar(255) ARRAY,
service_rate decimal,
minimum_budget decimal,
service_thumbnail varchar(255)
)
AS $$
BEGIN
    RETURN QUERY
    SELECT s.service_id, s.service_title, s.service_description,
    ARRAY(SELECT sk.name FROM service_skills ss JOIN skills sk ON ss.skill_id = sk.id WHERE ss.service_id = s.service_id UNION
    SELECT sk.name FROM portfolio_skills ps JOIN skills sk ON ps.skill_id = sk.id JOIN portfolio_service p_s ON p_s.portfolio_id = ps.portfolio_id WHERE p_s.service_id = s.service_id),
    s.service_rate, s.minimum_budget, s.service_thumbnail
    FROM services s WHERE freelancer_id = _freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_all_freelancer_dedicated_resources (_freelancer_id uuid)
RETURNS TABLE (
resource_id uuid,
resource_name varchar(255),
resource_title varchar(255),
resource_summary varchar(3000),
resource_skills varchar(255) ARRAY,
resource_rate decimal,
minimum_duration varchar(255),
resource_image varchar(255)
)
AS $$
BEGIN
    RETURN QUERY
    SELECT r.resource_id, r.resource_name, r.resource_title, r.resource_summary,
    ARRAY(SELECT sk.name FROM resource_skills rs JOIN skills sk ON rs.skill_id = sk.id WHERE rs.resource_id = r.resource_id UNION
    SELECT sk.name FROM portfolio_skills ps JOIN skills sk ON ps.skill_id = sk.id JOIN portfolio_resource p_r ON p_r.portfolio_id = ps.portfolio_id WHERE p_r.resource_id = r.resource_id),
    r.resource_rate, r.minimum_duration, r.resource_image
    FROM dedicated_resource r WHERE freelancer_id = _freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_service_details(_service_id UUID)
RETURNS TABLE(
    service_id UUID,
    freelancer_id UUID,
    service_title VARCHAR(255),
    service_description VARCHAR(5000),
    service_rate DECIMAL,
    minimum_budget DECIMAL,
    service_thumbnail VARCHAR(255),
    service_views INT,
    service_skills varchar(255) ARRAY,
    portfolio_ids UUID ARRAY
) AS $$
DECLARE
    portfolio_exists BOOLEAN;
BEGIN
    portfolio_exists := EXISTS (SELECT 1 FROM portfolio_service p_s WHERE p_s.service_id = _service_id);

    IF portfolio_exists THEN
        RETURN QUERY
            SELECT
                s.service_id,s.freelancer_id,s.service_title,s.service_description,s.service_rate,s.minimum_budget,s.service_thumbnail,s.service_views,
                ARRAY(SELECT sk.name FROM service_skills ss JOIN skills sk ON ss.skill_id = sk.id WHERE ss.service_id = _service_id),
                ARRAY(SELECT ps.portfolio_id FROM portfolio_service ps WHERE ps.service_id=_service_id)
            FROM services s
--             JOIN portfolio_service ps ON s.service_id = ps.service_id
--             JOIN portfolios p ON ps.portfolio_id = p.portfolio_id
            WHERE s.service_id = _service_id;
    ELSE
        RETURN QUERY
            SELECT
                s.service_id,s.freelancer_id,s.service_title,s.service_description,s.service_rate,s.minimum_budget,s.service_thumbnail,s.service_views,
                ARRAY(SELECT sk.name FROM service_skills ss JOIN skills sk ON ss.skill_id = sk.id WHERE ss.service_id = _service_id),
                NULL::UUID ARRAY AS portfolio_ids
--                 NULL::UUID AS portfolio_id, NULL::TEXT AS title, NULL::TEXT AS cover_image_url
            FROM services s
            WHERE s.service_id = _service_id;
    END IF;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_resource_details(_resource_id UUID)
RETURNS TABLE(
    resource_id UUID,
    freelancer_id UUID,
    resource_name VARCHAR(255),
    resource_title VARCHAR(255),
    resource_summary VARCHAR(3000),
    resource_rate DECIMAL,
    minimum_duration VARCHAR(255),
    resource_image VARCHAR(255),
    resource_views INT,
    resource_skills varchar(255) ARRAY,
    portfolio_ids UUID ARRAY
) AS $$
DECLARE
    portfolio_exists BOOLEAN;
BEGIN
    portfolio_exists := EXISTS (SELECT 1 FROM portfolio_resource pr WHERE pr.resource_id = _resource_id);

    IF portfolio_exists THEN
        RETURN QUERY
            SELECT
                r.resource_id, r.freelancer_id, r.resource_name, r.resource_title, r.resource_summary, r.resource_rate, r.minimum_duration, r.resource_image, r.resource_views,
                ARRAY(SELECT sk.name FROM resource_skills rs JOIN skills sk ON rs.skill_id = sk.id WHERE rs.resource_id = _resource_id),
                ARRAY(SELECT pr.portfolio_id FROM portfolio_resource pr WHERE pr.resource_id = _resource_id)
            FROM dedicated_resource r
            WHERE r.resource_id = _resource_id;
    ELSE
        RETURN QUERY
            SELECT
                r.resource_id, r.freelancer_id, r.resource_name, r.resource_title, r.resource_summary, r.resource_rate, r.minimum_duration, r.resource_image, r.resource_views,
                ARRAY(SELECT sk.name FROM resource_skills rs JOIN skills sk ON rs.skill_id = sk.id WHERE rs.resource_id = _resource_id),
                NULL::UUID ARRAY AS portfolio_ids
            FROM dedicated_resource r
            WHERE r.resource_id = _resource_id;
    END IF;
END;
$$ 
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_freelancer_team_members (_freelancer_id uuid)
RETURNS TABLE (team_member_id uuid,freelancer_id uuid, member_name varchar(255), title varchar(255), member_type varchar(255), member_email varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT *
    FROM featured_team_member f
    WHERE f.freelancer_id = _freelancer_id;
END;
$$
LANGUAGE plpgsql;


