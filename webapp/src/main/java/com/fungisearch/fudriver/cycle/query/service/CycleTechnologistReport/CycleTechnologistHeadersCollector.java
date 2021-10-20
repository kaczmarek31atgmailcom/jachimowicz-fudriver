package com.fungisearch.fudriver.cycle.query.service.CycleTechnologistReport;

import com.fungisearch.fudriver.cycle.query.dto.CycleTechnologistHeaderDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Generuje headery raportu pracy technologów dla zamkniętych cykli
 */
public class CycleTechnologistHeadersCollector {

    private final List<TechnoCycleHeader> headers;
    private final List<TechnoCycleDetail> details;

    CycleTechnologistHeadersCollector(List<TechnoCycleHeader> headers, List<TechnoCycleDetail> details) {
        this.headers = headers;
        this.details = details;
    }

    public List<CycleTechnologistHeaderDto> getHeaders() {
        List<CycleTechnologistHeaderDto> result = getCycleTechnologistHeaderDtos(headers);
        result = countTechnologistSubTotals(result, details);
        return result;
    }

    private List<CycleTechnologistHeaderDto> countTechnologistSubTotals(List<CycleTechnologistHeaderDto> result, List<TechnoCycleDetail> details) {
        result.forEach(r -> r.totalInne = getInneTechnologistTotal(r.technologistId, details));
        result.forEach(r -> r.totalKraj = getKrajTechnologistTotal(r.technologistId, details));
        result.forEach(r -> r.totalExport = getExportTechnologistTotal(r.technologistId, details));
        result.forEach(r -> r.total = getTechnologistTotal(r.technologistId, details));
        result.forEach(r -> r.quality = getQuality(r.totalExport, r.total));
        result.forEach(r -> r.kgT = getKgT(r.totalWeight, r.total));
        result.forEach(r -> r.kgM = getKgM(r.totalArea, r.total));
        return result;
    }

    private long getInneTechnologistTotal(long technologistId, List<TechnoCycleDetail> details) {
        return details.stream()
                .filter(d -> d.technologistId == technologistId)
                .mapToLong(d -> d.inne)
                .sum();
    }

    private long getKrajTechnologistTotal(long technologistId, List<TechnoCycleDetail> details) {
        return details.stream()
                .filter(d -> d.technologistId == technologistId)
                .mapToLong(d -> d.kraj)
                .sum();
    }

    private long getExportTechnologistTotal(long technologistId, List<TechnoCycleDetail> details) {
        return details.stream()
                .filter(d -> d.technologistId == technologistId)
                .mapToLong(d -> d.export)
                .sum();
    }

    private long getTechnologistTotal(long technologistId, List<TechnoCycleDetail> details) {
        return details.stream()
                .filter(d -> d.technologistId == technologistId)
                .mapToLong(d -> d.total)
                .sum();
    }

    private long getQuality(long export, long total) {
        return (total > 0) ? ((export * 10000) / total) : 0;
    }

    private long getKgT(long weight, long total) {
        return (weight > 0) ? ((total * 1000) / weight) / 10 : 0;
    }

    private long getKgM(long area, long total) {
        return (area > 0) ? (total / area) : 0;
    }

    private List<CycleTechnologistHeaderDto> getCycleTechnologistHeaderDtos(List<TechnoCycleHeader> headers) {
        List<CycleTechnologistHeaderDto> result = new ArrayList<>();
        for (TechnoCycleHeader header : headers) {
            CycleTechnologistHeaderDto dto = new CycleTechnologistHeaderDto();
            dto.technologistId = header.technologistId;
            dto.technologistName = header.technologistName;
            dto.technologistSurname = header.technologistSurname;
            dto.totalWeight = header.weight;
            dto.totalArea = header.area;
            dto.cyclesAmount = header.cycleAmount;
            result.add(dto);
        }
        return result;
    }
}