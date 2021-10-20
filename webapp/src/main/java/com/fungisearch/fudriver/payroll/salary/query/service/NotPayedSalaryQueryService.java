package com.fungisearch.fudriver.payroll.salary.query.service;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.payroll.calendar.command.model.CalendarFactory;
import com.fungisearch.fudriver.payroll.salary.query.dao.SalaryDao;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.HarvestByPersonAndWageDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.PersonSalaryHeaderDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.WorkTimeByPersonAndDayTypeDto;
import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;
import com.fungisearch.fudriver.person.person.command.model.Person;
import org.aspectj.runtime.internal.PerObjectMap;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotPayedSalaryQueryService {
    private final SalaryDao salaryDao;
    private final CalendarFactory calendarFactory;
    private final WorkTimeChecker workTimeChecker;

    @Autowired
    public NotPayedSalaryQueryService(SalaryDao salaryDao, CalendarFactory calendarFactory, WorkTimeChecker workTimeChecker) {
        this.salaryDao = salaryDao;
        this.calendarFactory = calendarFactory;
        this.workTimeChecker = workTimeChecker;
    }

    public List<PersonSalaryHeaderDto> getHeaders(Date day) {
        calendarFactory.findDay(day);
        List<HarvestByPersonAndWageDto> rawHarvest = salaryDao.getHarvestByPersonAndWage(getTimeShort(day));
        List<WorkTimeByPersonAndDayTypeDto> rawHours = salaryDao.getWorkTimeByPersonAndDayType(getTimeShort(day));
        List<WorkTimeByPersonAndDayTypeDto> hoursAndPauses = countHoursAndPauses(rawHours);
        List<PersonSalaryHeaderDto> names = salaryDao.getPersonNames(getTimeShort(day));
        List<PersonSalaryHeaderDto> result = new ArrayList<>();
        result.addAll(getHarvestHeaders(names, rawHarvest));
        result.addAll(getHoursHeaders(names, hoursAndPauses));
        result = addHarvestQuality(result,getTimeShort(day));
        Set<Long> invalidPeriodsPersonIds = new HashSet(workTimeChecker.findPeopleWithInvalidWorkTime(day));
        for(PersonSalaryHeaderDto dto: result){
            if(invalidPeriodsPersonIds.contains(dto.id)){
                dto.isValid = false;
            }
        }
        Map<Long,Integer> workingDaysAmountMap = salaryDao.getWorkingDaysAmount(getTimeShort(day));
        result.forEach(o -> o.workDaysAmount = getPersonWorkingDaysAmount(o.id,workingDaysAmountMap));
        HashMap<Long, PersonSalaryHeaderDto> set = new HashMap<>();
        for(PersonSalaryHeaderDto dto: result){
            set.put(dto.id,dto);
        }
        return new ArrayList<>(set.values());
    }

    private int getPersonWorkingDaysAmount(Long personId,Map<Long,Integer> workingDaysAmountMap){
        int result = 0;
        if(workingDaysAmountMap.containsKey(personId)){
            result = workingDaysAmountMap.get(personId);
        }
        return result;
    }

    private List<WorkTimeByPersonAndDayTypeDto> countHoursAndPauses(List<WorkTimeByPersonAndDayTypeDto> rawHours) {
        Map<Date,Map<Long,WorkTimeByPersonAndDayTypeDto>> map = new HashMap<>();
        for(WorkTimeByPersonAndDayTypeDto workTimeByPersonAndDayTypeDto: rawHours){
            if(map.containsKey(DateUtils.getStartOfDay(workTimeByPersonAndDayTypeDto.startTime))){
                if(map.get(DateUtils.getStartOfDay(workTimeByPersonAndDayTypeDto.startTime)).containsKey(workTimeByPersonAndDayTypeDto.personId)){
                    if(map.get(DateUtils.getStartOfDay(workTimeByPersonAndDayTypeDto.startTime)).get(workTimeByPersonAndDayTypeDto.personId).startTime.after(workTimeByPersonAndDayTypeDto.startTime)){
                        map.get(DateUtils.getStartOfDay(workTimeByPersonAndDayTypeDto.startTime)).get(workTimeByPersonAndDayTypeDto.personId).startTime = workTimeByPersonAndDayTypeDto.startTime;
                    }
                    if(map.get(DateUtils.getStartOfDay(workTimeByPersonAndDayTypeDto.startTime)).get(workTimeByPersonAndDayTypeDto.personId).endTime.before(workTimeByPersonAndDayTypeDto.endTime)){
                        map.get(DateUtils.getStartOfDay(workTimeByPersonAndDayTypeDto.endTime)).get(workTimeByPersonAndDayTypeDto.personId).endTime = workTimeByPersonAndDayTypeDto.endTime;
                    }
                } else {
                    map.get(DateUtils.getStartOfDay(workTimeByPersonAndDayTypeDto.startTime)).put(workTimeByPersonAndDayTypeDto.personId,workTimeByPersonAndDayTypeDto);
                }
            } else {
                Map<Long,WorkTimeByPersonAndDayTypeDto> personMap = new HashMap<>();
                personMap.put(workTimeByPersonAndDayTypeDto.personId,workTimeByPersonAndDayTypeDto);
                map.put(DateUtils.getStartOfDay(workTimeByPersonAndDayTypeDto.startTime),personMap);
            }
        }
        List<WorkTimeByPersonAndDayTypeDto> result = new ArrayList<>();
        for(Map<Long,WorkTimeByPersonAndDayTypeDto> personMap: map.values()){
            for(WorkTimeByPersonAndDayTypeDto dto: personMap.values()){
                result.add(dto);
            }
        }
        return result;
    }

    List<PersonSalaryHeaderDto> getHarvestHeaders(List<PersonSalaryHeaderDto> names, List<HarvestByPersonAndWageDto> harvest) {
        Map<Long, PersonSalaryHeaderDto> headers = new HashMap<>();
        for (HarvestByPersonAndWageDto item : harvest) {
            if (findPerson(names, item.personId).payrollType.equals(PayrollTypeEnum.ACCORD)) {
                PersonSalaryHeaderDto dto;
                if (headers.containsKey(item.personId)) {
                    dto = headers.get(item.personId);
                    dto.totalHarvest += item.totalKg;
                } else {
                    dto = findPerson(names, item.personId);
                    dto.id = item.personId;
                    dto.totalHarvest = item.totalKg;
                    headers.put(item.personId, dto);
                }
                switch (item.dayType) {
                    case REGULAR_DAY:
                        dto.regularHarvestSalary += item.salary;
                        break;
                    case SUNDAY:
                        dto.sundayHarvestSalary += item.salary;
                        break;
                    case BONUS_DAY:
                        dto.bonusHarvestSalary += item.salary;
                        break;
                    default:
                        throw new IllegalStateException("Invalid salary day type: " + item.dayType);
                }
            }
        }


        Map<Long,PersonSalaryHeaderDto> nullWagePeople = new HashMap<>();
        for(PersonSalaryHeaderDto name: names){
            if(name.totalHarvest > 0 && (!(headers.containsKey(name.id))) && name.payrollType.equals(PayrollTypeEnum.ACCORD)) {
                    nullWagePeople.put(name.id, name);
            }
        }


        List<PersonSalaryHeaderDto> result = new ArrayList<>();
        result.addAll(headers.values());
        result.addAll(nullWagePeople.values());
        return result;
    }

    List<PersonSalaryHeaderDto> addHarvestQuality(List<PersonSalaryHeaderDto> headers, String timeshort){
        Map<Long,Long> qualityMap = salaryDao.getExportRate(timeshort);
        for(PersonSalaryHeaderDto dto: headers){
            if (qualityMap.containsKey(dto.id)) {
                dto.quality = qualityMap.get(dto.id);
            }
        }
    return headers;
    }

    public List<PersonSalaryHeaderDto> getHoursHeaders(List<PersonSalaryHeaderDto> names, List<WorkTimeByPersonAndDayTypeDto> hours) {
        Map<Long, PersonSalaryHeaderDto> headers = new HashMap<>();
        for (WorkTimeByPersonAndDayTypeDto item : hours) {
            PersonSalaryHeaderDto dto;
            if (headers.containsKey(item.personId)) {
                dto = headers.get(item.personId);
            } else {
                dto = findPerson(names, item.personId);
                dto.id = item.personId;
                headers.put(item.personId, dto);
            }
            DateTime startDateTime = new DateTime(item.startTime);
            DateTime endDateTime = new DateTime(item.endTime);
            int minutes = Minutes.minutesBetween(startDateTime, endDateTime).getMinutes();
            switch (item.dayType) {
                case REGULAR_DAY:
                    dto.regularMinutes += minutes;
                    break;
                case SUNDAY:
                    dto.sundayMinutes += minutes;
                    break;
                case BONUS_DAY:
                    dto.bonusMinutes += minutes;
                    break;
                default:
                    throw new IllegalStateException("Non existing salary day type: " + item.dayType);
            }
        }
        return new ArrayList<>(countHoursSalary(new ArrayList<>(headers.values())));
    }


    List<PersonSalaryHeaderDto> countHoursSalary(List<PersonSalaryHeaderDto> hoursSalary) {
        for (PersonSalaryHeaderDto person : hoursSalary) {
            person.regularHoursSalary = (person.regularHoursWage * person.regularMinutes) / 60;
            person.sundayHoursSalary = (person.sundayHoursWage * person.sundayMinutes) / 60;
            person.bonusHoursSalary = (person.bonusHoursWage * person.bonusMinutes) / 60;
        }
        return hoursSalary;
    }

    PersonSalaryHeaderDto findPerson(List<PersonSalaryHeaderDto> list, long id) {
        return list.stream()
                .filter(t -> t.id == id)
                .reduce((a, b) -> {
                    throw new IllegalStateException("Two person entities with the same id: " + a + " " + b);
                })
                .get();
    }

    int findMinutes(Date start, Date end) {
        DateTime startTime = new DateTime(start);
        DateTime endTime = new DateTime(end);
        return Minutes.minutesBetween(startTime, endTime).getMinutes();
    }

    String getTimeShort(Date day) {
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyyMM");
        DateTime dateTime = new DateTime(day);
        return df.print(dateTime);
    }


}
