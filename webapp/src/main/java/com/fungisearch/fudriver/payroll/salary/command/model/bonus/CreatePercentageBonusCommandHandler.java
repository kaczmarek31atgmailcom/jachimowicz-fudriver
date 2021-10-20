package com.fungisearch.fudriver.payroll.salary.command.model.bonus;

import com.fungisearch.fudriver.common.command.CommandResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreatePercentageBonusCommandHandler {
    private final PayrollBonusFactory payrollBonusFactory;

    @Autowired
    public CreatePercentageBonusCommandHandler(PayrollBonusFactory payrollBonusFactory) {
        this.payrollBonusFactory = payrollBonusFactory;
    }

    public CommandResult handle(CreateBonusCommand command){

        PayrollPercentageBonus payrollPercentageBonus = payrollBonusFactory.getPercentageBonusBuilder()
                .name(command.name)
                .param(command.param)
                .build();
        return new CommandResult(payrollPercentageBonus.id, CommandResult.Status.OK,"PercentageBonusCreated");
    }
}
