package com.fungisearch.fudriver.zarobki.query.service;

import com.fungisearch.fudriver.zarobki.query.dao.ZarobkiDao;
import com.fungisearch.fudriver.zarobki.query.dto.PickerZarobkiDto;
import com.fungisearch.fudriver.zarobki.query.dto.PickerZarobkiTypeGroupsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Merges results in type groups with other like hours, total, export etc.
 */
@Service
public class PickerZarobkiService {

    @Autowired
    private ZarobkiDao zarobkiDao;

    public List<PickerZarobkiDto> getReportForLeader(Date startDay, Date endDay) throws ParseException {
        List<PickerZarobkiDto> result = zarobkiDao.getPickerZarobki(startDay, endDay);
        Map<Long,List<PickerZarobkiTypeGroupsDto>> totalByGroups = zarobkiDao.getPickerZarobkiByGroups(startDay,endDay);
        Map<Long, Long> pickerHours = zarobkiDao.getPickerHours(getStartOfDay(startDay),getEndOfDay(endDay));
        for(PickerZarobkiDto dto: result){
            dto.totalByGroups = totalByGroups.get(dto.id);
            if(pickerHours.containsKey(dto.id)){
                dto.minutes = pickerHours.get(dto.id);
            } else {
                dto.minutes = 0L;
            }
        }
        return result;
    }

    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    private static Date localDateTimeToDate(LocalDateTime startOfDay) {
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

}
