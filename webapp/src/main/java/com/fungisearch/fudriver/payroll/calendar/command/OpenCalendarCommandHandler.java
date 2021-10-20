package com.fungisearch.fudriver.payroll.calendar.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.payroll.calendar.command.model.Calendar;
import com.fungisearch.fudriver.payroll.calendar.command.model.CalendarFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OpenCalendarCommandHandler {

    private final CalendarFactory calendarFactory;

    @Autowired
    public OpenCalendarCommandHandler(CalendarFactory calendarFactory) {
        this.calendarFactory = calendarFactory;
    }

    public CommandResult handle(OpenCalendarCommand command) {
        calendarFactory.findDay(command.date);
        return CommandResult.OK;
    }
}


