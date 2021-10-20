package com.fungisearch.fudriver.payroll.salary.command.model;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLogFactory;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.user.query.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
@Transactional
public class CloseMonthCommandHandler {
    private final MonthlyPayoffFactory monthlyPayoffFactory;
    private final UserService userService;
    private final UserFactory userFactory;
    private final PayrollMonthFactory payrollMonthFactory;
    private final TimeWorkLogFactory timeWorkLogFactory;

    @Autowired
    public CloseMonthCommandHandler(MonthlyPayoffFactory monthlyPayoffFactory, UserService userService, UserFactory userFactory, PayrollMonthFactory payrollMonthFactory, TimeWorkLogFactory timeWorkLogFactory) {
        this.monthlyPayoffFactory = monthlyPayoffFactory;
        this.userService = userService;
        this.userFactory = userFactory;
        this.payrollMonthFactory = payrollMonthFactory;
        this.timeWorkLogFactory = timeWorkLogFactory;
    }

    public CommandResult handle(CloseMonthCommand command){

        monthlyPayoffFactory.getHeaderBuilder()
                .creator(userFactory.find(userService.getCurrentUserId()))
                .month(payrollMonthFactory.find(command.monthId))
                .build();
        return new CommandResult(command.monthId, CommandResult.Status.OK,"MonthClosed");
    }
}
