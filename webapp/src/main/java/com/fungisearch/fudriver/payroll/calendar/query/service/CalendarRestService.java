package com.fungisearch.fudriver.payroll.calendar.query.service;

import com.fungisearch.fudriver.payroll.calendar.query.dao.CalendarDao;
import com.fungisearch.fudriver.payroll.calendar.query.dto.CalendarDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by marcin on 13.05.16.
 */
@RestController
public class CalendarRestService {

    @Autowired
    private CalendarDao calendarDao;

    @RequestMapping(value = "/rest/calendar", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<CalendarDto> findDaysInMonth(@RequestParam (value = "month") String month){
        return calendarDao.getDaysInMonth(month);
    }
}
