CREATE DATABASE paymentapp;

\c paymentapp 


CREATE TABLE users (
  user_id SERIAL PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL
);


CREATE TABLE jobs (
  job_id SERIAL PRIMARY KEY
);

CREATE TABLE transactions (
  transaction_id SERIAL PRIMARY KEY,
  amount DECIMAL(10,2) NOT NULL, 
  status ENUM('pending', 'approved', 'declined', 'refunded', 'chargeback') NOT NULL, 
  status_description TEXT,
  --payment_token VARCHAR(100),
  date_time TIMESTAMP NOT NULL,
  freelancer_id INT, 
  employer_id INT,
  invoice_id INT,
  gateway_id INT,
  payment_method_id INT,
  FOREIGN KEY (freelancer_id) REFERENCES users(user_id),
  FOREIGN KEY (employer_id) REFERENCES users(user_id),
  FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id),
  FOREIGN KEY (gateway_id) REFERENCES payment_gateways(gateway_id),
  FOREIGN KEY (payment_method_id) REFERENCES payment_methods(payment_method_id) 
);

CREATE TABLE invoices (
  invoice_id SERIAL PRIMARY KEY,
  job_id INT,
  payment_terms TEXT,
  due_date DATE,
  status ENUM('draft', 'sent', 'paid', 'overdue') NOT NULL,
  FOREIGN KEY (job_id) REFERENCES jobs(job_id) 
);

CREATE TABLE payment_methods (
  payment_method_id SERIAL PRIMARY KEY,
  user_id INT, 
  payment_type ENUM('card', 'paypal', 'bank_transfer') NOT NULL, 
  gateway_reference VARCHAR(100), 
  last_four_digits VARCHAR(4), 
  expiry_date DATE,
  FOREIGN KEY (user_id) REFERENCES users(user_id) 
);

CREATE TABLE payment_gateways (
  gateway_id SERIAL PRIMARY KEY,
  gateway_name VARCHAR(50) NOT NULL
);

CREATE TABLE disputes (
  dispute_id SERIAL PRIMARY KEY,
  transaction_id INT,
  reason TEXT,
  status ENUM('open', 'resolved', 'escalated') NOT NULL, 
  resolution_notes TEXT,
  FOREIGN KEY (transaction_id) REFERENCES transactions(transaction_id) 
);

