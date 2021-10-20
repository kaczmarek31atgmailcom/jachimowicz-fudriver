package com.fungisearch.fudriver.person.person.query.yearBar.service;


import com.fungisearch.fudriver.person.person.query.yearBar.dao.YearBarRepository;
import com.fungisearch.fudriver.person.person.query.yearBar.dto.SimpleWorkPeriodDto;
import com.fungisearch.fudriver.person.person.query.yearBar.dto.WorkPeriodDto;
import com.fungisearch.fudriver.person.person.query.yearBar.model.BarPeriod;
import com.fungisearch.fudriver.person.person.query.yearBar.model.BarType;
import com.fungisearch.fudriver.person.person.query.yearBar.model.YearBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 09.01.16.
 */


@Service
public class YearBarService {

    @Autowired
    private YearBarRepository yearBarRepository;

    public List<WorkPeriodDto> getWorkPeriodsForPerson(Long id) {
        return yearBarRepository.findAllForPerson(id);
    }


public List<SimpleWorkPeriodDto> getSimpleWorkPeriods(Long personId){
    return yearBarRepository.findSimpleWorkPeriodsForPerson(personId);
}

    public boolean isDayActive(Date queryDate, List<WorkPeriodDto> workPeriodDtos) {
        boolean result = false;
        for (WorkPeriodDto workPeriodDto : workPeriodDtos) {
            Date startDate = workPeriodDto.getStartDate();
            Date endDate = workPeriodDto.getEndDate();
            if (endDate == null) {
                endDate = new Date();
            }
            if (queryDate.after(startDate) && queryDate.before(endDate)) {
                return true;
            }
        }
        return result;
    }

    public List<YearBar> getYearBars(Long personId) {
        List<WorkPeriodDto> workPeriods = getWorkPeriodsForPerson(personId);
        if(workPeriods.size() == 0){
            return new ArrayList<YearBar>();
        }
        List<YearBar> result = new ArrayList<YearBar>();
        Date startDate = workPeriods.get(0).getStartDate();
        Integer startYear = getYearFromDate(startDate);
        List<Date> dates = getAllDaysOfTheAllYears(startDate);
        YearBar yearBar = new YearBar(startYear);
        BarPeriod barPeriod = new BarPeriod();
        Boolean activeStatus = false;
        Integer currentYear = startYear;
        Date lastDay = dates.get(dates.size() - 1);
        if(dates.size() > 0){
            activeStatus = isDayActive(dates.get(0), workPeriods);
            barPeriod.setStartDate(dates.get(0));
            if(activeStatus) {
                barPeriod.setBarType(BarType.ACTIVE);
            } else {
                barPeriod.setBarType(BarType.INACTIVE);
            }
        }
        for (Date day : dates) {
            boolean currentDayStatus = isDayActive(day, workPeriods);
            if (currentDayStatus != activeStatus) {
                barPeriod.setEndDate(getDayBefore(day));
                barPeriod.setPercentOfTheYear(getPercentOfYear(barPeriod.getStartDate(), barPeriod.getEndDate()));
                yearBar.addToBarPeriods(barPeriod);

                barPeriod = new BarPeriod();
                barPeriod.setStartDate(getDayBefore(day));
                activeStatus = currentDayStatus;
                if (currentDayStatus) {
                    barPeriod.setBarType(BarType.ACTIVE);
                } else {
                    barPeriod.setBarType(BarType.INACTIVE);
                }
            }

            if (currentYear != getYearFromDate(day)) {
                barPeriod.setEndDate(getDayBefore(day));
                barPeriod.setPercentOfTheYear(getPercentOfYear(barPeriod.getStartDate(), barPeriod.getEndDate()));
                yearBar.addToBarPeriods(barPeriod);
                result.add(yearBar);
                currentYear++;
                yearBar = new YearBar(currentYear);
                barPeriod = new BarPeriod();
                barPeriod.setStartDate(getDayBefore(day));
                if (isDayActive(getDayAfter(day), workPeriods)) {
                    barPeriod.setBarType(BarType.ACTIVE);
                } else {
                    barPeriod.setBarType(BarType.INACTIVE);
                }
            }
            if (day == lastDay) {
                barPeriod.setEndDate(day);
                barPeriod.setPercentOfTheYear(getPercentOfYear(barPeriod.getStartDate(), barPeriod.getEndDate()));
                yearBar.addToBarPeriods(barPeriod);
                result.add(yearBar);
            }
        }
        return result;
    }


    public int getYearFromDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        String year = dateFormat.format(date);
        return Integer.parseInt(year);
    }


    public Date getDayBefore(Date input) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(input);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public Date getDayAfter(Date input) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(input);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public Date getFirstDayOfTheYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    public Date getLastDayOfTheYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        return calendar.getTime();
    }


    public List<Date> getAllDaysOfTheAllYears(Date date) {
        Date firstDayOfYear = getFirstDayOfTheYear(date);
        Date lastDay = new Date();
        Date startDay = firstDayOfYear;
        List<Date> result = new ArrayList<Date>();
        while (lastDay.after(startDay)) {
            result.add(startDay);
            startDay = getDayAfter(startDay);
        }
        return result;
    }

    public List<Date> getDaysFromDate(Date date) {
        List<Date> result = new ArrayList<Date>();
        Date today = new Date();
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(today);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        while (cal.before(endDate)) {
            result.add(cal.getTime());
            cal.add(Calendar.DATE, 1);
        }
        result.add(today);
        return result;
    }

    public int getNumberOfDaysInYear(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR);
    }

    public int getPercentOfYear(Date startDay, Date endDay) {
        int daysInYear = getNumberOfDaysInYear(startDay);
        double diff = endDay.getTime() - startDay.getTime();
        double diffDays = diff / (24 * 60 * 60 * 1000);
        diffDays = diffDays + 2;
        Double percent = (diffDays * 100) / daysInYear;
        return percent.intValue();
    }


}
