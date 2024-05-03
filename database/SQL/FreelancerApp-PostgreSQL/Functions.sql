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
    attachments TEXT[],
    user_type varchar(255),
    website_link VARCHAR(255),
    facebook_link VARCHAR(255),
    linkedin_link VARCHAR(255),
    professional_video_link VARCHAR(255),
    company_history VARCHAR(3000),
    operating_since TIMESTAMP,
    service_skills varchar(255) ARRAY,
    resource_skills varchar(255) ARRAY
) AS $$
DECLARE
BEGIN
RETURN QUERY
    SELECT
        f.*,
        ARRAY(SELECT sk.name FROM service_skills ss JOIN services s ON ss.service_id = s.service_id JOIN skills sk ON sk.id = ss.skill_id WHERE s.freelancer_id = _freelancer_id),
        ARRAY(SELECT sk.name FROM resource_skills rs JOIN dedicated_resource r ON rs.resource_id = r.resource_id JOIN skills sk ON sk.id =rs.skill_id WHERE r.freelancer_id = _freelancer_id)
    FROM freelancers f
    WHERE f.freelancer_id = _freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_portfolio (_portfolio_id uuid)
RETURNS TABLE (portfolio_id uuid, freelancer_id uuid, title varchar(255), cover_image_url varchar(255), attachments text[], is_draft boolean)
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM portfolios WHERE portfolio_id = _portfolio_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_my_portfolios (_freelancer_id uuid)
RETURNS TABLE (portfolio_id uuid, title varchar(255), cover_image_url varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT portfolio_id, title, cover_image_url FROM portfolios WHERE freelancer_id = _freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_my_services (_freelancer_id uuid)
RETURNS TABLE (service_id uuid, service_title varchar(255), service_description varchar(5000), service_skills text[], service_rate decimal, minimum_budget decimal, service_thumbnail varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM services WHERE freelancer_id = _freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_my_dedicated_resources (freelancer_id uuid)
RETURNS TABLE (resource_id uuid, resource_name varchar(255), resource_title varchar(255), resource_summary varchar(3000), resource_skills text[], resource_rate decimal, minimum_duration resource_duration_enum, resource_image varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM dedicated_resource WHERE freelancer_id = freelancer_id;
END;
$$
LANGUAGE plpgsql;

--CREATE OR REPLACE FUNCTION get_freelancer_portfolios (_freelancer_id uuid)
--RETURNS TABLE (portfolio_id uuid, title varchar(255), cover_image_url varchar(255))
--AS $$
--BEGIN
--    RETURN QUERY
--    SELECT portfolio_id, title, cover_image_url FROM portfolios WHERE freelancer_id = _freelancer_id AND is_draft = false;
--END;
--$$
--LANGUAGE plpgsql;

--CREATE OR REPLACE FUNCTION get_freelancer_services (_freelancer_id uuid)
--RETURNS TABLE (service_id uuid, service_title varchar(255), service_description varchar(5000), service_skills text[], service_rate decimal, minimum_budget decimal, service_thumbnail varchar(255))
--AS $$
--BEGIN
--    RETURN QUERY
--    SELECT * FROM services WHERE freelancer_id = _freelancer_id AND is_draft = false;
--END;
--$$
--LANGUAGE plpgsql;
--
--CREATE OR REPLACE FUNCTION get_freelancer_dedicated_resources (freelancer_id uuid)
--RETURNS TABLE (resource_id uuid, resource_name varchar(255), resource_title varchar(255), resource_summary varchar(3000), resource_skills text[], resource_rate decimal, minimum_duration resource_duration_enum, resource_image varchar(255))
--AS $$
--BEGIN
--    RETURN QUERY
--    SELECT * FROM dedicated_resource WHERE freelancer_id = freelancer_id AND is_draft = false;
--END;
--$$
--LANGUAGE plpgsql;

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
    portfolio_id UUID,
    portfolio_title VARCHAR(255),
    portfolio_cover_image_url VARCHAR(255)
) AS $$
DECLARE
    portfolio_exists BOOLEAN;
BEGIN
    portfolio_exists := EXISTS (SELECT 1 FROM portfolio_service WHERE service_id = _service_id);

    IF portfolio_exists THEN
        RETURN QUERY
            SELECT
                s.*,
                ARRAY(SELECT sk.name FROM service_skills ss JOIN skills sk ON ss.skill_id = sk.id WHERE ss.service_id = _service_id),
                p.portfolio_id, p.title, p.cover_image_url
            FROM services s
            JOIN portfolio_service ps ON s.service_id = ps.service_id
            JOIN portfolios p ON ps.portfolio_id = p.portfolio_id
            WHERE s.service_id = _service_id;
    ELSE
        RETURN QUERY
            SELECT
                s.*,
                ARRAY(SELECT sk.name FROM service_skills ss JOIN skills sk ON ss.skill_id = sk.id WHERE ss.service_id = _service_id),
                NULL::UUID AS portfolio_id, NULL::TEXT AS title, NULL::TEXT AS cover_image_url
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
    resource_skills varchar(255) ARRAY,
    resource_rate DECIMAL,
    minimum_duration resource_duration_enum,
    resource_image VARCHAR(255),
    resource_views INT,
    portfolio_id UUID,
    portfolio_title VARCHAR(255),
    portfolio_cover_image_url VARCHAR(255)

) AS $$
DECLARE
    portfolio_exists BOOLEAN;
BEGIN
    portfolio_exists := EXISTS (SELECT 1 FROM portfolio_resource WHERE resource_id = _resource_id);

    IF portfolio_exists THEN
        RETURN QUERY
            SELECT
                r.*,
                ARRAY(SELECT sk.name FROM resource_skills rs JOIN skills sk ON rs.skill_id = sk.id WHERE rs.resource_id = _resource_id),
                p.portfolio_id, p.title, p.cover_image_url
            FROM dedicated_resource r
            JOIN portfolio_resource pr ON r.resource_id = pr.resource_id
            JOIN portfolios p ON pr.portfolio_id = p.portfolio_id
            WHERE r.resource_id = _resource_id;
    ELSE
        RETURN QUERY
            SELECT
                r.*,
                ARRAY(SELECT sk.name FROM resource_skills rs JOIN skills sk ON rs.skill_id = sk.id WHERE rs.resource_id = _resource_id),
                NULL::UUID AS portfolio_id, NULL::TEXT AS title, NULL::TEXT AS cover_image_url
            FROM dedicated_resource r
            WHERE r.resource_id = _resource_id;
    END IF;
END;
$$ 
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_freelancer_quotes (_freelancer_id uuid, _quote_status quote_status_enum)
RETURNS TABLE (quote_id uuid, job_id uuid, proposal varchar(3000), quote_status quote_status_enum, bids_used decimal, bid_date timestamp)
AS $$
BEGIN
    IF quote_status IS NULL THEN
        RETURN QUERY
        SELECT * FROM quotes WHERE freelancer_id = _freelancer_id;
    ELSE
        RETURN QUERY
        SELECT * FROM quotes WHERE freelancer_id = freelancer_id AND quote_status = _quote_status;
    END IF;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_freelancer_quote_details (_quote_id uuid)
RETURNS TABLE (quote_id uuid, freelancer_id uuid, job_id uuid, proposal varchar(3000), quote_status quote_status_enum, bids_used decimal, bid_date timestamp)
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM quotes WHERE quote_id = _quote_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_bids_usage_history (_freelancer_id uuid)
RETURNS TABLE (bids_used decimal, bid_date timestamp)
AS $$
BEGIN
    RETURN QUERY
    SELECT bids_used, bid_date
    FROM quotes
    WHERE freelancer_id = _freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION bids_usage_summary (_freelancer_id uuid)
RETURNS TABLE (all_time_bids_used decimal, last_month_bids_used decimal, last_year_bids_used decimal)
AS $$
BEGIN
    RETURN QUERY
    SELECT 
        (SELECT SUM(bids_used) FROM quotes WHERE freelancer_id = _freelancer_id) AS all_time_bids_used,
        (SELECT SUM(bids_used) FROM quotes WHERE freelancer_id = _freelancer_id AND bid_date > CURRENT_DATE - INTERVAL '1 month') AS last_month_bids_used,
        (SELECT SUM(bids_used) FROM quotes WHERE freelancer_id = _freelancer_id AND bid_date > CURRENT_DATE - INTERVAL '1 year') AS last_year_bids_used;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_freelancer_quote_templates (_freelancer_id uuid)
RETURNS TABLE (quote_template_id uuid, template_name varchar(255), template_description varchar(10000), attachments text[])
AS $$
BEGIN
    RETURN QUERY
    SELECT quote_template_id, template_name, template_description, attachments
    FROM quote_templates
    WHERE freelancer_id = _freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_freelancer_job_watchlist (_freelancer_id uuid)
RETURNS TABLE (watchlist_id uuid, job_id uuid)
AS $$
BEGIN
    RETURN QUERY
    SELECT watchlist_id, job_id
    FROM job_watchlist
    WHERE freelancer_id = _freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_freelancer_job_invitations (_freelancer_id uuid)
RETURNS TABLE (invitation_id uuid, client_id uuid, job_id uuid, invitation_date timestamp)
AS $$
BEGIN
    RETURN QUERY
    SELECT invitation_id, client_id, job_id, invitation_date
    FROM job_invitations
    WHERE freelancer_id = _freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_freelancer_team_members (_freelancer_id uuid)
RETURNS TABLE (team_member_id uuid, member_name varchar(255), title team_member_role, member_type team_member_type, member_email varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT team_member_id, member_name, title, member_type, member_email
    FROM featured_team_member
    WHERE freelancer_id = _freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION view_last_bids_until_threshold (_freelancer_id uuid)
RETURNS TABLE (
    quote_id uuid,
    freelancer_id uuid,
    job_id uuid,
    proposal varchar(3000),
    quote_status varchar(255),
    bids_used decimal,
    bid_date timestamp
) 
AS $$
DECLARE
    total_bids DECIMAL := 0;
    bid_record RECORD;
BEGIN
    CREATE TEMP TABLE temp_quotes AS
    SELECT *, 0 AS cumulative_sum
    FROM quotes
    WHERE freelancer_id = _freelancer_id
    ORDER BY bid_date DESC;

    FOR bid_record IN SELECT * FROM temp_quotes LOOP
        total_bids := total_bids + bid_record.bids_used;
        UPDATE temp_quotes SET cumulative_sum = total_bids WHERE quote_id = bid_record.quote_id;
    END LOOP;

    RETURN QUERY SELECT * FROM temp_quotes WHERE cumulative_sum <= 100 ORDER BY bid_date DESC;

    DROP TABLE IF EXISTS temp_quotes;
END;
$$ 
LANGUAGE plpgsql;
