package com.fungisearch.fudriver.person.person.query.service;

import com.fungisearch.fudriver.person.person.query.dao.PersonDao;
import com.fungisearch.fudriver.person.person.query.dto.ForeignerAlertDto;
import com.fungisearch.fudriver.person.person.query.dto.PersonHeaderDto;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PersonHeadersService {
    private final PersonDao personDao;

    public PersonHeadersService(PersonDao personDao) {
        this.personDao = personDao;
    }

    List<PersonHeaderDto> getActiveHeaders() {
        List<PersonHeaderDto> headers = personDao.getActiveHeaders();
        ForeignerAlertDto alerts = personDao.getForeignerAlert();
        return checkAlerts(headers, alerts);
    }

    List<PersonHeaderDto> getAllHeaders() {
        List<PersonHeaderDto> headers = personDao.getAllHeaders();
        ForeignerAlertDto alerts = personDao.getForeignerAlert();
        return checkAlerts(headers, alerts);
    }


    private List<PersonHeaderDto> checkAlerts(List<PersonHeaderDto> headers, ForeignerAlertDto alerts) {
        for (PersonHeaderDto dto : headers) {
            if (dto.isForeigner && ((isInPeriod(alerts.passportDays, dto.koniecWaznosciPaszportu)) ||
                    (isInPeriod(alerts.statementDays, dto.koniecWaznosciZezwolenia)) ||
                    (isInPeriod(alerts.stayDays, dto.dataWymeldowania)) ||
                    (isInPeriod(alerts.visaDays, dto.koniecWaznosciWizy)))) {
                dto.isAlertFired = true;

            }
        }
    return headers;
    }

    private boolean isInPeriod(int period, Date day){
        if(period == 0){
            return false;
        }
        if(day != null){
            LocalDate theDay = new LocalDate(day);
            return Days.daysBetween(new LocalDate(),theDay).getDays() <= period;
        } else {
            return true;
        }
    }
}
