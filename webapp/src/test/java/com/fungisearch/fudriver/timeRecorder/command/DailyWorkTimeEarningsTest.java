package com.fungisearch.fudriver.timeRecorder.command;

import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;
import com.fungisearch.fudriver.timeRecorder.command.model.*;
import com.fungisearch.fudriver.timeRecorder.command.repository.TimeWorkLogHibernateRepository;
import com.fungisearch.fudriver.timeRecorder.command.service.DailyWorkTimeEarningsUpdater;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiEntry;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import com.fungisearch.fudriver.timeRecorder.command.service.ScheduledOpenedTimeWorkLogsRemover;
import org.junit.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import java.util.concurrent.TimeUnit;

public class DailyWorkTimeEarningsTest {
    private static LocalContainerEntityManagerFactoryBean bean;
    private EntityManager em;
    private AnnotationConfigApplicationContext events;
    private TimeWorkLogFactory factory;
    private Person person;

    @BeforeClass
    public static void database() {
        bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(new DriverManagerDataSource("jdbc:h2:mem:dailyEarnings;DB_CLOSE_DELAY=-1", "sa", ""));
        bean.setPackagesToScan("com.fungisearch.fudriver");
        bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        bean.setJpaProperties(properties);
        bean.afterPropertiesSet();
    }

    @AfterClass
    public static void closeDatabase() { bean.destroy(); }

    @Before
    public void setup() {
        DateTimeUtils.setCurrentMillisFixed(date("2026-11-14T18:00").getTime());
        em = bean.getObject().createEntityManager();
        em.getTransaction().begin();
        person = new Person();
        ReflectionTestUtils.setField(person, "active", true);
        ReflectionTestUtils.setField(person, "payrollType", PayrollTypeEnum.ACCORD);
        ReflectionTestUtils.setField(person, "isForeigner", false);
        em.persist(person);
        TimeWorkLogHibernateRepository repository = new TimeWorkLogHibernateRepository();
        ReflectionTestUtils.setField(repository, "em", em);
        DailyWorkTimeEarningsUpdater updater = new DailyWorkTimeEarningsUpdater();
        ReflectionTestUtils.setField(updater, "em", em);
        events = new AnnotationConfigApplicationContext();
        events.getBeanFactory().registerSingleton("updater", updater);
        events.refresh();
        factory = new TimeWorkLogFactory(repository, mock(BeanValidator.class), events, null);
    }

    @After
    public void cleanup() {
        DateTimeUtils.setCurrentMillisSystem();
        em.getTransaction().rollback();
        em.close();
        events.close();
    }

    @Test
    public void shouldIncludeBreaksAndRecalculateAfterEditDeleteAndMovingToAnotherDay() {
        TimeWorkLog morning = closed("2026-11-14T08:00", "2026-11-14T10:00");
        TimeWorkLog afternoon = closed("2026-11-14T13:00", "2026-11-14T16:00");
        assertEquals(450, minutes("2026-11-14"));
        assertEquals(1, earnings("2026-11-14").size());
        afternoon.edit(new TimeWorkLog.Edit().startDate(date("2026-11-14T13:00"))
                .endDate(date("2026-11-14T17:00")));
        assertEquals(510, minutes("2026-11-14"));
        afternoon.edit(new TimeWorkLog.Edit().startDate(date("2026-11-15T13:00"))
                .endDate(date("2026-11-15T16:00")));
        assertEquals(120, minutes("2026-11-14"));
        assertEquals(180, minutes("2026-11-15"));
        morning.delete();
        assertEquals(0, minutes("2026-11-14"));
        afternoon.delete();
        assertEquals(0, minutes("2026-11-15"));
    }

    @Test
    public void shouldIgnoreOpenPeriodsUntilTheyAreClosed() {
        TimeWorkLog log = factory.create(person);
        log.register();
        String today = new DateTime().toString("yyyy-MM-dd");
        ReflectionTestUtils.setField(person, "payrollType", PayrollTypeEnum.HOURLY);
        assertTrue(earnings(today).isEmpty());
        log.edit(new TimeWorkLog.Edit().startDate(date(today)));
        assertTrue(earnings(today).isEmpty());
        log.close();
        assertEquals(TimeUnit.MILLISECONDS.toMinutes(log.getEndTime().getTime() - log.getStartTime().getTime()), minutes(today));
        assertEquals(1, earnings(today).size());
        log.delete();
        assertEquals(0, minutes(today));
    }

    @Test
    public void shouldConsolidateLegacyMinutesAndPreserveHarvestData() {
        ZarobkiEntry harvest = earning("2026-11-14", 40);
        harvest.rodzajId = 7L;
        harvest.export = 12.5;
        earning("2026-11-14", 120);
        earning("2026-11-14", 180);
        closed("2026-11-14T08:00", "2026-11-14T10:00");
        closed("2026-11-14T13:00", "2026-11-14T16:00");
        assertEquals(450, minutes("2026-11-14"));
        assertEquals(Double.valueOf(12.5), harvest.export);
        assertEquals(Long.valueOf(7), harvest.rodzajId);
        assertEquals(Long.valueOf(0), harvest.workingMinutes);
        assertEquals(3, earnings("2026-11-14").size());
    }

    @Test(expected = com.fungisearch.fudriver.exception.WorkPeriodDifferentDayException.class)
    public void shouldRejectPeriodsCrossingMidnight() {
        closed("2026-10-31T22:00", "2026-11-01T02:00");
    }

    @Test
    public void shouldUpdateForBothBadgeClosingEndpointsWithoutAppendingPeriodEarnings() {
        ReflectionTestUtils.setField(person, "payrollType", PayrollTypeEnum.HOURLY);
        PersonFactory people = mock(PersonFactory.class);
        when(people.createByRfid("badge")).thenReturn(person);
        AddTimeRecordCommand command = new AddTimeRecordCommand();
        command.rfid = "badge";
        OpenTimeRecordCommandHandler opening = new OpenTimeRecordCommandHandler(factory, null, people);
        opening.handle(command);
        TimeWorkLog first = factory.findOpen(person.getId());
        String today = new DateTime().toString("yyyy-MM-dd");
        first.edit(new TimeWorkLog.Edit().startDate(date(today)));
        new CloseTimeRecordCommandHandler(factory, null, people).handle(command);
        long expected = TimeUnit.MILLISECONDS.toMinutes(first.getEndTime().getTime() - first.getStartTime().getTime());
        assertEquals(expected, minutes(today));
        AddTimeRecordCommandHandler toggle = new AddTimeRecordCommandHandler();
        ReflectionTestUtils.setField(toggle, "timeWorkLogFactory", factory);
        ReflectionTestUtils.setField(toggle, "personFactory", people);
        toggle.handle(command);
        assertEquals(expected, minutes(today));
        toggle.handle(command);
        assertEquals(1, earnings(today).size());
        assertTrue(minutes(today) >= expected);
    }

    @Test
    public void shouldSaveDailyMinutesDuringScheduledClosingAndNotDuplicateThem() {
        DateTimeUtils.setCurrentMillisFixed(date("2026-11-14T23:59:00").getTime());
        closed("2026-11-14T08:00", "2026-11-14T10:00");
        TimeWorkLog open = factory.create(person);
        open.register();
        open.edit(new TimeWorkLog.Edit().startDate(date("2026-11-14T13:00")));
        assertEquals(120, minutes("2026-11-14"));
        ScheduledOpenedTimeWorkLogsRemover scheduler = new ScheduledOpenedTimeWorkLogsRemover();
        ReflectionTestUtils.setField(scheduler, "timeWorkLogFactory", factory);
        scheduler.closeOpenWorkPeriods();
        assertFalse(open.getOpened());
        assertEquals(date("2026-11-14T23:59:00"), open.getEndTime());
        assertEquals(929, minutes("2026-11-14"));
        scheduler.closeOpenWorkPeriods();
        assertEquals(929, minutes("2026-11-14"));
        assertEquals(1, earnings("2026-11-14").size());
    }

    @Test
    public void shouldAssignDelayedScheduledClosingToTheDayTheWorkStarted() {
        DateTimeUtils.setCurrentMillisFixed(date("2026-11-15T00:00:05").getTime());
        TimeWorkLog open = factory.create(person);
        open.register();
        open.edit(new TimeWorkLog.Edit().startDate(date("2026-11-14T22:00")));
        ScheduledOpenedTimeWorkLogsRemover scheduler = new ScheduledOpenedTimeWorkLogsRemover();
        ReflectionTestUtils.setField(scheduler, "timeWorkLogFactory", factory);
        scheduler.closeOpenWorkPeriods();
        assertEquals(date("2026-11-14T23:59:00"), open.getEndTime());
        assertEquals(119, minutes("2026-11-14"));
        assertTrue(earnings("2026-11-15").isEmpty());
    }

    @Test
    public void shouldDeductBreakOnlyAboveThreeAndAHalfHoursForAccord() {
        TimeWorkLog log = closed("2026-11-14T08:00", "2026-11-14T11:30");
        assertEquals(210, minutes("2026-11-14"));
        log.edit(new TimeWorkLog.Edit().startDate(date("2026-11-14T08:00"))
                .endDate(date("2026-11-14T11:31")));
        assertEquals(181, minutes("2026-11-14"));
    }

    @Test
    public void shouldSumHourlyPeriodsAndAddFifteenMinutesOnlyOnce() {
        ReflectionTestUtils.setField(person, "payrollType", PayrollTypeEnum.HOURLY);
        closed("2026-11-14T08:00", "2026-11-14T10:00");
        closed("2026-11-14T11:00", "2026-11-14T12:00");
        assertEquals(180, minutes("2026-11-14"));
        TimeWorkLog third = closed("2026-11-14T13:00", "2026-11-14T14:00");
        assertEquals(255, minutes("2026-11-14"));
        TimeWorkLog fourth = closed("2026-11-14T15:00", "2026-11-14T16:00");
        assertEquals(315, minutes("2026-11-14"));
        third.delete();
        fourth.delete();
        assertEquals(180, minutes("2026-11-14"));
    }

    @Test
    public void shouldPreservePaidMinutesAndNotCreateAnotherEntry() {
        ZarobkiEntry paid = earning("2026-11-14", 90);
        paid.payed = true;
        TimeWorkLog log = closed("2026-11-14T08:00", "2026-11-14T12:00");
        log.edit(new TimeWorkLog.Edit().startDate(date("2026-11-14T08:00"))
                .endDate(date("2026-11-14T13:00")));
        assertEquals(90, minutes("2026-11-14"));
        assertEquals(1, earnings("2026-11-14").size());
        assertTrue(paid.payed);
    }

    @Test
    public void shouldRejectDuplicateContainedContainingAndPartiallyOverlappingPeriods() {
        closed("2026-11-14T10:00", "2026-11-14T12:00");
        String[][] conflicts = {
                {"10:00", "12:00"}, {"10:30", "11:00"}, {"09:00", "13:00"},
                {"09:00", "11:00"}, {"11:00", "13:00"}
        };
        for (String[] conflict : conflicts) {
            expectOverlap(() -> closed("2026-11-14T" + conflict[0], "2026-11-14T" + conflict[1]));
        }
        closed("2026-11-14T08:00", "2026-11-14T10:00");
        closed("2026-11-14T12:00", "2026-11-14T14:00");
        assertEquals(Long.valueOf(3), em.createQuery("select count(t) from TimeWorkLog t", Long.class).getSingleResult());
    }

    @Test
    public void shouldRejectOverlappingEditAndAllowEditingTheSamePeriod() {
        TimeWorkLog first = closed("2026-11-14T08:00", "2026-11-14T10:00");
        TimeWorkLog second = closed("2026-11-14T12:00", "2026-11-14T14:00");
        expectOverlap(() -> second.edit(new TimeWorkLog.Edit().startDate(date("2026-11-14T09:00"))
                .endDate(date("2026-11-14T13:00"))));
        assertEquals(date("2026-11-14T12:00"), second.getStartTime());
        first.edit(new TimeWorkLog.Edit().startDate(date("2026-11-14T08:00"))
                .endDate(date("2026-11-14T11:00")));
        assertEquals(date("2026-11-14T11:00"), first.getEndTime());
    }

    @Test
    public void shouldRejectOverlapWithOpenPeriodAndAllowOtherEmployees() {
        DateTimeUtils.setCurrentMillisFixed(date("2026-11-14T10:00").getTime());
        TimeWorkLog open = factory.create(person);
        open.register();
        open.edit(new TimeWorkLog.Edit().startDate(date("2026-11-14T10:00")));
        closed("2026-11-14T08:00", "2026-11-14T10:00");
        expectOverlap(() -> closed("2026-11-14T11:00", "2026-11-14T12:00"));
        expectOverlap(() -> open.edit(new TimeWorkLog.Edit().startDate(date("2026-11-14T09:00"))));
        Person other = new Person();
        ReflectionTestUtils.setField(other, "active", true);
        ReflectionTestUtils.setField(other, "isForeigner", false);
        em.persist(other);
        factory.getClosedTimeWorkLogBuilder().person(other).startTime(date("2026-11-14T11:00"))
                .endTime(date("2026-11-14T12:00")).build();
    }

    @Test
    public void shouldCloseYesterdayAndOpenTodayOnTheSameBadgeScan() {
        TimeWorkLog yesterday = factory.create(person);
        yesterday.register();
        yesterday.edit(new TimeWorkLog.Edit().startDate(date("2026-11-14T08:00")));
        DateTimeUtils.setCurrentMillisFixed(date("2026-11-15T08:00").getTime());
        TimeWorkLog today = factory.create(person);
        today.register();
        assertFalse(yesterday.getOpened());
        assertEquals(date("2026-11-14T23:59"), yesterday.getEndTime());
        assertTrue(today.getOpened());
        assertNotEquals(yesterday.getId(), today.getId());
        assertEquals(date("2026-11-15T08:00"), today.getStartTime());
        assertEquals(929, minutes("2026-11-14"));
    }

    @Test
    public void shouldClosePastPeriodBeforeTheNextPeriodWhenEditing() {
        TimeWorkLog next = closed("2026-11-14T13:00", "2026-11-14T16:00");
        // Historical inconsistent data, bypassing application overlap validation.
        TimeWorkLog old = new TimeWorkLog(new TimeWorkLogHibernateRepository(), events, mock(BeanValidator.class));
        old.setPerson(person);
        old.setStartTime(date("2026-11-14T08:00"));
        old.setOpened(true);
        em.persist(old);
        em.flush();
        DateTimeUtils.setCurrentMillisFixed(date("2026-11-16T08:00").getTime());
        next.edit(new TimeWorkLog.Edit().startDate(date("2026-11-14T13:00"))
                .endDate(date("2026-11-14T17:00")));
        assertFalse(old.getOpened());
        assertEquals(next.getStartTime(), old.getEndTime());
        assertEquals(510, minutes("2026-11-14"));
    }

    @Test(expected = com.fungisearch.fudriver.exception.WorkPeriodDifferentDayException.class)
    public void shouldRejectEditingEndOntoAnotherDay() {
        TimeWorkLog log = closed("2026-11-14T08:00", "2026-11-14T16:00");
        log.edit(new TimeWorkLog.Edit().startDate(date("2026-11-14T08:00"))
                .endDate(date("2026-11-15T01:00")));
    }

    @Test
    public void shouldClosePastPeriodAtTheStartOfManuallyAddedPeriod() {
        TimeWorkLog old = factory.create(person);
        old.register();
        old.edit(new TimeWorkLog.Edit().startDate(date("2026-11-14T08:00")));
        DateTimeUtils.setCurrentMillisFixed(date("2026-11-16T08:00").getTime());
        TimeWorkLog added = closed("2026-11-14T13:00", "2026-11-14T16:00");
        assertEquals(added.getStartTime(), old.getEndTime());
        assertFalse(old.getOpened());
        assertEquals(450, minutes("2026-11-14"));
    }

    private void expectOverlap(Runnable action) {
        try {
            action.run();
            fail("Overlapping work period should be rejected");
        } catch (com.fungisearch.fudriver.exception.OverlappingWorkPeriodException expected) {
            // The proposed change must fail before the entity is modified.
        }
    }

    private TimeWorkLog closed(String start, String end) {
        return factory.getClosedTimeWorkLogBuilder().person(person).startTime(date(start)).endTime(date(end)).build();
    }

    private Date date(String value) { return DateTime.parse(value).toDate(); }

    private List<ZarobkiEntry> earnings(String day) {
        return em.createQuery("select z from ZarobkiEntry z where z.pickerId = :id and z.harvestTime >= :start and z.harvestTime < :end", ZarobkiEntry.class)
                .setParameter("id", person.getId()).setParameter("start", date(day))
                .setParameter("end", new DateTime(date(day)).plusDays(1).toDate()).getResultList();
    }

    private long minutes(String day) {
        return earnings(day).stream().mapToLong(z -> z.workingMinutes == null ? 0 : z.workingMinutes).sum();
    }

    private ZarobkiEntry earning(String day, long minutes) {
        ZarobkiEntry entry = new ZarobkiEntry();
        entry.pickerId = person.getId();
        entry.harvestTime = date(day);
        entry.timeshort = Long.valueOf(new DateTime(date(day)).toString("yyyyMM"));
        entry.payed = false;
        entry.workingMinutes = minutes;
        em.persist(entry);
        return entry;
    }
}
