package com.fungisearch.fudriver.person.person.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.exception.ClosedTimeSheetException;
import com.fungisearch.fudriver.exception.NonExistingTimeSheetUpdateException;
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
public class InactivatePersonCommandHandler {

    @Autowired
    private PersonFactory personFactory;

    @Autowired
    private TimeSheetFactory timeSheetFactory;

    public CommandResult handle(InactivatePersonCommand command) throws ClosedTimeSheetException, NonExistingTimeSheetUpdateException {

        personFactory.find(command.personId).inactivate();

        TimeSheet timeSheet = timeSheetFactory.findLatestOne(command.personId);
        timeSheet.closePeriod();

        return CommandResult.OK;
    }
}
