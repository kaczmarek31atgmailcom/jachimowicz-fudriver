package com.fungisearch.fudriver.zarobki.query.service.stand;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.person.person.query.dao.PersonDao;
import com.fungisearch.fudriver.person.person.query.dto.PersonDto;
import com.fungisearch.fudriver.type.command.model.ExportType;
import com.fungisearch.fudriver.zarobki.query.dao.ZarobkiDao;
import com.fungisearch.fudriver.zarobki.query.dto.StandDetailDto;
import com.fungisearch.fudriver.zarobki.query.dto.StandHeaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StandService {
    private final PersonDao personDao;
    private final ZarobkiDao zarobkiDao;

    @Autowired
    public StandService(PersonDao personDao, ZarobkiDao zarobkiDao) {
        this.personDao = personDao;
        this.zarobkiDao = zarobkiDao;
    }

    public StandHeaderDto getStandStats(long personId, Date yesterday, Date startPeriod, Date endPeriod) {
        StandHeaderDto result = getPersonName(personId);
        result.lastDayDetails = zarobkiDao.getStandDetails(personId, DateUtils.getStartOfDay(yesterday), DateUtils.getEndOfDay(yesterday));
        result.periodDetails = zarobkiDao.getStandDetails(personId, DateUtils.getStartOfDay(startPeriod), DateUtils.getEndOfDay(endPeriod));
        result.lastDayExport = countExportPercent(result.lastDayDetails);
        result.periodExport = countExportPercent(result.periodDetails);
        return result;
    }

    private int countExportPercent(List<StandDetailDto> details) {
        long total = 0;
        long totalExport = 0;
        int result = 0;
        totalExport = details.stream().filter(o -> o.exportType.equals(ExportType.EXPORT)).mapToLong(o -> o.weight).sum();
        total = details.stream().mapToLong(o -> o.weight).sum();
        if (total > 0) {
            result = (int) ((totalExport * 10000) / total);
        }
        return result;
    }

    private StandHeaderDto getPersonName(long personId) {
        PersonDto person = personDao.getPerson(personId);
        StandHeaderDto header = new StandHeaderDto();
        header.personId = person.id;
        header.personName = person.imie;
        header.personSurname = person.nazwisko;
        header.lastDayDetails = new ArrayList<>();
        header.periodDetails = new ArrayList<>();
        return header;
    }
}
