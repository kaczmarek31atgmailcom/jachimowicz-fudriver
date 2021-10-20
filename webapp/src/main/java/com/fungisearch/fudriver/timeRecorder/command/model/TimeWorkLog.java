package com.fungisearch.fudriver.timeRecorder.command.model;

import com.fungisearch.fudriver.common.event.CustomApplicationEvent;
import com.fungisearch.fudriver.common.event.EventTypeEnum;
import com.fungisearch.fudriver.exception.StartDateAfterEndDateException;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.timeRecorder.command.repository.TimeWorkLogRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
        int daysAmount = Days.daysBetween(new DateTime(this.getStartTime()), new DateTime()).getDays();
        Date endTime = new Date();
        if (daysAmount == 0) {
            this.endTime = new Date();
            this.isOpened = false;
            beanValidator.validate(this);
            CustomApplicationEvent event = new CustomApplicationEvent(EventTypeEnum.EMPLOYEE_WORK_ENDED, this.person.getId());
            applicationEventPublisher.publishEvent(event);
        } else {
            this.endTime = new DateTime(this.getStartTime()).withTime(23, 59, 59, 0).toDate();
            this.isOpened = false;
            beanValidator.validate(this);
        }
    }

    public void register() {
        if (this.startTime == null) {
            this.startTime = new Date();
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
        beanValidator.validate(this);
        timeWorkLogRepository.save(this);
    }

    public void delete() {
        if (this.isOpened) {
            this.isOpened = false;
            CustomApplicationEvent event = new CustomApplicationEvent(EventTypeEnum.EMPLOYEE_WORK_ENDED, this.person.getId());
            applicationEventPublisher.publishEvent(event);
        }
        beanValidator.validate(this);
        timeWorkLogRepository.delete(this);
    }

    public void edit(Edit edit) {
        if (edit.endDate != null) {
            if (edit.startDate.compareTo(edit.endDate) > 0) {
                throw new StartDateAfterEndDateException();
            }
            this.endTime = edit.endDate;
            this.isOpened = false;
        }
        this.startTime = edit.startDate;
        beanValidator.validate(this);
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
            TimeWorkLog timeWorkLog;
            List<TimeWorkLog> collisionTimeWorkLogs = timeWorkLogRepository.findForPeriod(startTime, endTime, person);
            if (collisionTimeWorkLogs.isEmpty()) {
                timeWorkLog = new TimeWorkLog(this.timeWorkLogRepository, this.applicationEventPublisher, this.beanValidator);
                timeWorkLog.startTime = startTime;
                timeWorkLog.endTime = endTime;
                timeWorkLog.person = person;
                timeWorkLog.isOpened = false;
                timeWorkLog.create();
            } else {
                throw new IllegalStateException("Already existing workPeriod in the same time");
            }
            return timeWorkLog;
        }
    }
}
