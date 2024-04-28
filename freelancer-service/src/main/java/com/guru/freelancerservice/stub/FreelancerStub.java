package com.guru.freelancerservice.stub;

import com.guru.freelancerservice.models.Freelancer;
import com.guru.freelancerservice.models.user_type_enum;
import com.guru.freelancerservice.repositories.FreelancerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Configuration
public class FreelancerStub {
    private final FreelancerRepository freelancerRepository;
    //private final CompanyRepository companyRepository;

    public FreelancerStub(FreelancerRepository freelancerRepository) {
        this.freelancerRepository = freelancerRepository;
        //this.companyRepository = companyRepository;
    }

    @Bean
    CommandLineRunner commandLineRunner(FreelancerRepository repository) {
        return args -> {

//            Company companyJob = new Company(
//                    "company job",
//                    "company job description "
//            );
//
//            companyRepository.save(companyJob);
//            Job job1 = new Job(
//                    "software engineer1",
//                    "job description 1",
//                    10000,
//                    20000,
//                    "Cairo",
//                    companyJob
//            );
//            Job job2 = new Job(
//                    "software engineer2",
//                    "job description 2",
//                    10000,
//                    20000,
//                    "Cairo",
//                    companyJob
//            );
//            Job job3 = new Job(
//                    "software engineer3",
//                    "job description 3",
//                    10000,
//                    20000,
//                    "Cairo",
//                    companyJob
//            );
//            Freelancer freelancer1= Freelancer.builder()
//                    .freelancer_name("freelancer1")
//                    .image_url("image_url1")
//                    .visibility(true)
//                    .profile_views(100)
//                    .job_invitations_num(10)
//                    .available_bids(20)
//                    .all_time_earnings(1000)
//                    .employers_num(5)
//                    .highest_paid(20000)
//                    //insert date of now
//                    .membership_date(new Timestamp((new Date()).getTime()))
//                    .tagline("tagline1")
//                    .bio("bio1")
//                    .work_terms("work_terms1")
//                    .attachments(List.of("attachment1"))
//                    .user_type(user_type_enum.INDIVIDUAL)
//                    .website_link("website_link1")
//                    .facebook_link("facebook_link1")
//                    .linkedin_link("linkedin_link1")
//                    .professional_video_link("professional_video_link1")
//                    .company_history("company_history1")
//                    //insert date of 1-1-2021 with timezone
//                    .operating_since(new Timestamp( 2021, 1, 1, 0, 0, 0, 0))
//                    .build();

            Freelancer freelancer2=new Freelancer(
                    "freelancer2",
                    "image_url2",
                    true,
                    200,
                    20,
                    30,
                    2000,
                    10,
                    30000,
                    new Timestamp((new Date()).getTime()),
                    "tagline2",
                    "bio2",
                    "work_terms2",
                    List.of("attachment2"),
                    user_type_enum.INDIVIDUAL,
                    "website_link2",
                    "facebook_link2",
                    "linkedin_link2",
                    "professional_video_link2",
                    "company_history2",
                    new Timestamp( 2021, 1, 1, 0, 0, 0, 0)
            );

            repository.save(freelancer2);
        };
    }
}



