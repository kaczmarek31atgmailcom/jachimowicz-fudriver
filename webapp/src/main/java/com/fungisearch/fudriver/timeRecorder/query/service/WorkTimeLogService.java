package com.fungisearch.fudriver.timeRecorder.query.service;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.person.person.query.yearBar.model.BarPeriod;
import com.fungisearch.fudriver.person.person.query.yearBar.model.BarType;
import com.fungisearch.fudriver.timeRecorder.query.dao.WorkTimeLogDao;
import com.fungisearch.fudriver.timeRecorder.query.dto.DayBarDto;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkTimeLogService {

    private final WorkTimeLogDao workTimeLogDao;

    @Autowired
    public WorkTimeLogService(WorkTimeLogDao workTimeLogDao) {
        this.workTimeLogDao = workTimeLogDao;
    }

    public List<DayBarDto> getDays(Long personId, Date startDay, Date endDay) {
        List<BarPeriod> activeBars = workTimeLogDao.getPeriodsForPerson(personId, DateUtils.getStartOfDay(startDay), DateUtils.getEndOfDay(endDay));
        List<BarPeriod> addedStartAndEndDates = addMissingStartAndEndDates(activeBars);
        List<DayBarDto> emptyDays = getAllDays(startDay,endDay);
        List<DayBarDto> allDaysAndActiveBars = mergeActiveBarsAndDays(emptyDays, addedStartAndEndDates);
        List<DayBarDto> allDaysAddedPauses = addPauses(allDaysAndActiveBars);
        List<DayBarDto> allDaysAddedInactivities = addInactivities(allDaysAddedPauses);
        List<DayBarDto> result = sort(allDaysAddedInactivities);
        return result;
    }

    private List<DayBarDto> addInactivities(List<DayBarDto> allDays) {
        BarPeriod begin;
        BarPeriod end;
        for(DayBarDto dayBarDto: allDays){
            begin = new BarPeriod();
            end = new BarPeriod();
            if(dayBarDto.barPeriods.isEmpty()){
                BarPeriod inactivePeriod = new BarPeriod();
                inactivePeriod.setStartDate(DateUtils.getStartOfDay(dayBarDto.day));
                inactivePeriod.setEndDate(DateUtils.getEndOfDay(dayBarDto.day));
                inactivePeriod.setPercentOfTheDay(100);
                inactivePeriod.setBarType(BarType.INACTIVE);
                dayBarDto.barPeriods.add(inactivePeriod);
            } else {
                if(dayBarDto.barPeriods.get(0).getStartDate().after(DateUtils.getStartOfDay(dayBarDto.day))){
                    begin.setStartDate(DateUtils.getStartOfDay(dayBarDto.day));
                    begin.setEndDate(decreaseByOneSec(dayBarDto.barPeriods.get(0).getStartDate()));
                    begin.setPercentOfTheDay(countPercentOfTheDay(begin));
                    begin.setBarType(BarType.INACTIVE);
                }
                if(dayBarDto.barPeriods.get(dayBarDto.barPeriods.size() - 1).getEndDate().before(decreaseByOneSec(DateUtils.getEndOfDay(dayBarDto.day)))){
                    end.setStartDate(increaseByOneSec(dayBarDto.barPeriods.get(dayBarDto.barPeriods.size() - 1).getEndDate()));
                    end.setEndDate(DateUtils.getEndOfDay(dayBarDto.day));
                    end.setPercentOfTheDay(countPercentOfTheDay(end));
                    end.setBarType(BarType.INACTIVE);
                }
                if((begin != null) && (begin.getStartDate() != null)) {
                    dayBarDto.barPeriods.add(begin);
                }
                if((end != null) && (end.getStartDate() != null)) {
                    dayBarDto.barPeriods.add(end);
                }
            }
        }
        return allDays;
    }

    private List<DayBarDto> sort(List<DayBarDto> dayBars){
        if(dayBars.size()>1) {
            Collections.sort(dayBars, (o1, o2) -> o1.day.compareTo(o2.day));
        }
        for(DayBarDto dayBarDto: dayBars){
            if(dayBarDto.barPeriods.size() > 1) {
                Collections.sort(dayBarDto.barPeriods, (o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate()));
            }
        }
        return dayBars;
    }

    private List<DayBarDto> addPauses(List<DayBarDto> allDaysAndActiveBars) {
        List<BarPeriod> pauses;
        for (DayBarDto dayBarDto : allDaysAndActiveBars) {
            if (dayBarDto.barPeriods.size() > 1) {
                pauses = new ArrayList<>();
                for (int i = dayBarDto.barPeriods.size() - 1; i > 0; i--) {
                    Date endPeriod = decreaseByOneSec(dayBarDto.barPeriods.get(i).getStartDate());
                    Date startPeriod = increaseByOneSec(dayBarDto.barPeriods.get(i - 1).getEndDate());
                    BarPeriod pause = new BarPeriod();
                    pause.setStartDate(startPeriod);
                    pause.setEndDate(endPeriod);
                    pause.setBarType(BarType.PAUSE);
                    pause.setPercentOfTheDay(countPercentOfTheDay(pause));
                    pause.setPercentOfTheYear(0);
                    pauses.add(pause);
                }
                dayBarDto.barPeriods.addAll(pauses);
                Collections.sort(dayBarDto.barPeriods, (o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate()));
            }
        }
        return allDaysAndActiveBars;
    }

    private List<DayBarDto> getAllDays(Date startDate, Date endDate) {
        List<Date> allDays = getDatesBetween(startDate,endDate);
        List<DayBarDto> result = new ArrayList<>();
        for (Date day : allDays) {
            DayBarDto dto = new DayBarDto();
            dto.day = day;
            dto.barPeriods = new ArrayList<>();
            result.add(dto);
        }

        return result;
    }

    private List<BarPeriod> addMissingStartAndEndDates(List<BarPeriod> barPeriods) {
        for (BarPeriod barPeriod : barPeriods) {
            if (barPeriod.getStartDate() == null) {
                barPeriod.setStartDate(DateUtils.getStartOfDay(barPeriod.getEndDate()));
                barPeriod.setOpened(false);
            }
        }

        for (BarPeriod barPeriod : barPeriods) {
            if (barPeriod.getEndDate() == null) {
                if (DateUtils.getEndOfDay(barPeriod.getStartDate()).after(new Date())) {
                    barPeriod.setEndDate(new Date());
                } else {
                    barPeriod.setEndDate(DateUtils.getEndOfDay(barPeriod.getStartDate()));
                }
                barPeriod.setOpened(false);
            }
        }
        return barPeriods;
    }

    private List<DayBarDto> mergeActiveBarsAndDays(List<DayBarDto> emptyDays, List<BarPeriod> activeBars) {
        for (DayBarDto dayBarDto : emptyDays) {
            for (BarPeriod barPeriod : activeBars) {
                if (DateUtils.getStartOfDay(barPeriod.getStartDate()).equals(DateUtils.getStartOfDay(dayBarDto.day))) {
                    barPeriod.setBarType(BarType.ACTIVE);
                    barPeriod.setPercentOfTheDay(countPercentOfTheDay(barPeriod));
                    barPeriod.setPercentOfTheYear(0);
                    dayBarDto.barPeriods.add(barPeriod);
                }
            }
        }

        for (DayBarDto dayBarDto : emptyDays) {
            Collections.sort(dayBarDto.barPeriods, (o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate()));
        }
        return emptyDays;
    }

    private List<Date> getUniqueDates(List<BarPeriod> barPeriods) {
        List<Date> uniqueDays = new ArrayList(barPeriods.stream()
                .map(p -> DateUtils.getStartOfDay(p.getStartDate()))
                .collect(Collectors.toSet()));
        Collections.sort(uniqueDays, (o1, o2) -> o1.compareTo(o2));
        return uniqueDays;
    }


    private List<Date> getDatesBetween(Date startDate, Date endDate) {
        endDate = DateUtils.getEndOfDay(endDate);
        List<Date> days = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        while (cal.getTime().before(endDate)) {
            days.add(DateUtils.getStartOfDay(cal.getTime()));
            cal.add(Calendar.DATE, 1);
        }
        Collections.sort(days, (o1, o2) -> o1.compareTo(o2));
        return days;
    }

    private double countPercentOfTheDay(BarPeriod barPeriod) {
        DateTime startTime = new DateTime(barPeriod.getStartDate());
        DateTime endTime = new DateTime(barPeriod.getEndDate());
        endTime = endTime.plusSeconds(1);
        Seconds seconds = Seconds.secondsBetween(startTime, endTime);
        double result = (double) seconds.getSeconds() * 100 / 86400;
        return (double) (Math.round(result * 100)) / 100;
    }

    private Date increaseByOneSec(Date date) {
        DateTime dt = new DateTime(date);
        dt = dt.plusSeconds(1);
        return dt.toDate();
    }

    private Date decreaseByOneSec(Date date) {
        DateTime dt = new DateTime(date);
        dt = dt.minusSeconds(1);
        return dt.toDate();
    }

}
