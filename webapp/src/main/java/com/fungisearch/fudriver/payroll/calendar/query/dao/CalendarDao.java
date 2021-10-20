package com.fungisearch.fudriver.payroll.calendar.query.dao;

import com.fungisearch.fudriver.payroll.calendar.query.dto.CalendarDto;

import java.util.List;

/**
 * Created by marcin on 13.05.16.
 */
public interface CalendarDao {
    List<CalendarDto> getDaysInMonth(String dayMonth);

}
