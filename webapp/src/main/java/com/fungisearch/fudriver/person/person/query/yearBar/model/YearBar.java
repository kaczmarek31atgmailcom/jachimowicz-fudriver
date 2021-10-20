package com.fungisearch.fudriver.person.person.query.yearBar.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcin on 09.01.16.
 */
public class YearBar implements Serializable{


    private static final long serialVersionUID = 2808919796601609803L;

    Integer year;
    List<BarPeriod> barPeriods;

    public YearBar(int year){
        this.year = year;
        this.barPeriods = new ArrayList<BarPeriod>();
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void addToBarPeriods(BarPeriod barPeriod){
        barPeriods.add(barPeriod);
    }

    public List<BarPeriod> getBarPeriods(){
        return this.barPeriods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YearBar yearBar = (YearBar) o;

        return year.equals(yearBar.year);

    }

    @Override
    public int hashCode() {
        return year.hashCode();
    }
}
