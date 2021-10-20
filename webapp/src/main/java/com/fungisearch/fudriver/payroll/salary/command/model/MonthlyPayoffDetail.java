package com.fungisearch.fudriver.payroll.salary.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.common.event.CustomApplicationEvent;
import com.fungisearch.fudriver.common.event.EventTypeEnum;
import com.fungisearch.fudriver.payroll.calendar.command.model.Calendar;
import com.fungisearch.fudriver.payroll.calendar.command.model.CalendarFactory;
import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.salary.command.model.bonus.BonusPersonMonthAssignmentFactory;
import com.fungisearch.fudriver.payroll.salary.command.model.bonus.PayrollBonus;
import com.fungisearch.fudriver.payroll.salary.command.model.bonus.PersonMonthTotals;
import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollPayoffRepository;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.PersonSalaryHeaderDto;
import com.fungisearch.fudriver.payroll.salary.query.service.NotPayedSalaryQueryService;
import com.fungisearch.fudriver.payroll.wage.command.model.Wage;
import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLogDay;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLogFactory;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.user.command.model.User;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiEntry;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiFactory;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "monthly_payoff_detail")
public class MonthlyPayoffDetail extends BaseEntity {

    public transient PayrollPayoffRepository payrollPayoffRepository;
    public transient BeanValidator beanValidator;
    public transient TypeFactory typeFactory;
    public transient ZarobkiFactory zarobkiFactory;
    public transient WageFactory wageFactory;
    public transient CalendarFactory calendarFactory;
    public transient TimeWorkLogFactory timeWorkLogFactory;
    public transient BonusPersonMonthAssignmentFactory bonusPersonMonthAssignmentFactory;
    public transient PayrollTransactionFactory payrollTransactionFactory;
    public transient ApplicationEventPublisher applicationEventPublisher;
    public transient NotPayedSalaryQueryService notPayedSalaryQueryService;
    public transient PersonFactory personFactory;

    public final transient Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "header_id")
    private MonthlyPayoffHeader header;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "person_name")
    private String personName;

    @Column(name = "person_surname")
    private String personSurname;

    @ManyToOne
    @JoinColumn(name = "month_id")
    private PayrollMonth month;

    @Column(name = "amount")
    private long amount;

    @Column(name = "payroll_type")
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private PayrollTypeEnum payrollType;


    @OneToMany(targetEntity = HarvestSalary.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "monthlyPayoffDetail")
    private Set<HarvestSalary> harvestSalaries;

    @OneToMany(targetEntity = WorkTimeSalary.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "monthlyPayoffDetail")
    private Set<WorkTimeSalary> workTimeSalaries;

    @OneToMany(targetEntity = BonusSalary.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "monthlyPayoffDetail")
    private Set<BonusSalary> bonusSalaries;


    public Long getId() {
        return id;
    }

    public MonthlyPayoffHeader getHeader() {
        return header;
    }

    public Person getPerson() {
        return person;
    }

    public String getPersonName() {
        return personName;
    }

    public String getPersonSurname() {
        return personSurname;
    }

    public PayrollMonth getMonth() {
        return month;
    }

    public long getAmount() {
        return amount;
    }

    public Set<HarvestSalary> getHarvestSalaries() {
        return harvestSalaries;
    }

    public Set<WorkTimeSalary> getWorkTimeSalaries() {
        return workTimeSalaries;
    }

    public Set<BonusSalary> getBonusSalaries() {
        return bonusSalaries;
    }

    public PayrollTypeEnum getPayrollType() {
        return payrollType;
    }

    private MonthlyPayoffDetail() {
    }

    public MonthlyPayoffDetail(PayrollPayoffRepository payrollPayoffRepository, BeanValidator beanValidator, TypeFactory typeFactory, ZarobkiFactory zarobkiFactory, WageFactory wageFactory, CalendarFactory calendarFactory, TimeWorkLogFactory timeWorkLogFactory, BonusPersonMonthAssignmentFactory bonusPersonMonthAssignmentFactory, PayrollTransactionFactory payrollTransactionFactory, ApplicationEventPublisher applicationEventPublisher, NotPayedSalaryQueryService notPayedSalaryQueryService,PersonFactory personFactory) {
        this.payrollPayoffRepository = payrollPayoffRepository;
        this.beanValidator = beanValidator;
        this.typeFactory = typeFactory;
        this.zarobkiFactory = zarobkiFactory;
        this.wageFactory = wageFactory;
        this.calendarFactory = calendarFactory;
        this.timeWorkLogFactory = timeWorkLogFactory;
        this.bonusPersonMonthAssignmentFactory = bonusPersonMonthAssignmentFactory;
        this.payrollTransactionFactory = payrollTransactionFactory;
        this.applicationEventPublisher = applicationEventPublisher;
        this.notPayedSalaryQueryService = notPayedSalaryQueryService;
        this.personFactory = personFactory;
    }

    public void countSalary(User user) {
        long salary = 0L;
        switch (person.getPayrollType()) {
            case ACCORD:
                setHarvestSalaries();
                for (HarvestSalary harvestSalary : harvestSalaries) {
                    salary += harvestSalary.getAmountMoney();
                }
                break;
            case HOURLY:
                setWorkTimeSalaries();
                salary += countWorkTimeSalary();
                break;
            default:
                throw new IllegalStateException("Invalid person payroll type " + person.getId());
        }
        setBonusSalaries();
        for (BonusSalary bonusSalary : bonusSalaries) {
            salary += bonusSalary.getAmountMoney();
        }
        this.amount = salary;
        payrollTransactionFactory.getIncreaseTransactionBuilder()
                .creator(user)
                .person(this.person)
                .amountMoney(salary)
                .build();
    }


    private long countWorkTimeSalary() {
        long salary = 0;
        Map<Date, TimeWorkLogDay> workDays = timeWorkLogFactory.getMonthDaysSummarized(month.firstDay, person);
        for (Date key : workDays.keySet()) {
            Calendar day = calendarFactory.findDay(key);
            switch (day.getSalaryDayType()) {
                case REGULAR_DAY:
                    salary += (workDays.get(key).totalMinutes * person.getRegularWage()) / 60;
                    break;
                case SUNDAY:
                    salary += (workDays.get(key).totalMinutes * person.getSundayWage()) / 60;
                    break;
                case BONUS_DAY:
                    salary += (workDays.get(key).totalMinutes * person.getBonusWage()) / 60;
                    break;
                default:
                    throw new IllegalStateException("Incorrect salary day type");
            }
        }
        return salary;
    }

    public void setHarvestSalaries() {
        Map<Long, Type> types = new HashMap<>();
        List<Type> typeList = typeFactory.findAll();
        for (Type type : typeList) {
            types.put(type.getId(), type);
        }
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMM");
        DateTime dt = new DateTime(month.firstDay);
        List<ZarobkiEntry> zarobki = zarobkiFactory.findPersonZarobkiInMonth(person.getId(), Long.valueOf(formatter.print(dt)));
        if (!(this.harvestSalaries.isEmpty())) {
            throw new IllegalStateException("Harvest Salaries Already Exist");
        }
        Map<String, ZarobkiAgregator> agregators = new HashMap<>();
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyyMMdd");
        Map<DateTime, Calendar> monthDays = calendarFactory.findMonth(month.firstDay);
        for (ZarobkiEntry zarobkiEntry : zarobki) {
            String key = zarobkiEntry.rodzajId.toString() + monthDays.get(new DateTime(zarobkiEntry.getHarvestTime())).getSalaryDayType();
            ZarobkiAgregator agregator;
            if (agregators.containsKey(key)) {
                agregator = agregators.get(key);
                agregator.amountG += (new Double(zarobkiEntry.ilosc * 1000)).longValue();
            } else {
                agregator = new ZarobkiAgregator();
                agregator.amountG = (new Double(zarobkiEntry.ilosc * 1000)).longValue();
                agregator.typeId = zarobkiEntry.rodzajId;
                agregator.dayType = monthDays.get(new DateTime(zarobkiEntry.getHarvestTime())).getSalaryDayType();
                agregators.put(key, agregator);
            }
            zarobkiEntry.setPayed();
        }
        List<ZarobkiAgregator> agregatorsList = new ArrayList<>(agregators.values());

        for (ZarobkiAgregator agregator : agregatorsList) {
            Wage wg = findWage(person, types.get(agregator.typeId), agregator.dayType);
            this.harvestSalaries.add(
                    new HarvestSalary.HarvestSalaryBuilder(payrollPayoffRepository, beanValidator)
                            .month(month)
                            .person(person)
                            .type(types.get(agregator.typeId))
                            .wage(findWage(person, types.get(agregator.typeId), agregator.dayType))
                            .salaryDayType(agregator.dayType)
                            .amountG(agregator.amountG)
                            .monthlyPayoffDetail(this)
                            .build()
            );
        }
    }


    public void setWorkTimeSalaries(){
        List<PersonSalaryHeaderDto> hoursDtoList = notPayedSalaryQueryService.getHeaders(month.firstDay);
        for(PersonSalaryHeaderDto dto: hoursDtoList){
            if(dto.payrollType.equals(PayrollTypeEnum.HOURLY)){
                Person person = personFactory.find(dto.id);
                if(dto.regularMinutes > 0){
                    this.workTimeSalaries.add(new WorkTimeSalary.WorkTimeSalaryBuilder(payrollPayoffRepository, beanValidator)
                            .person(person)
                            .minutes(dto.regularMinutes)
                            .monthlyPayoffDetail(this)
                            .payrollMonth(month)
                            .salaryDayType(SalaryDayTypeEnum.REGULAR_DAY)
                            .build());
                }
                if(dto.sundayMinutes > 0){
                    this.workTimeSalaries.add(new WorkTimeSalary.WorkTimeSalaryBuilder(payrollPayoffRepository, beanValidator)
                            .person(person)
                            .minutes(dto.sundayMinutes)
                            .monthlyPayoffDetail(this)
                            .payrollMonth(month)
                            .salaryDayType(SalaryDayTypeEnum.SUNDAY)
                            .build());
                }
                if(dto.bonusMinutes > 0){
                    this.workTimeSalaries.add(new WorkTimeSalary.WorkTimeSalaryBuilder(payrollPayoffRepository, beanValidator)
                            .person(person)
                            .minutes(dto.bonusMinutes)
                            .monthlyPayoffDetail(this)
                            .payrollMonth(month)
                            .salaryDayType(SalaryDayTypeEnum.BONUS_DAY)
                            .build());
                }
            }
        }

    }


    public void setWorkTimeSalaries_Back() {
        DateTime firstDayOfMonth = new DateTime(month.firstDay).millisOfDay().withMinimumValue();
        List<TimeWorkLog> monthlyWorkTime = timeWorkLogFactory.findAllInMonth(firstDayOfMonth, this.person);
        Map<DateTime, Calendar> monthDays = calendarFactory.findMonth(month.firstDay);
        Map<SalaryDayTypeEnum, Long> workTimeMap = new HashMap<>();
        //żle wylicza sumę czasu pracy
        for (TimeWorkLog timeWorkLog : monthlyWorkTime) {
            if(timeWorkLog.getEndTime() == null || timeWorkLog.getStartTime() == null){
                CustomApplicationEvent event = new CustomApplicationEvent(EventTypeEnum.NULL_WORK_DATE, person.id);
                applicationEventPublisher.publishEvent(event);
            }
            if (!(DateUtils.isSameDay(timeWorkLog.getStartTime(), timeWorkLog.getEndTime()))) {
                timeWorkLog.setStartTime(com.fungisearch.fudriver.common.DateUtils.getStartOfDay(timeWorkLog.getStartTime()));
            }
//            timeWorkLog.setStartTime((new DateTime(timeWorkLog.getEndTime()).millisOfDay().withMinimumValue()).toDate());
            SalaryDayTypeEnum dayType = monthDays.get(new DateTime(timeWorkLog.getStartTime()).millisOfDay().withMinimumValue()).getSalaryDayType();
            long minutes = 0;
            if (timeWorkLog.getEndTime() != null) {
                minutes = new Long(Minutes.minutesBetween(new DateTime(timeWorkLog.getStartTime()), new DateTime(timeWorkLog.getEndTime())).getMinutes());
            }
            if (minutes > 0) {
                if (workTimeMap.containsKey(dayType)) {
                    workTimeMap.put(dayType, workTimeMap.get(dayType) + minutes);
                } else {
                    workTimeMap.put(dayType, minutes);
                }
            }

        }
        for (Map.Entry<SalaryDayTypeEnum, Long> entry : workTimeMap.entrySet()) {
            this.workTimeSalaries.add(new WorkTimeSalary.WorkTimeSalaryBuilder(payrollPayoffRepository, beanValidator)
                    .person(person)
                    .minutes(entry.getValue())
                    .monthlyPayoffDetail(this)
                    .payrollMonth(month)
                    .salaryDayType(entry.getKey())
                    .build());
        }
    }

    public void setBonusSalaries() {
        List<PayrollBonus> bonuses = bonusPersonMonthAssignmentFactory.findAssignedBonusesForPersonInMonth(this.person, this.getMonth());
        for (PayrollBonus bonus : bonuses) {
            this.bonusSalaries.add(new BonusSalary.BonusSalaryBuilder(payrollPayoffRepository, beanValidator)
                    .bonus(bonus)
                    .payoffDetail(this)
                    .person(this.person)
                    .month(this.getMonth())
                    .amountMoney(bonus.getValue(getPersonMonthTotals()))
                    .build());
        }
    }

    private PersonMonthTotals getPersonMonthTotals() {
        PersonMonthTotals totals = new PersonMonthTotals();
        totals.payrollType = this.person.getPayrollType();
        if (totals.payrollType.equals(PayrollTypeEnum.ACCORD)) {
            for (HarvestSalary harvestSalary : this.getHarvestSalaries()) {
                totals.harvestSalary += harvestSalary.getAmountMoney();
            }
        }
        if (totals.payrollType.equals(PayrollTypeEnum.HOURLY)) {
            totals.hoursSalary = countWorkTimeSalary();
        }
        return totals;
    }

    private Wage findWage(Person person, Type type, SalaryDayTypeEnum dayType) {
        return person.getWageHeader()
                .getWages().stream()
                .filter(w -> w.getType().getId().equals(type.getId()))
                .filter(w -> w.getDayType().equals(dayType))
                .collect(Collectors.toList())
                .get(0);

    }

    private void create() {
        beanValidator.validate(this);
        payrollPayoffRepository.save(this);
    }

    public static class MonthlyPayoffDetailBuilder {
        private PayrollPayoffRepository payrollPayoffRepository;
        private BeanValidator beanValidator;
        private TypeFactory typeFactory;
        private ZarobkiFactory zarobkiFactory;
        private WageFactory wageFactory;
        private CalendarFactory calendarFactory;
        private TimeWorkLogFactory timeWorkLogFactory;
        private BonusPersonMonthAssignmentFactory bonusPersonMonthAssignmentFactory;
        private PayrollTransactionFactory payrollTransactionFactory;
        private ApplicationEventPublisher applicationEventPublisher;
        private MonthlyPayoffHeader header;
        private PayrollMonth payrollMonth;
        private final NotPayedSalaryQueryService notPayedSalaryQueryService;
        private final PersonFactory personFactory;
        private Person person;


        public MonthlyPayoffDetailBuilder(PayrollPayoffRepository payrollPayoffRepository, BeanValidator beanValidator, TypeFactory typeFactory, ZarobkiFactory zarobkiFactory, WageFactory wageFactory, CalendarFactory calendarFactory, PersonFactory personFactory, MonthlyPayoffHeader header, PayrollMonth payrollMonth, TimeWorkLogFactory timeWorkLogFactory, BonusPersonMonthAssignmentFactory bonusPersonMonthAssignmentFactory, PayrollTransactionFactory payrollTransactionFactory, ApplicationEventPublisher applicationEventPublisher, NotPayedSalaryQueryService notPayedSalaryQueryService, PersonFactory personFactory1) {
            this.payrollPayoffRepository = payrollPayoffRepository;
            this.beanValidator = beanValidator;
            this.typeFactory = typeFactory;
            this.zarobkiFactory = zarobkiFactory;
            this.wageFactory = wageFactory;
            this.calendarFactory = calendarFactory;
            this.timeWorkLogFactory = timeWorkLogFactory;
            this.bonusPersonMonthAssignmentFactory = bonusPersonMonthAssignmentFactory;
            this.payrollTransactionFactory = payrollTransactionFactory;
            this.applicationEventPublisher = applicationEventPublisher;
            this.header = header;
            this.payrollMonth = payrollMonth;
            this.notPayedSalaryQueryService = notPayedSalaryQueryService;
            this.personFactory = personFactory1;
        }

        public MonthlyPayoffDetailBuilder person(Person person) {
            this.person = person;
            return this;
        }

        public MonthlyPayoffDetail build() {
            MonthlyPayoffDetail monthlyPayoffDetail = new MonthlyPayoffDetail(payrollPayoffRepository,beanValidator,typeFactory,zarobkiFactory,wageFactory,calendarFactory,timeWorkLogFactory,bonusPersonMonthAssignmentFactory,payrollTransactionFactory,applicationEventPublisher,notPayedSalaryQueryService,personFactory);
            monthlyPayoffDetail.person = person;
            monthlyPayoffDetail.month = payrollMonth;
            monthlyPayoffDetail.harvestSalaries = new HashSet<>();
            monthlyPayoffDetail.workTimeSalaries = new HashSet<>();
            monthlyPayoffDetail.bonusSalaries = new HashSet<>();
            monthlyPayoffDetail.amount = 0;
            monthlyPayoffDetail.header = header;
            monthlyPayoffDetail.personName = person.getName();
            monthlyPayoffDetail.personSurname = person.getSurname();
            monthlyPayoffDetail.payrollType = person.getPayrollType();
            if(monthlyPayoffDetail.payrollType == null){
                CustomApplicationEvent event = new CustomApplicationEvent(EventTypeEnum.NULL_PAYROLL_TYPE, person.id);
                applicationEventPublisher.publishEvent(event);
            }
            monthlyPayoffDetail.create();

            return monthlyPayoffDetail;
        }
    }


    private class ZarobkiAgregator {
        public long typeId;
        public long amountG;
        public SalaryDayTypeEnum dayType;
    }


}



