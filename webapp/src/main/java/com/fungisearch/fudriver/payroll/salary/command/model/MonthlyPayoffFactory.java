package com.fungisearch.fudriver.payroll.salary.command.model;

import com.fungisearch.fudriver.payroll.calendar.command.model.CalendarFactory;
import com.fungisearch.fudriver.payroll.salary.command.model.bonus.BonusPersonMonthAssignmentFactory;
import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollPayoffRepository;
import com.fungisearch.fudriver.payroll.salary.query.service.NotPayedSalaryQueryService;
import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLogFactory;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class MonthlyPayoffFactory {

    private final PayrollPayoffRepository payrollPayoffRepository;
    private final BeanValidator beanValidator;
    private final ZarobkiFactory zarobkiFactory;
    private final TypeFactory typeFactory;
    private final WageFactory wageFactory;
    private final CalendarFactory calendarFactory;
    private final PersonFactory personFactory;
    private final TimeWorkLogFactory timeWorkLogFactory;
    private final BonusPersonMonthAssignmentFactory bonusPersonMonthAssignmentFactory;
    private final PayrollTransactionFactory payrollTransactionFactory;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final NotPayedSalaryQueryService notPayedSalaryQueryService;

    @Autowired
    public MonthlyPayoffFactory(PayrollPayoffRepository payrollPayoffRepository, BeanValidator beanValidator, ZarobkiFactory zarobkiFactory, TypeFactory typeFactory, WageFactory wageFactory, CalendarFactory calendarFactory, PersonFactory personFactory, TimeWorkLogFactory timeWorkLogFactory, BonusPersonMonthAssignmentFactory bonusPersonMonthAssignmentFactory, PayrollTransactionFactory payrollTransactionFactory, ApplicationEventPublisher applicationEventPublisher, NotPayedSalaryQueryService notPayedSalaryQueryService) {
        this.payrollPayoffRepository = payrollPayoffRepository;
        this.beanValidator = beanValidator;
        this.zarobkiFactory = zarobkiFactory;
        this.typeFactory = typeFactory;
        this.wageFactory = wageFactory;
        this.calendarFactory = calendarFactory;
        this.personFactory = personFactory;
        this.timeWorkLogFactory = timeWorkLogFactory;
        this.bonusPersonMonthAssignmentFactory = bonusPersonMonthAssignmentFactory;
        this.payrollTransactionFactory = payrollTransactionFactory;
        this.applicationEventPublisher = applicationEventPublisher;
        this.notPayedSalaryQueryService = notPayedSalaryQueryService;
    }

    public MonthlyPayoffHeader.MonthlyPayoffHeaderBuilder getHeaderBuilder() {
        return new MonthlyPayoffHeader.MonthlyPayoffHeaderBuilder(payrollPayoffRepository, beanValidator, personFactory, typeFactory,zarobkiFactory,wageFactory,calendarFactory,timeWorkLogFactory,bonusPersonMonthAssignmentFactory,payrollTransactionFactory,applicationEventPublisher, notPayedSalaryQueryService);
    }

}
