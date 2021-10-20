package com.fungisearch.fudriver.payroll.wage.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateEmployeeSalaryTypeCommandHandler {

    private final PersonFactory personFactory;

    @Autowired
    public UpdateEmployeeSalaryTypeCommandHandler(PersonFactory personFactory) {
        this.personFactory = personFactory;
    }

    public CommandResult handle(UpdateEmployeeSalaryTypeCommand command) {
        switch (command.payrollType) {
            case HOURLY:
                personFactory.find(command.personId).switchToHourlyPayed();
                break;
            case ACCORD:
                personFactory.find(command.personId).switchToAccordAccordPayed();
                break;
            default:
                throw new IllegalStateException("Invalid Payroll Type");
        }
        return new CommandResult(command.personId, CommandResult.Status.OK, "EmployeePayrollTypeUpdated");
    }
}
