drop table if exists Freelancer;
drop table if exists featuredTeamMember;
drop table if exists portfolios;
drop table if exists service;
drop table if exists PortfolioService;
drop table if exists dedicatedResource;
drop table if exists PortfolioResource;
drop type if exists UserTypeEnum;
drop type if exists ResourceDurationEnum;

CREATE TYPE UserTypeEnum AS ENUM ('INDIVIDUAL', 'COMPANY');
CREATE TYPE ResourceDurationEnum AS ENUM ('3Months', '6Months','1Year','Ongoing');
CREATE TYPE QuoteStatusEnum AS ENUM ('AWAITING_ACCEPTANCE', 'PRIORITY', 'ACCEPTED', 'ARCHIVED');
CREATE TYPE TeamMemberType AS ENUM ('INDEPENDENT_ACCOUNTS', 'SUB_ACCOUNTS', 'NO_ACCESS_MEMBERS');
CREATE TYPE TeamMemberRole AS ENUM ('CONSULTANT', 'MANAGER');

CREATE TABLE Freelancer(
    FreelancerID uuid PRIMARY KEY,
    FreelancerName VARCHAR(50),
    Visibility BOOLEAN,
    ProfileViews INT,
    JobInvitationsNum INT,
    AvailableBids INT,
    AllTimeEarnings decimal,
    EmployersNum INT,
    HighestPaid decimal,
    MembershipDate Timestamp,
    Tagline varchar(190),
    Bio varchar(3000),
    WorkTerms varchar(2000),
    Attachments text[],
    UserType UserTypeEnum,
    WebsiteLink varchar (255),
    FacebookLink varchar (255),
    LinkedInLink varchar (255),
    ProfessionalVideoLink varchar (255),
    CompanyHistroy varchar(3000),
    OperatingSince Timestamp
);

CREATE TABLE featuredTeamMember(
    TeamMemberID uuid primary key,
    FreelancerID uuid,
    membername varchar(255),
    title TeamMemberRole,
    memberType TeamMemberType,
    memberEmail varchar(255),
    FOREIGN KEY (FreelancerID) REFERENCES Freelancers(FreelancerID)
);

CREATE TABLE portfolios(
    portfolioID uuid primary key,    
    FreelancerID uuid,
    title varchar(255),
    coverImageUrl varchar (255),    
    Attachments text[],
    isDraft boolean,    
    FOREIGN KEY (FreelancerID) REFERENCES Freelancers(FreelancerID)    
);

CREATE TABLE service(
    serviceID uuid primary key,
    FreelancerID uuid,
    ServiceTitle varchar(255),
    ServiceDescription varchar(5000), 
    ServiceSkills varchar(255),
    ServiceRate decimal,
    MinimumBudget decimal,
    serviceThumbnail varchar(255),       
    FOREIGN KEY (FreelancerID) REFERENCES Freelancers(FreelancerID)    
);

CREATE TABLE PortfolioService (
    serviceID uuid,
    portfolioID uuid,
    FOREIGN KEY (serviceID) REFERENCES service(serviceID),
    FOREIGN KEY (portfolioID) REFERENCES portfolios(portfolioID)        
);

CREATE TABLE dedicatedResource(
    resourceID uuid primary key,
    FreelancerID uuid,
    resourcename varchar(255),
    resourcetitle varchar(255), 
    resourcesummary varchar(3000),
    resourceSkills varchar(255),
    resourceRate decimal,
    MinimumDuration ResourceDurationEnum,
    resourceImage varchar(255),       
    FOREIGN KEY (FreelancerID) REFERENCES Freelancers(FreelancerID)    
);

CREATE TABLE PortfolioResource(
    resourceID uuid,
    portfolioID uuid,
    FOREIGN KEY (resourceID) REFERENCES dedicatedResource(resourceID),
    FOREIGN KEY (portfolioID) REFERENCES portfolios(portfolioID)        
);

CREATE TABLE quotes(
    quoteid uuid primary key,
    FreelancerID uuid,
    jobid uuid,
    proposal varchar(3000),
    quoteStatus QuoteStatusEnum,
    bidsUsed decimal,
    bidDate timestamp,
    FOREIGN KEY (FreelancerID) REFERENCES Freelancers(FreelancerID),
    FOREIGN KEY (jobid) REFERENCES jobs(jobid)    
);

CREATE TABLE quoteTemplates(
    quoteTemplateID uuid primary key,
    FreelancerID uuid,
    templateName varchar(255),
    templateDescription varchar(10000),
    Attachments text[],
    FOREIGN KEY (FreelancerID) REFERENCES Freelancers(FreelancerID)
);

CREATE TABLE jobWatchlist(
    watchlistID uuid primary key,
    FreelancerID uuid,
    jobid uuid,
    FOREIGN KEY (FreelancerID) REFERENCES Freelancers(FreelancerID),
    FOREIGN KEY (jobid) REFERENCES jobs(jobid)    
);

CREATE TABLE jobInvitations(
    invitationID uuid primary key,
    FreelancerID uuid,
    clientID uuid,
    jobid uuid,
    invitationDate timestamp,
    FOREIGN KEY (FreelancerID) REFERENCES Freelancers(FreelancerID),
    FOREIGN KEY (clientID) REFERENCES client(clientID),
    FOREIGN KEY (jobid) REFERENCES jobs(jobid)    
);