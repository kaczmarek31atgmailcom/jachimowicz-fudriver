package com.fungisearch.fudriver.timeRecorder.command.model;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.repository.PersonRepository;
import com.fungisearch.fudriver.timeRecorder.command.repository.TimeWorkLogRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class TimeWorkLogFactory {

    private final TimeWorkLogRepository timeWorkLogRepository;
    private final BeanValidator beanValidator;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PersonRepository personRepository;

    @Autowired
    public TimeWorkLogFactory(TimeWorkLogRepository timeWorkLogRepository, BeanValidator beanValidator, ApplicationEventPublisher applicationEventPublisher, PersonRepository personRepository) {
        this.timeWorkLogRepository = timeWorkLogRepository;
        this.beanValidator = beanValidator;
        this.applicationEventPublisher = applicationEventPublisher;
        this.personRepository = personRepository;
    }

    public TimeWorkLog create(Person person) {
        TimeWorkLog timeWorkLog = null;
        if (person != null) {
            timeWorkLog = timeWorkLogRepository.findOpen(person.getId());
            if (timeWorkLog == null) {
                TimeWorkLog.TimeWorkLogBuilder builder = new TimeWorkLog.TimeWorkLogBuilder(this.timeWorkLogRepository, this.applicationEventPublisher, this.beanValidator);
                timeWorkLog = builder.person(person).build();
            } else {
                timeWorkLog.setBeanValidator(beanValidator);
                timeWorkLog.setTimeWorkLogRepository(timeWorkLogRepository);
                timeWorkLog.applicationEventPublisher = this.applicationEventPublisher;
            }
        }
        return timeWorkLog;
    }

    public TimeWorkLog findOpen(long personId) {
        TimeWorkLog result = timeWorkLogRepository.findOpen(personId);
        if (result != null) {
            result.applicationEventPublisher = applicationEventPublisher;
            result.setTimeWorkLogRepository(timeWorkLogRepository);
            result.setBeanValidator(beanValidator);
        }
        return result;
    }

    public TimeWorkLog find(Long id) {
        TimeWorkLog timeWorkLog = timeWorkLogRepository.find(id);
        timeWorkLog.setBeanValidator(beanValidator);
        timeWorkLog.setTimeWorkLogRepository(timeWorkLogRepository);
        timeWorkLog.applicationEventPublisher = this.applicationEventPublisher;
        return timeWorkLog;
    }

    public List<TimeWorkLog> findAllOpened() {
        List<TimeWorkLog> result = timeWorkLogRepository.findAllOpened();
        if (!result.isEmpty()) {
            for (TimeWorkLog timeWorkLog : result) {
                timeWorkLog.setTimeWorkLogRepository(this.timeWorkLogRepository);
                timeWorkLog.setBeanValidator(this.beanValidator);
                timeWorkLog.applicationEventPublisher = this.applicationEventPublisher;
            }
        }
        return result;
    }


    public List<TimeWorkLog> findAllInMonth(DateTime firstDayOfMonth, Person person) {
        DateTime firstDay = firstDayOfMonth.withDayOfMonth(1);
        DateTime lastDay = firstDay.dayOfMonth().withMaximumValue();
        List<TimeWorkLog> result = timeWorkLogRepository.findForPeriod(DateUtils.getStartOfDay(firstDay.toDate()), DateUtils.getEndOfDay(lastDay.toDate()), person);
        if (!(result.isEmpty())) {
            for (TimeWorkLog timeWorkLog : result) {
                timeWorkLog.applicationEventPublisher = applicationEventPublisher;
                timeWorkLog.setTimeWorkLogRepository(timeWorkLogRepository);
                timeWorkLog.setBeanValidator(beanValidator);
            }
        }
        return result;
    }

    public List<Person> findAllPeopleInMonth(DateTime firstDayOfMonth) {
        DateTime firstDay = firstDayOfMonth.withDayOfMonth(1);
        DateTime lastDay = firstDay.dayOfMonth().withMaximumValue();
        List<Person> result = timeWorkLogRepository.findPeopleInPeriod(firstDay.toDate(), lastDay.toDate());

        if (!(result.isEmpty())) {
            for (Person person : result) {
                person.setPersonRepository(personRepository);
                person.setBeanValidator(beanValidator);
            }
        }
        return result;
    }

    public TimeWorkLog.ClosedTimeWorkLogBuilder getClosedTimeWorkLogBuilder() {
        return new TimeWorkLog.ClosedTimeWorkLogBuilder(timeWorkLogRepository, applicationEventPublisher, beanValidator);
    }

    public Map<Date, TimeWorkLogDay> getMonthDaysSummarized(Date monthDay, Person person) {
        Map<Date, TimeWorkLogDay> result = new HashMap<>();
        List<TimeWorkLog> timeWorkLogs = findAllInMonth(new DateTime(monthDay), person);
        for (TimeWorkLog timeWorkLog : timeWorkLogs) {
            TimeWorkLogDay timeWorkLogDay;
            if (result.containsKey(DateUtils.getStartOfDay(timeWorkLog.getStartTime()))) {
                timeWorkLogDay = result.get(DateUtils.getStartOfDay(timeWorkLog.getStartTime()));
                if (timeWorkLogDay.startTime.after(timeWorkLog.getStartTime())) {
                    timeWorkLogDay.startTime = timeWorkLog.getStartTime();
                }
                if (timeWorkLogDay.endTime.before(timeWorkLog.getEndTime())) {
                    timeWorkLogDay.endTime = timeWorkLog.getEndTime();
                }
            } else {
                timeWorkLogDay = new TimeWorkLogDay();
                timeWorkLogDay.startTime = timeWorkLog.getStartTime();
                timeWorkLogDay.endTime = timeWorkLog.getEndTime();
                timeWorkLogDay.day = DateUtils.getStartOfDay(timeWorkLog.getStartTime());
                result.put(DateUtils.getStartOfDay(timeWorkLog.getStartTime()), timeWorkLogDay);
            }
            timeWorkLogDay.timeWorkLogs.add(timeWorkLog);
        }
        return countTotals(result);
    }

    private Map<Date, TimeWorkLogDay> countTotals(Map<Date, TimeWorkLogDay> input) {
        for (Date key : input.keySet()) {
            input.get(key).workMinutes = getWorkMinutes(input.get(key).timeWorkLogs);
            input.get(key).totalMinutes = getTotalMinutes(input.get(key).timeWorkLogs);
            input.get(key).pauseMinutes = input.get(key).totalMinutes - input.get(key).workMinutes;
        }
        return input;
    }

    private long getWorkMinutes(List<TimeWorkLog> timeWorkLogs) {
        long result = 0;
        for (TimeWorkLog timeWorkLog : timeWorkLogs) {
            long duration = timeWorkLog.getEndTime().getTime() - timeWorkLog.getStartTime().getTime();
            result += TimeUnit.MILLISECONDS.toMinutes(duration);
        }
        return result;
    }

    private long getTotalMinutes(List<TimeWorkLog> timeWorkLogs) {
        Date startTime = null;
        Date endTime = null;
        for (TimeWorkLog timeWorkLog : timeWorkLogs) {
            if (startTime == null) {
                startTime = timeWorkLog.getStartTime();
            }
            if (endTime == null) {
                endTime = timeWorkLog.getEndTime();
            }
            if (timeWorkLog.getStartTime().before(startTime)) {
                startTime = timeWorkLog.getStartTime();
            }
            if (timeWorkLog.getEndTime().after(endTime)) {
                endTime = timeWorkLog.getEndTime();
            }
        }

        long duration = endTime.getTime() - startTime.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(duration);
    }


}
