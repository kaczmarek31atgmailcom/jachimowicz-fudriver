package com.fungisearch.fudriver.testTools.UT;


import com.fungisearch.fudriver.common.CreateFakeAuthentication;
import com.fungisearch.fudriver.payroll.calendar.command.model.CalendarFactory;
import com.fungisearch.fudriver.payroll.salary.command.model.MonthlyPayoffHeader;
import com.fungisearch.fudriver.payroll.salary.command.model.PayrollMonth;
import com.fungisearch.fudriver.payroll.salary.command.model.PayrollTransactionFactory;
import com.fungisearch.fudriver.payroll.salary.command.model.bonus.BonusPersonMonthAssignmentFactory;
import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollMonthRepository;
import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollPayoffRepository;
import com.fungisearch.fudriver.payroll.salary.query.service.NotPayedSalaryQueryService;
import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLogFactory;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Date;

public class CreateMonthlyPayoffHeaderUT {

    private final PayrollPayoffRepository payrollPayoffRepository;
    private final BeanValidator beanValidator;
    private final PersonFactory personFactory;
    private final TypeFactory typeFactory;
    private final ZarobkiFactory zarobkiFactory;
    private final WageFactory wageFactory;
    private final CalendarFactory calendarFactory;
    private final TimeWorkLogFactory timeWorkLogFactory;
    private final BonusPersonMonthAssignmentFactory bonusPersonMonthAssignmentFactory;
    private final PayrollTransactionFactory payrollTransactionFactory;
    private final PayrollMonthRepository payrollMonthRepository;
    private final UserService userService;
    private final UserFactory userFactory;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final NotPayedSalaryQueryService notPayedSalaryQueryService;

    public CreateMonthlyPayoffHeaderUT(PayrollPayoffRepository payrollPayoffRepository, BeanValidator beanValidator, PersonFactory personFactory, TypeFactory typeFactory, ZarobkiFactory zarobkiFactory, WageFactory wageFactory, CalendarFactory calendarFactory, TimeWorkLogFactory timeWorkLogFactory, BonusPersonMonthAssignmentFactory bonusPersonMonthAssignmentFactory, PayrollTransactionFactory payrollTransactionFactory, PayrollMonthRepository payrollMonthRepository, UserService userService, UserFactory userFactory, ApplicationEventPublisher applicationEventPublisher, NotPayedSalaryQueryService notPayedSalaryQueryService) {
        this.payrollPayoffRepository = payrollPayoffRepository;
        this.beanValidator = beanValidator;
        this.personFactory = personFactory;
        this.typeFactory = typeFactory;
        this.zarobkiFactory = zarobkiFactory;
        this.wageFactory = wageFactory;
        this.calendarFactory = calendarFactory;
        this.timeWorkLogFactory = timeWorkLogFactory;
        this.bonusPersonMonthAssignmentFactory = bonusPersonMonthAssignmentFactory;
        this.payrollTransactionFactory = payrollTransactionFactory;
        this.payrollMonthRepository = payrollMonthRepository;
        this.userService = userService;
        this.userFactory = userFactory;
        this.applicationEventPublisher = applicationEventPublisher;
        this.notPayedSalaryQueryService = notPayedSalaryQueryService;
    }

    @Before
    public void setUp(){
        new CreateFakeAuthentication().authenticatate();
    }

    public MonthlyPayoffHeader create(){
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd");
        Date day = df.parseDateTime("2017-03-01").toDate();
        PayrollMonth month = new PayrollMonth.PayrollMonthBuilder(payrollMonthRepository,beanValidator).firstDay(day).build();
        MonthlyPayoffHeader header = new MonthlyPayoffHeader.MonthlyPayoffHeaderBuilder(payrollPayoffRepository,beanValidator,personFactory,typeFactory,zarobkiFactory,wageFactory,calendarFactory,timeWorkLogFactory,bonusPersonMonthAssignmentFactory,payrollTransactionFactory,applicationEventPublisher,notPayedSalaryQueryService)
                .month(month)
                .creator(userFactory.find(userService.getCurrentUserId()))
                .build();
        return header;
    }
}
