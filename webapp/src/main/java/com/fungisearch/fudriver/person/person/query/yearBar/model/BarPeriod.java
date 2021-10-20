package com.fungisearch.fudriver.person.person.query.yearBar.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by marcin on 09.01.16.
 */
public class BarPeriod implements Serializable, Comparable<BarPeriod>{


    private static final long serialVersionUID = 6709182096098676923L;

    private Date startDate;
    private Date endDate;
    private BarType barType;
    private int percentOfTheYear;
    private double percentOfTheDay;

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    private boolean isOpened;

    public double getPercentOfTheDay() {
        return percentOfTheDay;
    }

    public void setPercentOfTheDay(double percentOfTheDay) {
        this.percentOfTheDay = percentOfTheDay;
    }



    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BarType getBarType() {
        return barType;
    }

    public void setBarType(BarType barType) {
        this.barType = barType;
    }

    public int getPercentOfTheYear() {
        return percentOfTheYear;
    }

    public void setPercentOfTheYear(int percentOfTheYear) {
        this.percentOfTheYear = percentOfTheYear;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BarPeriod barPeriod = (BarPeriod) o;

        if (startDate != null ? !startDate.equals(barPeriod.startDate) : barPeriod.startDate != null) return false;
        if (endDate != null ? !endDate.equals(barPeriod.endDate) : barPeriod.endDate != null) return false;
        return barType == barPeriod.barType;

    }

    @Override
    public int hashCode() {
        int result = startDate != null ? startDate.hashCode() : 0;
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (barType != null ? barType.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(BarPeriod o) {
        return getStartDate().compareTo(o.getStartDate());
    }
}
