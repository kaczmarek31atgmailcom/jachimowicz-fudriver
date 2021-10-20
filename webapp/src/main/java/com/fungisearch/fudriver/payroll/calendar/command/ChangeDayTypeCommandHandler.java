package com.fungisearch.fudriver.payroll.calendar.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.payroll.calendar.command.model.Calendar;
import com.fungisearch.fudriver.payroll.calendar.command.model.CalendarFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by marcin on 13.05.16.
 */
@Service
@Transactional
public class ChangeDayTypeCommandHandler {

    @Autowired
    private CalendarFactory calendarFactory;


    public CommandResult handle(ChangeDayTypeCommand command) {
        CommandResult commandResult;
        Calendar calendar = calendarFactory.findDay(command.date);
        switch(command.dayType){
            case 1: calendar.setSunday();
                commandResult = CommandResult.OK;
                break;
            case 2: calendar.setBonusDay();
                commandResult = CommandResult.OK;
                break;
            default: commandResult = new CommandResult(CommandResult.Status.ERROR);
                break;
        }
return commandResult;
    }
}
