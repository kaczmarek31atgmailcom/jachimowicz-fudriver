package com.fungisearch.fudriver.testTools.UT;

import com.fungisearch.fudriver.payroll.calendar.command.model.Calendar;
import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.calendar.command.repository.CalendarRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import java.util.Date;

/**
 * Created by marcin on 08.06.17.
 */
public class CreateCalendarUT {

    private final CalendarRepository calendarRepository;
    private final BeanValidator beanValidator;

    public CreateCalendarUT(CalendarRepository calendarRepository, BeanValidator beanValidator) {
        this.calendarRepository = calendarRepository;
        this.beanValidator = beanValidator;
    }

    public Calendar create(Date day, SalaryDayTypeEnum dayType) {
        Calendar calendar = new Calendar.CalendarBuilder(calendarRepository, beanValidator)
                .date(day)
                .salaryDayType(dayType)
                .build();
        calendar.save();
        return calendar;
    }
}
