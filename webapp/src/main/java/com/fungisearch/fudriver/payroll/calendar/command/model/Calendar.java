package com.fungisearch.fudriver.payroll.calendar.command.model;

import com.fungisearch.fudriver.exception.DateParseException;
import com.fungisearch.fudriver.payroll.calendar.command.repository.CalendarRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "calendar")
public class Calendar {

    private transient CalendarRepository calendarRepository;
    private transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date date;

    @Column(name = "editable")
    @Enumerated(EnumType.ORDINAL)
    private SalaryDayTypeEnum salaryDayType;


    private Calendar() {
    }

    public Calendar(CalendarRepository calendarRepository, BeanValidator beanValidator) {
        this.calendarRepository = calendarRepository;
        this.beanValidator = beanValidator;
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public SalaryDayTypeEnum getSalaryDayType() {
        return salaryDayType;
    }

    public void setCalendarRepository(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public void setBeanValidator(BeanValidator beanValidator) {
        this.beanValidator = beanValidator;
    }


    public void setRegularDay(){
        this.salaryDayType = SalaryDayTypeEnum.REGULAR_DAY;
        beanValidator.validate(this);
    }

    public void setSunday() {
        if (this.salaryDayType.equals(SalaryDayTypeEnum.SUNDAY)) {
            this.salaryDayType = SalaryDayTypeEnum.REGULAR_DAY;
        } else {
            this.salaryDayType = SalaryDayTypeEnum.SUNDAY;
        }
        beanValidator.validate(this);
        //calendarRepository.update(this);
    }

    public void setBonusDay() {
        if (this.salaryDayType.equals(SalaryDayTypeEnum.BONUS_DAY)) {
            this.salaryDayType = SalaryDayTypeEnum.REGULAR_DAY;
        } else {
            this.salaryDayType = SalaryDayTypeEnum.BONUS_DAY;
        }
        beanValidator.validate(this);
        //calendarRepository.update(this);
    }

    public void openMonth(){
        if(! isCalendarOpened()){
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.setTime(this.date);
            int maxDay = c.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
            String strDate;
            SimpleDateFormat yearMonthDayFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat yearMonthFormat = new SimpleDateFormat("yyyy-MM");
            String currentMonth = yearMonthFormat.format(this.date);
            Date theDay;
            for (int i = 1; i <= maxDay; i++) {
                strDate = currentMonth + "-" + addTrailingZero(i);
                try {
                    theDay = yearMonthDayFormat.parse(strDate);
                    Calendar day = new com.fungisearch.fudriver.payroll.calendar.command.model.Calendar(this.calendarRepository,this.beanValidator);
                    day.date = theDay;
                    day.save();
                } catch (ParseException e) {
                    throw new DateParseException();
                }
            }
        }
    }


    private String addTrailingZero(int input) {
        String inputStr = String.valueOf(input);
        while (inputStr.length() < 2) {
            inputStr = "0" + input;
        }
        return inputStr;
    }


    public void save() {
            SimpleDateFormat sdf = new SimpleDateFormat("u");
            int weekDay = Integer.parseInt(sdf.format(this.date));
            if (weekDay == 7) {
                this.salaryDayType = SalaryDayTypeEnum.SUNDAY;
            } else {
                this.salaryDayType = SalaryDayTypeEnum.REGULAR_DAY;
            }
            beanValidator.validate(this);
            calendarRepository.create(this);
    }


    public boolean isCalendarOpened() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(this.date);
        c.set(java.util.Calendar.DAY_OF_MONTH, c.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
        Date endDate = c.getTime();
        c.set(java.util.Calendar.DAY_OF_MONTH, c.getActualMinimum(java.util.Calendar.DAY_OF_MONTH));
        Date startDate = c.getTime();
        return (calendarRepository.findBetweenDates(startDate, endDate).size() > 0);
    }

    public static class CalendarBuilder {
        private CalendarRepository calendarRepository;
        private BeanValidator beanValidator;
        private Date date;
        private SalaryDayTypeEnum salaryDayType;

        public CalendarBuilder(CalendarRepository calendarRepository, BeanValidator beanValidator) {
            this.calendarRepository = calendarRepository;
            this.beanValidator = beanValidator;
        }

        public CalendarBuilder date(Date date) {
            this.date = date;
            return this;
        }

        public CalendarBuilder salaryDayType(SalaryDayTypeEnum salaryDayType) {
            this.salaryDayType = salaryDayType;
            return this;
        }

        public Calendar build() {
            Calendar calendar = new Calendar(this.calendarRepository, this.beanValidator);
            calendar.date = this.date;
            calendar.salaryDayType = this.salaryDayType;
            calendar.save();
            return calendar;
        }
    }
}
