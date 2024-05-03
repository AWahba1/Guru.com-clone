package com.guru.jobservice.validators;

import com.guru.jobservice.dtos.JobRequest;
import com.guru.jobservice.enums.PaymentType;
import com.guru.jobservice.exceptions.ValidationException;

public class JobRequestValidator {

    public static void validatePaymentType(JobRequest jobRequest)
    {

        if (jobRequest.getPaymentType().equals(PaymentType.FIXED.getValue()))
        {
            if (jobRequest.getFixedPriceRange() == null)
            {
                throw new ValidationException("A price range must be set if payment type is fixed");
            }

            if (jobRequest.getDuration()!=null || jobRequest.getHoursPerWeek()!=null || jobRequest.getMinHourlyRate()!=null || jobRequest.getMaxHourlyRate()!=null)
            {
                throw new ValidationException("Job Duration, Hours per week, min and max hourly rates should be null if payment type is fixed");
            }
        }

        if (jobRequest.getPaymentType().equals(PaymentType.HOURLY.getValue()))
        {
            if (jobRequest.getFixedPriceRange() != null)
            {
                throw new ValidationException("A price range must be null if payment type is hourly");
            }

            if(jobRequest.getDuration()==null || jobRequest.getHoursPerWeek()==null || jobRequest.getMinHourlyRate()==null || jobRequest.getMaxHourlyRate()==null)
            {
                throw new ValidationException("Job Duration, Hours per week, min and max hourly rates should be SET if payment type is hourly");
            }
        }

        if (jobRequest.getMinHourlyRate()!= null && jobRequest.getMaxHourlyRate()!= null )
        {
            if (jobRequest.getMinHourlyRate().compareTo(jobRequest.getMaxHourlyRate()) > 0)
                throw new ValidationException("Minimum Hourly Rate cannot be greater than max Hourly Rate");
        }
    }
}
