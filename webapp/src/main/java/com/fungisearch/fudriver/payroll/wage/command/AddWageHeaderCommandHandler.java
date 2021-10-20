package com.fungisearch.fudriver.payroll.wage.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AddWageHeaderCommandHandler {

    private WageFactory wageFactory;

    @Autowired
    public AddWageHeaderCommandHandler(WageFactory wageFactory) {
        this.wageFactory = wageFactory;
    }

    public CommandResult handle(AddWageHeaderCommand command){
        WageHeader wageHeader = wageFactory.headerBuilder().name(command.name).build();
        return new CommandResult(wageHeader.getId(), CommandResult.Status.OK,"WageHeaderCreated");
    }

}
