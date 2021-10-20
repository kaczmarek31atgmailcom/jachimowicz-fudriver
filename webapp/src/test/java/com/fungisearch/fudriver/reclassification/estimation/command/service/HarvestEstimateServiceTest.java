package com.fungisearch.fudriver.reclassification.estimation.command.service;

import com.fungisearch.fudriver.reclassification.estimation.command.repository.HarvestEstimationRepository;
import com.fungisearch.fudriver.reclassification.estimation.query.dto.CycleEstimationDetailDto;
import com.fungisearch.fudriver.reclassification.estimation.query.dto.CycleEstimationHeaderDto;
import com.mchange.util.AssertException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class HarvestEstimateServiceTest {

    @Mock
    private HarvestEstimationRepository harvestEstimationRepository;

    @InjectMocks
    private HarvestEstimateService harvestEstimateService;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void shouldGetAverageHarvestByDayOfSingleCycle(){
        //Given
        CycleEstimationHeaderDto header = getHeader(100,getDateByString("2019-10-10"),1L);
        CycleEstimationDetailDto detail = getDetail(100,1,getDateByString("2019-10-11"));
        CycleEstimationDetailDto detail1 = getDetail(120,1,getDateByString("2019-10-12"));
        header.details.add(detail);
        header.details.add(detail1);
        List<CycleEstimationHeaderDto> headers = new ArrayList<>();
        headers.add(header);
        //When
        //Integer result0 = harvestEstimateService.getAverageHarvestByDay(0,headers,1);
        //Integer result1 = harvestEstimateService.getAverageHarvestByDay(1,headers,1);
        Integer result2 = harvestEstimateService.getAverageHarvestByDay(2,headers,1);
        System.out.println(result2);
        //System.out.println("result0: " + result0);
        //System.out.println("result1: " + result1);
        //System.out.println("result2: " + result2);
        //Then
        //Assert.assertEquals(new Integer(0),result0);
        //Assert.assertEquals(new Integer(100),result1);
        //Assert.assertEquals(new Integer(120),result2);
    }


    private Date getDateByString(String day){
        Date initDate = null;
        try {
            initDate = format.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return initDate;
    }

    private CycleEstimationHeaderDto getHeader(int squareMeters, Date initDate, long cycleId){
        CycleEstimationHeaderDto header = new CycleEstimationHeaderDto();
        header.squareMeters = squareMeters;
        header.initDate = initDate;
        header.cycleId = cycleId;
        header.details = new ArrayList<>();
        return header;
    }

    private CycleEstimationDetailDto getDetail(int weight,int groupId, Date day){
        CycleEstimationDetailDto detail = new CycleEstimationDetailDto();
        detail.weight = weight;
        detail.groupId = groupId;
        detail.day = day;
        return detail;
    }
}
