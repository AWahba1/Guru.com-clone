CREATE KEYSPACE freelancer
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

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

CREATE TABLE freelancerReports(
    freelancer_id UUID,
    reported_freelancer_id UUID,
    reported_freelancer_name TEXT,
    report TEXT,
    created_at TIMESTAMP,
    PRIMARY KEY (reported_freelancer_id)
);

SELECT * FROM Feedback_By_Freelancer WHERE freelancer_id = <freelancer_id> ORDER BY created_at DESC;
SELECT * FROM Feedback_By_Project WHERE job_id = <job_id> ORDER BY created_at DESC;
SELECT * FROM Feedback_By_Client WHERE client_id = <client_id> ORDER BY created_at DESC;
SELECT * FROM Testomonial_By_Freelancer WHERE freelancer_id = <freelancer_id>;
SELECT * FROM jobReports WHERE job_id = <job_id>;

INSERT INTO Feedback_By_Freelancer (feedback_id, freelancer_id, client_id, job_id, client_name, job_title, satisfied, created_at) VALUES (UUID(), <freelancer_id>, <client_id>, <job_id>, <client_name>, <job_title>, <satisfied>, <created_at>);
INSERT INTO Feedback_By_Project (feedback_id, freelancer_id, client_id, job_id, client_name, job_title, satisfied, created_at) VALUES (UUID(), <freelancer_id>, <client_id>, <job_id>, <client_name>, <job_title>, <satisfied>, <created_at>);
INSERT INTO Feedback_By_Client (feedback_id, freelancer_id, client_id, job_id, client_name, job_title, satisfied, created_at) VALUES (UUID(), <freelancer_id>, <client_id>, <job_id>, <client_name>, <job_title>, <satisfied>, <created_at>);
