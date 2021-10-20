package com.fungisearch.fudriver.reclassification.estimation.command.service;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.reclassification.estimation.query.dao.EstimationDao;
import com.fungisearch.fudriver.reclassification.estimation.query.dto.CycleEstimationDetailDto;
import com.fungisearch.fudriver.reclassification.estimation.query.dto.CycleEstimationHeaderDto;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HarvestEstimateService {

    private final EstimationDao estimationDao;

    public HarvestEstimateService(EstimationDao estimationDao) {
        this.estimationDao = estimationDao;
    }

    public void countEstimations(int squareMeters) {
        List<CycleEstimationHeaderDto> headers = estimationDao.getAllCycles();
    }


    public int getAverageHarvestByDay(Integer day, List<CycleEstimationHeaderDto> headers, Integer groupId) {
        List<Integer> allHarvests = headers
                .stream()
                .map(h -> getHarvestInGivenDay(groupId, DateUtils.getDateAfterNumberOfDays(h.initDate, day), h.details) / (h.squareMeters > 0 ? h.squareMeters : 1))
                .collect(Collectors.toList());

        if(allHarvests.size() > 0){
            return allHarvests.stream().reduce(0,Integer::sum) / allHarvests.size() * 100;
        }
        return 0;
    }

    private int getHarvestInGivenDay(Integer groupId, Date day, List<CycleEstimationDetailDto> detailDtos) {
        return detailDtos
                .stream()
                .filter(d -> d.groupId.equals(groupId))
                .filter(d -> DateUtils.isTheSameDay(d.day, day))
                .map(d -> d.weight)
                .findAny()
                .orElse(0);
    }


}
