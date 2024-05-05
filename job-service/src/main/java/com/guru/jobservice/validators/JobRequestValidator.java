package com.guru.jobservice.validators;

import com.guru.jobservice.dtos.CreateUpdateRequest;
import com.guru.jobservice.enums.PaymentType;
import com.guru.jobservice.exceptions.ValidationException;

public class JobRequestValidator {

    public static void validatePaymentType(CreateUpdateRequest createUpdateRequest)
    {

        if (createUpdateRequest.getPaymentType().equals(PaymentType.FIXED.getValue()))
        {
            if (createUpdateRequest.getFixedPriceRange() == null)
            {
                throw new ValidationException("A price range must be set if payment type is fixed");
            }

            if (createUpdateRequest.getDuration()!=null || createUpdateRequest.getHoursPerWeek()!=null || createUpdateRequest.getMinHourlyRate()!=null || createUpdateRequest.getMaxHourlyRate()!=null)
            {
                throw new ValidationException("Job Duration, Hours per week, min and max hourly rates should be null if payment type is fixed");
            }
        }

        if (createUpdateRequest.getPaymentType().equals(PaymentType.HOURLY.getValue()))
        {
            if (createUpdateRequest.getFixedPriceRange() != null)
            {
                throw new ValidationException("A price range must be null if payment type is hourly");
            }

            if(createUpdateRequest.getDuration()==null || createUpdateRequest.getHoursPerWeek()==null || createUpdateRequest.getMinHourlyRate()==null || createUpdateRequest.getMaxHourlyRate()==null)
            {
                throw new ValidationException("Job Duration, Hours per week, min and max hourly rates should be SET if payment type is hourly");
            }
        }

        if (createUpdateRequest.getMinHourlyRate()!= null && createUpdateRequest.getMaxHourlyRate()!= null )
        {
            if (createUpdateRequest.getMinHourlyRate().compareTo(createUpdateRequest.getMaxHourlyRate()) > 0)
                throw new ValidationException("Minimum Hourly Rate cannot be greater than max Hourly Rate");
        }
    }
}
