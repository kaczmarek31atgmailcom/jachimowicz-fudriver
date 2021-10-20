package com.fungisearch.fudriver.testTools;

import com.fungisearch.fudriver.payroll.calendar.command.model.Calendar;
import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.calendar.command.repository.CalendarRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CreateCalendar {

    @Autowired
    private CalendarRepository calendarRepository;
    @Autowired
    private BeanValidator beanValidator;

    public Calendar create(Date day, SalaryDayTypeEnum dayType) {
        Calendar calendar = new Calendar.CalendarBuilder(calendarRepository, beanValidator)
                .date(day)
                .salaryDayType(dayType)
                .build();
        calendar.save();
        calendar.openMonth();
        return calendar;
    }
}
