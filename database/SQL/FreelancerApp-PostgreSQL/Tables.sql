CREATE TYPE UserTypeEnum AS ENUM ('INDIVIDUAL', 'COMPANY');

CREATE TABLE Freelancer (
    FreelancerID uuid PRIMARY KEY,
    FreelancerName VARCHAR(50),
    Visibility BOOLEAN,
    ProfileViews INT,
    JobInvitationsNum INT,
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

Create TABLE featuredTeamMember(
TeamMemberID uuid primary key,
FreelancerID uuid,
membername varchar(255),
title varchar(255),
image varchar(255),
resume varchar(255),
FOREIGN KEY (FreelancerID) REFERENCES Freelancer(FreelancerID)
);

Create TABLE portfolio(
portfolioID uuid primary key,    
FreelancerID uuid,
title varchar(255),
coverImageUrl varchar (255),    
Attachments text[],
isDraft boolean,    
FOREIGN KEY (FreelancerID) REFERENCES Freelancer(FreelancerID)    
);

Create TABLE service(
serviceID uuid primary key,
FreelancerID uuid,
ServiceTitle varchar(255),
ServiceDescription varchar(5000), 
ServiceSkills varchar(255),
ServiceRate decimal,
MinimumBudget decimal,
serviceThumbnail varchar(255),       
FOREIGN KEY (FreelancerID) REFERENCES Freelancer(FreelancerID)    
);

Create TABLE PortfolioService (
serviceID uuid,
portfolioID uuid,
FOREIGN KEY (serviceID) REFERENCES service(serviceID),
FOREIGN KEY (portfolioID) REFERENCES portfolio(portfolioID)        
)
