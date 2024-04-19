CREATE OR REPLACE FUNCTION get_freelancer_profile (
    IN freelancer_id UUID,
    OUT freelancer_cursor REFCURSOR,
    OUT freelancer_skills_cursor REFCURSOR
)
RETURNS RECORD AS $$
DECLARE
BEGIN
    OPEN freelancer_cursor FOR
    SELECT * FROM freelancers WHERE freelancer_id = get_freelancer_profile.freelancer_id;

    OPEN freelancer_skills_cursor FOR
    SELECT s.service_skills FROM services s WHERE s.freelancer_id = get_freelancer_profile.freelancer_id 
    UNION 
    SELECT r.resource_skills FROM dedicated_resource r WHERE r.freelancer_id = get_freelancer_profile.freelancer_id;
END;
$$ 
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_my_portfolios (freelancer_id uuid)
RETURNS TABLE (portfolio_id uuid, title varchar(255), cover_image_url varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT portfolio_id, title, cover_image_url FROM portfolios WHERE freelancer_id = freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_freelancer_portfolios (freelancer_id uuid)
RETURNS TABLE (portfolio_id uuid, title varchar(255), cover_image_url varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT portfolio_id, title, cover_image_url FROM portfolios WHERE freelancer_id = freelancer_id AND is_draft = false;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_portfolio (in_portfolio_id uuid)
RETURNS TABLE (portfolio_id uuid, freelancer_id uuid, title varchar(255), cover_image_url varchar(255), attachments text[], is_draft boolean)
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM portfolios WHERE portfolio_id = in_portfolio_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_my_services (freelancer_id uuid)
RETURNS TABLE (service_id uuid, service_title varchar(255), service_description varchar(5000), service_skills text[], service_rate decimal, minimum_budget decimal, service_thumbnail varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM services WHERE freelancer_id = freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_service_details(
    IN service_id UUID,
    OUT service_details_result REFCURSOR,
    OUT portfolio_result REFCURSOR
)
RETURNS RECORD AS $$
BEGIN
    OPEN service_details_result FOR
    SELECT * FROM services WHERE service_id = get_service_details.service_id;

    IF EXISTS (SELECT * FROM portfolio_service WHERE service_id = get_service_details.service_id) THEN
        OPEN portfolio_result FOR
        SELECT p.portfolio_id, p.title, p.cover_image_url 
        FROM portfolios p 
        JOIN portfolio_service ps ON p.portfolio_id = ps.portfolio_id
        WHERE ps.service_id = get_service_details.service_id;
    ELSE
        OPEN portfolio_result FOR
        SELECT NULL::UUID AS portfolio_id, NULL::TEXT AS title, NULL::TEXT AS cover_image_url;
    END IF;
END;
$$ 
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_freelancer_services (freelancer_id uuid)
RETURNS TABLE (service_id uuid, service_title varchar(255), service_description varchar(5000), service_skills text[], service_rate decimal, minimum_budget decimal, service_thumbnail varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM services WHERE freelancer_id = freelancer_id AND is_draft = false;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_freelancer_dedicated_resources (freelancer_id uuid)
RETURNS TABLE (resource_id uuid, resource_name varchar(255), resource_title varchar(255), resource_summary varchar(3000), resource_skills text[], resource_rate decimal, minimum_duration resource_duration_enum, resource_image varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM dedicated_resource WHERE freelancer_id = freelancer_id AND is_draft = false;
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

CREATE OR REPLACE FUNCTION get_resource_details(
    IN resource_id UUID,
    OUT resource_details_result REFCURSOR,
    OUT portfolio_result REFCURSOR
)
RETURNS RECORD AS $$
BEGIN
    OPEN resource_details_result FOR
    SELECT * FROM dedicated_resource WHERE resource_id = get_resource_details.resource_id;

    IF EXISTS (SELECT * FROM portfolio_resource WHERE resource_id = get_resource_details.resource_id) THEN
        OPEN portfolio_result FOR
        SELECT p.portfolio_id, p.title, p.cover_image_url 
        FROM portfolios p 
        JOIN portfolio_resource pr ON p.portfolio_id = pr.portfolio_id
        WHERE pr.resource_id = get_resource_details.resource_id;
    ELSE
        OPEN portfolio_result FOR
        SELECT NULL::UUID AS portfolio_id, NULL::TEXT AS title, NULL::TEXT AS cover_image_url;
    END IF;
END;
$$ 
LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION get_freelancer_quotes (freelancer_id uuid, in_quote_status quote_status_enum)
RETURNS TABLE (quote_id uuid, job_id uuid, proposal varchar(3000), quote_status quote_status_enum, bids_used decimal, bid_date timestamp)
AS $$
BEGIN
    IF quote_status IS NULL THEN
        RETURN QUERY
        SELECT * FROM quotes WHERE freelancer_id = freelancer_id;
    ELSE
        RETURN QUERY
        SELECT * FROM quotes WHERE freelancer_id = freelancer_id AND quote_status = in_quote_status;
    END IF;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_freelancer_quote_details (in_quote_id uuid)
RETURNS TABLE (quote_id uuid, freelancer_id uuid, job_id uuid, proposal varchar(3000), quote_status quote_status_enum, bids_used decimal, bid_date timestamp)
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM quotes WHERE quote_id = in_quote_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_bids_usage_history (freelancer_id uuid)
RETURNS TABLE (bids_used decimal, bid_date timestamp)
AS $$
BEGIN
    RETURN QUERY
    SELECT bids_used, bid_date
    FROM quotes
    WHERE freelancer_id = freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION bids_usage_summary (freelancer_id uuid)
RETURNS TABLE (all_time_bids_used decimal, last_month_bids_used decimal, last_year_bids_used decimal)
AS $$
BEGIN
    RETURN QUERY
    SELECT 
        (SELECT SUM(bids_used) FROM quotes WHERE freelancer_id = freelancer_id) AS all_time_bids_used,
        (SELECT SUM(bids_used) FROM quotes WHERE freelancer_id = freelancer_id AND bid_date > CURRENT_DATE - INTERVAL '1 month') AS last_month_bids_used,
        (SELECT SUM(bids_used) FROM quotes WHERE freelancer_id = freelancer_id AND bid_date > CURRENT_DATE - INTERVAL '1 year') AS last_year_bids_used;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_freelancer_quote_templates (freelancer_id uuid)
RETURNS TABLE (quote_template_id uuid, template_name varchar(255), template_description varchar(10000), attachments text[])
AS $$
BEGIN
    RETURN QUERY
    SELECT quote_template_id, template_name, template_description, attachments
    FROM quote_templates
    WHERE freelancer_id = freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_freelancer_job_watchlist (freelancer_id uuid)
RETURNS TABLE (watchlist_id uuid, job_id uuid)
AS $$
BEGIN
    RETURN QUERY
    SELECT watchlist_id, job_id
    FROM job_watchlist
    WHERE freelancer_id = freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_freelancer_job_invitations (freelancer_id uuid)
RETURNS TABLE (invitation_id uuid, client_id uuid, job_id uuid, invitation_date timestamp)
AS $$
BEGIN
    RETURN QUERY
    SELECT invitation_id, client_id, job_id, invitation_date
    FROM job_invitations
    WHERE freelancer_id = freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_freelancer_team_members (freelancer_id uuid)
RETURNS TABLE (team_member_id uuid, member_name varchar(255), title team_member_role, member_type team_member_type, member_email varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT team_member_id, member_name, title, member_type, member_email
    FROM featured_team_member
    WHERE freelancer_id = freelancer_id;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION view_last_bids_until_threshold (p_freelancer_id uuid) 
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
    WHERE freelancer_id = p_freelancer_id
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
