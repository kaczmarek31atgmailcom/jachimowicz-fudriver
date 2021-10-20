package com.fungisearch.fudriver.payroll.wage.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UpdateWageValueCommandHandler {
    private final WageFactory wageFactory;

    @Autowired
    public UpdateWageValueCommandHandler(WageFactory wageFactory) {
        this.wageFactory = wageFactory;
    }

    public CommandResult handle(UpdateWageValueCommand command) {
        wageFactory
                .findWage(command.wageId)
                .updateValue(command.value);
        return new CommandResult(command.wageId, CommandResult.Status.OK,"WageValueUpdated");
    }
}
