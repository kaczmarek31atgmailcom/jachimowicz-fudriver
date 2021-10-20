package com.fungisearch.fudriver.person.person.query.yearBar.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by marcin on 07.01.16.
 */
public class WorkPeriodDto implements Serializable {

    private static final long serialVersionUID = -3103596443795546617L;

    private Integer year;
    private Date startDate;
    private Integer startDayOfTheYear;
    private Date endDate;
    private Integer endDayOfTheYear;
    boolean active;
    public Long version;

    public Integer getYear() {
        return year;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getStartDayOfTheYear() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getStartDate());
        return cal.get(Calendar.DAY_OF_YEAR);
    }


    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getEndDayOfTheYear() {
        Date endDate = getEndDate();
        if (endDate == null){
            endDate = new Date();
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(getEndDate());
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
