CREATE OR REPLACE PROCEDURE toggle_profile_visibility (IN freelancer_id uuid)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN
        UPDATE freelancers SET visibility = NOT visibility WHERE freelancer_id = freelancer_id;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE update_freelancer_profile_about_section (
    IN _freelancer_id uuid,
    IN new_freelancer_name varchar(50),
    IN new_image_url varchar(255),
    IN new_tagline varchar(190),
    IN new_bio varchar(3000),
    IN new_work_terms varchar(2000),
    IN new_attachments varchar(255) ARRAY,
    IN new_user_type varchar(255),
    IN new_website_link varchar(255),
    IN new_facebook_link varchar(255),
    IN new_linkedin_link varchar(255),
    IN new_professional_video_link varchar(255),
    IN new_company_history varchar(3000),
    IN new_operating_since TIMESTAMP
)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN
        IF new_freelancer_name IS NOT NULL THEN
            UPDATE freelancers SET freelancer_name = new_freelancer_name WHERE freelancer_id = _freelancer_id;
        END IF;

        IF new_image_url IS NOT NULL THEN
            UPDATE freelancers SET image_url = new_image_url WHERE freelancer_id = _freelancer_id;
        END IF;

        IF new_tagline IS NOT NULL THEN
            UPDATE freelancers SET tagline = new_tagline WHERE freelancer_id = _freelancer_id;
        END IF;

        IF new_bio IS NOT NULL THEN
            UPDATE freelancers SET bio = new_bio WHERE freelancer_id = _freelancer_id;
        END IF;

        IF new_work_terms IS NOT NULL THEN
            UPDATE freelancers SET work_terms = new_work_terms WHERE freelancer_id = _freelancer_id;
        END IF;

        IF new_attachments IS NOT NULL THEN
            UPDATE freelancers SET attachments = new_attachments WHERE freelancer_id = _freelancer_id;
        END IF;

        IF new_user_type IS NOT NULL THEN
            UPDATE freelancers SET user_type = new_user_type WHERE freelancer_id = _freelancer_id;
        END IF;

        IF new_website_link IS NOT NULL THEN
            UPDATE freelancers SET website_link = new_website_link WHERE freelancer_id = _freelancer_id;
        END IF;

        IF new_facebook_link IS NOT NULL THEN
            UPDATE freelancers SET facebook_link = new_facebook_link WHERE freelancer_id = _freelancer_id;
        END IF;

        IF new_linkedin_link IS NOT NULL THEN
            UPDATE freelancers SET linkedin_link = new_linkedin_link WHERE freelancer_id = _freelancer_id;
        END IF;

        IF new_professional_video_link IS NOT NULL THEN
            UPDATE freelancers SET professional_video_link = new_professional_video_link WHERE freelancer_id = _freelancer_id;
        END IF;

        IF new_company_history IS NOT NULL THEN
            UPDATE freelancers SET company_history = new_company_history WHERE freelancer_id = _freelancer_id;
        END IF;

        IF new_operating_since IS NOT NULL THEN
            UPDATE freelancers SET operating_since = new_operating_since WHERE freelancer_id = _freelancer_id;
        END IF;
    EXCEPTION
        WHEN others THEN
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;


CREATE OR REPLACE PROCEDURE add_portfolio (
    IN _freelancer_id uuid,
    IN title varchar(255), 
    IN cover_image_url varchar(255), 
    IN attachments varchar(255) ARRAY
)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        IF cover_image_url IS NULL THEN
            INSERT INTO portfolios (portfolio_id, freelancer_id, title, cover_image_url, attachments, is_draft) 
            VALUES (gen_random_uuid(), _freelancer_id, title, cover_image_url, attachments, true);
        ELSE
            INSERT INTO portfolios (portfolio_id, freelancer_id, title, cover_image_url, attachments, is_draft) 
            VALUES (gen_random_uuid(), _freelancer_id, title, cover_image_url, attachments, false);
        END IF;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE unpublish_portfolio (_portfolio_id uuid)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN
        
        UPDATE portfolios SET is_draft = true WHERE portfolio_id = _portfolio_id;
        DELETE FROM portfolio_service WHERE portfolio_id = _portfolio_id;
        DELETE FROM portfolio_resource WHERE portfolio_id = _portfolio_id;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_portfolio (_portfolio_id uuid)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        DELETE FROM portfolios WHERE portfolio_id = _portfolio_id;
        DELETE FROM portfolio_service WHERE portfolio_id = _portfolio_id;
        DELETE FROM portfolio_resource WHERE portfolio_id = _portfolio_id;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE update_portfolio (
    IN _portfolio_id uuid,
    IN new_title varchar(255),
    IN new_cover_image_url varchar(255),
    IN new_attachments TEXT[]
)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        IF new_title IS NOT NULL THEN
            UPDATE portfolios SET title = new_title WHERE portfolio_id = _portfolio_id;
        END IF;

        IF new_cover_image_url IS NOT NULL THEN
            UPDATE portfolios SET cover_image_url = new_cover_image_url WHERE portfolio_id = _portfolio_id;
        END IF;

        IF new_attachments IS NOT NULL THEN
            UPDATE portfolios SET attachments = new_attachments WHERE portfolio_id = _portfolio_id;
        END IF;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE add_service (
    IN _freelancer_id uuid, 
    IN service_title varchar(255), 
    IN service_description varchar(5000), 
    IN service_skills varchar(255) ARRAY, 
    IN service_rate decimal, 
    IN minimum_budget decimal, 
    IN service_thumbnail varchar(255),
    IN portfolio_ids uuid ARRAY
)
LANGUAGE plpgsql
AS $$
DECLARE
    gen_service_id uuid;
BEGIN
    BEGIN
        gen_service_id = gen_random_uuid();
        INSERT INTO services (service_id, freelancer_id, service_title, service_description, service_skills, service_rate, minimum_budget, service_thumbnail, is_draft) 
        VALUES (gen_service_id, _freelancer_id, service_title, service_description, service_skills, service_rate, minimum_budget, service_thumbnail, false);

        IF portfolio_ids IS NOT NULL THEN
            FOR i IN 1..array_length(portfolio_ids, 1) LOOP
                INSERT INTO portfolio_service (service_id, portfolio_id) 
                VALUES (gen_service_id, portfolio_ids[i]);
            END LOOP;
        END IF;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE unpublish_service (IN _service_id uuid)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE services s SET is_draft = true WHERE s.service_id = _service_id;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_service (IN _service_id uuid)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        DELETE FROM services WHERE service_id = _service_id;
        DELETE FROM portfolio_service WHERE service_id = _service_id;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE update_service (
    IN _service_id uuid,
    IN new_service_title varchar(255),
    IN new_service_description varchar(5000),
    IN new_service_skills varchar(255) ARRAY,
    IN new_service_rate decimal,
    IN new_minimum_budget decimal,
    IN new_service_thumbnail varchar(255),
    IN new_portfolio_id uuid ARRAY
)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        IF new_service_title IS NOT NULL THEN
            UPDATE services SET service_title = new_service_title WHERE service_id = _service_id;
        END IF;

        IF new_service_description IS NOT NULL THEN
            UPDATE services SET service_description = new_service_description WHERE service_id = _service_id;
        END IF;

        IF new_service_skills IS NOT NULL THEN
            UPDATE services SET service_skills = new_service_skills WHERE service_id = _service_id;
        END IF;

        IF new_service_rate IS NOT NULL THEN
            UPDATE services SET service_rate = new_service_rate WHERE service_id = _service_id;
        END IF;

        IF new_minimum_budget IS NOT NULL THEN
            UPDATE services SET minimum_budget = new_minimum_budget WHERE service_id = _service_id;
        END IF;

        IF new_service_thumbnail IS NOT NULL THEN
            UPDATE services SET service_thumbnail = new_service_thumbnail WHERE service_id = _service_id;
        END IF;

        IF new_portfolio_id IS NOT NULL THEN
            DELETE FROM portfolio_service WHERE service_id = _service_id;
            FOR i IN 1..array_length(new_portfolio_id, 1) LOOP
                INSERT INTO portfolio_service (service_id, portfolio_id) 
                VALUES (_service_id, new_portfolio_id[i]);
            END LOOP;
        END IF;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE add_dedicated_resource (
    IN _freelancer_id uuid, 
    IN resource_name varchar(255), 
    IN resource_title varchar(255), 
    IN resource_summary varchar(3000), 
    IN resource_skills varchar(255) ARRAY, 
    IN resource_rate decimal, 
    IN minimum_duration varchar(255), 
    IN resource_image varchar(255),
    IN portfolio_ids uuid ARRAY
)
LANGUAGE plpgsql
AS $$
DECLARE
    gen_resource_id uuid;
BEGIN
    BEGIN
        gen_resource_id = gen_random_uuid();
        INSERT INTO dedicated_resource (resource_id, freelancer_id, resource_name, resource_title, resource_summary, resource_skills, resource_rate, minimum_duration, resource_image, is_draft) 
        VALUES (gen_resource_id, _freelancer_id, resource_name, resource_title, resource_summary, resource_skills, resource_rate, minimum_duration, resource_image, false);

        IF portfolio_ids IS NOT NULL THEN
            FOR i IN 1.. array_length(portfolio_ids,1) LOOP
                INSERT INTO portfolio_resource (resource_id, portfolio_id) 
                VALUES (gen_resource_id, portfolio_ids[i]);
            END LOOP;
        END IF;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE unpublish_dedicated_resource (IN resource_id uuid)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE dedicated_resource SET is_draft = true WHERE resource_id = resource_id;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_dedicated_resource (IN resource_id uuid)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        DELETE FROM dedicated_resource WHERE resource_id = resource_id;
        DELETE FROM portfolio_resource WHERE resource_id = resource_id;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE update_dedicated_resource (
    IN resource_id uuid,
    IN new_resource_name varchar(255),
    IN new_resource_title varchar(255),
    IN new_resource_summary varchar(3000),
    IN new_resource_skills text[],
    IN new_resource_rate decimal,
    IN new_minimum_duration resource_duration_enum,
    IN new_resource_image varchar(255),
    IN new_portfolio_id uuid[]
)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        IF new_resource_name IS NOT NULL THEN
            UPDATE dedicated_resource SET resource_name = new_resource_name WHERE resource_id = resource_id;
        END IF;

        IF new_resource_title IS NOT NULL THEN
            UPDATE dedicated_resource SET resource_title = new_resource_title WHERE resource_id = resource_id;
        END IF;

        IF new_resource_summary IS NOT NULL THEN
            UPDATE dedicated_resource SET resource_summary = new_resource_summary WHERE resource_id = resource_id;
        END IF;

        IF new_resource_skills IS NOT NULL THEN
            UPDATE dedicated_resource SET resource_skills = new_resource_skills WHERE resource_id = resource_id;
        END IF;

        IF new_resource_rate IS NOT NULL THEN
            UPDATE dedicated_resource SET resource_rate = new_resource_rate WHERE resource_id = resource_id;
        END IF;

        IF new_minimum_duration IS NOT NULL THEN
            UPDATE dedicated_resource SET minimum_duration = new_minimum_duration WHERE resource_id = resource_id;
        END IF;

        IF new_resource_image IS NOT NULL THEN
            UPDATE dedicated_resource SET resource_image = new_resource_image WHERE resource_id = resource_id;
        END IF;

        IF new_portfolio_id IS NOT NULL THEN
            DELETE FROM portfolio_resource WHERE resource_id = resource_id;
            FOR i IN 1..LENGTH(new_portfolio_id) LOOP
                INSERT INTO portfolio_resource (resource_id, portfolio_id) 
                VALUES (UUID(), resource_id, new_portfolio_id[i]);
            END LOOP;
        END IF;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE add_quote (
    IN freelancer_id uuid, 
    IN job_id uuid, 
    IN proposal varchar(3000), 
    IN bids_used decimal, 
    IN bid_date timestamp
)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        INSERT INTO quotes (quote_id, freelancer_id, job_id, proposal, quote_status, bids_used, bid_date) 
        VALUES (UUID(), freelancer_id, job_id, proposal, 'AWAITING_ACCEPTANCE', bids_used, bid_date);

        UPDATE freelancers SET available_bids = available_bids - bids_used WHERE freelancer_id = add_quote.freelancer_id;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE update_quote (
    IN quote_id uuid,
    IN new_proposal varchar(3000),
    IN new_bids_used decimal,
    IN new_quote_status quote_status_enum
)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        IF new_proposal IS NOT NULL THEN
            UPDATE quotes SET proposal = new_proposal WHERE quote_id = quote_id;
        END IF;

        IF new_bids_used IS NOT NULL THEN
            UPDATE quotes SET bids_used = new_bids_used WHERE quote_id = quote_id;
        END IF;

        IF new_quote_status IS NOT NULL THEN
            UPDATE quotes SET quote_status = new_quote_status WHERE quote_id = quote_id;
        END IF;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE add_quote_template (
    IN freelancer_id uuid, 
    IN template_name varchar(255), 
    IN template_description varchar(10000), 
    IN attachments text[]
)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        INSERT INTO quote_templates (quote_template_id, freelancer_id, template_name, template_description, attachments) 
        VALUES (UUID(), freelancer_id, template_name, template_description, attachments);

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE update_quote_template (
    IN quote_template_id uuid,
    IN new_template_name varchar(255),
    IN new_template_description varchar(10000),
    IN new_attachments text[]
)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        IF new_template_name IS NOT NULL THEN
            UPDATE quote_templates SET template_name = new_template_name WHERE quote_template_id = quote_template_id;
        END IF;

        IF new_template_description IS NOT NULL THEN
            UPDATE quote_templates SET template_description = new_template_description WHERE quote_template_id = quote_template_id;
        END IF;

        IF new_attachments IS NOT NULL THEN
            UPDATE quote_templates SET attachments = new_attachments WHERE quote_template_id = quote_template_id;
        END IF;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE add_job_watchlist (
    IN freelancer_id uuid, 
    IN job_id uuid
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO job_watchlist (watchlist_id, freelancer_id, job_id) VALUES (UUID(), freelancer_id, job_id);
END;
$$;

CREATE OR REPLACE PROCEDURE remove_job_watchlist (
    IN watchlist_id uuid
)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM job_watchlist WHERE watchlist_id = watchlist_id;
END;
$$;

CREATE OR REPLACE PROCEDURE invite_to_job (
    IN freelancer_id uuid, 
    IN client_id uuid, 
    IN job_id uuid, 
    IN invitation_date timestamp
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO job_invitations (invitation_id, freelancer_id, client_id, job_id, invitation_date) 
    VALUES (UUID(), freelancer_id, client_id, job_id, invitation_date);
END;
$$;

CREATE OR REPLACE PROCEDURE add_featured_team_members (
    IN freelancer_id uuid, 
    IN member_names varchar[], 
    IN titles varchar[], 
    IN member_types varchar[], 
    IN member_emails varchar[]
)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        IF array_length(member_names, 1) IS NOT NULL THEN
            FOR i IN 1..array_length(member_names, 1) LOOP
                INSERT INTO featured_team_member (team_member_id, freelancer_id, member_name, title, member_type, member_email) 
                VALUES (UUID(), freelancer_id, member_names[i], titles[i], member_types[i], member_emails[i]);
            END LOOP;
        END IF;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE add_no_access_members (
    IN freelancer_id uuid, 
    IN member_names varchar[]
)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        IF array_length(member_names, 1) IS NOT NULL THEN
            FOR i IN 1..array_length(member_names, 1) LOOP
                INSERT INTO featured_team_member (team_member_id, freelancer_id, member_name) 
                VALUES (UUID(), freelancer_id, member_names[i]);
            END LOOP;
        END IF;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_team_member (IN team_member_id uuid)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM featured_team_member WHERE team_member_id = team_member_id;
END;
$$;