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

        INSERT INTO quotes (id, freelancer_id, job_id, proposal, bids_used, bid_date)
        VALUES (gen_random_uuid(), _freelancer_id, job_id, proposal, bids_used, bid_date);

        UPDATE freelancers F SET F.available_bids = available_bids - bids_used WHERE F.freelancer_id = _freelancer_id;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CALL add_quote('66666666-6666-6666-6666-666666666666'::uuid, '11111111-1111-1111-1111-111111111111'::uuid, 'werak fera5'::varchar, 47, '2024-04-30T00:20:58.390+00:00'::timestamp);

DROP PROCEDURE IF EXISTS update_quote;
CREATE OR REPLACE PROCEDURE update_quote (
    IN _quote_id uuid,
    IN new_proposal varchar(3000),
    IN new_bids_used int,
    IN new_quote_status varchar(255)
)
LANGUAGE plpgsql
AS $$
BEGIN
    BEGIN

        IF new_proposal IS NOT NULL THEN
            UPDATE quotes SET proposal = new_proposal WHERE id = _quote_id;
        END IF;

        IF new_bids_used IS NOT NULL THEN
            UPDATE quotes SET bids_used = new_bids_used WHERE id = _quote_id;
        END IF;

        IF new_quote_status IS NOT NULL THEN
            UPDATE quotes SET quote_status = new_quote_status WHERE id = _quote_id;
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
    IN template_name VARCHAR(255),
    IN template_description VARCHAR(10000),
    IN attachments TEXT[]
)
LANGUAGE plpgsql
AS $$
DECLARE
	new_quote_template_id UUID;
    attachment_json text;
BEGIN
    BEGIN

        INSERT INTO quote_templates (id, freelancer_id, template_name, template_description)
        VALUES (gen_random_uuid(), freelancer_id, template_name, template_description)
		RETURNING id INTO new_quote_template_id;
		
		-- Insert attachments related to quote template
		FOREACH attachment_json IN ARRAY attachments
		LOOP
			INSERT INTO quote_templates_attachments (id, quote_template_id, url, filename, created_at) 
			VALUES (gen_random_uuid(), new_quote_template_id, REPLACE((attachment_json::json->'url')::text, '"', ''), REPLACE((attachment_json::json->'filename')::text, '"', '') , CURRENT_TIMESTAMP);
		END LOOP;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CALL add_quote_template(
    '44444444-4444-4444-4444-444444444444', -- Freelancer ID
    'Sample Template',                    -- Template Name
    'Description for Sample Template',    -- Template Description
    ARRAY[
        '{"url": "https://example.com/attachment1.pdf", "filename": "attachment1.pdf"}',
        '{"url": "https://example.com/attachment2.docx", "filename": "attachment2.docx"}'
    ]                                     -- Attachments
);

DROP PROCEDURE IF EXISTS update_quote_template;
CREATE OR REPLACE PROCEDURE update_quote_template (
    IN _quote_template_id uuid,
    IN new_template_name VARCHAR(255),
    IN new_template_description VARCHAR(10000),
    IN new_attachments text[]
)
LANGUAGE plpgsql
AS $$
DECLARE
	attachment_json text;
BEGIN
    BEGIN

        IF new_template_name IS NOT NULL THEN
            UPDATE quote_templates qt SET template_name = new_template_name WHERE qt.id = _quote_template_id;
        END IF;

        IF new_template_description IS NOT NULL THEN
            UPDATE quote_templates qt SET template_description = new_template_description WHERE qt.id = _quote_template_id;
        END IF;

        IF new_attachments IS NOT NULL THEN
			 -- Delete old quote template attachments
			 DELETE FROM quote_templates_attachments qta WHERE qta.quote_template_id = _quote_template_id;

			 -- Insert attachments related to quote template
			FOREACH attachment_json IN ARRAY new_attachments
			LOOP
				INSERT INTO quote_templates_attachments (id, quote_template_id, url, filename, created_at) 
				VALUES (gen_random_uuid(), _quote_template_id, REPLACE((attachment_json::json->'url')::text, '"', ''), REPLACE((attachment_json::json->'filename')::text, '"', '') , CURRENT_TIMESTAMP);
			END LOOP;
        END IF;

    EXCEPTION
        WHEN others THEN
            ROLLBACK;
            RAISE EXCEPTION 'Error occurred: % - %', SQLSTATE, SQLERRM;
    END;
END;
$$;

CALL update_quote_template(
    '22222222-2222-2222-2222-222222222222', -- Freelancer ID
    'New Template',                    -- Template Name
    'New Description',    -- Template Description
    ARRAY[
        '{"url": "https://example.com/attachment3.pdf", "filename": "attachment3.pdf"}',
        '{"url": "https://example.com/attachment4.docx", "filename": "attachment4.docx"}'
    ]                                     -- Attachments
);



DROP PROCEDURE IF EXISTS add_job_watchlist;
CREATE OR REPLACE PROCEDURE add_job_watchlist (
    IN freelancer_id uuid,
    IN job_id uuid
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO job_watchlist (watchlist_id, freelancer_id, job_id) VALUES (gen_random_uuid(), freelancer_id, job_id);
END;
$$;

DROP PROCEDURE IF EXISTS remove_job_watchlist;
CREATE OR REPLACE PROCEDURE remove_job_watchlist (
    IN _watchlist_id uuid
)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM job_watchlist jw WHERE jw.watchlist_id = _watchlist_id;
END;
$$;

DROP PROCEDURE IF EXISTS invite_to_job;
CREATE OR REPLACE PROCEDURE invite_to_job (
    IN freelancer_id uuid,
    IN client_id uuid,
    IN job_id uuid
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO job_invitations (invitation_id, freelancer_id, client_id, job_id, invitation_date)
    VALUES (gen_random_uuid(), freelancer_id, client_id, job_id, NOW());
END;
$$;

CALL invite_to_job('d433a93f-9a0f-4a55-9a72-65e19c2da25a'::uuid, '11111111-1111-1111-1111-111111111111'::uuid,'11111111-1111-1111-1111-111111111111'::uuid, '2024-05-07 18:07:53.868213'::timestamp);

DROP FUNCTION IF EXISTS get_freelancer_quotes;
CREATE OR REPLACE FUNCTION get_freelancer_quotes (_freelancer_id uuid, _quote_status varchar(255))
RETURNS TABLE (
id uuid,
freelancer_id uuid,
job_id uuid,
proposal varchar(3000),
quote_status varchar(255),
bids_used integer, bid_date timestamp
)
AS $$
BEGIN
    IF quote_status IS NULL THEN
        RETURN QUERY
        SELECT * FROM quotes q WHERE q.freelancer_id = _freelancer_id;
    ELSE
        RETURN QUERY
        SELECT * FROM quotes q WHERE q.freelancer_id = _freelancer_id AND q.quote_status = _quote_status;
    END IF;
END;
$$
LANGUAGE plpgsql;


DROP FUNCTION IF EXISTS get_freelancer_quote_details;
CREATE OR REPLACE FUNCTION get_freelancer_quote_details (_quote_id uuid)
RETURNS TABLE (
id uuid, freelancer_id uuid, job_id uuid, proposal varchar(3000), quote_status varchar(255), bids_used integer, bid_date timestamp)
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM quotes q WHERE q.id = _quote_id;
END;
$$
LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS get_freelancer_quote_templates;
CREATE OR REPLACE FUNCTION get_freelancer_quote_templates(_freelancer_id uuid)
RETURNS TABLE (
    id uuid, 
    freelancer_id uuid, 
    template_name VARCHAR(255), 
    template_description VARCHAR(10000), 
    attachments JSON
)
AS $$
BEGIN
    RETURN QUERY
    SELECT 
        qt.id,
        qt.freelancer_id,
        qt.template_name,
        qt.template_description,
        (
            SELECT JSON_AGG(json_build_object('url', qta.url, 'filename', qta.filename)) 
            FROM quote_templates_attachments qta 
            WHERE qta.quote_template_id = qt.id
        ) AS attachments
    FROM quote_templates qt 
    WHERE qt.freelancer_id = _freelancer_id;
END;
$$
LANGUAGE plpgsql;

SELECT * FROM get_freelancer_quote_templates('44444444-4444-4444-4444-444444444444');


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
RETURNS TABLE (invitation_id uuid, freelancer_id uuid, client_id uuid, job_id uuid, invitation_date timestamp)
AS $$
BEGIN
    RETURN QUERY
    SELECT ji.invitation_id, ji.freelancer_id, ji.client_id, ji.job_id, ji.invitation_date
    FROM job_invitations ji
    WHERE ji.freelancer_id = _freelancer_id;
END;
$$
LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS bids_usage_summary;
CREATE OR REPLACE FUNCTION bids_usage_summary (_freelancer_id uuid)
RETURNS TABLE (all_time_bids_used decimal, last_month_bids_used decimal, last_year_bids_used decimal)
AS $$
BEGIN
    RETURN QUERY
    SELECT
        COALESCE((SELECT SUM(bids_used)::decimal FROM quotes WHERE freelancer_id = _freelancer_id), 0) AS all_time_bids_used,
        COALESCE((SELECT SUM(bids_used)::decimal FROM quotes WHERE freelancer_id = _freelancer_id AND bid_date > CURRENT_DATE - INTERVAL '1 month'), 0) AS last_month_bids_used,
        COALESCE((SELECT SUM(bids_used)::decimal FROM quotes WHERE freelancer_id = _freelancer_id AND bid_date > CURRENT_DATE - INTERVAL '1 year'), 0) AS last_year_bids_used;
END;
$$
LANGUAGE plpgsql;

SELECT * FROM bids_usage_summary('44444444-4444-4444-4444-444444444444');

DROP FUNCTION IF EXISTS get_bids_usage_history;
CREATE OR REPLACE FUNCTION get_bids_usage_history (_freelancer_id uuid)
RETURNS TABLE (bids_used int, bid_date timestamp)
AS $$
BEGIN
    RETURN QUERY
    SELECT q.bids_used,q.bid_date
    FROM quotes q
    WHERE q.freelancer_id = _freelancer_id;
END;
$$
LANGUAGE plpgsql;

SELECT * FROM get_bids_usage_history('44444444-4444-4444-4444-444444444444');

DROP FUNCTION IF EXISTS view_last_bids_until_threshold;
CREATE OR REPLACE FUNCTION view_last_bids_until_threshold (_freelancer_id uuid, threshold int)
RETURNS TABLE (
    id uuid,
    freelancer_id uuid,
    job_id uuid,
    proposal varchar(3000),
    quote_status varchar(255),
    bids_used integer,
    bid_date timestamp,
    cumulative_sum int
)
AS $$
DECLARE
    total_bids int := 0;
    bid_record RECORD;
BEGIN
    CREATE TEMP TABLE temp_quotes AS
    SELECT *, 0 AS cumulative_sum
    FROM quotes q
    WHERE q.freelancer_id = _freelancer_id
    ORDER BY bid_date DESC;

    -- Iterate over the result set and update cumulative_sum
    FOR bid_record IN SELECT * FROM temp_quotes LOOP
        total_bids := total_bids + bid_record.bids_used;
        UPDATE temp_quotes tq SET cumulative_sum = total_bids WHERE tq.id = bid_record.id;
    END LOOP;

    -- Select from temp_quotes and return the result
    RETURN QUERY SELECT * FROM temp_quotes tq WHERE tq.cumulative_sum <= threshold ORDER BY bid_date DESC;

    -- Optional: Drop the temporary table
    DROP TABLE IF EXISTS temp_quotes;
END;
$$
LANGUAGE plpgsql;

SELECT * FROM view_last_bids_until_threshold('44444444-4444-4444-4444-444444444444', 5000);