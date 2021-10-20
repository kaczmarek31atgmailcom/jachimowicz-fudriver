package com.fungisearch.fudriver.timeRecorder.query.dao;

import com.fungisearch.fudriver.timeRecorder.query.dto.PayedSalaryWorkTimeDto;
import com.fungisearch.fudriver.timeRecorder.query.dto.PersonDailyWorkTimeDto;
import com.fungisearch.fudriver.timeRecorder.query.dto.PersonWorkTimeDto;
import com.fungisearch.fudriver.timeRecorder.query.dto.WorkTimeDto;
import com.fungisearch.fudriver.person.person.query.yearBar.model.BarPeriod;
import com.fungisearch.fudriver.zarobki.query.dto.PickerZarobkiDto;

import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 19.05.16.
 */
public interface WorkTimeLogDao {
    List<BarPeriod> getPeriodsForPerson(Long personId,Date startDate, Date endDate);
    List<WorkTimeDto> getWorkTimeForPersonInPeriod(Long personId, Date startDate, Date endDate);
    List<PersonWorkTimeDto> getPersonWorkTime(Date startDate, Date endDate);
    List<PayedSalaryWorkTimeDto> getWorkTime(int personId, Date startDay, Date endDay);
    List<PersonDailyWorkTimeDto> getWorkTimeIncludingPauses(Date startTime, Date endTime);
}
