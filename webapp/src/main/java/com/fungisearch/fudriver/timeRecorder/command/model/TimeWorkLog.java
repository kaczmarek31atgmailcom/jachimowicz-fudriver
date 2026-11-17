package com.fungisearch.fudriver.timeRecorder.command.model;

import com.fungisearch.fudriver.common.event.CustomApplicationEvent;
import com.fungisearch.fudriver.common.event.EventTypeEnum;
import com.fungisearch.fudriver.exception.StartDateAfterEndDateException;
import com.fungisearch.fudriver.exception.OverlappingWorkPeriodException;
import com.fungisearch.fudriver.exception.WorkPeriodDifferentDayException;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.timeRecorder.command.repository.TimeWorkLogRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@SuppressWarnings("JpaObjectClassSignatureInspection")
@Entity
@Table(name = "time_work_log")
public class TimeWorkLog {

    private transient TimeWorkLogRepository timeWorkLogRepository;
    private transient BeanValidator beanValidator;
    public transient ApplicationEventPublisher applicationEventPublisher;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Column(name = "start_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
    private Date startTime;

    @Column(name = "end_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
    private Date endTime;

    @Column(name = "isOpened")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isOpened;

    public Long getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Boolean getOpened() {
        return isOpened;
    }

    public void setOpened(Boolean opened) {
        isOpened = opened;
    }

    private TimeWorkLog() {
    }

    public void setTimeWorkLogRepository(TimeWorkLogRepository timeWorkLogRepository) {
        this.timeWorkLogRepository = timeWorkLogRepository;
    }

    public void setBeanValidator(BeanValidator beanValidator) {
        this.beanValidator = beanValidator;
    }


    public TimeWorkLog(TimeWorkLogRepository repository, ApplicationEventPublisher applicationEventPublisher, BeanValidator beanValidator) {
        this.timeWorkLogRepository = repository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.beanValidator = beanValidator;
    }

    public void close() {
        if (!this.isOpened) {
            throw new IllegalStateException("Can not close work period that is not opened " + this.id);
        }
        if (this.endTime != null) {
            throw new IllegalStateException("Can not close work period that has already set end time " + this.id);
        }
        closePeriod();
    }

    private void closePeriod() {
        timeWorkLogRepository.lockPerson(person.getId());
        Date previousEnd = endTime;
        DateTime now = new DateTime();
        if (new DateTime(startTime).toLocalDate().isBefore(now.toLocalDate())) {
            closePastPeriod();
            return;
        }
        Date closingTime = now.toDate();
        validatePeriod(startTime, closingTime);
        this.endTime = closingTime;
        this.isOpened = false;
        beanValidator.validate(this);
        applicationEventPublisher.publishEvent(new CustomApplicationEvent(EventTypeEnum.EMPLOYEE_WORK_ENDED, person.getId()));
        workTimeChanged(startTime, previousEnd, startTime, endTime);
    }

    public void closePastPeriod() {
        closePastPeriod(null);
    }

    void closePastPeriod(Date proposedNextStart) {
        timeWorkLogRepository.lockPerson(person.getId());
        DateTime day = new DateTime(startTime);
        if (!Boolean.TRUE.equals(isOpened) || !day.toLocalDate().isBefore(new DateTime().toLocalDate())) return;
        Date next = timeWorkLogRepository.findNextStart(person.getId(), id, startTime,
                day.plusDays(1).withTimeAtStartOfDay().toDate());
        if (proposedNextStart != null && !proposedNextStart.before(startTime)
                && new DateTime(proposedNextStart).toLocalDate().equals(day.toLocalDate())
                && (next == null || proposedNextStart.before(next))) next = proposedNextStart;
        Date closing = day.withTime(23, 59, 0, 0).toDate();
        // A start after 23:59 must never produce a negative period.
        if (closing.before(startTime)) closing = startTime;
        if (next != null && next.before(closing)) closing = next;
        Date previousEnd = endTime;
        endTime = closing;
        isOpened = false;
        beanValidator.validate(this);
        workTimeChanged(startTime, previousEnd, startTime, endTime);
    }

    public void register() {
        timeWorkLogRepository.lockPerson(person.getId());
        if (this.startTime == null) {
            Date openingTime = new DateTime().toDate();
            validatePeriod(openingTime, null);
            this.startTime = openingTime;
            this.isOpened = true;
            beanValidator.validate(this);
            timeWorkLogRepository.save(this);
            CustomApplicationEvent event = new CustomApplicationEvent(EventTypeEnum.EMPLOYEE_WORK_STARTED, this.person.getId());
            applicationEventPublisher.publishEvent(event);
        } else {
            closePeriod();
        }
    }

    private void create() {
        PastWorkPeriodCloser.close(person.getId(), timeWorkLogRepository, applicationEventPublisher, beanValidator, id, startTime);
        timeWorkLogRepository.lockPerson(person.getId());
        validatePeriod(startTime, endTime);
        beanValidator.validate(this);
        timeWorkLogRepository.save(this);
        workTimeChanged(null, null, startTime, endTime);
    }

    public void delete() {
        timeWorkLogRepository.lockPerson(person.getId());
        if (this.isOpened) {
            this.isOpened = false;
            CustomApplicationEvent event = new CustomApplicationEvent(EventTypeEnum.EMPLOYEE_WORK_ENDED, this.person.getId());
            applicationEventPublisher.publishEvent(event);
        }
        beanValidator.validate(this);
        timeWorkLogRepository.delete(this);
        workTimeChanged(startTime, endTime, null, null);
    }

    public void edit(Edit edit) {
        PastWorkPeriodCloser.close(person.getId(), timeWorkLogRepository, applicationEventPublisher, beanValidator, id, edit.startDate);
        timeWorkLogRepository.lockPerson(person.getId());
        validatePeriod(edit.startDate, edit.endDate == null ? endTime : edit.endDate);
        Date previousStart = startTime;
        Date previousEnd = endTime;
        if (edit.endDate != null) {
            if (edit.startDate.compareTo(edit.endDate) > 0) {
                throw new StartDateAfterEndDateException();
            }
            this.endTime = edit.endDate;
            this.isOpened = false;
        }
        this.startTime = edit.startDate;
        beanValidator.validate(this);
        if (!isOpened) workTimeChanged(previousStart, previousEnd, startTime, endTime);
    }

    private void validatePeriod(Date start, Date end) {
        if (start == null) throw new IllegalArgumentException("Work period start is required");
        if (end != null && start.after(end)) throw new StartDateAfterEndDateException();
        if (end != null && !new DateTime(start).toLocalDate().equals(new DateTime(end).toLocalDate())) {
            throw new WorkPeriodDifferentDayException();
        }
        if (timeWorkLogRepository.hasOverlap(person.getId(), id, start, end)) {
            throw new OverlappingWorkPeriodException();
        }
    }

    private void workTimeChanged(Date previousStart, Date previousEnd, Date start, Date end) {
        applicationEventPublisher.publishEvent(new WorkTimeChanged(person.getId(), previousStart, previousEnd, start, end));
    }

    public static class Edit {
        private Date startDate;
        private Date endDate;

        public Edit startDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public Edit endDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }
    }


    public static class TimeWorkLogBuilder {
        private TimeWorkLogRepository timeWorkLogRepository;
        private BeanValidator beanValidator;
        private ApplicationEventPublisher applicationEventPublisher;
        private Date startTime;
        private Person person;

        public TimeWorkLogBuilder(TimeWorkLogRepository timeWorkLogRepository, ApplicationEventPublisher applicationEventPublisher, BeanValidator beanValidator) {
            this.timeWorkLogRepository = timeWorkLogRepository;
            this.beanValidator = beanValidator;
            this.applicationEventPublisher = applicationEventPublisher;
        }

        public TimeWorkLogBuilder person(Person person) {
            this.person = person;
            return this;
        }

        public TimeWorkLogBuilder startTime(Date startTime) {
            this.startTime = startTime;
            return this;
        }

        public TimeWorkLog build() {
            TimeWorkLog timeWorkLog = new TimeWorkLog(this.timeWorkLogRepository, this.applicationEventPublisher, this.beanValidator);
            timeWorkLog.person = this.person;
            timeWorkLog.startTime = startTime;
            timeWorkLog.isOpened = true;
            return timeWorkLog;
        }
    }

    public static class ClosedTimeWorkLogBuilder {
        private TimeWorkLogRepository timeWorkLogRepository;
        private BeanValidator beanValidator;
        private ApplicationEventPublisher applicationEventPublisher;
        private Date startTime;
        private Date endTime;
        private Person person;

        public ClosedTimeWorkLogBuilder(TimeWorkLogRepository timeWorkLogRepository, ApplicationEventPublisher applicationEventPublisher, BeanValidator beanValidator) {
            this.timeWorkLogRepository = timeWorkLogRepository;
            this.beanValidator = beanValidator;
            this.applicationEventPublisher = applicationEventPublisher;
        }

        public ClosedTimeWorkLogBuilder person(Person person) {
            this.person = person;
            return this;
        }

        public ClosedTimeWorkLogBuilder startTime(Date startTime) {
            this.startTime = startTime;
            return this;
        }

        public ClosedTimeWorkLogBuilder endTime(Date endTime) {
            this.endTime = endTime;
            return this;
        }


        public TimeWorkLog build() {
            TimeWorkLog timeWorkLog = new TimeWorkLog(this.timeWorkLogRepository, this.applicationEventPublisher, this.beanValidator);
            timeWorkLog.startTime = startTime;
            timeWorkLog.endTime = endTime;
            timeWorkLog.person = person;
            timeWorkLog.isOpened = false;
            timeWorkLog.create();
            return timeWorkLog;
        }
    }
}
