package com.fungisearch.fudriver.payroll.salary.command.model;

import com.fungisearch.fudriver.payroll.calendar.command.model.Calendar;
import com.fungisearch.fudriver.payroll.calendar.command.model.CalendarFactory;
import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.calendar.command.repository.CalendarRepository;
import com.fungisearch.fudriver.payroll.salary.command.model.bonus.BonusPersonMonthAssignmentFactory;
import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollMonthRepository;
import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollPayoffRepository;
import com.fungisearch.fudriver.payroll.salary.query.service.NotPayedSalaryQueryService;
import com.fungisearch.fudriver.payroll.wage.command.model.Wage;
import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import com.fungisearch.fudriver.payroll.wage.command.repository.WageRepository;
import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.person.person.command.repository.PersonRepository;
import com.fungisearch.fudriver.testTools.UT.*;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLogFactory;
import com.fungisearch.fudriver.timeRecorder.command.repository.TimeWorkLogRepository;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.type.command.repository.TypeRepository;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiEntry;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiFactory;
import com.fungisearch.fudriver.zarobki.command.repository.ZarobkiRepository;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MonthlyPayoffDetailTest {

    @Mock
    private PayrollPayoffRepository payrollPayoffRepository;
    @Mock
    private BeanValidator beanValidator;
    @Mock
    private TypeFactory typeFactory;
    @Mock
    private ZarobkiFactory zarobkiFactory;
    @Mock
    private WageFactory wageFactory;
    @Mock
    private CalendarFactory calendarFactory;
    @Mock
    private ZarobkiRepository zarobkiRepository;
    @Mock
    private WageRepository wageRepository;
    @Mock
    private TypeRepository typeRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonFactory personFactory;
    @Mock
    private CalendarRepository calendarRepository;
    @Mock
    private TimeWorkLogFactory timeWorkLogFactory;
    @Mock
    private TimeWorkLogRepository timeWorkLogRepository;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @Mock
    private BonusPersonMonthAssignmentFactory bonusPersonMonthAssignmentFactory;
    @Mock
    private PayrollTransactionFactory payrollTransactionFactory;
    @Mock
    private PayrollMonthRepository payrollMonthRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserFactory userFactory;
    @Mock
    private NotPayedSalaryQueryService notPayedSalaryQueryService;


    @Test
    public void shouldCreateHarvestSalaryDetail() {
        //Given
        Type type = new CreateTypeUT(typeRepository, beanValidator, wageFactory).create("test_type", 1.23);
        List<Type> types = new ArrayList<>();
        types.add(type);
        when(typeRepository.findAll()).thenReturn(types);
        WageHeader wageHeader = new CreateWageHeaderUT(wageRepository, beanValidator, typeRepository).create("test_header");
        Wage wage = new CreateWageUT(wageRepository, beanValidator, wageHeader).create(1L, type, SalaryDayTypeEnum.REGULAR_DAY);
        wageHeader.getWages().add(wage);
        Person person = new CreatePersonUT(personRepository, beanValidator)
                .create(PayrollTypeEnum.ACCORD, "test_name", "test_surname");
        person.id = 1L;
        MonthlyPayoffHeader payoffHeader = new CreateMonthlyPayoffHeaderUT(payrollPayoffRepository, beanValidator,personFactory,typeFactory,zarobkiFactory,wageFactory,calendarFactory,timeWorkLogFactory,bonusPersonMonthAssignmentFactory,payrollTransactionFactory,payrollMonthRepository,userService,userFactory, applicationEventPublisher, notPayedSalaryQueryService).create();

        person.setWageHeader(wageHeader);
        PayrollMonth payrollMonth = new CreatePayrollMonthUT(payrollMonthRepository, beanValidator).create(new Date());
        when(personFactory.find(any(Long.class))).thenReturn(person);
        //When
        MonthlyPayoffDetail detail = new MonthlyPayoffDetail.MonthlyPayoffDetailBuilder(payrollPayoffRepository, beanValidator, typeFactory, zarobkiFactory, wageFactory, calendarFactory, personFactory, payoffHeader, payrollMonth,timeWorkLogFactory,bonusPersonMonthAssignmentFactory,payrollTransactionFactory,applicationEventPublisher, notPayedSalaryQueryService, personFactory)
                .person(person)
                .build();
        //Then
        assertEquals(person.getName(), detail.getPersonName());
    }


    public void shouldCountSalaryAmount(){
        //Given
        DateTimeFormatter ft = DateTimeFormat.forPattern("yyyy-MM-dd");
        Date day = ft.parseDateTime("2017-02-02").toDate();
        Type type = new CreateTypeUT(typeRepository, beanValidator, wageFactory).create("test_type", 2);
        List<Type> types = new ArrayList<>();
        types.add(type);
        when(typeRepository.findAll()).thenReturn(types);
        WageHeader wageHeader = new CreateWageHeaderUT(wageRepository, beanValidator, typeRepository).create("test_header");
        for(Wage wage: wageHeader.getWages()){
            wage.updateValue(100L);
        }
        Person person = new CreatePersonUT(personRepository, beanValidator)
                .create(PayrollTypeEnum.ACCORD, "test_name", "test_surname");
        person.id = 1L;
        MonthlyPayoffHeader payoffHeader = new CreateMonthlyPayoffHeaderUT(payrollPayoffRepository, beanValidator,personFactory,typeFactory,zarobkiFactory,wageFactory,calendarFactory,timeWorkLogFactory,bonusPersonMonthAssignmentFactory,payrollTransactionFactory,payrollMonthRepository,userService,userFactory, applicationEventPublisher, notPayedSalaryQueryService).create();
        person.setWageHeader(wageHeader);
        PayrollMonth payrollMonth = new CreatePayrollMonthUT(payrollMonthRepository, beanValidator).create(day);
        when(personFactory.find(any(Long.class))).thenReturn(person);
        ZarobkiEntry zarobki = new CreateZarobkiUT(zarobkiRepository,beanValidator)
                .createExport(type,person,day);
        List<ZarobkiEntry> zarobkiEntries = new ArrayList<>();
        zarobkiEntries.add(zarobki);
        when(zarobkiFactory.findPersonZarobkiInMonth(any(Long.class),any(Long.class))).thenReturn(zarobkiEntries);
        when(typeFactory.findById(any(Long.class))).thenReturn(type);
        Calendar calendar = new CreateCalendarUT(calendarRepository, beanValidator).create(day,SalaryDayTypeEnum.REGULAR_DAY);
        when(calendarFactory.findDay(any(Date.class))).thenReturn(calendar);
        MonthlyPayoffDetail detail = new MonthlyPayoffDetail.MonthlyPayoffDetailBuilder(payrollPayoffRepository, beanValidator, typeFactory, zarobkiFactory, wageFactory, calendarFactory, personFactory, payoffHeader, payrollMonth, timeWorkLogFactory,bonusPersonMonthAssignmentFactory,payrollTransactionFactory,applicationEventPublisher, notPayedSalaryQueryService, personFactory)
                .person(person)
                .build();
        //When
        detail.setHarvestSalaries();
        Set<HarvestSalary> salaries = detail.getHarvestSalaries();
        //Then
        List<HarvestSalary> salariesList = new ArrayList<>(salaries);
        assertEquals(1,salaries.size());
        assertEquals(200L,salariesList.get(0).getAmountMoney());
    }


    public void shouldCountRegularHours(){
        //Given
        DateTimeFormatter ft = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTime startDate = ft.parseDateTime("2017-03-01 10:00");
        DateTime endDate = ft.parseDateTime("2017-03-01 13:00");
        PayrollMonth payrollMonth = new CreatePayrollMonthUT(payrollMonthRepository, beanValidator).create(startDate.toDate());
        Person person = new Person.PersonBuilder(personRepository,beanValidator)
                .id(1L)
                .nr(1L)
                .name("test_person")
                .surname("test_surname")
                .payrollType(PayrollTypeEnum.HOURLY)
                .build();
        person.setRegularWage(10L);
        person.create();
        TimeWorkLog timeWorkLog = new TimeWorkLog.TimeWorkLogBuilder(timeWorkLogRepository,applicationEventPublisher,beanValidator)
                .person(person)
                .startTime(startDate.toDate())
                .build();
        timeWorkLog.register();
        timeWorkLog.edit(new TimeWorkLog.Edit().startDate(startDate.toDate()).endDate(endDate.toDate()));
        when(personFactory.find(any(Long.class))).thenReturn(person);
        List<TimeWorkLog> timeWorkLogs = new ArrayList<>();
        timeWorkLogs.add(timeWorkLog);
        when(timeWorkLogFactory.findAllInMonth(any(DateTime.class),any(Person.class))).thenReturn(timeWorkLogs);
        Calendar calendar = new Calendar.CalendarBuilder(calendarRepository,beanValidator).date(startDate.toDate()).salaryDayType(SalaryDayTypeEnum.REGULAR_DAY).build();
        when(calendarFactory.findDay(any(Date.class))).thenReturn(calendar);
        MonthlyPayoffHeader payoffHeader = new CreateMonthlyPayoffHeaderUT(payrollPayoffRepository, beanValidator,personFactory,typeFactory,zarobkiFactory,wageFactory,calendarFactory,timeWorkLogFactory,bonusPersonMonthAssignmentFactory,payrollTransactionFactory,payrollMonthRepository,userService,userFactory, applicationEventPublisher, notPayedSalaryQueryService).create();
        MonthlyPayoffDetail detail = new MonthlyPayoffDetail.MonthlyPayoffDetailBuilder(payrollPayoffRepository, beanValidator, typeFactory, zarobkiFactory, wageFactory, calendarFactory, personFactory, payoffHeader, payrollMonth, timeWorkLogFactory,bonusPersonMonthAssignmentFactory,payrollTransactionFactory,applicationEventPublisher, notPayedSalaryQueryService, personFactory)
                .person(person)
                .build();
        //When
        detail.setWorkTimeSalaries();
        Set<WorkTimeSalary> workTimeSalaries = detail.getWorkTimeSalaries();
        List<WorkTimeSalary> salaryList = new ArrayList<>(workTimeSalaries);
        //Then;
        assertEquals(180,salaryList.get(0).getRegularMinutes());
        assertEquals(0,salaryList.get(0).getSundayMinutes());
        assertEquals(0,salaryList.get(0).getBonusMinutes());
    }


    public void shouldCountSundayHours(){
        //Given
        DateTimeFormatter ft = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTime startDate = ft.parseDateTime("2017-03-01 10:00");
        DateTime endDate = ft.parseDateTime("2017-03-01 13:00");
        PayrollMonth payrollMonth = new CreatePayrollMonthUT(payrollMonthRepository, beanValidator).create(startDate.toDate());
        Calendar calendar = new Calendar.CalendarBuilder(calendarRepository,beanValidator).date(startDate.toDate()).salaryDayType(SalaryDayTypeEnum.SUNDAY).build();
        when(calendarFactory.findDay(any(Date.class))).thenReturn(calendar);
        Person person = new Person.PersonBuilder(personRepository,beanValidator)
                .id(1L)
                .nr(1L)
                .name("test_person")
                .surname("test_surname")
                .payrollType(PayrollTypeEnum.HOURLY)
                .build();
        person.setRegularWage(10L);
        person.create();
        TimeWorkLog timeWorkLog = new TimeWorkLog.TimeWorkLogBuilder(timeWorkLogRepository,applicationEventPublisher,beanValidator)
                .person(person)
                .startTime(startDate.toDate())
                .build();
        timeWorkLog.register();
        timeWorkLog.edit(new TimeWorkLog.Edit().startDate(startDate.toDate()).endDate(endDate.toDate()));
        when(personFactory.find(any(Long.class))).thenReturn(person);
        List<TimeWorkLog> timeWorkLogs = new ArrayList<>();
        timeWorkLogs.add(timeWorkLog);
        when(timeWorkLogFactory.findAllInMonth(any(DateTime.class),any(Person.class))).thenReturn(timeWorkLogs);

        MonthlyPayoffHeader payoffHeader = new CreateMonthlyPayoffHeaderUT(payrollPayoffRepository, beanValidator,personFactory,typeFactory,zarobkiFactory,wageFactory,calendarFactory,timeWorkLogFactory,bonusPersonMonthAssignmentFactory,payrollTransactionFactory,payrollMonthRepository,userService,userFactory, applicationEventPublisher, notPayedSalaryQueryService).create();
        MonthlyPayoffDetail detail = new MonthlyPayoffDetail.MonthlyPayoffDetailBuilder(payrollPayoffRepository, beanValidator, typeFactory, zarobkiFactory, wageFactory, calendarFactory, personFactory, payoffHeader, payrollMonth, timeWorkLogFactory,bonusPersonMonthAssignmentFactory,payrollTransactionFactory,applicationEventPublisher, notPayedSalaryQueryService, personFactory)
                .person(person)
                .build();
        //When
        detail.setWorkTimeSalaries();
        Set<WorkTimeSalary> workTimeSalaries = detail.getWorkTimeSalaries();
        List<WorkTimeSalary> salaryList = new ArrayList<>(workTimeSalaries);
        //Then;
        assertEquals(180,salaryList.get(0).getSundayMinutes());
        assertEquals(0,salaryList.get(0).getRegularMinutes());
        assertEquals(0,salaryList.get(0).getBonusMinutes());
    }


    public void shouldCountBonusHours(){
        //Given
        DateTimeFormatter ft = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTime startDate = ft.parseDateTime("2017-03-01 10:00");
        DateTime endDate = ft.parseDateTime("2017-03-01 13:00");
        PayrollMonth payrollMonth = new CreatePayrollMonthUT(payrollMonthRepository, beanValidator).create(startDate.toDate());
        Person person = new Person.PersonBuilder(personRepository,beanValidator)
                .id(1L)
                .nr(1L)
                .name("test_person")
                .surname("test_surname")
                .payrollType(PayrollTypeEnum.HOURLY)
                .build();
        person.setRegularWage(10L);
        person.create();
        TimeWorkLog timeWorkLog = new TimeWorkLog.TimeWorkLogBuilder(timeWorkLogRepository,applicationEventPublisher,beanValidator)
                .person(person)
                .startTime(startDate.toDate())
                .build();
        timeWorkLog.register();
        timeWorkLog.edit(new TimeWorkLog.Edit().startDate(startDate.toDate()).endDate(endDate.toDate()));
        when(personFactory.find(any(Long.class))).thenReturn(person);
        List<TimeWorkLog> timeWorkLogs = new ArrayList<>();
        timeWorkLogs.add(timeWorkLog);
        when(timeWorkLogFactory.findAllInMonth(any(DateTime.class),any(Person.class))).thenReturn(timeWorkLogs);
        Calendar calendar = new Calendar.CalendarBuilder(calendarRepository,beanValidator).date(startDate.toDate()).salaryDayType(SalaryDayTypeEnum.BONUS_DAY).build();
        when(calendarFactory.findDay(any(Date.class))).thenReturn(calendar);
        MonthlyPayoffHeader payoffHeader = new CreateMonthlyPayoffHeaderUT(payrollPayoffRepository, beanValidator,personFactory,typeFactory,zarobkiFactory,wageFactory,calendarFactory,timeWorkLogFactory,bonusPersonMonthAssignmentFactory,payrollTransactionFactory,payrollMonthRepository,userService,userFactory, applicationEventPublisher, notPayedSalaryQueryService).create();
        MonthlyPayoffDetail detail = new MonthlyPayoffDetail.MonthlyPayoffDetailBuilder(payrollPayoffRepository, beanValidator, typeFactory, zarobkiFactory, wageFactory, calendarFactory, personFactory, payoffHeader, payrollMonth, timeWorkLogFactory,bonusPersonMonthAssignmentFactory,payrollTransactionFactory,applicationEventPublisher, notPayedSalaryQueryService, personFactory)
                .person(person)
                .build();
        //When
        detail.setWorkTimeSalaries();
        Set<WorkTimeSalary> workTimeSalaries = detail.getWorkTimeSalaries();
        List<WorkTimeSalary> salaryList = new ArrayList<>(workTimeSalaries);
        //Then;
        assertEquals(180,salaryList.get(0).getBonusMinutes());
        assertEquals(0,salaryList.get(0).getRegularMinutes());
        assertEquals(0,salaryList.get(0).getSundayMinutes());
    }

}
