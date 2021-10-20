package com.fungisearch.fudriver.payroll.salary.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.payroll.Exception.WageNotFoundException;
import com.fungisearch.fudriver.payroll.calendar.command.model.CalendarFactory;
import com.fungisearch.fudriver.payroll.salary.command.model.bonus.BonusPersonMonthAssignmentFactory;
import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollPayoffRepository;
import com.fungisearch.fudriver.payroll.salary.query.service.NotPayedSalaryQueryService;
import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLogFactory;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.user.command.model.User;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiFactory;
import org.springframework.context.ApplicationEventPublisher;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "monthly_payoff_header")
public class MonthlyPayoffHeader extends BaseEntity {

    public transient PayrollPayoffRepository payrollPayoffRepository;
    public transient BeanValidator beanValidator;
    public transient PersonFactory personFactory;
    public transient TypeFactory typeFactory;
    public transient ZarobkiFactory zarobkiFactory;
    public transient WageFactory wageFactory;
    public transient CalendarFactory calendarFactory;
    public transient TimeWorkLogFactory timeWorkLogFactory;
    public transient BonusPersonMonthAssignmentFactory bonusPersonMonthAssignmentFactory;
    public transient PayrollTransactionFactory payrollTransactionFactory;



    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "month_id", nullable = false)
    private PayrollMonth month;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User creator;

    @Temporal(TemporalType.DATE)
    @Column(name = "creation_date")
    private Date date = new Date();

    @OneToMany(targetEntity = MonthlyPayoffDetail.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "header")
    private Set<MonthlyPayoffDetail> details = new HashSet<>();

    private MonthlyPayoffHeader() {
    }

    public MonthlyPayoffHeader(PayrollPayoffRepository payrollPayoffRepository, BeanValidator beanValidator, PersonFactory personFactory, TypeFactory typeFactory, ZarobkiFactory zarobkiFactory, WageFactory wageFactory, CalendarFactory calendarFactory, TimeWorkLogFactory timeWorkLogFactory, BonusPersonMonthAssignmentFactory bonusPersonMonthAssignmentFactory,PayrollTransactionFactory payrollTransactionFactory) {
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
    }

    public Long getId() {
        return id;
    }

    public PayrollMonth getMonth() {
        return month;
    }

    public User getCreator() {
        return creator;
    }

    public Date getDate() {
        return date;
    }

    public Set<MonthlyPayoffDetail> getDetails() {
        return details;
    }

    private void create() {
        beanValidator.validate(this);
        payrollPayoffRepository.save(this);
    }

    private void countSalary(){
        for(MonthlyPayoffDetail detail: details){
            detail.countSalary(this.creator);
        }
    }

    public static class MonthlyPayoffHeaderBuilder {
        private PayrollPayoffRepository payrollPayoffRepository;
        private BeanValidator beanValidator;
        private PersonFactory personFactory;
        private TypeFactory typeFactory;
        private ZarobkiFactory zarobkiFactory;
        private WageFactory wageFactory;
        private CalendarFactory calendarFactory;
        private TimeWorkLogFactory timeWorkLogFactory;
        private BonusPersonMonthAssignmentFactory bonusPersonMonthAssignmentFactory;
        private PayrollTransactionFactory payrollTransactionFactory;
        private ApplicationEventPublisher applicationEventPublisher;
        public final NotPayedSalaryQueryService notPayedSalaryQueryService;
        private PayrollMonth month;
        private User creator;


        public MonthlyPayoffHeaderBuilder(PayrollPayoffRepository payrollPayoffRepository, BeanValidator beanValidator, PersonFactory personFactory, TypeFactory typeFactory, ZarobkiFactory zarobkiFactory, WageFactory wageFactory, CalendarFactory calendarFactory, TimeWorkLogFactory timeWorkLogFactory, BonusPersonMonthAssignmentFactory bonusPersonMonthAssignmentFactory, PayrollTransactionFactory payrollTransactionFactory, ApplicationEventPublisher applicationEventPublisher, NotPayedSalaryQueryService notPayedSalaryQueryService) {
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
            this.applicationEventPublisher = applicationEventPublisher;
            this.notPayedSalaryQueryService = notPayedSalaryQueryService;
        }

        public MonthlyPayoffHeaderBuilder month(PayrollMonth month) {
            this.month = month;
            return this;
        }

        public MonthlyPayoffHeaderBuilder creator(User creator) {
            this.creator = creator;
            return this;
        }

        public MonthlyPayoffHeader build() {
            MonthlyPayoffHeader monthlyPayoffHeader = new MonthlyPayoffHeader(payrollPayoffRepository, beanValidator, personFactory, typeFactory, zarobkiFactory, wageFactory, calendarFactory, timeWorkLogFactory, bonusPersonMonthAssignmentFactory,payrollTransactionFactory);
            monthlyPayoffHeader.month = month;
            monthlyPayoffHeader.creator = creator;
            List<Person> allPeople = personFactory.findAllByMonth(month);
            monthlyPayoffHeader.create();
            for (Person person : allPeople) {
                if((person.getWageHeader() == null) && (person.getPayrollType() == PayrollTypeEnum.ACCORD)){
                    throw new WageNotFoundException(person.getId() + " " + person.getName() + " " + person.getSurname());
                }

                monthlyPayoffHeader.details.add(new MonthlyPayoffDetail.MonthlyPayoffDetailBuilder(payrollPayoffRepository, beanValidator, typeFactory, zarobkiFactory, wageFactory, calendarFactory, personFactory, monthlyPayoffHeader, this.month, timeWorkLogFactory, bonusPersonMonthAssignmentFactory,payrollTransactionFactory,applicationEventPublisher, notPayedSalaryQueryService, personFactory)
                        .person(person)
                        .build());
            }
            monthlyPayoffHeader.countSalary();
            monthlyPayoffHeader.getMonth().close();
            return monthlyPayoffHeader;
        }
    }
}
