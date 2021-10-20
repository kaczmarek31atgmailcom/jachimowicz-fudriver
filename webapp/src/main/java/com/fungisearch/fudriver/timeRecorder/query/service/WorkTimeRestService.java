package com.fungisearch.fudriver.timeRecorder.query.service;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.timeRecorder.query.dao.WorkTimeLogDao;
import com.fungisearch.fudriver.timeRecorder.query.dto.DayBarDto;
import com.fungisearch.fudriver.timeRecorder.query.dto.PersonDailyWorkTimeDto;
import com.fungisearch.fudriver.timeRecorder.query.dto.WorkTimeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 20.05.16.
 */
@RestController
public class WorkTimeRestService {

    @Autowired
    private WorkTimeLogService workTimeLogService;

    @Autowired
    private WorkTimeLogDao workTimeLogDao;

    @Autowired
    private PersonWorkTimeService personWorkTimeService;

    @RequestMapping(value = "/rest/person/dayBars", method = RequestMethod.GET, produces = "application/json; charset=UTF8")
    public List<DayBarDto> getDayBars(@RequestParam(value = "personId") Long personId,
                                      @RequestParam(value = "startDay") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDay,
                                      @RequestParam(value = "endDay") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDay) {
        List<DayBarDto> result = workTimeLogService.getDays(personId, DateUtils.getStartOfDay(startDay), DateUtils.getEndOfDay(endDay));
        return result;
    }


    @RequestMapping(value = "/rest/person/workPeriods", method = RequestMethod.GET, produces = "application/json; charset=UTF8")
    public List<WorkTimeDto> getWorkPeriods(@RequestParam(value = "personId") Long personId,
                                            @RequestParam(value = "startDay") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDay,
                                            @RequestParam(value = "endDay") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDay) {
        return workTimeLogDao.getWorkTimeForPersonInPeriod(personId, DateUtils.getStartOfDay(startDay), DateUtils.getEndOfDay(endDay));
    }

    @RequestMapping(value = "/rest/person/workMinutes/{startDate}/{endDate}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public List<PersonDailyWorkTimeDto> getWorkMinutes(@PathVariable(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                       @PathVariable(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        //return personWorkTimeService.getDailyWorkMinutes(DateUtils.getStartOfDay(startDate), DateUtils.getEndOfDay(endDate));
        return workTimeLogDao.getWorkTimeIncludingPauses(DateUtils.getStartOfDay(startDate), DateUtils.getEndOfDay(endDate));
    }

    @RequestMapping(value = "/rest/currtime")
    public Date getCurrentTime(){
        return new Date();
        //LocalDateTime now = LocalDateTime.now();
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        //return now.format(formatter);
    }
}
