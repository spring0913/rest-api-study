package me.bmsung.demorestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors){
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong");
            errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong");

            LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
            if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime())
                    || endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())
                    || endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
                errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong");
            }

            LocalDateTime beginEventDateTime = eventDto.getBeginEventDateTime();
            if (beginEventDateTime.isAfter(eventDto.getEndEventDateTime())
                    || beginEventDateTime.isAfter(eventDto.getCloseEnrollmentDateTime())
                    || beginEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
                errors.rejectValue("beginEventDateTime", "wrongValue", "beginEventDateTime is wrong");
            }

            LocalDateTime closeEnrollmentDateTime = eventDto.getCloseEnrollmentDateTime();
            if (closeEnrollmentDateTime.isAfter(eventDto.getBeginEventDateTime())
                    || closeEnrollmentDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())
                    || closeEnrollmentDateTime.isAfter(eventDto.getEndEventDateTime())) {
                errors.rejectValue("closeEnrollmentDateTime", "wrongValue", "closeEnrollmentDateTime is wrong");
            }
        }
    }
}
