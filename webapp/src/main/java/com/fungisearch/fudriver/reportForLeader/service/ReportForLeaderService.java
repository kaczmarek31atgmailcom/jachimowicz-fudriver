package com.fungisearch.fudriver.reportForLeader.service;


import com.fungisearch.fudriver.person.person.query.dao.PersonDao;
import com.fungisearch.fudriver.reportForLeader.dao.ReportForLeaderDao;
import com.fungisearch.fudriver.reportForLeader.dto.WorkPeriodDto;
import com.fungisearch.fudriver.reportForLeader.model.CollectedByChamber;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportForLeaderService {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ReportForLeaderDao reportForLeaderDao;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyymmdd");
    private static final int MINUTES_IN_HOUR = 60;
    private static final int ROUND_FACTOR = 100;
    private static final int MINUTES_FORMAT_LENGTH = 2;

    /*

    /**
     * Oblicza procent kraju we wszystkich grzybach
     *
     * @param export
     * @param kraj
     * @return
     */
    private double countKrajFactor(double export, double kraj) {
        double result = 0;
        if (kraj > 0) {
            result = (kraj / (kraj + export)) * 100;
            result = (double) Math.round(result * ROUND_FACTOR) / ROUND_FACTOR;
        }
        return result;
    }

    /**
     * Oblicza ilosc zebranych kilogramow na godzine
     *
     * @param export
     * @param kraj
     * @param minutes
     * @return
     */
    double countTotalPerHour(double export, double kraj, long minutes) {
        if (((export + kraj) > 0) && (minutes > 0)) {
            double result = (export + kraj) / minutes * MINUTES_IN_HOUR;
            return (double) Math.round(result * ROUND_FACTOR) / ROUND_FACTOR;
        } else {
            return 0.0;
        }
    }

    /**
     * Przelicza ilość minut na reprezentację tekstową w formacie godziny:minuty
     *
     * @param totalMinutes
     * @return
     */
    String minutesToHours(Long totalMinutes) {
        String result;
        int hours = (int) (totalMinutes / MINUTES_IN_HOUR);
        int minutes = (int) (totalMinutes % MINUTES_IN_HOUR);
        String minutesString = String.valueOf(minutes);
        while (minutesString.length() < MINUTES_FORMAT_LENGTH) {
            minutesString = "0" + minutesString;
        }
        result = String.valueOf(hours) + ":" + minutesString;
        return result;
    }

    /**
     * Zwraca listę obiektów CollectedByChamber pokazujących ilość kraju i exportu w halach
     * Uzywane w raporcie dla leaderki
     */
    public List<CollectedByChamber> getCollectedByChumber(String startDate, String endDate) {
        return reportForLeaderDao.getCollectedByChamber(startDate, endDate);
    }


    public Map<Long, Long> getWorkingMinutes(List<WorkPeriodDto> workPeriods) {
        Map<Long, Long> result = new HashMap<Long, Long>();
        for (WorkPeriodDto dto : workPeriods) {
            if (dto.endDate == null) {
                dto.endDate = new Date();
            }
            DateTime startTime = new DateTime(dto.startDate);
            DateTime endTime = new DateTime(dto.endDate);
            Duration duration = new Duration(startTime, endTime);
            long minutes = duration.getStandardMinutes();
            if (result.containsKey(dto.personId)) {
                long newValue = result.get(dto.personId) + minutes;
                result.put(dto.personId, newValue);
            } else {
                result.put(dto.personId, minutes);
            }
        }
        return result;
    }

    public List<WorkPeriodDto> cutStartAndEndDates(List<WorkPeriodDto> workPeriods, Date startDate, Date endDate){
        DateTime endTime = new DateTime(endDate).millisOfDay().withMaximumValue();
        endDate = endTime.toDate();
        List<WorkPeriodDto> result = new ArrayList<WorkPeriodDto>();
        for(WorkPeriodDto workPeriodDto: workPeriods){
            if(workPeriodDto.startDate.compareTo(startDate)< 0){
                workPeriodDto.startDate = startDate;
            }
            if(workPeriodDto.endDate == null){
                workPeriodDto.endDate = new Date();
            }

            if(workPeriodDto.endDate.compareTo(endDate)>0){
                workPeriodDto.endDate = endDate;
            }
            result.add(workPeriodDto);
        }
    return result;
    }

}



