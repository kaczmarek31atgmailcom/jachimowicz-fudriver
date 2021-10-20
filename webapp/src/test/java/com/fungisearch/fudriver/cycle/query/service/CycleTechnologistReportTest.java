package com.fungisearch.fudriver.cycle.query.service;


import com.fungisearch.fudriver.cycle.query.dao.CycleDao;
import com.fungisearch.fudriver.cycle.query.dto.CycleTechnologistDetailDto;
import com.fungisearch.fudriver.cycle.query.dto.CycleTechnologistDto;
import com.fungisearch.fudriver.cycle.query.dto.CycleTechnologistHeaderDto;
import com.fungisearch.fudriver.cycle.query.service.CycleTechnologistReport.CycleTechnologistReport;
import com.fungisearch.fudriver.cycle.query.service.CycleTechnologistReport.TechnoCycleDetail;
import com.fungisearch.fudriver.cycle.query.service.CycleTechnologistReport.TechnoCycleHeader;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CycleTechnologistReportTest {

    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");

    @Mock
    CycleDao cycleDao;

    @InjectMocks
    CycleTechnologistReport cycleTechnologistReport;

    @Test
    public void shouldReturnTwoElementHeaderList() {
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        //When
        List<CycleTechnologistHeaderDto> tested = cycleTechnologistReport.getReport(1234, 1234).headers;
        //Then
        assertEquals(2, tested.size());
    }

    @Test
    public void shouldReturnValidHeaderCycleAmounts() {
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        //When
        List<CycleTechnologistHeaderDto> tested = cycleTechnologistReport.getReport(1234, 1234).headers;
        tested.sort(Comparator.comparing(CycleTechnologistHeaderDto::getTechnologistId));
        //Then
        assertEquals(10, tested.get(0).cyclesAmount);
        assertEquals(20, tested.get(1).cyclesAmount);
    }


    @Test
    public void shouldReturnValidHeaderCycleAreas() {
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        //When
        List<CycleTechnologistHeaderDto> tested = cycleTechnologistReport.getReport(1234, 1234).headers;
        tested.sort(Comparator.comparing(CycleTechnologistHeaderDto::getTechnologistId));
        //Then
        assertEquals(10, tested.get(0).totalArea);
        assertEquals(20, tested.get(1).totalArea);
    }

    @Test
    public void shouldReturnValidHeaderCycleWeight() {
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        //When
        List<CycleTechnologistHeaderDto> tested = cycleTechnologistReport.getReport(1234, 1234).headers;
        tested.sort(Comparator.comparing(CycleTechnologistHeaderDto::getTechnologistId));
        //Then
        assertEquals(1000, tested.get(0).totalWeight);
        assertEquals(2000, tested.get(1).totalWeight);
    }

    @Test
    public void shouldReturnValidHeaderCycleInne() {
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistHeaderDto> tested = cycleTechnologistReport.getReport(1234, 1234).headers;
        tested.sort(Comparator.comparing(CycleTechnologistHeaderDto::getTechnologistId));
        //Then
        assertEquals(20L, tested.get(0).totalInne);
        assertEquals(60L, tested.get(1).totalInne);
    }

    @Test
    public void shouldReturnValidHeaderCycleKraj() {
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistHeaderDto> tested = cycleTechnologistReport.getReport(1234, 1234).headers;
        tested.sort(Comparator.comparing(CycleTechnologistHeaderDto::getTechnologistId));
        //Then
        assertEquals(50L, tested.get(0).totalKraj);
        assertEquals(60L, tested.get(1).totalKraj);
    }

    @Test
    public void shouldReturnValidHeaderCycleExport() {
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistHeaderDto> tested = cycleTechnologistReport.getReport(1234, 1234).headers;
        tested.sort(Comparator.comparing(CycleTechnologistHeaderDto::getTechnologistId));
        //Then
        assertEquals(120L, tested.get(0).totalExport);
        assertEquals(150L, tested.get(1).totalExport);
    }

    @Test
    public void shouldReturnValidHeaderCycleTotal() {
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistHeaderDto> tested = cycleTechnologistReport.getReport(1234, 1234).headers;
        tested.sort(Comparator.comparing(CycleTechnologistHeaderDto::getTechnologistId));
        //Then
        assertEquals(190L, tested.get(0).total);
        assertEquals(270L, tested.get(1).total);
    }

    @Test
    public void shouldReturnValidHeaderCycleQuality() {
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistHeaderDto> tested = cycleTechnologistReport.getReport(1234, 1234).headers;
        tested.sort(Comparator.comparing(CycleTechnologistHeaderDto::getTechnologistId));
        //Then
        assertEquals(6315L, tested.get(0).quality);
        assertEquals(5555L, tested.get(1).quality);
    }

    @Test
    public void shouldReturnValidHeaderCycleKgT() {
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistHeaderDto> tested = cycleTechnologistReport.getReport(1234, 1234).headers;
        tested.sort(Comparator.comparing(CycleTechnologistHeaderDto::getTechnologistId));
        //Then
        assertEquals(19L, tested.get(0).kgT);
        assertEquals(13L, tested.get(1).kgT);
    }

    @Test
    public void shouldReturnValidHeaderCycleKgM() {
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistHeaderDto> tested = cycleTechnologistReport.getReport(1234, 1234).headers;
        tested.sort(Comparator.comparing(CycleTechnologistHeaderDto::getTechnologistId));
        //Then
        assertEquals(19L, tested.get(0).kgM);
        assertEquals(13L, tested.get(1).kgM);
    }

    @Test
    public void shouldReturnValidInne(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        CycleTechnologistDto tested = cycleTechnologistReport.getReport(1234, 1234);
        //then
        assertEquals(80L,tested.totalInne);
    }

    @Test
    public void shouldReturnValidKraj(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        CycleTechnologistDto tested = cycleTechnologistReport.getReport(1234, 1234);
        //then
        assertEquals(110L,tested.totalKraj);
    }

    @Test
    public void shouldReturnValidExport(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        CycleTechnologistDto tested = cycleTechnologistReport.getReport(1234, 1234);
        //then
        assertEquals(270L,tested.totalExport);
    }

    @Test
    public void shouldReturnValidTotal(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        CycleTechnologistDto tested = cycleTechnologistReport.getReport(1234, 1234);
        //then
        assertEquals(460L,tested.total);
    }

    @Test
    public void shouldReturnValidTotalCyclesAmount(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        CycleTechnologistDto tested = cycleTechnologistReport.getReport(1234, 1234);
        //then
        assertEquals(30L,tested.cyclesAmount);
    }

    @Test
    public void shouldReturnValidTotalCyclesArea(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        CycleTechnologistDto tested = cycleTechnologistReport.getReport(1234, 1234);
        //then
        assertEquals(30L,tested.totalArea);
    }

    @Test
    public void shouldReturnValidTotalCyclesWeight(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        CycleTechnologistDto tested = cycleTechnologistReport.getReport(1234, 1234);
        //then
        assertEquals(3000L,tested.totalWeight);
    }

    @Test
    public void shouldReturnValidTotalCyclesKgM(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        CycleTechnologistDto tested = cycleTechnologistReport.getReport(1234, 1234);
        //then
        assertEquals(15L,tested.kgM);
    }

    @Test
    public void shouldReturnValidTotalCyclesKgT(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        CycleTechnologistDto tested = cycleTechnologistReport.getReport(1234, 1234);
        //then
        assertEquals(153L,tested.kgT);
    }

    @Test
    public void shouldReturnValidTotalQuality(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        CycleTechnologistDto tested = cycleTechnologistReport.getReport(1234, 1234);
        //then
        assertEquals(5869L,tested.quality);
    }


    @Test
    public void detailsListShouldContainSixElements(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistDetailDto> tested = cycleTechnologistReport.getReport(1234, 1234).details;
        tested.sort(Comparator.comparing(CycleTechnologistDetailDto::getTechnologistId));
        //Then
        assertEquals(6,tested.size());
    }

    @Test
    public void detailsListShouldContain3ElementsForEachTechnologis(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistDetailDto> tested = cycleTechnologistReport.getReport(1234, 1234).details;
        List<CycleTechnologistDetailDto> tested1 = tested.stream().filter(t -> (t.technologistId == 1)).collect(Collectors.toList());
        tested1.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        List<CycleTechnologistDetailDto> tested2 = tested.stream().filter(t -> (t.technologistId == 2)).collect(Collectors.toList());
        tested2.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        //Then
        assertEquals(3,tested1.size());
        assertEquals(3,tested2.size());
    }

    @Test
    public void detailsListShouldContainValidDates(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistDetailDto> tested = cycleTechnologistReport.getReport(1234, 1234).details;
        List<CycleTechnologistDetailDto> tested1 = tested.stream().filter(t -> (t.technologistId == 1)).collect(Collectors.toList());
        tested1.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        List<CycleTechnologistDetailDto> tested2 = tested.stream().filter(t -> (t.technologistId == 2)).collect(Collectors.toList());
        tested2.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        //Then
        assertEquals(formatter.parseDateTime("20170101").toDate(),tested1.get(0).date);
        assertEquals(formatter.parseDateTime("20170102").toDate(),tested1.get(1).date);
        assertEquals(formatter.parseDateTime("20170103").toDate(),tested1.get(2).date);

        assertEquals(formatter.parseDateTime("20170101").toDate(),tested2.get(0).date);
        assertEquals(formatter.parseDateTime("20170103").toDate(),tested2.get(1).date);
        assertEquals(formatter.parseDateTime("20170105").toDate(),tested2.get(2).date);
    }

    @Test
    public void detailsListShouldContainValidInne(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistDetailDto> tested = cycleTechnologistReport.getReport(1234, 1234).details;
        List<CycleTechnologistDetailDto> tested1 = tested.stream().filter(t -> (t.technologistId == 1)).collect(Collectors.toList());
        tested1.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        List<CycleTechnologistDetailDto> tested2 = tested.stream().filter(t -> (t.technologistId == 2)).collect(Collectors.toList());
        tested2.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        //Then
        assertEquals(10L,tested1.get(0).totalInne);
        assertEquals(10L,tested1.get(1).totalInne);
        assertEquals(0L,tested1.get(2).totalInne);

        assertEquals(20L,tested2.get(0).totalInne);
        assertEquals(20L,tested2.get(1).totalInne);
        assertEquals(20L,tested2.get(2).totalInne);
    }

    @Test
    public void detailsListShouldContainValidKraj(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistDetailDto> tested = cycleTechnologistReport.getReport(1234, 1234).details;
        List<CycleTechnologistDetailDto> tested1 = tested.stream().filter(t -> (t.technologistId == 1)).collect(Collectors.toList());
        tested1.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        List<CycleTechnologistDetailDto> tested2 = tested.stream().filter(t -> (t.technologistId == 2)).collect(Collectors.toList());
        tested2.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        //Then
        assertEquals(20L,tested1.get(0).totalKraj);
        assertEquals(20L,tested1.get(1).totalKraj);
        assertEquals(10L,tested1.get(2).totalKraj);

        assertEquals(30L,tested2.get(0).totalKraj);
        assertEquals(30L,tested2.get(1).totalKraj);
        assertEquals(0L,tested2.get(2).totalKraj);
    }

    @Test
    public void detailsListShouldContainValidExport(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistDetailDto> tested = cycleTechnologistReport.getReport(1234, 1234).details;
        List<CycleTechnologistDetailDto> tested1 = tested.stream().filter(t -> (t.technologistId == 1)).collect(Collectors.toList());
        tested1.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        List<CycleTechnologistDetailDto> tested2 = tested.stream().filter(t -> (t.technologistId == 2)).collect(Collectors.toList());
        tested2.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        //Then
        assertEquals(30L,tested1.get(0).totalExport);
        assertEquals(40L,tested1.get(1).totalExport);
        assertEquals(50L,tested1.get(2).totalExport);

        assertEquals(40L,tested2.get(0).totalExport);
        assertEquals(50L,tested2.get(1).totalExport);
        assertEquals(60L,tested2.get(2).totalExport);
    }

    @Test
    public void detailsListShouldContainValidTotal(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistDetailDto> tested = cycleTechnologistReport.getReport(1234, 1234).details;
        List<CycleTechnologistDetailDto> tested1 = tested.stream().filter(t -> (t.technologistId == 1)).collect(Collectors.toList());
        tested1.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        List<CycleTechnologistDetailDto> tested2 = tested.stream().filter(t -> (t.technologistId == 2)).collect(Collectors.toList());
        tested2.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        //Then
        assertEquals(60L,tested1.get(0).total);
        assertEquals(70L,tested1.get(1).total);
        assertEquals(60L,tested1.get(2).total);

        assertEquals(90L,tested2.get(0).total);
        assertEquals(100L,tested2.get(1).total);
        assertEquals(80L,tested2.get(2).total);
    }

    @Test
    public void detailsListShouldContainValidQuality(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistDetailDto> tested = cycleTechnologistReport.getReport(1234, 1234).details;
        List<CycleTechnologistDetailDto> tested1 = tested.stream().filter(t -> (t.technologistId == 1)).collect(Collectors.toList());
        tested1.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        List<CycleTechnologistDetailDto> tested2 = tested.stream().filter(t -> (t.technologistId == 2)).collect(Collectors.toList());
        tested2.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        //Then
        assertEquals(5000L,tested1.get(0).quality);
        assertEquals(5714L,tested1.get(1).quality);
        assertEquals(8333L,tested1.get(2).quality);

        assertEquals(4444L,tested2.get(0).quality);
        assertEquals(5000L,tested2.get(1).quality);
        assertEquals(7500L,tested2.get(2).quality);
    }

    @Test
    public void detailsListShouldContainValidWeight(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistDetailDto> tested = cycleTechnologistReport.getReport(1234, 1234).details;
        List<CycleTechnologistDetailDto> tested1 = tested.stream().filter(t -> (t.technologistId == 1)).collect(Collectors.toList());
        tested1.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        List<CycleTechnologistDetailDto> tested2 = tested.stream().filter(t -> (t.technologistId == 2)).collect(Collectors.toList());
        tested2.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        //Then
        assertEquals(1000L,tested1.get(0).totalWeight);
        assertEquals(1000L,tested1.get(1).totalWeight);
        assertEquals(1000L,tested1.get(2).totalWeight);

        assertEquals(2000L,tested2.get(0).totalWeight);
        assertEquals(2000L,tested2.get(1).totalWeight);
        assertEquals(2000L,tested2.get(2).totalWeight);
    }


    @Test
    public void detailsListShouldContainValidArea(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistDetailDto> tested = cycleTechnologistReport.getReport(1234, 1234).details;
        List<CycleTechnologistDetailDto> tested1 = tested.stream().filter(t -> (t.technologistId == 1)).collect(Collectors.toList());
        tested1.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        List<CycleTechnologistDetailDto> tested2 = tested.stream().filter(t -> (t.technologistId == 2)).collect(Collectors.toList());
        tested2.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        //Then
        assertEquals(10L,tested1.get(0).totalArea);
        assertEquals(10L,tested1.get(1).totalArea);
        assertEquals(10L,tested1.get(2).totalArea);

        assertEquals(20L,tested2.get(0).totalArea);
        assertEquals(20L,tested2.get(1).totalArea);
        assertEquals(20L,tested2.get(2).totalArea);
    }

    @Test
    public void detailsListShouldContainValidKgM(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistDetailDto> tested = cycleTechnologistReport.getReport(1234, 1234).details;
        List<CycleTechnologistDetailDto> tested1 = tested.stream().filter(t -> (t.technologistId == 1)).collect(Collectors.toList());
        tested1.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        List<CycleTechnologistDetailDto> tested2 = tested.stream().filter(t -> (t.technologistId == 2)).collect(Collectors.toList());
        tested2.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        //Then
        assertEquals(6L,tested1.get(0).kgM);
        assertEquals(7L,tested1.get(1).kgM);
        assertEquals(6L,tested1.get(2).kgM);

        assertEquals(4L,tested2.get(0).kgM);
        assertEquals(5L,tested2.get(1).kgM);
        assertEquals(4L,tested2.get(2).kgM);
    }

    @Test
    public void detailsListShouldContainValidKgT(){
        //Given
        when(cycleDao.findTechnoHeaders(any(Integer.class), any(Integer.class))).thenReturn(createTechnoHeader());
        when(cycleDao.findTechnoDetails(any(Integer.class), any(Integer.class))).thenReturn(createTechnoDetails());
        //When
        List<CycleTechnologistDetailDto> tested = cycleTechnologistReport.getReport(1234, 1234).details;
        List<CycleTechnologistDetailDto> tested1 = tested.stream().filter(t -> (t.technologistId == 1)).collect(Collectors.toList());
        tested1.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        List<CycleTechnologistDetailDto> tested2 = tested.stream().filter(t -> (t.technologistId == 2)).collect(Collectors.toList());
        tested2.sort(Comparator.comparing(CycleTechnologistDetailDto::getDate));
        //Then
        assertEquals(6000L,tested1.get(0).kgT);
        assertEquals(7000L,tested1.get(1).kgT);
        assertEquals(6000L,tested1.get(2).kgT);

        assertEquals(4500L,tested2.get(0).kgT);
        assertEquals(5000L,tested2.get(1).kgT);
        assertEquals(4000L,tested2.get(2).kgT);
    }


    private List<TechnoCycleHeader> createTechnoHeader() {
        List<TechnoCycleHeader> list = new ArrayList<>();
        list.add(createTechnoHeader(10, 1000, 10, 1));
        list.add(createTechnoHeader(20, 2000, 20, 2));
        return list;
    }

    private TechnoCycleHeader createTechnoHeader(int cycleAmount, int weight, int area, int technologistId) {
        TechnoCycleHeader header = new TechnoCycleHeader();
        header.cycleAmount = cycleAmount;
        header.weight = weight;
        header.area = area;
        header.technologistId = technologistId;
        return header;
    }

    private List<TechnoCycleDetail> createTechnoDetails() {
        List<TechnoCycleDetail> list = new ArrayList<>();
        list.add(createTechnoDetail(formatter.parseDateTime("20170101").toDate(), 10L, 20L, 30L, 1L));
        list.add(createTechnoDetail(formatter.parseDateTime("20170102").toDate(), 10L, 20L, 40L, 1L));
        list.add(createTechnoDetail(formatter.parseDateTime("20170103").toDate(), 0L, 10L, 50L, 1L));

        list.add(createTechnoDetail(formatter.parseDateTime("20170101").toDate(), 20L, 30L, 40L, 2L));
        list.add(createTechnoDetail(formatter.parseDateTime("20170103").toDate(), 20L, 30L, 50L, 2L));
        list.add(createTechnoDetail(formatter.parseDateTime("20170105").toDate(), 20L, 0L, 60L, 2L));

        return list;
    }

    private TechnoCycleDetail createTechnoDetail(Date day, long inne, long kraj, long export, long technologistId) {
        TechnoCycleDetail detail = new TechnoCycleDetail();
        detail.inne = inne;
        detail.kraj = kraj;
        detail.export = export;
        detail.total = inne + kraj + export;
        detail.day = day;
        detail.technologistId = technologistId;
        return detail;
    }
}