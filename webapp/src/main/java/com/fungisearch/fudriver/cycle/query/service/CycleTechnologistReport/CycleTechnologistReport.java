package com.fungisearch.fudriver.cycle.query.service.CycleTechnologistReport;

import com.fungisearch.fudriver.cycle.query.dao.CycleDao;
import com.fungisearch.fudriver.cycle.query.dto.CycleTechnologistDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Raport dla porównywania wyników technologów w cyklach zamkniętych w danym okresie
 */
@Service
public class CycleTechnologistReport {

    private final CycleDao cycleDao;

    @Autowired
    public CycleTechnologistReport(CycleDao cycleDao) {
        this.cycleDao = cycleDao;
    }

    public CycleTechnologistDto getReport(int startDate, int endDate){

        List<TechnoCycleHeader> headers = cycleDao.findTechnoHeaders(startDate, endDate);
        List<TechnoCycleDetail> details = cycleDao.findTechnoDetails(startDate, endDate);
        CycleTechnologistDto result = new CycleTechnologistDto();
        result = countTotals(result,details,headers);
        result.headers = new CycleTechnologistHeadersCollector(headers,details).getHeaders();
        result.details = new CycleTechnologistDetailsCollector(headers,details).getDetails();
        return result;
    }

    private CycleTechnologistDto countTotals(CycleTechnologistDto result, List<TechnoCycleDetail> details, List<TechnoCycleHeader> headers) {
        result.totalInne = details.stream().mapToLong(d -> d.inne).sum();
        result.totalKraj = details.stream().mapToLong(d -> d.kraj).sum();
        result.totalExport = details.stream().mapToLong(d -> d.export).sum();
        result.total = details.stream().mapToLong(d -> d.total).sum();
        result.cyclesAmount = headers.stream().mapToInt(h -> h.cycleAmount).sum();
        result.totalArea = headers.stream().mapToLong(h -> h.area).sum();
        result.totalWeight = headers.stream().mapToLong(h ->h.weight).sum();
        result.kgM = (result.totalArea > 0) ? (result.total / result.totalArea) : 0;
        result.kgT = (result.totalWeight > 0) ? (result.total * 1000 / result.totalWeight) : 0;
        result.quality = (result.total > 0) ? ((result.totalExport * 10000) / (result.total) ) : 0;
        return result;
    }
}
