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
    IN portfolio_skills UUID ARRAY,
    IN attachments varchar(255) ARRAY
)
LANGUAGE plpgsql
AS $$
DECLARE
    gen_portfolio_id uuid;
BEGIN
    BEGIN
        gen_portfolio_id = gen_random_uuid();
        IF cover_image_url IS NULL THEN
            INSERT INTO portfolios (portfolio_id, freelancer_id, title, cover_image_url, attachments, is_draft) 
            VALUES (gen_portfolio_id, _freelancer_id, title, cover_image_url, attachments, true);
        ELSE
            INSERT INTO portfolios (portfolio_id, freelancer_id, title, cover_image_url, attachments, is_draft) 
            VALUES (gen_portfolio_id, _freelancer_id, title, cover_image_url, attachments, false);
        END IF;

        IF portfolio_skills IS NOT NULL THEN
            IF array_length(portfolio_skills, 1) > 0 THEN
                FOR i IN 1..array_length(portfolio_skills, 1) LOOP
                    INSERT INTO portfolio_skills (portfolio_id, skill_id)
                    VALUES (gen_portfolio_id, portfolio_skills[i]);
                END LOOP;
            END IF;
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

CREATE OR REPLACE PROCEDURE publish_portfolio (_portfolio_id uuid)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        UPDATE portfolios SET is_draft = false WHERE portfolio_id = _portfolio_id;

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
    IN new_portfolio_skills UUID ARRAY,
    IN new_attachments varchar(255) ARRAY
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

        IF new_portfolio_skills IS NOT NULL THEN
            IF array_length(new_portfolio_skills, 1) > 0 THEN
                DELETE FROM portfolio_skills WHERE portfolio_id = _portfolio_id;
                FOR i IN 1..array_length(new_portfolio_skills, 1) LOOP
                    INSERT INTO portfolio_skills (portfolio_id, skill_id)
                    VALUES (_portfolio_id, new_portfolio_skills[i]);
                 END LOOP;
            END IF;
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
    IN _service_skills UUID ARRAY,
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
        INSERT INTO services (service_id, freelancer_id, service_title, service_description, service_rate, minimum_budget, service_thumbnail, is_draft)
        VALUES (gen_service_id, _freelancer_id, service_title, service_description, service_rate, minimum_budget, service_thumbnail, false);

        IF _service_skills IS NOT NULL THEN
            IF array_length(_service_skills, 1) > 0 THEN
                FOR i IN 1..array_length(_service_skills, 1) LOOP
                    INSERT INTO service_skills (service_id, skill_id)
                    VALUES (gen_service_id, _service_skills[i]);
                END LOOP;
            END IF;
        END IF;

        IF portfolio_ids IS NOT NULL THEN
            IF array_length(portfolio_ids, 1) > 0 THEN
                FOR i IN 1..array_length(portfolio_ids, 1) LOOP
                    INSERT INTO portfolio_service (service_id, portfolio_id)
                    VALUES (gen_service_id, portfolio_ids[i]);
                END LOOP;
            END IF;
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

CREATE OR REPLACE PROCEDURE publish_service (IN _service_id uuid)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE services s SET is_draft = false WHERE s.service_id = _service_id;
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
    IN new_service_skills UUID ARRAY,
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
            IF array_length(new_service_skills, 1) > 0 THEN
                DELETE FROM service_skills WHERE service_id = _service_id;
                FOR i IN 1..array_length(new_service_skills, 1) LOOP
                    INSERT INTO service_skills (service_id, skill_id)
                    VALUES (_service_id, new_service_skills[i]);
                END LOOP;
            END IF;
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
            IF array_length(new_portfolio_id, 1) > 0 THEN
                DELETE FROM portfolio_service WHERE service_id = _service_id;
                FOR i IN 1..array_length(new_portfolio_id, 1) LOOP
                    INSERT INTO portfolio_service (service_id, portfolio_id)
                    VALUES (_service_id, new_portfolio_id[i]);
                END LOOP;
            END IF;
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
    IN _resource_skills UUID ARRAY,
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
        INSERT INTO dedicated_resource (resource_id, freelancer_id, resource_name, resource_title, resource_summary, resource_rate, minimum_duration, resource_image, is_draft)
        VALUES (gen_resource_id, _freelancer_id, resource_name, resource_title, resource_summary, resource_rate, minimum_duration, resource_image, false);

        IF _resource_skills IS NOT NULL THEN
            IF array_length(_resource_skills, 1) > 0 THEN
                FOR i IN 1..array_length(_resource_skills, 1) LOOP
                    INSERT INTO resource_skills (resource_id, skill_id)
                    VALUES (gen_resource_id, _resource_skills[i]);
                END LOOP;
            END IF;
        END IF;

        IF portfolio_ids IS NOT NULL THEN
            IF array_length(portfolio_ids,1) > 0 THEN
                FOR i IN 1.. array_length(portfolio_ids,1) LOOP
                    INSERT INTO portfolio_resource (resource_id, portfolio_id) 
                    VALUES (gen_resource_id, portfolio_ids[i]);
                END LOOP;
            END IF;
        END IF;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE unpublish_dedicated_resource (IN _resource_id uuid)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE dedicated_resource SET is_draft = true WHERE resource_id = _resource_id;
END;
$$;

CREATE OR REPLACE PROCEDURE publish_dedicated_resource (IN _resource_id uuid)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE dedicated_resource SET is_draft = false WHERE resource_id = _resource_id;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_dedicated_resource (IN _resource_id uuid)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        DELETE FROM dedicated_resource WHERE resource_id = _resource_id;
        DELETE FROM portfolio_resource WHERE resource_id = _resource_id;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE update_dedicated_resource (
    IN _resource_id uuid,
    IN new_resource_name varchar(255),
    IN new_resource_title varchar(255),
    IN new_resource_summary varchar(3000),
    IN new_resource_skills UUID ARRAY,
    IN new_resource_rate decimal,
    IN new_minimum_duration varchar(255),
    IN new_resource_image varchar(255),
    IN new_portfolio_id uuid ARRAY
)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        IF new_resource_name IS NOT NULL THEN
            UPDATE dedicated_resource SET resource_name = new_resource_name WHERE resource_id = _resource_id;
        END IF;

        IF new_resource_title IS NOT NULL THEN
            UPDATE dedicated_resource SET resource_title = new_resource_title WHERE resource_id = _resource_id;
        END IF;

        IF new_resource_summary IS NOT NULL THEN
            UPDATE dedicated_resource SET resource_summary = new_resource_summary WHERE resource_id = _resource_id;
        END IF;

        IF new_resource_skills IS NOT NULL THEN
            IF array_length(new_resource_skills, 1) > 0 THEN
                DELETE FROM resource_skills WHERE resource_id = _resource_id;
                FOR i IN 1..array_length(new_resource_skills, 1) LOOP
                    INSERT INTO resource_skills (resource_id, skill_id)
                    VALUES (_resource_id, new_resource_skills[i]);
                END LOOP;
            END IF;
        END IF;

        IF new_resource_rate IS NOT NULL THEN
            UPDATE dedicated_resource SET resource_rate = new_resource_rate WHERE resource_id = _resource_id;
        END IF;

        IF new_minimum_duration IS NOT NULL THEN
            UPDATE dedicated_resource SET minimum_duration = new_minimum_duration WHERE resource_id = _resource_id;
        END IF;

        IF new_resource_image IS NOT NULL THEN
            UPDATE dedicated_resource SET resource_image = new_resource_image WHERE resource_id = _resource_id;
        END IF;

        IF new_portfolio_id IS NOT NULL THEN
            IF array_length(new_portfolio_id, 1) > 0 THEN
                DELETE FROM portfolio_resource WHERE resource_id = _resource_id;
                FOR i IN 1..array_length(new_portfolio_id, 1) LOOP
                    INSERT INTO portfolio_resource (resource_id, portfolio_id)
                    VALUES (_resource_id, new_portfolio_id[i]);
                END LOOP;
            END IF;
        END IF;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE add_featured_team_members (
    IN _freelancer_id uuid,
    IN _member_names varchar(255) ARRAY,
    IN _titles varchar(255) ARRAY,
    IN _member_types varchar(255) ARRAY,
    IN _member_emails varchar(255) ARRAY
)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        IF array_length(_member_names, 1) IS NOT NULL THEN
            FOR i IN 1..array_length(_member_names, 1) LOOP
                INSERT INTO featured_team_member (team_member_id, freelancer_id, member_name, title, member_type, member_email) 
                VALUES (gen_random_uuid(), _freelancer_id, _member_names[i], _titles[i], _member_types[i], _member_emails[i]);
            END LOOP;
        END IF;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_team_member (IN _team_member_id uuid)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM featured_team_member WHERE team_member_id = _team_member_id;
END;
$$;

CREATE OR REPLACE PROCEDURE increment_profile_views(IN _freelancer_id uuid, IN _viewer_id uuid)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO profile_views (freelancer_id, viewer_id) VALUES (_freelancer_id, _viewer_id);
    UPDATE freelancers SET profile_views = profile_views + 1 WHERE id = _freelancer_id;
    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;

END;
$$;

DROP PROCEDURE IF EXISTS add_to_favourites;

CREATE OR REPLACE PROCEDURE add_to_favourites(
    _client_id UUID,
    _freelancer_id UUID
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Insert the freelancer into the client's favorites if not already present
    IF NOT EXISTS (SELECT 1 FROM client_favourites WHERE client_id = _client_id AND freelancer_id = _freelancer_id) THEN
        INSERT INTO client_favourites (client_id, freelancer_id, added_at)
        VALUES (_client_id, _freelancer_id, CURRENT_TIMESTAMP);
    END IF;
END;
$$;

DROP PROCEDURE IF EXISTS remove_from_favourites;

CREATE OR REPLACE PROCEDURE remove_from_favourites(
    _client_id UUID,
    _freelancer_id UUID
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Delete the freelancer from the client's favorites if present
    IF EXISTS (SELECT 1 FROM client_favourites WHERE client_id = _client_id AND freelancer_id = _freelancer_id) THEN
        DELETE FROM client_favourites
        WHERE client_id = _client_id AND freelancer_id = _freelancer_id;
    END IF;
END;
$$;


-- CREATE OR REPLACE PROCEDURE increment_portfolio_views (IN _portfolio_id uuid, IN _viewer_id uuid)
-- LANGUAGE plpgsql
-- AS $$
-- BEGIN
--     INSERT INTO portfolio_views (portfolio_id, viewer_id) VALUES (_portfolio_id, _viewer_id);
--     UPDATE portfolios SET portfolio_views = portfolio_views + 1 WHERE portfolio_id = _portfolio_id;
--     EXCEPTION
--         WHEN others THEN
--             ROLLBACK;
--             RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;

-- END;
-- $$;

-- CREATE OR REPLACE PROCEDURE increment_service_views (IN _service_id uuid, IN _viewer_id uuid)
-- LANGUAGE plpgsql
-- AS $$
-- BEGIN
--     INSERT INTO service_views (service_id, viewer_id) VALUES (_service_id, _viewer_id);
--     UPDATE services SET service_views = service_views + 1 WHERE service_id = _service_id;
--     EXCEPTION
--         WHEN others THEN
--             ROLLBACK;
--             RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;

-- END;
-- $$;

-- CREATE OR REPLACE PROCEDURE increment_resource_views (IN _resource_id uuid, IN _viewer_id uuid)
-- LANGUAGE plpgsql
-- AS $$
-- BEGIN
--     INSERT INTO resource_views (resource_id, viewer_id) VALUES (_resource_id, _viewer_id);
--     UPDATE dedicated_resource SET resource_views = resource_views + 1 WHERE resource_id = _resource_id;
--     EXCEPTION
--         WHEN others THEN
--             ROLLBACK;
--             RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;

-- END;
-- $$;
