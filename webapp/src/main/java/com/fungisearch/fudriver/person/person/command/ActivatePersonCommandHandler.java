package com.fungisearch.fudriver.person.person.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.exception.NotClosedTimeSheetException;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.person.person.command.model.TimeSheet;
import com.fungisearch.fudriver.person.person.command.model.TimeSheetFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by marcin on 03.03.16.
 */
@Service
@Transactional
public class ActivatePersonCommandHandler {

    @Autowired
    private PersonFactory personFactory;

    @Autowired
    private TimeSheetFactory timeSheetFactory;

    public CommandResult handle(ActivatePersonCommand command) throws NotClosedTimeSheetException {

        personFactory.find(command.personId).activate(personFactory.findFirstNotReservedNumber());

        TimeSheet.TimeSheetBuilder timeSheetBuilder = timeSheetFactory.builder();
        TimeSheet timeSheet = timeSheetBuilder.personId(command.personId).build();
        timeSheet.openPeriod();

        return CommandResult.OK;
    }
}
