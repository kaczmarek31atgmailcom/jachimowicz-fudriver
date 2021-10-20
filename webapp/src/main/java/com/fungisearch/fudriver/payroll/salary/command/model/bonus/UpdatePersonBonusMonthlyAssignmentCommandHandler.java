package com.fungisearch.fudriver.payroll.salary.command.model.bonus;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.payroll.salary.command.model.PayrollMonthFactory;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UpdatePersonBonusMonthlyAssignmentCommandHandler {

    private final BonusPersonMonthAssignmentFactory bonusPersonMonthAssignmentFactory;
    private final PayrollBonusFactory payrollBonusFactory;
    private final PayrollMonthFactory payrollMonthFactory;
    private final PersonFactory personFactory;

    @Autowired
    public UpdatePersonBonusMonthlyAssignmentCommandHandler(BonusPersonMonthAssignmentFactory bonusPersonMonthAssignmentFactory, PayrollBonusFactory payrollBonusFactory, PayrollMonthFactory payrollMonthFactory, PersonFactory personFactory) {
        this.bonusPersonMonthAssignmentFactory = bonusPersonMonthAssignmentFactory;
        this.payrollBonusFactory = payrollBonusFactory;
        this.payrollMonthFactory = payrollMonthFactory;
        this.personFactory = personFactory;
    }

    public CommandResult handle(UpdatePersonBonusMonthlyAssignmentCommand command) {
        boolean isAssigned = bonusPersonMonthAssignmentFactory.changeBonusAssignment(
                personFactory.find(command.personId),
                payrollBonusFactory.findBonus(command.bonusId),
                payrollMonthFactory.find(command.monthId));
        if (isAssigned) {
            return new CommandResult(command.personId,CommandResult.Status.OK, "BonusAssigned");
        } else {
            return new CommandResult(command.personId,CommandResult.Status.OK, "BonusDeleted");
        }
    }
}
