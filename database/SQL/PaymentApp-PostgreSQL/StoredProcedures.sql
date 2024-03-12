\c paymentapp 

CREATE OR REPLACE FUNCTION create_transaction(
    p_amount DECIMAL,
    p_status ENUM,
    p_status_description TEXT,
    p_payment_token VARCHAR,
    p_date_time TIMESTAMP,
    p_freelancer_id INT,
    p_employer_id INT,
    p_invoice_id INT,
    p_gateway_id INT,
    p_payment_method_id INT
)
RETURNS INT AS $$
DECLARE
    new_transaction_id INT;
BEGIN
    INSERT INTO transactions(amount, status, status_description, payment_token, date_time, freelancer_id, employer_id, invoice_id, gateway_id, payment_method_id)
    VALUES (p_amount, p_status, p_status_description, p_payment_token, p_date_time, p_freelancer_id, p_employer_id, p_invoice_id, p_gateway_id, p_payment_method_id)
    RETURNING transaction_id INTO new_transaction_id;
    
    RETURN new_transaction_id;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION create_invoice(
    p_job_id INT,
    p_payment_terms TEXT,
    p_due_date DATE,
    p_status ENUM
)
RETURNS INT AS $$
DECLARE
    new_invoice_id INT;
BEGIN
    INSERT INTO invoices(job_id, payment_terms, due_date, status)
    VALUES (p_job_id, p_payment_terms, p_due_date, p_status)
    RETURNING invoice_id INTO new_invoice_id;
    
    RETURN new_invoice_id;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION create_payment_method(
    p_user_id INT,
    p_payment_type ENUM,
    p_gateway_reference VARCHAR,
    p_last_four_digits VARCHAR,
    p_expiry_date DATE
)
RETURNS INT AS $$
DECLARE
    new_payment_method_id INT;
BEGIN
    INSERT INTO payment_methods(user_id, payment_type, gateway_reference, last_four_digits, expiry_date)
    VALUES (p_user_id, p_payment_type, p_gateway_reference, p_last_four_digits, p_expiry_date)
    RETURNING payment_method_id INTO new_payment_method_id;
    
    RETURN new_payment_method_id;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION resolve_dispute(
    p_dispute_id INT,
    p_resolution_notes TEXT
)
RETURNS VOID AS $$
BEGIN
    UPDATE disputes
    SET status = 'resolved', resolution_notes = p_resolution_notes
    WHERE dispute_id = p_dispute_id;
END;
$$ LANGUAGE plpgsql;