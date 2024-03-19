CREATE TABLE Feedback_By_Freelancer (
    feedback_id UUID,
    freelancer_id UUID,
    client_id UUID,
    job_id UUID,
    client_name TEXT,
    job_title TEXT,
    satisfied BOOLEAN,
    created_at TIMESTAMP,
    PRIMARY KEY (freelancer_id, created_at, feedback_id)
);
CREATE TABLE Feedback_By_Project (
    feedback_id UUID,
    freelancer_id UUID,
    client_id UUID,
    job_id UUID,
    client_name TEXT,
    job_title TEXT,
    satisfied BOOLEAN,
    created_at TIMESTAMP,
    PRIMARY KEY (job_id, created_at, feedback_id)
);
CREATE TABLE Feedback_By_Client (
    feedback_id UUID,
    freelancer_id UUID,
    client_id UUID,
    job_id UUID,
    client_name TEXT,
    job_title TEXT,
    satisfied BOOLEAN,
    created_at TIMESTAMP,
    PRIMARY KEY (client_id, created_at, feedback_id)
);

CREATE TABLE Testomonial_By_Freelancer (
    freelancer_id UUID,
    reviewer_name TEXT,
    review TEXT,
    PRIMARY KEY (freelancer_id)
);

CREATE TABLE jobReports (
    job_id UUID,
    freelancer_id UUID,
    client_id UUID,
    client_name TEXT,
    job_title TEXT,
    job_status TEXT,
    report TEXT,
    created_at TIMESTAMP,
    PRIMARY KEY (job_id)
);
