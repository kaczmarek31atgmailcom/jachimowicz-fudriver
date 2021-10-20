package com.fungisearch.fudriver.cycle.query.service;

import com.fungisearch.fudriver.cycle.command.model.PeriodName;
import com.fungisearch.fudriver.cycle.query.dao.CycleDao;
import com.fungisearch.fudriver.cycle.query.dto.CycleDatesDto;
import com.fungisearch.fudriver.cycle.query.dto.CycleDayDto;
import com.fungisearch.fudriver.cycle.query.dto.CyclePeriodDto;
import com.fungisearch.fudriver.zarobki.query.dao.ZarobkiDao;
import com.fungisearch.fudriver.zarobki.query.dto.DailyHarvestByTypeGroupDto;
import com.fungisearch.fudriver.zarobki.query.dto.ZarobkiByCycleDto;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CycleDailyReportService {
    private final CycleDao cycleDao;
    private final ZarobkiDao zarobkiDao;

    @Autowired
    public CycleDailyReportService(CycleDao cycleDao, ZarobkiDao zarobkiDao) {
        this.cycleDao = cycleDao;
        this.zarobkiDao = zarobkiDao;
    }

    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");

    public List<CyclePeriodDto> getCycleDetails(long cycleId) {
        CycleDatesDto cycleDates = cycleDao.findCycleDatesById(cycleId);
        List<ZarobkiByCycleDto> zarobki = zarobkiDao.getZarobkiByCycle(cycleId);
        List<DailyHarvestByTypeGroupDto> groupsHarvest = zarobkiDao.getDailyHarvestByGroupsForCycle(cycleId);
        CyclePeriodDto firstPeriod = getPeriodTotals(getFirstPeriod(cycleDates,zarobki),cycleDates);
        firstPeriod.periodName = PeriodName.FIRST_PERIOD;
        firstPeriod.cycleDates = getFirstPeriod(cycleDates,zarobki);
        firstPeriod.typeGroups = getGroupHarvestInPeriod(cycleDates.startFirstPeriod,getFirstPeriodEndDay(cycleDates),groupsHarvest);
        CyclePeriodDto secondPeriod = getPeriodTotals(getSecondPeriod(cycleDates,zarobki), cycleDates);
        secondPeriod.periodName = PeriodName.SECOND_PERIOD;
        secondPeriod.cycleDates = getSecondPeriod(cycleDates,zarobki);
        secondPeriod.typeGroups = getGroupHarvestInPeriod(cycleDates.startSecondPeriod,getSecondPeriodEndDay(cycleDates),groupsHarvest);
        CyclePeriodDto thirdPeriod = getPeriodTotals(getThirdPeriod(cycleDates,zarobki), cycleDates);
        thirdPeriod.periodName = PeriodName.THIRD_PERIOD;
        thirdPeriod.cycleDates = getThirdPeriod(cycleDates,zarobki);
        thirdPeriod.typeGroups = getGroupHarvestInPeriod(cycleDates.startThirdPeriod,getThirdPeriodEndDay(cycleDates),groupsHarvest);
        List<CyclePeriodDto> result = new ArrayList<>();
        result.add(firstPeriod);
        result.add(secondPeriod);
        result.add(thirdPeriod);
        return result;
    }

    protected List<DailyHarvestByTypeGroupDto> getGroupHarvestInPeriod(Integer startDate, LocalDateTime endDay, List<DailyHarvestByTypeGroupDto> groupHarvest) {
        if ((startDate == null) || !(startDate > 0)) {
            return new ArrayList<>();
        } else {
            LocalDateTime startDay = formatter.parseLocalDateTime(startDate.toString());
            return groupHarvest.stream()
                    .filter(g -> new LocalDateTime(g.date).isAfter(startDay.minusDays(1)))
                    .filter(g -> new LocalDateTime(g.date).isBefore(endDay))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
    }
    protected List<CycleDayDto> getFirstPeriod(CycleDatesDto cycleDates, List<ZarobkiByCycleDto> zarobki) {
        LocalDateTime endPeriod = getFirstPeriodEndDay(cycleDates);
        return zarobki.stream()
                .filter(z -> new LocalDateTime(z.date).isBefore((endPeriod)))
                .map(z -> convertZarobkiToCycleDay(z, cycleDates))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    protected List<CycleDayDto> getSecondPeriod(CycleDatesDto cycleDates, List<ZarobkiByCycleDto> zarobki) {
        if ((cycleDates.startSecondPeriod == null) || !(cycleDates.startSecondPeriod > 0)) {
            return new ArrayList<>();
        }
        LocalDateTime startPeriod = formatter.parseLocalDateTime(cycleDates.startSecondPeriod.toString()).minusDays(1);
        LocalDateTime endPeriod = getSecondPeriodEndDay(cycleDates);
        return zarobki.stream()
                .filter(z -> new LocalDateTime(z.date).isBefore((endPeriod)))
                .filter(z -> new LocalDateTime(z.date).isAfter(startPeriod))
                .map(z -> convertZarobkiToCycleDay(z, cycleDates))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    protected List<CycleDayDto> getThirdPeriod(CycleDatesDto cycleDates, List<ZarobkiByCycleDto> zarobki) {
        if ((cycleDates.startThirdPeriod == null) || !(cycleDates.startThirdPeriod > 0)) {
            return new ArrayList<>();
        }
        LocalDateTime startPeriod = formatter.parseLocalDateTime(cycleDates.startThirdPeriod.toString()).minusDays(1);
        LocalDateTime endPeriod = getThirdPeriodEndDay(cycleDates);
        return zarobki.stream()
                .filter(z -> new LocalDateTime(z.date).isBefore((endPeriod).plusDays(1)))
                .filter(z -> new LocalDateTime(z.date).isAfter(startPeriod))
                .map(z -> convertZarobkiToCycleDay(z, cycleDates))
                .collect(Collectors.toCollection(ArrayList::new));
    }


    protected CycleDayDto convertZarobkiToCycleDay(ZarobkiByCycleDto zarobki, CycleDatesDto cycleDates) {
        CycleDayDto dto = new CycleDayDto();
        dto.date = zarobki.getDate();
        dto.summaryKraj = zarobki.totalKrajG;
        dto.summaryExport = zarobki.totalExportG;
        dto.summaryInne = zarobki.totalInneG;
        //dto.summaryTotal = dto.summaryKraj + dto.summaryExport + dto.summaryInne;
        dto.summaryTotal = dto.summaryKraj + dto.summaryExport;

        if (cycleDates.area > 0) {
            dto.kgM = dto.summaryTotal / cycleDates.area;
        } else {
            dto.kgM = 0;
        }
        if (cycleDates.weight > 0) {
            dto.kgTon = (dto.summaryTotal * 1000 / cycleDates.weight);

            dto.kgDryTon = (dto.summaryTotal * 1000) / (cycleDates.weight - (cycleDates.weight * (cycleDates.humidity)) / 100);
        } else {
            dto.kgDryTon = 0;
            dto.kgTon = 0;
        }
        dto.quality = zarobki.quality;
        return dto;
    }

    private LocalDateTime getFirstPeriodEndDay(CycleDatesDto cycleDates) {
        LocalDateTime endOfFirstPeriod;
        if ((cycleDates.startSecondPeriod != null) && (cycleDates.startSecondPeriod > 0)) {
            endOfFirstPeriod = formatter.parseLocalDateTime(cycleDates.startSecondPeriod.toString());
        } else {
            endOfFirstPeriod = getSecondPeriodEndDay(cycleDates);
        }
        return endOfFirstPeriod;
    }

    private LocalDateTime getSecondPeriodEndDay(CycleDatesDto cycleDates) {
        LocalDateTime endOfSecondPeriod;
        if ((cycleDates.startThirdPeriod != null) && (cycleDates.startThirdPeriod > 0)){
            endOfSecondPeriod = formatter.parseLocalDateTime(cycleDates.startThirdPeriod.toString());
        } else {
            endOfSecondPeriod = getThirdPeriodEndDay(cycleDates);
        }
        return endOfSecondPeriod;
    }

    private LocalDateTime getThirdPeriodEndDay(CycleDatesDto cycleDates) {
        LocalDateTime endOfThirdPeriod;
        if ((cycleDates.endDate != null) && (cycleDates.endDate > 0)){
            endOfThirdPeriod = formatter.parseLocalDateTime(cycleDates.endDate.toString());
        } else {
            endOfThirdPeriod = new LocalDateTime();
        }
        return endOfThirdPeriod;
    }


    protected CyclePeriodDto getPeriodTotals(List<CycleDayDto> days, CycleDatesDto cycleDates) {
        CyclePeriodDto cyclePeriodDto = new CyclePeriodDto();
        cyclePeriodDto.totalKraj = days.stream().mapToLong(CycleDayDto::getSummaryKraj).sum();
        cyclePeriodDto.totalInne = days.stream().mapToLong(CycleDayDto::getSummaryInne).sum();
        cyclePeriodDto.totalExport = days.stream().mapToLong(CycleDayDto::getSummaryExport).sum();
        //cyclePeriodDto.total = cyclePeriodDto.totalInne + cyclePeriodDto.totalKraj + cyclePeriodDto.totalExport;
        cyclePeriodDto.total = cyclePeriodDto.totalKraj + cyclePeriodDto.totalExport;
        cyclePeriodDto.kgM = (cycleDates.area > 0) ? (cyclePeriodDto.total / cycleDates.area) : 0;
        cyclePeriodDto.kgTon = (cycleDates.weight > 0) ? ((cyclePeriodDto.total * 1000) / cycleDates.weight) : 0;
        cyclePeriodDto.kgDryTon = ((cycleDates.weight > 0) && (cycleDates.humidity >= 0) && (cycleDates.humidity < 100))
                ? ((cyclePeriodDto.total * 1000) / (cycleDates.weight - ((cycleDates.weight * cycleDates.humidity)/ 100)))
                : 0;
        cyclePeriodDto.quality = (cyclePeriodDto.total > 0) ? (cyclePeriodDto.totalExport * 10000) / cyclePeriodDto.total : 0;
        return cyclePeriodDto;
    }
}
