package com.fungisearch.fudriver.payroll.salary.command.model.bonus;

import com.fungisearch.fudriver.common.command.CommandResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class DeleteBonusCommandHandler {
    private final PayrollBonusFactory payrollBonusFactory;

    @Autowired
    public DeleteBonusCommandHandler(PayrollBonusFactory payrollBonusFactory) {
        this.payrollBonusFactory = payrollBonusFactory;
    }

    public CommandResult handle(long bonusId){
        payrollBonusFactory.findBonus(bonusId).inactivate();
        return new CommandResult(bonusId, CommandResult.Status.OK,"BonusRemoved");
    }
}
