CREATE FUNCTION GetMyPortfolios (freelancerID uuid)
RETURNS TABLE (portfolioID uuid, title varchar(255), coverImageUrl varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT portfolioID, title, coverImageUrl FROM portfolios WHERE FreelancerID = GetMyPortfolios.freelancerID;
END;
$$;
LANGUAGE plpgsql;

CREATE FUNCTION GetFreelancerPortfolios (freelancerID uuid)
RETURNS TABLE (portfolioID uuid, title varchar(255), coverImageUrl varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT portfolioID, title, coverImageUrl FROM portfolios WHERE FreelancerID = GetFreelancerPortfolios.freelancerID AND isDraft = false;
END;
$$;
LANGUAGE plpgsql;

CREATE FUNCTION GetPortfolio (portfolioID uuid)
RETURNS TABLE (portfolioID uuid, FreelancerID uuid, title varchar(255), coverImageUrl varchar(255), Attachments text[], isDraft boolean)
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM portfolios WHERE portfolioID = GetPortfolio.portfolioID;
END;
$$;
LANGUAGE plpgsql;

CREATE FUNCTION GetMyServices (freelancerID uuid)
RETURNS TABLE (serviceID uuid, ServiceTitle varchar(255), ServiceDescription varchar(5000), ServiceSkills text[], ServiceRate decimal, MinimumBudget decimal, serviceThumbnail varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM service WHERE FreelancerID = GetMyServices.freelancerID;
END;
$$;
LANGUAGE plpgsql;

CREATE FUNCTION GetFreelancerServices (freelancerID uuid)
RETURNS TABLE (serviceID uuid, ServiceTitle varchar(255), ServiceDescription varchar(5000), ServiceSkills text[], ServiceRate decimal, MinimumBudget decimal, serviceThumbnail varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM service WHERE FreelancerID = GetFreelancerServices.freelancerID AND isDraft = false;
END;
$$;
LANGUAGE plpgsql;

CREATE FUNCTION GetFreelancerDedicatedResources (freelancerID uuid)
RETURNS TABLE (resourceID uuid, resourcename varchar(255), resourcetitle varchar(255), resourcesummary varchar(3000), resourceSkills text[], resourceRate decimal, MinimumDuration ResourceDurationEnum, resourceImage varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM dedicatedResource WHERE FreelancerID = GetFreelancerDedicatedResources.freelancerID AND isDraft = false;
END;
$$;
LANGUAGE plpgsql;

CREATE FUNCTION GetMyDedicatedResources (freelancerID uuid)
RETURNS TABLE (resourceID uuid, resourcename varchar(255), resourcetitle varchar(255), resourcesummary varchar(3000), resourceSkills text[], resourceRate decimal, MinimumDuration ResourceDurationEnum, resourceImage varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM dedicatedResource WHERE FreelancerID = GetMyDedicatedResources.freelancerID;
END;
$$;
LANGUAGE plpgsql;

CREATE FUNCTION GetFreelancerQuotes (freelancerID uuid, quoteStatus QuoteStatusEnum)
RETURNS TABLE (quoteid uuid, jobid uuid, proposal varchar(3000), quoteStatus QuoteStatusEnum, bidsUsed decimal, bidDate timestamp)
AS $$
BEGIN
    IF quoteStatus IS NULL THEN
        RETURN QUERY
        SELECT * FROM quotes WHERE FreelancerID = GetFreelancerQuotes.freelancerID;
    ELSE
        RETURN QUERY
        SELECT * FROM quotes WHERE FreelancerID = GetFreelancerQuotes.freelancerID AND quoteStatus = GetFreelancerQuotes.quoteStatus;
    END IF;
END;
$$;
LANGUAGE plpgsql;

CREATE FUNCTION GetFreelancerQuoteDetails (quoteID uuid)
RETURNS TABLE (quoteid uuid, FreelancerID uuid, jobid uuid, proposal varchar(3000), quoteStatus QuoteStatusEnum, bidsUsed decimal, bidDate timestamp)
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM quotes WHERE quoteid = GetFreelancerQuoteDetails.quoteID;
END;
$$;
LANGUAGE plpgsql;

CREATE FUNCTION GetBidsUsageHistory (freelancerID uuid)
RETURNS TABLE (bidsUsed decimal, bidDate timestamp)
AS $$
BEGIN
    RETURN QUERY
    SELECT bidsUsed, bidDate
    FROM quotes
    WHERE FreelancerID = GetBidsUsageHistory.freelancerID;
END;
$$;
LANGUAGE plpgsql;

CREATE FUNCTION BidsUsageSummary (freelancerID uuid)
RETURNS TABLE (allTimeBidsUsed decimal, lastMonthBidsUsed decimal, lastYearBidsUsed decimal)
AS $$
BEGIN
    RETURN QUERY
    SELECT 
        (SELECT SUM(bidsUsed) FROM quotes WHERE FreelancerID = BidsUsageSummary.freelancerID) AS allTimeBidsUsed,
        (SELECT SUM(bidsUsed) FROM quotes WHERE FreelancerID = BidsUsageSummary.freelancerID AND bidDate > DATE_SUB(NOW(), INTERVAL 1 MONTH)) AS lastMonthBidsUsed,
        (SELECT SUM(bidsUsed) FROM quotes WHERE FreelancerID = BidsUsageSummary.freelancerID AND bidDate > DATE_SUB(NOW(), INTERVAL 1 YEAR)) AS lastYearBidsUsed;
END;
$$;
LANGUAGE plpgsql;

CREATE FUNCTION GetFreelancerQuoteTemplates (freelancerID uuid)
RETURNS TABLE (quoteTemplateID uuid, templateName varchar(255), templateDescription varchar(10000), Attachments text[])
AS $$
BEGIN
    RETURN QUERY
    SELECT quoteTemplateID, templateName, templateDescription, Attachments
    FROM quoteTemplates
    WHERE FreelancerID = GetFreelancerQuoteTemplates.freelancerID;
END;
$$;
LANGUAGE plpgsql;

CREATE FUNCTION GetFreelancerJobWatchlist (freelancerID uuid)
RETURNS TABLE (watchlistID uuid, jobid uuid)
AS $$
BEGIN
    RETURN QUERY
    SELECT watchlistID, jobid
    FROM jobWatchlist
    WHERE FreelancerID = GetFreelancerJobWatchlist.freelancerID;
END;
$$;
LANGUAGE plpgsql;


CREATE FUNCTION GetFreelancerJobInvitations (freelancerID uuid)
RETURNS TABLE (invitationID uuid, clientID uuid, jobid uuid, invitationDate timestamp)
AS $$
BEGIN
    RETURN QUERY
    SELECT invitationID, clientID, jobid, invitationDate
    FROM jobInvitations
    WHERE FreelancerID = GetFreelancerJobInvitations.freelancerID;
END;
$$;
LANGUAGE plpgsql;

CREATE FUNCTION GetFreelancerTeamMembers (freelancerID uuid)
RETURNS TABLE (TeamMemberID uuid, membername varchar(255), title TeamMemberRole, memberType TeamMemberType, memberEmail varchar(255))
AS $$
BEGIN
    RETURN QUERY
    SELECT TeamMemberID, membername, title, memberType, memberEmail
    FROM featuredTeamMember
    WHERE FreelancerID = GetFreelancerTeamMembers.freelancerID;
END;
$$;
LANGUAGE plpgsql;