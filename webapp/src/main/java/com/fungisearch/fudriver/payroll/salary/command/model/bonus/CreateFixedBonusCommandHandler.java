package com.fungisearch.fudriver.payroll.salary.command.model.bonus;

import com.fungisearch.fudriver.common.command.CommandResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CreateFixedBonusCommandHandler {
    private final PayrollBonusFactory payrollBonusFactory;

    @Autowired
    public CreateFixedBonusCommandHandler(PayrollBonusFactory payrollBonusFactory) {
        this.payrollBonusFactory = payrollBonusFactory;
    }

    public CommandResult handle(CreateBonusCommand command){
        PayrollFixedBonus bonus = payrollBonusFactory.getPayrollFixedBonusBuilder()
                .name(command.name)
                .param(command.param)
                .build();
        return new CommandResult(bonus.id, CommandResult.Status.OK,"Fixed bonus created");
    }
}
