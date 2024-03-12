CREATE DATABASE "Guru";

CREATE TABLE IF NOT EXISTS public.users
(
    id integer NOT NULL,
    username character varying(50) COLLATE pg_catalog."default" NOT NULL,
    email character varying(100) COLLATE pg_catalog."default" NOT NULL,
    passwordhashed character varying(255) COLLATE pg_catalog."default" NOT NULL,
    fullname character varying(100) COLLATE pg_catalog."default" NOT NULL,
    profilepicture character varying(255) COLLATE pg_catalog."default",
    accountcreateddate date NOT NULL,
    lastlogindate date,
    accountstate accountstate,
    accounttype accounttype,
    verstate verstate,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_email_key UNIQUE (email),
    CONSTRAINT users_username_key UNIQUE (username)
)

TABLESPACE pg_default;


CREATE TABLE IF NOT EXISTS public.employer
(
    profileid integer NOT NULL DEFAULT nextval('employer_profileid_seq'::regclass),
    userid integer,
    companyname character varying(100) COLLATE pg_catalog."default" NOT NULL,
    industry character varying(100) COLLATE pg_catalog."default",
    overview text COLLATE pg_catalog."default",
    projectsposted integer DEFAULT 0,
    CONSTRAINT employer_pkey PRIMARY KEY (profileid),
    CONSTRAINT employer_userid_key UNIQUE (userid),
    CONSTRAINT fk_employer FOREIGN KEY (userid)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.freelancers
(
    profileid integer NOT NULL,
    userid integer,
    title character varying(100) COLLATE pg_catalog."default" NOT NULL,
    overview text COLLATE pg_catalog."default",
    skills text COLLATE pg_catalog."default",
    portfolio text COLLATE pg_catalog."default",
    hourlyrate numeric(10,2),
    availability character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT freelancers_pkey PRIMARY KEY (profileid),
    CONSTRAINT freelancers_userid_key UNIQUE (userid),
    CONSTRAINT fk_freelancer_user FOREIGN KEY (userid)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

