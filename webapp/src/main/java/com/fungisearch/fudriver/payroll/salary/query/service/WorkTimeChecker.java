package com.fungisearch.fudriver.payroll.salary.query.service;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.payroll.salary.query.dao.SalaryDao;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.PersonWorkTimeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WorkTimeChecker {
    private final SalaryDao salaryDao;

    @Autowired
    public WorkTimeChecker(SalaryDao salaryDao) {
        this.salaryDao = salaryDao;
    }

    public List<Long> findPeopleWithInvalidWorkTime(Date monthDay) {
        Date startTime = DateUtils.getStartOfDay(monthDay);
        Date endTime = DateUtils.getEndOfDay(monthDay);
        List<PersonWorkTimeDto> workTimeList = salaryDao.getMonthlyWorkTime(startTime, endTime);
        Map<Long, List<PersonWorkTimeDto>> peoplePeriods = divideByPersonIds(workTimeList);
        Set<Long> result = new HashSet<>();
        for (long personId : peoplePeriods.keySet()) {
            if (findOverlappingPeriodUsersIds(peoplePeriods.get(personId))) {
                result.add(personId);
            }
        }
        Set<Long> peopleIdsWithNotClosedPeriods = findPersonIdsWithNotClosedPeriods(workTimeList);
        Set<Long> peopleIdsWithDatesNotInTheSameDay = findPersonIdsWithNotSameDayPeriodDates(workTimeList);
        result.addAll(peopleIdsWithNotClosedPeriods);
        result.addAll(peopleIdsWithDatesNotInTheSameDay);
        return new ArrayList<>(result);
    }

    private Map<Long, List<PersonWorkTimeDto>> divideByPersonIds(List<PersonWorkTimeDto> input) {
        Map<Long, List<PersonWorkTimeDto>> result = new HashMap<>();
        for (PersonWorkTimeDto personWorkTimeDto : input) {
            if (result.containsKey(personWorkTimeDto.personId)) {
                result.get(personWorkTimeDto.personId).add(personWorkTimeDto);
            } else {
                List<PersonWorkTimeDto> workTimeDtos = new ArrayList<>();
                workTimeDtos.add(personWorkTimeDto);
                result.put(personWorkTimeDto.personId, workTimeDtos);
            }
        }
        return result;
    }

    private boolean findOverlappingPeriodUsersIds(List<PersonWorkTimeDto> input) {
        Map<Date, List<PersonWorkTimeDto>> dailyPeriods = dividePersonPeriodsByDays(input);
        for (Date date : dailyPeriods.keySet()) {
            if (isDayContainingOverlappingPeriods(dailyPeriods.get(date))) {
                return true;
            }
        }
        return false;
    }

    private boolean isDayContainingOverlappingPeriods(List<PersonWorkTimeDto> input) {
        if (input.size() > 2) {
            if (input.get(input.size() - 1).startTime.before(input.get(input.size() - 2).getEndTime())) {
                return true;
            } else {
                input.remove(input.size() - 1);
                isDayContainingOverlappingPeriods(input);
            }
        } else if (input.size() == 2) {
            if (input.get(1).startTime.before(input.get(0).endTime)) {
                return true;
            }
        }
        return false;
    }

    private Map<Date, List<PersonWorkTimeDto>> dividePersonPeriodsByDays(List<PersonWorkTimeDto> input) {
        Map<Date, List<PersonWorkTimeDto>> result = new HashMap<>();
        for (PersonWorkTimeDto personWorkTimeDto : input) {
            if (result.containsKey(DateUtils.getStartOfDay(personWorkTimeDto.startTime))) {
                result.get(DateUtils.getStartOfDay(personWorkTimeDto.startTime)).add(personWorkTimeDto);
            } else {
                List<PersonWorkTimeDto> personWorkTimeDtos = new ArrayList<>();
                personWorkTimeDtos.add(personWorkTimeDto);
                result.put(DateUtils.getStartOfDay(personWorkTimeDto.startTime), personWorkTimeDtos);
            }
        }
        result.forEach((k, v) -> v.sort((o1, o2) -> o1.getStartTime().compareTo(o2.getStartTime())));
        return result;
    }

    private Set<Long> findPersonIdsWithNotClosedPeriods(List<PersonWorkTimeDto> workTimeList) {
        Set<Long> result = new HashSet<>();
        for(PersonWorkTimeDto personWorkTimeDto: workTimeList){
            if(!(personWorkTimeDto.isClosed())){
                result.add(personWorkTimeDto.getPersonId());
            }
        }
        return result;
    }

    private Set<Long> findPersonIdsWithNotSameDayPeriodDates(List<PersonWorkTimeDto> workTimeList) {
        Set<Long> result = new HashSet<>();
        for(PersonWorkTimeDto workTime: workTimeList){
            if(workTime.startTime == null || workTime.endTime == null){
                result.add(workTime.personId);
            }
            else if(!(DateUtils.isTheSameDay(workTime.startTime, workTime.endTime))){
                result.add(workTime.personId);
            }
        }
        return result;
    }
}
