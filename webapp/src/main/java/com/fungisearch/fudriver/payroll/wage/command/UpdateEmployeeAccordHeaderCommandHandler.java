package com.fungisearch.fudriver.payroll.wage.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateEmployeeAccordHeaderCommandHandler {
    private final PersonFactory personFactory;
    private final WageFactory wageFactory;

    @Autowired
    public UpdateEmployeeAccordHeaderCommandHandler(PersonFactory personFactory, WageFactory wageFactory) {
        this.personFactory = personFactory;
        this.wageFactory = wageFactory;
    }

    public CommandResult handle(UpdateEmployeeAccordHeaderCommand command) {
        personFactory
                .find(command.personId)
                .setWageHeader(wageFactory
                        .findHeader(command.headerId));
        return new CommandResult(command.personId, CommandResult.Status.OK,"EmployeeAccordHeaderUpdated");
    }
}
