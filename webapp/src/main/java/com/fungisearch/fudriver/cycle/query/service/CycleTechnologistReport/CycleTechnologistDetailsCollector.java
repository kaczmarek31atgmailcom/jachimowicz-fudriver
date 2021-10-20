package com.fungisearch.fudriver.cycle.query.service.CycleTechnologistReport;

import com.fungisearch.fudriver.cycle.query.dto.CycleTechnologistDetailDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Przetwarza dane do detail raportu wyników technologów w zamkniętych cyklach
 */
public class CycleTechnologistDetailsCollector {
    private final List<TechnoCycleHeader> headers;
    private final List<TechnoCycleDetail> details;

    CycleTechnologistDetailsCollector(List<TechnoCycleHeader> headers, List<TechnoCycleDetail> details) {
        this.headers = headers;
        this.details = details;
    }

    public List<CycleTechnologistDetailDto> getDetails() {
        return details.stream()
                .map(this::createDay)
                .collect(Collectors.toList());
    }

    private CycleTechnologistDetailDto createDay(TechnoCycleDetail detail) {
        CycleTechnologistDetailDto dto = new CycleTechnologistDetailDto();
        dto.date = detail.day;
        dto.technologistId = detail.technologistId;
        dto.technologistName = detail.technologistName;
        dto.technologistSurname = detail.technologistSurname;
        dto.totalInne = detail.inne;
        dto.totalKraj = detail.kraj;
        dto.totalExport = detail.export;
        dto.total = detail.total;
        dto.quality = (detail.total > 0) ? (detail.export * 10000) / detail.total : 0;
        dto.totalWeight = getTotalWeight(detail.technologistId);
        dto.totalArea = getTotalArea(detail.technologistId);
        dto.kgM = (dto.totalArea > 0) ? dto.total / dto.totalArea : 0;
        dto.kgT = (dto.totalWeight > 0) ? ((dto.total * 100000) / dto.totalWeight) : 0;
        return dto;
    }

    private long getTotalWeight(long technologistId) {
        return headers.stream()
                .filter(h -> h.technologistId == technologistId)
                .mapToLong(h -> h.weight)
                .sum();
    }

    private long getTotalArea(long technologistId) {
        return headers.stream()
                .filter(h -> h.technologistId == technologistId)
                .mapToLong(h -> h.area)
                .sum();
    }
}
