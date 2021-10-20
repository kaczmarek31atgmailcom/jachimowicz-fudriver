package com.fungisearch.fudriver.payroll.calendar.command.model;

import com.fungisearch.fudriver.payroll.calendar.command.repository.CalendarRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CalendarFactory {

    private final CalendarRepository calendarRepository;
    private final BeanValidator beanValidator;

    @Autowired
    public CalendarFactory(CalendarRepository calendarRepository, BeanValidator beanValidator) {
        this.calendarRepository = calendarRepository;
        this.beanValidator = beanValidator;
    }


    public Calendar findDay(Date date) {
        Calendar calendar = calendarRepository.findByDate(date);
        if (calendar == null) {
            calendar = new Calendar.CalendarBuilder(calendarRepository, beanValidator).date(date).build();
            calendar.openMonth();
        }


        calendar.setCalendarRepository(this.calendarRepository);
        calendar.setBeanValidator(this.beanValidator);
        return calendar;
    }

    public Map<DateTime,Calendar> findMonth(Date day) {
        DateTime firstDay = new DateTime(day).withDayOfMonth(1);
        DateTime lastDay = firstDay.dayOfMonth().withMaximumValue();
        List<Calendar> calendarList = calendarRepository.findBetweenDates(firstDay.toDate(), lastDay.toDate());
        Map<DateTime,Calendar> theMap = new HashMap<>();
        if (!(calendarList.isEmpty())) {
            for (Calendar calendar : calendarList) {
                calendar.setCalendarRepository(calendarRepository);
                calendar.setBeanValidator(beanValidator);
                theMap.put(new DateTime(calendar.getDate()),calendar);
            }
        }
        return theMap;
    }
}
