package com.fungisearch.fudriver.timeRecorder.query.service;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.person.person.query.dao.PersonDao;
import com.fungisearch.fudriver.person.person.query.dto.PersonHeaderDto;
import com.fungisearch.fudriver.timeRecorder.query.dao.WorkTimeLogDao;
import com.fungisearch.fudriver.timeRecorder.query.dto.PersonDailyWorkTimeDto;
import com.fungisearch.fudriver.timeRecorder.query.dto.PersonWorkTimeDto;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by marcin on 13.01.17.
 */
@Service
public class PersonWorkTimeService {

    private final WorkTimeLogDao workTimeLogDao;
    private final PersonDao personDao;


    @Autowired
    public PersonWorkTimeService(WorkTimeLogDao workTimeLogDao, PersonDao personDao) {
        this.workTimeLogDao = workTimeLogDao;
        this.personDao = personDao;

    }

    public List<PersonDailyWorkTimeDto> getDailyWorkMinutes(Date startDate, Date endDate) {
        List<PersonWorkTimeDto> workPeriods = workTimeLogDao.getPersonWorkTime(DateUtils.getStartOfDay(startDate), DateUtils.getEndOfDay(endDate));
        List<PersonDailyWorkTimeDto> result = new ArrayList<>();
        List<PersonWorkTimeDto> splitedWorkPeriods = new ArrayList<>();
        for(PersonWorkTimeDto personWorkTimeDto: workPeriods){
            splitedWorkPeriods.addAll(splitToDays(personWorkTimeDto,endDate));
        }

        PersonDailyWorkTimeDto dto;
        for (PersonWorkTimeDto workTimeDto : splitedWorkPeriods) {
            dto = findPerson(workTimeDto.personId, workTimeDto.startDate, result);
            if (dto == null) {
                PersonHeaderDto personHeaderDto = findPersonById(workTimeDto.personId);
                dto = new PersonDailyWorkTimeDto();
                dto.personId = workTimeDto.personId;
                dto.nr = personHeaderDto.nr;
                dto.name = personHeaderDto.name;
                dto.surname = personHeaderDto.surname;
                dto.day = DateUtils.getStartOfDay(workTimeDto.startDate);
                dto.workMinutes = 0L;
                if((!dto.day.before(startDate)) && (!dto.day.after(endDate))) {
                    result.add(dto);
                }
            }
            dto.workMinutes += minutesBetween(workTimeDto.startDate, workTimeDto.endDate);
        }
        return result;
    }

    protected long minutesBetween(Date startTime, Date endTime) {
        long duration = endTime.getTime() - startTime.getTime();
        DateTime dt = new DateTime(endTime);
        if ((dt.getHourOfDay() == 23)&&(dt.getMinuteOfHour()==59)&&(dt.getSecondOfMinute() == 59)) {
            duration++;
        }
        return TimeUnit.MILLISECONDS.toMinutes(duration);
    }

    protected PersonDailyWorkTimeDto findPerson(long personId, Date day, List<PersonDailyWorkTimeDto> workTimeList) {
        PersonDailyWorkTimeDto result = null;
        for (PersonDailyWorkTimeDto personDailyWorkTimeDto : workTimeList) {
            if ((personDailyWorkTimeDto.personId.equals(personId)) && (personDailyWorkTimeDto.day.equals(DateUtils.getStartOfDay(day)))) {
                result = personDailyWorkTimeDto;
                break;
            }
        }
        return result;
    }


    protected  List<PersonWorkTimeDto> splitToDays(PersonWorkTimeDto period,Date endDate) {
        List<PersonWorkTimeDto> result = new ArrayList<>();
        endDate = DateUtils.getEndOfDay(endDate);

        if (period.endDate == null) {
            period.endDate = endDate;
        }
        if(endDate.after(period.endDate)){
            endDate = period.endDate;
        }
        PersonWorkTimeDto dto;
        Calendar theDay = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        theDay.setTime(period.startDate);
        endTime.setTime(endDate);
        while (theDay.before(endTime)) {
            dto = new PersonWorkTimeDto();
            dto.personId = period.personId;
            /* tu problem */
            dto.startDate = DateUtils.getStartOfDay(theDay.getTime());
            dto.endDate = DateUtils.getEndOfDay(theDay.getTime());
            result.add(dto);
            theDay.add(Calendar.DATE, 1);
        }
        Collections.sort(result, new Comparator<PersonWorkTimeDto>() {
            @Override
            public int compare(PersonWorkTimeDto o1, PersonWorkTimeDto o2) {
                return o1.startDate.compareTo(o2.startDate);
            }
        });
        if(!result.isEmpty()) {
            result.get(0).startDate = period.startDate;
        }
        result = setEndDateOfLastPeriod(result, endDate);
        return result;
    }

    protected List<PersonWorkTimeDto> setEndDateOfLastPeriod(List<PersonWorkTimeDto> personWorkTimeList, Date endDate){
        Date today = new Date();
        if((personWorkTimeList != null) && !(personWorkTimeList.isEmpty()) ) {
                if (endDate.before(today)) {
                    personWorkTimeList.get(personWorkTimeList.size() - 1).endDate = endDate;
                } else {
                    personWorkTimeList.get(personWorkTimeList.size() - 1).endDate = new Date();
                }
        }
        return personWorkTimeList;
    }

    protected PersonHeaderDto findPersonById(long personId) {
        PersonHeaderDto result = new PersonHeaderDto();
        for (PersonHeaderDto person : personDao.getAllHeaders()) {
            if (person.id.equals(personId)) {
                result = person;
            }
        }
        return result;
    }
}
