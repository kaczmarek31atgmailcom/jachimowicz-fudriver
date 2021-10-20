package com.fungisearch.fudriver.payroll.calendar.command.service;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.payroll.calendar.command.ChangeDayTypeCommand;
import com.fungisearch.fudriver.payroll.calendar.command.ChangeDayTypeCommandHandler;
import com.fungisearch.fudriver.payroll.calendar.command.OpenCalendarCommand;
import com.fungisearch.fudriver.payroll.calendar.command.OpenCalendarCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by marcin on 13.05.16.
 */
@RestController
public class CalendarRestController {

    @Autowired
    private OpenCalendarCommandHandler openCalendarCommandHandler;

    @Autowired
    private ChangeDayTypeCommandHandler changeDayTypeHandler;


    @RequestMapping(value = "/rest/calendar", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @Transactional
    CommandResult openMonth(@RequestBody OpenCalendarCommand command){
        CommandResult result = openCalendarCommandHandler.handle(command);
        return result;
    }

    @RequestMapping(value = "/rest/calendar", method = RequestMethod.PUT, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @Transactional
    CommandResult changeDayStatus(@RequestBody ChangeDayTypeCommand command){
        CommandResult result = changeDayTypeHandler.handle(command);
        return result;
    }
}
