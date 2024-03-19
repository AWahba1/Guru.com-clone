CREATE PROCEDURE GetFreelancerProfile (IN freelancerID uuid, OUT freelancer Cursor, OUT freelancerSkills Cursor)
BEGIN
    OPEN freelancer FOR
    SELECT * FROM Freelancers WHERE FreelancerID = freelancerID;

    OPEN freelancerSkills FOR
    SELECT s.ServiceSkills FROM service s WHERE s.FreelancerID = freelancerID 
    UNION 
    SELECT r.resourceSkills FROM dedicatedResource r WHERE r.FreelancerID = freelancerID;
END;

CREATE PROCEDURE ToggleProfileVisibility (IN freelancerID uuid)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;
    START TRANSACTION;
    UPDATE Freelancers SET Visibility = NOT Visibility WHERE FreelancerID = freelancerID;
    COMMIT;
END;

CREATE PROCEDURE UpdateFreelancerProfileAboutSection (
    IN freelancerID uuid,
    IN newFreelancerName varchar(50),
    IN newImageURL varchar(255),
    IN newTagline varchar(190),
    IN newBio varchar(3000),
    IN newWorkTerms varchar(2000),
    IN newAttachments text[],
    IN newUserType UserTypeEnum,
    IN newWebsiteLink varchar(255),
    IN newFacebookLink varchar(255),
    IN newLinkedInLink varchar(255),
    IN newProfessionalVideoLink varchar(255),
    IN newCompanyHistroy varchar(3000),
    IN newOperatingSince timestamp
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;

    START TRANSACTION;

    IF newFreelancerName IS NOT NULL THEN
        UPDATE Freelancers SET FreelancerName = newFreelancerName WHERE FreelancerID = freelancerID;
    END IF;

    IF newImageURL IS NOT NULL THEN
        UPDATE Freelancers SET imageURL = newImageURL WHERE FreelancerID = freelancerID;
    END IF;

    IF newTagline IS NOT NULL THEN
        UPDATE Freelancers SET Tagline = newTagline WHERE FreelancerID = freelancerID;
    END IF;

    IF newBio IS NOT NULL THEN
        UPDATE Freelancers SET Bio = newBio WHERE FreelancerID = freelancerID;
    END IF;

    IF newWorkTerms IS NOT NULL THEN
        UPDATE Freelancers SET WorkTerms = newWorkTerms WHERE FreelancerID = freelancerID;
    END IF;

    IF newAttachments IS NOT NULL THEN
        UPDATE Freelancers SET Attachments = newAttachments WHERE FreelancerID = freelancerID;
    END IF;

    IF newUserType IS NOT NULL THEN
        UPDATE Freelancers SET UserType = newUserType WHERE FreelancerID = freelancerID;
    END IF;

    IF newWebsiteLink IS NOT NULL THEN
        UPDATE Freelancers SET WebsiteLink = newWebsiteLink WHERE FreelancerID = freelancerID;
    END IF;

    IF newFacebookLink IS NOT NULL THEN
        UPDATE Freelancers SET FacebookLink = newFacebookLink WHERE FreelancerID = freelancerID;
    END IF;

    IF newLinkedInLink IS NOT NULL THEN
        UPDATE Freelancers SET LinkedInLink = newLinkedInLink WHERE FreelancerID = freelancerID;
    END IF;

    IF newProfessionalVideoLink IS NOT NULL THEN
        UPDATE Freelancers SET ProfessionalVideoLink = newProfessionalVideoLink WHERE FreelancerID = freelancerID;
    END IF;

    IF newCompanyHistroy IS NOT NULL THEN
        UPDATE Freelancers SET CompanyHistroy = newCompanyHistroy WHERE FreelancerID = freelancerID;
    END IF;

    IF newOperatingSince IS NOT NULL THEN
        UPDATE Freelancers SET OperatingSince = newOperatingSince WHERE FreelancerID = freelancerID;
    END IF;

    COMMIT;
END;

CREATE PROCEDURE AddPortfolio (
    IN freelancerID uuid, 
    IN title varchar(255), 
    IN coverImageUrl varchar(255), 
    Attachments text[], 
    )
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;
    START TRANSACTION;
    IF coverImageUrl IS NULL THEN
        INSERT INTO portfolios (portfolioID, FreelancerID, title, coverImageUrl,Attachments,isDraft) VALUES (UUID(), freelancerID, title, coverImageUrl,true);
    ELSE
        INSERT INTO portfolios (portfolioID, FreelancerID, title, coverImageUrl,Attachments,isDraft) VALUES (UUID(), freelancerID, title, coverImageUrl,Attachments,false);
    END IF;
    COMMIT;
END;

CREATE PROCEDURE UnpublishPortfolio (IN portfolioID uuid)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;
    START TRANSACTION;
    UPDATE portfolios SET isDraft = true WHERE portfolioID = portfolioID;
    DELETE FROM PortfolioService WHERE portfolioID = portfolioID;
    DELETE FROM PortfolioResource WHERE portfolioID = portfolioID;
    COMMIT;
END;

CREATE PROCEDURE DeletePortfolio (IN portfolioID uuid)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;
    START TRANSACTION;
    DELETE FROM portfolios WHERE portfolioID = portfolioID;
    DELETE FROM PortfolioService WHERE portfolioID = portfolioID;
    DELETE FROM PortfolioResource WHERE portfolioID = portfolioID;
    COMMIT;
END;

CREATE PROCEDURE UpdatePortfolio (
    IN portfolioID uuid,
    IN newTitle varchar(255),
    IN newCoverImageUrl varchar(255),
    IN newAttachments text[],
    IN newIsDraft boolean
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;

    START TRANSACTION;

    IF newTitle IS NOT NULL THEN
        UPDATE portfolios SET title = newTitle WHERE portfolioID = portfolioID;
    END IF;

    IF newCoverImageUrl IS NOT NULL THEN
        UPDATE portfolios SET coverImageUrl = newCoverImageUrl WHERE portfolioID = portfolioID;
    END IF;

    IF newAttachments IS NOT NULL THEN
        UPDATE portfolios SET Attachments = newAttachments WHERE portfolioID = portfolioID;
    END IF;

    IF newIsDraft IS NOT NULL THEN
        UPDATE portfolios SET isDraft = newIsDraft WHERE portfolioID = portfolioID;
    END IF;

    COMMIT;
END;

CREATE PROCEDURE GetServiceDetails (
    IN serviceID uuid,
    OUT serviceDetailsResult CURSOR,
    OUT portfolioResult CURSOR
)
BEGIN
    -- get the service details
    OPEN serviceDetailsResult FOR
    SELECT * FROM service WHERE serviceID = serviceID;
    -- check if the service has associated portfolios
    IF EXISTS (SELECT * FROM PortfolioService WHERE serviceID = serviceID) THEN
        -- get portfolios associated with the service
        OPEN portfolioResult FOR
        SELECT p.portfolioID, p.title, p.coverImageUrl 
        FROM portfolios p 
        JOIN PortfolioService ps ON p.portfolioID = ps.portfolioID
        WHERE ps.serviceID = serviceID;
    END IF;
END;

CREATE PROCEDURE AddService (
    IN freelancerID uuid, 
    IN ServiceTitle varchar(255), 
    IN ServiceDescription varchar(5000), 
    IN ServiceSkills [], 
    IN ServiceRate decimal, 
    IN MinimumBudget decimal, 
    IN serviceThumbnail varchar(255),
    IN portfolioID uuid[],
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;
    START TRANSACTION;
    INSERT INTO service (serviceID, FreelancerID, ServiceTitle, ServiceDescription, ServiceSkills, ServiceRate, MinimumBudget, serviceThumbnail, isDraft) VALUES (UUID(), freelancerID, ServiceTitle, ServiceDescription, ServiceSkills, ServiceRate, MinimumBudget, serviceThumbnail, false);
    IF portfolioID IS NOT NULL THEN
        FOR i IN 1..LENGTH(portfolioID) DO
            INSERT INTO PortfolioService (serviceID, portfolioID) VALUES (UUID(), serviceID, portfolioID[i]);
        END FOR;
    END IF;
    COMMIT;
END;

CREATE PROCEDURE UnpublishService (IN serviceID uuid)
BEGIN
    UPDATE service SET isDraft = true WHERE serviceID = serviceID;
END;

CREATE PROCEDURE DeleteService (IN serviceID uuid)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;
    START TRANSACTION;
    DELETE FROM service WHERE serviceID = serviceID;
    DELETE FROM PortfolioService WHERE serviceID = serviceID;
    COMMIT;
END;

CREATE PROCEDURE UpdateService (
    IN serviceID uuid,
    IN newServiceTitle varchar(255),
    IN newServiceDescription varchar(5000),
    IN newServiceSkills [],
    IN newServiceRate decimal,
    IN newMinimumBudget decimal,
    IN newServiceThumbnail varchar(255),
    IN newPortfolioID uuid[]
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;

    START TRANSACTION;

    IF newServiceTitle IS NOT NULL THEN
        UPDATE service SET ServiceTitle = newServiceTitle WHERE serviceID = serviceID;
    END IF;

    IF newServiceDescription IS NOT NULL THEN
        UPDATE service SET ServiceDescription = newServiceDescription WHERE serviceID = serviceID;
    END IF;

    IF newServiceSkills IS NOT NULL THEN
        UPDATE service SET ServiceSkills = newServiceSkills WHERE serviceID = serviceID;
    END IF;

    IF newServiceRate IS NOT NULL THEN
        UPDATE service SET ServiceRate = newServiceRate WHERE serviceID = serviceID;
    END IF;

    IF newMinimumBudget IS NOT NULL THEN
        UPDATE service SET MinimumBudget = newMinimumBudget WHERE serviceID = serviceID;
    END IF;

    IF newServiceThumbnail IS NOT NULL THEN
        UPDATE service SET serviceThumbnail = newServiceThumbnail WHERE serviceID = serviceID;
    END IF;

    IF newPortfolioID IS NOT NULL THEN
        DELETE FROM PortfolioService WHERE serviceID = serviceID;
        FOR i IN 1..LENGTH(newPortfolioID) DO
            INSERT INTO PortfolioService (serviceID, portfolioID) VALUES (UUID(), serviceID, newPortfolioID[i]);
        END FOR;
    END IF;

    COMMIT;
END;

CREATE PROCEDURE GetResourceDetails (
    IN resourceID uuid,
    OUT resourceDetailsResult CURSOR,
    OUT portfolioResult CURSOR
)
BEGIN
    -- get the resource details
    OPEN resourceDetailsResult FOR
    SELECT * FROM dedicatedResource WHERE resourceID = resourceID;
    -- check if the resource has associated portfolios
    IF EXISTS (SELECT * FROM PortfolioResource WHERE resourceID = resourceID) THEN
        -- get portfolios associated with the resource
        OPEN portfolioResult FOR
        SELECT p.portfolioID, p.title, p.coverImageUrl 
        FROM portfolios p 
        JOIN PortfolioResource pr ON p.portfolioID = pr.portfolioID
        WHERE pr.resourceID = resourceID;
    END IF;
END;

CREATE PROCEDURE AddDedicatedResource (
    IN freelancerID uuid, 
    IN resourcename varchar(255), 
    IN resourcetitle varchar(255), 
    IN resourcesummary varchar(3000), 
    IN resourceSkills text[], 
    IN resourceRate decimal, 
    IN MinimumDuration ResourceDurationEnum, 
    IN resourceImage varchar(255),
    IN portfolioID uuid[]
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;
    START TRANSACTION;
    INSERT INTO dedicatedResource (resourceID, FreelancerID, resourcename, resourcetitle, resourcesummary, resourceSkills, resourceRate, MinimumDuration, resourceImage, isDraft) VALUES (UUID(), freelancerID, resourcename, resourcetitle, resourcesummary, resourceSkills, resourceRate, MinimumDuration, resourceImage, false);
    IF portfolioID IS NOT NULL THEN
        FOR i IN 1..LENGTH(portfolioID) DO
            INSERT INTO PortfolioResource (resourceID, portfolioID) VALUES (UUID(), resourceID, portfolioID[i]);
        END FOR;
    END IF;
    COMMIT;
END;

CREATE PROCEDURE UnpublishDedicatedResource (IN resourceID uuid)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;
    START TRANSACTION;
    UPDATE dedicatedResource SET isDraft = true WHERE resourceID = resourceID;
    COMMIT;
END;

CREATE PROCEDURE DeleteDedicatedResource (IN resourceID uuid)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;
    START TRANSACTION;
    DELETE FROM dedicatedResource WHERE resourceID = resourceID;
    DELETE FROM PortfolioResource WHERE resourceID = resourceID;
    COMMIT;
END;

CREATE PROCEDURE UpdateDedicatedResource (
    IN resourceID uuid,
    IN newResourcename varchar(255),
    IN newResourcetitle varchar(255),
    IN newResourcesummary varchar(3000),
    IN newResourceSkills text[],
    IN newResourceRate decimal,
    IN newMinimumDuration ResourceDurationEnum,
    IN newResourceImage varchar(255),
    IN newPortfolioID uuid[]
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;

    START TRANSACTION;

    IF newResourcename IS NOT NULL THEN
        UPDATE dedicatedResource SET resourcename = newResourcename WHERE resourceID = resourceID;
    END IF;

    IF newResourcetitle IS NOT NULL THEN
        UPDATE dedicatedResource SET resourcetitle = newResourcetitle WHERE resourceID = resourceID;
    END IF;

    IF newResourcesummary IS NOT NULL THEN
        UPDATE dedicatedResource SET resourcesummary = newResourcesummary WHERE resourceID = resourceID;
    END IF;

    IF newResourceSkills IS NOT NULL THEN
        UPDATE dedicatedResource SET resourceSkills = newResourceSkills WHERE resourceID = resourceID;
    END IF;

    IF newResourceRate IS NOT NULL THEN
        UPDATE dedicatedResource SET resourceRate = newResourceRate WHERE resourceID = resourceID;
    END IF;

    IF newMinimumDuration IS NOT NULL THEN
        UPDATE dedicatedResource SET MinimumDuration = newMinimumDuration WHERE resourceID = resourceID;
    END IF;

    IF newResourceImage IS NOT NULL THEN
        UPDATE dedicatedResource SET resourceImage = newResourceImage WHERE resourceID = resourceID;
    END IF;

    IF newPortfolioID IS NOT NULL THEN
        DELETE FROM PortfolioResource WHERE resourceID = resourceID;
        FOR i IN 1..LENGTH(newPortfolioID) DO
            INSERT INTO PortfolioResource (resourceID, portfolioID) VALUES (UUID(), resourceID, newPortfolioID[i]);
        END FOR;
    END IF;

    COMMIT;
END;

CREATE PROCEDURE AddQuote (
    IN freelancerID uuid, 
    IN jobid uuid, 
    IN proposal varchar(3000), 
    IN bidsUsed decimal, 
    IN bidDate timestamp
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;
    START TRANSACTION;
    INSERT INTO quotes (quoteid, FreelancerID, jobid, proposal, quoteStatus, bidsUsed, bidDate) VALUES (UUID(), freelancerID, jobid, proposal,'AwatingAcceptance', bidsUsed, bidDate);
    UPDATE Freelancers SET AvailableBids = AvailableBids - bidsUsed WHERE FreelancerID = freelancerID;
    COMMIT;
END;

CREATE PROCEDURE UpdateQuote (
    IN quoteID uuid,
    IN newProposal varchar(3000),
    IN newBidsUsed decimal,
    IN newQuoteStatus QuoteStatusEnum
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;

    START TRANSACTION;

    IF newProposal IS NOT NULL THEN
        UPDATE quotes SET proposal = newProposal WHERE quoteid = quoteID;
    END IF;

    IF newBidsUsed IS NOT NULL THEN
        UPDATE quotes SET bidsUsed = newBidsUsed WHERE quoteid = quoteID;
    END IF;

    IF newQuoteStatus IS NOT NULL THEN
        UPDATE quotes SET quoteStatus = newQuoteStatus WHERE quoteid = quoteID;
    END IF;

    COMMIT;
END;

CREATE PROCEDURE ViewLastBidsUntilThreshold (IN freelancerID UUID)
BEGIN
    DECLARE total_bids DECIMAL DEFAULT 0;
    
    CREATE TEMPORARY TABLE temp_quotes AS
    SELECT *, (@cumulative_sum := @cumulative_sum + bidsUsed) AS cumulative_sum
    FROM quotes
    CROSS JOIN (SELECT @cumulative_sum := 0) AS dummy
    WHERE FreelancerID = freelancerID
    ORDER BY bidDate DESC;
    
    SELECT *
    FROM temp_quotes
    WHERE cumulative_sum <= 100
    ORDER BY bidDate DESC;
    
    DROP TEMPORARY TABLE IF EXISTS temp_quotes;
END;

CREATE PROCEDURE AddQuoteTemplate (
    IN freelancerID uuid, 
    IN templateName varchar(255), 
    IN templateDescription varchar(10000), 
    IN Attachments text[]
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;
    START TRANSACTION;
    INSERT INTO quoteTemplates (quoteTemplateID, FreelancerID, templateName, templateDescription, Attachments) VALUES (UUID(), freelancerID, templateName, templateDescription, Attachments);
    COMMIT;
END;

CREATE PROCEDURE UpdateQuoteTemplate (
    IN quoteTemplateID uuid,
    IN newTemplateName varchar(255),
    IN newTemplateDescription varchar(10000),
    IN newAttachments text[]
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;

    START TRANSACTION;

    IF newTemplateName IS NOT NULL THEN
        UPDATE quoteTemplates SET templateName = newTemplateName WHERE quoteTemplateID = quoteTemplateID;
    END IF;

    IF newTemplateDescription IS NOT NULL THEN
        UPDATE quoteTemplates SET templateDescription = newTemplateDescription WHERE quoteTemplateID = quoteTemplateID;
    END IF;

    IF newAttachments IS NOT NULL THEN
        UPDATE quoteTemplates SET Attachments = newAttachments WHERE quoteTemplateID = quoteTemplateID;
    END IF;

    COMMIT;
END;

CREATE PROCEDURE AddJobWatchlist (
    IN freelancerID uuid, 
    IN jobid uuid
)
BEGIN
    INSERT INTO jobWatchlist (watchlistID, FreelancerID, jobid) VALUES (UUID(), freelancerID, jobid);
END;

CREATE PROCEDURE RemoveJobWatchlist (
    IN watchlistID uuid
)
BEGIN
    DELETE FROM jobWatchlist WHERE watchlistID = watchlistID;
END;

CREATE PROCEDURE InviteToJob (
    IN freelancerID uuid, 
    IN clientID uuid, 
    IN jobid uuid, 
    IN invitationDate timestamp
)
BEGIN
    INSERT INTO jobInvitations (invitationID, FreelancerID, clientID, jobid, invitationDate) VALUES (UUID(), freelancerID, clientID, jobid, invitationDate);
END;

CREATE PROCEDURE AddFeaturedTeamMembers (
    IN freelancerID uuid, 
    IN membername [], 
    IN title TeamMemberRole[], 
    IN memberType [],
    IN memberEmail []
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;
    START TRANSACTION;
    IF membername IS NOT NULL THEN
        FOR i IN 1..LENGTH(membername) DO
            INSERT INTO featuredTeamMember (TeamMemberID, FreelancerID, membername, title, memberType, memberEmail) VALUES (UUID(), freelancerID, membername[i], title[i], memberType[i], memberEmail[i]);
        END FOR;
    END IF;
    COMMIT;
END;

CREATE PROCEDURE AddNoAccessMembers (
    IN freelancerID uuid, 
    IN membernames [], 
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET error_message = CONCAT('Error occurred: ', SQLSTATE(), ' - ', MYSQL_ERRNO());
    END;
    START TRANSACTION;
    IF membernames IS NOT NULL THEN
        FOR i IN 1..LENGTH(membernames) DO
            INSERT INTO featuredTeamMember (TeamMemberID, FreelancerID, membername, title, memberType, memberEmail) VALUES (UUID(), freelancerID, membernames[i], NULL, NULL,NULL);
        END FOR;
    END IF;
    COMMIT;
END;

CREATE PROCEDURE DeleteTeamMember (IN teamMemberID uuid)
BEGIN
    DELETE FROM featuredTeamMember WHERE TeamMemberID = teamMemberID;
END;