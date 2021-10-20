package com.fungisearch.fudriver.person.person.command.model;

import com.fungisearch.fudriver.exception.ClosedTimeSheetException;
import com.fungisearch.fudriver.exception.NonExistingTimeSheetUpdateException;
import com.fungisearch.fudriver.exception.NotClosedTimeSheetException;
import com.fungisearch.fudriver.person.person.command.repository.TimeSheetRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by marcin on 03.03.16.
 */
@Entity
@Table(name = "okresy_zatrudnienia")
public class TimeSheet {

    @Autowired
    private transient TimeSheetRepository timeSheetRepository;

    @Autowired
    private transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "ludzie_id")
    @NotNull
    private Long personId;

    @Column(name = "startdate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(name = "enddate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Version
    @Column(name = "version")
    private Long version;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setTimeSheetRepository(TimeSheetRepository timeSheetRepository) {
        this.timeSheetRepository = timeSheetRepository;
    }

    public void setBeanValidator(BeanValidator beanValidator) {
        this.beanValidator = beanValidator;
    }

    @SuppressWarnings("unused")
private TimeSheet(){}

    public TimeSheet(TimeSheetRepository timeSheetRepository, BeanValidator beanValidator){
        this.timeSheetRepository = timeSheetRepository;
        this.beanValidator = beanValidator;
    }

public void openPeriod() throws NotClosedTimeSheetException {
    TimeSheet latestOne = timeSheetRepository.findLatestOne(this.personId);
    if(latestOne != null && latestOne.endDate == null){
        throw new NotClosedTimeSheetException();
    }
    this.startDate = new Date();
    beanValidator.validate(this);
    timeSheetRepository.addTimeSheet(this);
}

public void closePeriod() throws NonExistingTimeSheetUpdateException, ClosedTimeSheetException {
    TimeSheet latestOne = timeSheetRepository.findLatestOne(this.personId);
    if(latestOne == null){
        throw new NonExistingTimeSheetUpdateException();
    }
    if(latestOne.endDate != null){
        throw new ClosedTimeSheetException();
    }
    this.id = latestOne.id;
    this.version = latestOne.version;
    this.startDate = latestOne.startDate;
    this.endDate = new Date();
    beanValidator.validate(this);
}

    public static class TimeSheetBuilder{
        private TimeSheetRepository timeSheetRepository;
        private BeanValidator beanValidator;
        private Long personId;
        private Long version;

        public TimeSheetBuilder(TimeSheetRepository timeSheetRepository, BeanValidator beanValidator){
            this.timeSheetRepository = timeSheetRepository;
            this.beanValidator = beanValidator;
        }

        public TimeSheetBuilder personId(Long personId){
            this.personId = personId;
            return this;
        }

        public TimeSheetBuilder version(Long version){
            this.version = version;
            return this;
        }

        public TimeSheet build(){
            TimeSheet timeSheet = new TimeSheet(this.timeSheetRepository,this.beanValidator);
            timeSheet.personId = this.personId;
            timeSheet.version = this.version;
            return timeSheet;
        }
    }

    public void edit(Edit edit){
        this.startDate = edit.startDate;
        this.endDate = edit.endDate;
        this.version = edit.version;
        beanValidator.validate(this);
    }

    public static class Edit{
        private Date startDate;
        private Date endDate;
        private Long version;

        public Edit startDate(Date startDate){
            this.startDate = startDate;
            return this;
        }

        public Edit endDate(Date endDate){
            this.endDate = endDate;
            return this;
        }

        public Edit version(Long version){
            this.version = version;
            return this;
        }
    }

}
