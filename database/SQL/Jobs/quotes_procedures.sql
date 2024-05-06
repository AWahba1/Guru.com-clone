DROP PROCEDURE IF EXISTS add_quote;
CREATE OR REPLACE PROCEDURE add_quote (
    IN _freelancer_id uuid,
    IN job_id uuid,
    IN proposal varchar(3000),
    IN bids_used int,
    IN bid_date timestamp
)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        INSERT INTO quotes (quote_id, freelancer_id, job_id, proposal, bids_used, bid_date)
        VALUES (gen_random_uuid(), _freelancer_id, job_id, proposal, bids_used, bid_date);

        UPDATE freelancers SET available_bids = available_bids - bids_used WHERE freelancer_id = _freelancer_id;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

DROP PROCEDURE IF EXISTS update_quote;
CREATE OR REPLACE PROCEDURE update_quote (
    IN quote_id uuid,
    IN new_proposal varchar(3000),
    IN new_bids_used int,
    IN new_quote_status varchar(255)
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

DROP PROCEDURE IF EXISTS add_quote_template;
CREATE OR REPLACE PROCEDURE add_quote_template (
    IN freelancer_id uuid,
    IN template_name varchar(255),
    IN template_description varchar(10000),
    IN attachments varchar(255) ARRAY
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

DROP PROCEDURE IF EXISTS update_quote_template;
CREATE OR REPLACE PROCEDURE update_quote_template (
    IN quote_template_id uuid,
    IN new_template_name varchar(255),
    IN new_template_description varchar(10000),
    IN new_attachments varchar(255) ARRAY
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

DROP PROCEDURE IF EXISTS add_job_watchlist;
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

DROP PROCEDURE IF EXISTS remove_job_watchlist;
CREATE OR REPLACE PROCEDURE remove_job_watchlist (
    IN watchlist_id uuid
)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM job_watchlist WHERE watchlist_id = watchlist_id;
END;
$$;

DROP PROCEDURE IF EXISTS invite_to_job;
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

DROP FUNCTION IF EXISTS get_freelancer_quotes;
CREATE OR REPLACE FUNCTION get_freelancer_quotes (_freelancer_id uuid, _quote_status quote_status_enum)
RETURNS TABLE (
quote_id uuid,
job_id uuid,
proposal varchar(3000),
quote_status quote_status_enum,
bids_used decimal, bid_date timestamp
)
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


DROP FUNCTION IF EXISTS get_freelancer_quote_details;
CREATE OR REPLACE FUNCTION get_freelancer_quote_details (_quote_id uuid)
RETURNS TABLE (
quote_id uuid, freelancer_id uuid, job_id uuid, proposal varchar(3000), quote_status quote_status_enum, bids_used decimal, bid_date timestamp)
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM quotes WHERE quote_id = _quote_id;
END;
$$
LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS get_freelancer_quote_templates;
CREATE OR REPLACE FUNCTION get_freelancer_quote_templates (_freelancer_id uuid)
RETURNS TABLE (quote_template_id uuid, template_name varchar(255), template_description varchar(10000), attachments varchar(255) ARRAY)
AS $$
BEGIN
    RETURN QUERY
    SELECT quote_template_id, template_name, template_description, attachments
    FROM quote_templates
    WHERE freelancer_id = _freelancer_id;
END;
$$
LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS get_freelancer_job_watchlist;
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

DROP FUNCTION IF EXISTS get_freelancer_job_invitations;
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