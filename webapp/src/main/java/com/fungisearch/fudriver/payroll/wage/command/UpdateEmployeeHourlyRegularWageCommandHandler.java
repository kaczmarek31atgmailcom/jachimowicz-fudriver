package com.fungisearch.fudriver.payroll.wage.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UpdateEmployeeHourlyRegularWageCommandHandler {
    private final PersonFactory personFactory;

    @Autowired
    public UpdateEmployeeHourlyRegularWageCommandHandler(PersonFactory personFactory) {
        this.personFactory = personFactory;
    }

    public CommandResult handle(UpdateEmployeeHourlyWageCommand command){
        personFactory
                .find(command.personId)
                .setRegularWage(command.value);
    return new CommandResult(command.personId, CommandResult.Status.OK,"RegularWageUpdated");
    }
}
