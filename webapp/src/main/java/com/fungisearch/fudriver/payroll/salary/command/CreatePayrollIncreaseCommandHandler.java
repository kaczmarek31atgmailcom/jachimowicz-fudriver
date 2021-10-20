package com.fungisearch.fudriver.payroll.salary.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.payroll.salary.command.model.PayrollTransactionFactory;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.user.query.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreatePayrollIncreaseCommandHandler {
    private final PayrollTransactionFactory payrollTransactionFactory;
    private final PersonFactory personFactory;
    private final UserService userService;
    private final UserFactory userFactory;

    @Autowired
    public CreatePayrollIncreaseCommandHandler(PayrollTransactionFactory payrollTransactionFactory, PersonFactory personFactory, UserService userService, UserFactory userFactory) {
        this.payrollTransactionFactory = payrollTransactionFactory;
        this.personFactory = personFactory;
        this.userService = userService;
        this.userFactory = userFactory;
    }

    public CommandResult handle(CreatePayrollAccountUpdateCommand command) {
        payrollTransactionFactory.getIncreaseTransactionBuilder()
                .creator(userFactory.find((userService.getCurrentUserId())))
                .person(personFactory.find(command.personId))
                .amountMoney(command.amount)
                .build();
        return new CommandResult(command.personId, CommandResult.Status.OK, "AccountIncreaseCreated");
    }
}
