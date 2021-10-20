package com.fungisearch.fudriver.payroll.calendar.command.repository;

import com.fungisearch.fudriver.payroll.calendar.command.model.Calendar;

import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 13.05.16.
 */
public interface CalendarRepository {
    List<Calendar> findBetweenDates(Date startDate, Date endDate);
    void create(Calendar calendar);
    Calendar findByDate(Date date);
    void update(Calendar calendar);
}
