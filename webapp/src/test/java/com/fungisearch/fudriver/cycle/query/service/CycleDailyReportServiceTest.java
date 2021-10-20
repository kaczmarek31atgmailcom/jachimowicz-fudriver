package com.fungisearch.fudriver.cycle.query.service;

import com.fungisearch.fudriver.cycle.query.dao.CycleDao;
import com.fungisearch.fudriver.cycle.query.dto.CycleDatesDto;
import com.fungisearch.fudriver.cycle.query.dto.CycleDayDto;
import com.fungisearch.fudriver.cycle.query.dto.CyclePeriodDto;
import com.fungisearch.fudriver.zarobki.query.dao.ZarobkiDao;
import com.fungisearch.fudriver.zarobki.query.dto.DailyHarvestByTypeGroupDto;
import com.fungisearch.fudriver.zarobki.query.dto.ZarobkiByCycleDto;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CycleDailyReportServiceTest {

    @Mock
    private CycleDao cycleDao;
    @Mock
    private ZarobkiDao zarobkiDao;

    @InjectMocks
    CycleDailyReportService service;

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");

    @Test
    public void shouldReturnArrayListFirstPeriod() {
        //Given
        CycleDatesDto cycleDatesDto = new CycleDatesDto();
        cycleDatesDto.startFirstPeriod = 20170101;
        cycleDatesDto.startSecondPeriod = 20170102;
        cycleDatesDto.startThirdPeriod = 20170103;
        cycleDatesDto.endDate = 20170104;
        //When
        List<CycleDayDto> tested = service.getFirstPeriod(cycleDatesDto, getZarobki());
        //Then
        Assert.assertTrue(tested instanceof ArrayList);
    }

    @Test
    public void firstDayOfFirstPeriodIsTheOldestHarvestDay() {
        //Given
        CycleDatesDto cycleDatesDto = new CycleDatesDto();
        cycleDatesDto.startFirstPeriod = 20170101;
        cycleDatesDto.startSecondPeriod = 20170103;
        cycleDatesDto.startThirdPeriod = 20170105;
        cycleDatesDto.endDate = 20170106;
        //When
        List<CycleDayDto> tested = service.getFirstPeriod(cycleDatesDto, getZarobki());
        tested.sort(Comparator.comparing(CycleDayDto::getDate));
        //Then
        assertEquals(new DateTime(tested.get(0).date), formatter.parseDateTime("20170101"));
    }

    @Test
    public void shouldReturnTwoElementArrayForFirstPeriod() {
        //Given
        CycleDatesDto cycleDatesDto = new CycleDatesDto();
        cycleDatesDto.startFirstPeriod = 20170101;
        cycleDatesDto.startSecondPeriod = 20170103;
        cycleDatesDto.startThirdPeriod = 20170105;
        cycleDatesDto.endDate = 20170106;
        //When
        List<CycleDayDto> tested = service.getFirstPeriod(cycleDatesDto, getZarobki());
        tested.sort(Comparator.comparing(CycleDayDto::getDate));
        //Then
        assertEquals(tested.size(), 2);
    }

    @Test
    public void shouldReturnValidFirstDayOfFirstPeriod() {
        //Given
        CycleDatesDto cycleDatesDto = new CycleDatesDto();
        cycleDatesDto.startFirstPeriod = 20170101;
        cycleDatesDto.startSecondPeriod = 20170103;
        cycleDatesDto.startThirdPeriod = 20170105;
        cycleDatesDto.endDate = 20170106;
        cycleDatesDto.area = 2;
        cycleDatesDto.humidity = 20;
        cycleDatesDto.weight = 200000;
        //When
        List<CycleDayDto> tested = service.getFirstPeriod(cycleDatesDto, getZarobki());
        tested.sort(Comparator.comparing(CycleDayDto::getDate));
        //Then
        assertEquals(new DateTime(tested.get(0).date), formatter.parseDateTime("20170101"));
        assertEquals(1000, tested.get(0).summaryKraj);
        assertEquals(8000, tested.get(0).summaryExport);
        assertEquals(1000, tested.get(0).summaryInne);
        assertEquals(9000, tested.get(0).summaryTotal);
        assertEquals(4500, tested.get(0).kgM);
        assertEquals(45, tested.get(0).kgTon);
        assertEquals(56, tested.get(0).kgDryTon);
        assertEquals(tested.get(0).quality, 80);
    }

    @Test
    public void shouldReturnValidFirstDayOfFirstPeriodIfNoStartOfSecondPeriod() {
        //Given
        CycleDatesDto cycleDatesDto = new CycleDatesDto();
        cycleDatesDto.startFirstPeriod = 20170101;
        cycleDatesDto.area = 2;
        cycleDatesDto.humidity = 20;
        cycleDatesDto.weight = 200000;
        //When
        List<CycleDayDto> tested = service.getFirstPeriod(cycleDatesDto, getZarobki());
        tested.sort(Comparator.comparing(CycleDayDto::getDate));
        //Then
        assertEquals(new DateTime(tested.get(0).date), formatter.parseDateTime("20170101"));
        assertEquals(1000, tested.get(0).summaryKraj);
        assertEquals(8000, tested.get(0).summaryExport);
        assertEquals(1000, tested.get(0).summaryInne);
        assertEquals(9000, tested.get(0).summaryTotal);
        assertEquals(4500, tested.get(0).kgM);
        assertEquals(45, tested.get(0).kgTon);
        assertEquals(56, tested.get(0).kgDryTon);
        assertEquals(tested.get(0).quality, 80);
    }


    @Test
    public void shouldReturnValidDateOfSecondDayOfTheFirstPeriod() {
        //Given
        CycleDatesDto cycleDatesDto = new CycleDatesDto();
        cycleDatesDto.startFirstPeriod = 20170101;
        cycleDatesDto.startSecondPeriod = 20170103;
        cycleDatesDto.startThirdPeriod = 20170105;
        cycleDatesDto.endDate = 20170106;
        //When
        List<CycleDayDto> tested = service.getFirstPeriod(cycleDatesDto, getZarobki());
        tested.sort(Comparator.comparing(CycleDayDto::getDate));
        //Then
        assertEquals(new DateTime(tested.get(1).date), formatter.parseDateTime("20170102"));
    }

    @Test
    public void kgPerTonIsZeroIfWeighNotSet() {
        //Given
        CycleDatesDto cycleDatesDto = new CycleDatesDto();
        cycleDatesDto.startFirstPeriod = 20170101;
        cycleDatesDto.startSecondPeriod = 20170103;
        cycleDatesDto.startThirdPeriod = 20170105;
        cycleDatesDto.endDate = 20170106;
        //When
        List<CycleDayDto> tested = service.getFirstPeriod(cycleDatesDto, getZarobki());
        tested.sort(Comparator.comparing(CycleDayDto::getDate));
        //Then
        assertEquals(0, tested.get(0).kgTon);
    }

    @Test
    public void kgPerDryTonIsZeroIfWeighNotSet() {
        //Given
        CycleDatesDto cycleDatesDto = new CycleDatesDto();
        cycleDatesDto.startFirstPeriod = 20170101;
        cycleDatesDto.startSecondPeriod = 20170103;
        cycleDatesDto.startThirdPeriod = 20170105;
        cycleDatesDto.endDate = 20170106;
        //When
        List<CycleDayDto> tested = service.getFirstPeriod(cycleDatesDto, getZarobki());
        tested.sort(Comparator.comparing(CycleDayDto::getDate));
        //Then
        assertEquals(0, tested.get(0).kgDryTon);
    }

    @Test
    public void kgMeterIsZeroIfAreaNotSet() {
        //Given
        CycleDatesDto cycleDatesDto = new CycleDatesDto();
        cycleDatesDto.startFirstPeriod = 20170101;
        cycleDatesDto.startSecondPeriod = 20170103;
        cycleDatesDto.startThirdPeriod = 20170105;
        cycleDatesDto.endDate = 20170106;
        //When
        List<CycleDayDto> tested = service.getFirstPeriod(cycleDatesDto, getZarobki());
        tested.sort(Comparator.comparing(CycleDayDto::getDate));
        //Then
        assertEquals(0, tested.get(0).kgM);
    }


    @Test
    public void shouldReturnTwoElementArrayForSecondPeriod() {
        //Given
        CycleDatesDto cycleDatesDto = new CycleDatesDto();
        cycleDatesDto.startFirstPeriod = 20170101;
        cycleDatesDto.startSecondPeriod = 20170103;
        cycleDatesDto.startThirdPeriod = 20170105;
        cycleDatesDto.endDate = 20170106;
        //When
        List<CycleDayDto> tested = service.getSecondPeriod(cycleDatesDto, getZarobki());
        tested.sort(Comparator.comparing(CycleDayDto::getDate));
        //Then
        assertEquals(tested.size(), 2);
    }

    @Test
    public void shouldReturnValidFirstDayOfSecondPeriod() {
        //Given
        CycleDatesDto cycleDatesDto = new CycleDatesDto();
        cycleDatesDto.startFirstPeriod = 20170101;
        cycleDatesDto.startSecondPeriod = 20170103;
        cycleDatesDto.startThirdPeriod = 20170105;
        cycleDatesDto.endDate = 20170106;
        cycleDatesDto.area = 2;
        cycleDatesDto.humidity = 20;
        cycleDatesDto.weight = 200000;
        //When
        List<CycleDayDto> tested = service.getSecondPeriod(cycleDatesDto, getZarobki());
        tested.sort(Comparator.comparing(CycleDayDto::getDate));
        //Then
        assertEquals(new DateTime(tested.get(0).date), formatter.parseDateTime("20170103"));
        assertEquals(0, tested.get(0).summaryKraj);
        assertEquals(10000, tested.get(0).summaryExport);
        assertEquals(0, tested.get(0).summaryInne);
        assertEquals(10000, tested.get(0).summaryTotal);
        assertEquals(5000, tested.get(0).kgM);
        assertEquals(50, tested.get(0).kgTon);
        assertEquals(62, tested.get(0).kgDryTon);
        assertEquals(tested.get(0).quality, 100);
    }

    @Test
    public void shouldReturnValidSecondDayOfSecondPeriod() {
        //Given
        CycleDatesDto cycleDatesDto = new CycleDatesDto();
        cycleDatesDto.startFirstPeriod = 20170101;
        cycleDatesDto.startSecondPeriod = 20170103;
        cycleDatesDto.startThirdPeriod = 20170105;
        cycleDatesDto.endDate = 20170106;
        cycleDatesDto.area = 2;
        cycleDatesDto.humidity = 20;
        cycleDatesDto.weight = 200000;
        //When
        List<CycleDayDto> tested = service.getSecondPeriod(cycleDatesDto, getZarobki());
        tested.sort(Comparator.comparing(CycleDayDto::getDate));
        //Then
        assertEquals(new DateTime(tested.get(1).date), formatter.parseDateTime("20170104"));
        assertEquals(10000, tested.get(1).summaryKraj);
        assertEquals(0, tested.get(1).summaryExport);
        assertEquals(0, tested.get(1).summaryInne);
        assertEquals(10000, tested.get(1).summaryTotal);
        assertEquals(5000, tested.get(1).kgM);
        assertEquals(50, tested.get(1).kgTon);
        assertEquals(62, tested.get(1).kgDryTon);
        assertEquals(tested.get(1).quality, 0);
    }

    @Test
    public void shouldReturnTwoDaysOfThirdPeriod() {
        //Given
        CycleDatesDto cycleDatesDto = new CycleDatesDto();
        cycleDatesDto.startFirstPeriod = 20170101;
        cycleDatesDto.startSecondPeriod = 20170103;
        cycleDatesDto.startThirdPeriod = 20170105;
        cycleDatesDto.endDate = 20170106;
        cycleDatesDto.area = 2;
        cycleDatesDto.humidity = 20;
        cycleDatesDto.weight = 200000;
        //When
        List<CycleDayDto> tested = service.getThirdPeriod(cycleDatesDto, getZarobki());
        //Then
        assertEquals(2, tested.size());
    }


    @Test
    public void shouldReturnValidFirstDayOfThirdPeriod() {
        //Given
        CycleDatesDto cycleDatesDto = new CycleDatesDto();
        cycleDatesDto.startFirstPeriod = 20170101;
        cycleDatesDto.startSecondPeriod = 20170103;
        cycleDatesDto.startThirdPeriod = 20170105;
        cycleDatesDto.endDate = 20170106;
        cycleDatesDto.area = 2;
        cycleDatesDto.humidity = 20;
        cycleDatesDto.weight = 200000;
        //When
        List<CycleDayDto> tested = service.getThirdPeriod(cycleDatesDto, getZarobki());
        tested.sort(Comparator.comparing(CycleDayDto::getDate));
        //Then
        assertEquals(new DateTime(tested.get(0).date), formatter.parseDateTime("20170105"));
        assertEquals(5000, tested.get(0).summaryKraj);
        assertEquals(5000, tested.get(0).summaryExport);
        assertEquals(0, tested.get(0).summaryInne);
        assertEquals(10000, tested.get(0).summaryTotal);
        assertEquals(5000, tested.get(0).kgM);
        assertEquals(50, tested.get(0).kgTon);
        assertEquals(62, tested.get(0).kgDryTon);
        assertEquals(tested.get(0).quality, 50);
    }


    @Test
    public void shouldReturnSixDaysForFirstPeriodIfOnlyStartDayIsSet() {
        //Given
        CycleDatesDto cycleDatesDto = new CycleDatesDto();
        cycleDatesDto.startFirstPeriod = 20170101;
        //When
        List<CycleDayDto> tested = service.getFirstPeriod(cycleDatesDto, getZarobki());
        //Then
        assertEquals(6, tested.size());
    }

    @Test
    public void shouldReturnFourDaysForFirstPeriodIfOnlyStartDayIsSet() {
        //Given
        CycleDatesDto cycleDatesDto = new CycleDatesDto();
        cycleDatesDto.startFirstPeriod = 20170101;
        cycleDatesDto.startSecondPeriod = 20170103;
        //When
        List<CycleDayDto> tested = service.getSecondPeriod(cycleDatesDto, getZarobki());
        //Then
        assertEquals(4, tested.size());
    }

    @Test
    public void shouldReturnTwoDaysForThirdPeriodIfNoEndDateIsSet() {
        //Given
        CycleDatesDto cycleDatesDto = new CycleDatesDto();
        cycleDatesDto.startFirstPeriod = 20170101;
        cycleDatesDto.startSecondPeriod = 20170103;
        cycleDatesDto.startSecondPeriod = 20170105;
        //When
        List<CycleDayDto> tested = service.getSecondPeriod(cycleDatesDto, getZarobki());
        tested.sort(Comparator.comparing(CycleDayDto::getDate));
        //Then
        assertEquals(2, tested.size());
        assertEquals(new DateTime(tested.get(0).date), formatter.parseDateTime("20170105"));
        assertEquals(new DateTime(tested.get(1).date), formatter.parseDateTime("20170106"));
    }

    @Test
    public void shouldReturnFourElementHarvestGroups(){
        //Given
        LocalDateTime endDate = formatter.parseLocalDateTime("20170103");
        //When
        List<DailyHarvestByTypeGroupDto> tested = service.getGroupHarvestInPeriod(20170101,endDate,getHarvestGroups());
        //Then
        assertEquals(4,tested.size());
    }

    @Test
    public void shouldReturnValidElementHarvestGroups(){
        //Given
        LocalDateTime startDate = formatter.parseLocalDateTime("20170102");
        LocalDateTime endDate = formatter.parseLocalDateTime("20170103");
        LocalDateTime startNextPeriodDate = formatter.parseLocalDateTime("20170104");
        //When
        List<DailyHarvestByTypeGroupDto> tested = service.getGroupHarvestInPeriod(20170102,startNextPeriodDate,getHarvestGroups());
        //Then
        assertEquals(1,tested.get(0).groupId);
        assertEquals("ala",tested.get(0).groupName);
        assertEquals(20000,tested.get(0).totalG);
        assertEquals(startDate, new LocalDateTime(tested.get(0).date));
        assertEquals(2,tested.get(1).groupId);
        assertEquals("ola",tested.get(1).groupName);
        assertEquals(22000,tested.get(1).totalG);
        assertEquals(startDate, new LocalDateTime(tested.get(1).date));

        assertEquals(1,tested.get(2).groupId);
        assertEquals("ala",tested.get(2).groupName);
        assertEquals(30000,tested.get(2).totalG);
        assertEquals(endDate, new LocalDateTime(tested.get(2).date));
        assertEquals(2,tested.get(3).groupId);
        assertEquals("ola",tested.get(3).groupName);
        assertEquals(33000,tested.get(3).totalG);
        assertEquals(endDate, new LocalDateTime(tested.get(3).date));
    }


    @Test
    public void shouldReturnValidTotalKraj() {
        //Given
        CycleDatesDto cycleDates = new CycleDatesDto();
        cycleDates.humidity = 20;
        cycleDates.area = 1000;
        cycleDates.weight = 1000;
        //When
        CyclePeriodDto tested = service.getPeriodTotals(getCycleDays(), cycleDates);
        //Then
        assertEquals(600,tested.totalKraj);
    }

    @Test
    public void shouldReturnValidTotalInne() {
        //Given
        CycleDatesDto cycleDates = new CycleDatesDto();
        cycleDates.humidity = 20;
        cycleDates.area = 1000;
        cycleDates.weight = 1000;
        //When
        CyclePeriodDto tested = service.getPeriodTotals(getCycleDays(), cycleDates);
        //Then
        assertEquals(1100,tested.totalInne);
    }

    @Test
    public void shouldReturnValidTotalExport() {
        //Given
        CycleDatesDto cycleDates = new CycleDatesDto();
        cycleDates.humidity = 20;
        cycleDates.area = 1000;
        cycleDates.weight = 1000;
        //When
        CyclePeriodDto tested = service.getPeriodTotals(getCycleDays(), cycleDates);
        //Then
        assertEquals(6000,tested.totalExport);
    }

    @Test
    public void shouldReturnValidTotal() {
        //Given
        CycleDatesDto cycleDates = new CycleDatesDto();
        cycleDates.humidity = 20;
        cycleDates.area = 1000;
        cycleDates.weight = 1000;
        //When
        CyclePeriodDto tested = service.getPeriodTotals(getCycleDays(), cycleDates);
        //Then
        assertEquals(6600,tested.total);
    }

    @Test
    public void shouldReturnZeroTheNoAreaSet() {
        //Given
        CycleDatesDto cycleDates = new CycleDatesDto();
        //When
        CyclePeriodDto tested = service.getPeriodTotals(getCycleDays(), cycleDates);
        //Then
        assertEquals(0,tested.kgM);
    }

    @Test
    public void shouldReturnKgM() {
        //Given
        CycleDatesDto cycleDates = new CycleDatesDto();
        cycleDates.area = 10;
        //When
        CyclePeriodDto tested = service.getPeriodTotals(getCycleDays(), cycleDates);
        //Then
        assertEquals(660,tested.kgM);
    }

    @Test
    public void shouldReturnZeroTheNoWeight() {
        //Given
        CycleDatesDto cycleDates = new CycleDatesDto();
        //When
        CyclePeriodDto tested = service.getPeriodTotals(getCycleDays(), cycleDates);
        //Then
        assertEquals(0,tested.kgTon);
    }

    @Test
    public void shouldReturnKgTon() {
        //Given
        CycleDatesDto cycleDates = new CycleDatesDto();
        cycleDates.weight = 3000;
        //When
        CyclePeriodDto tested = service.getPeriodTotals(getCycleDays(), cycleDates);
        //Then
        assertEquals(2200,tested.kgTon);
    }

    @Test
    public void shouldReturnZeroTheHumidityEquals100() {
        //Given
        CycleDatesDto cycleDates = new CycleDatesDto();
        cycleDates.weight = 3000;
        cycleDates.humidity = 100;
        //When
        CyclePeriodDto tested = service.getPeriodTotals(getCycleDays(), cycleDates);
        //Then
        assertEquals(0,tested.kgDryTon);
    }

    @Test
    public void shouldReturnZeroThenHumidityAndNoWeight() {
        //Given
        CycleDatesDto cycleDates = new CycleDatesDto();
        cycleDates.humidity = 20;
        //When
        CyclePeriodDto tested = service.getPeriodTotals(getCycleDays(), cycleDates);
        //Then
        assertEquals(0,tested.kgDryTon);
    }

    @Test
    public void shouldReturnKgDryToneWhenNoHumidity() {
        //Given
        CycleDatesDto cycleDates = new CycleDatesDto();
        cycleDates.weight = 3000;
        //When
        CyclePeriodDto tested = service.getPeriodTotals(getCycleDays(), cycleDates);
        //Then
        assertEquals(2200,tested.kgDryTon);
    }

    @Test
    public void shouldReturnKgDryTone() {
        //Given
        CycleDatesDto cycleDates = new CycleDatesDto();
        cycleDates.weight = 3000;
        cycleDates.humidity = 20;
        //When
        CyclePeriodDto tested = service.getPeriodTotals(getCycleDays(), cycleDates);
        //Then
        assertEquals(2750,tested.kgDryTon);
    }

    @Test
    public void shouldCountQuality() {
        //Given
        CycleDatesDto cycleDates = new CycleDatesDto();
        //When
        CyclePeriodDto tested = service.getPeriodTotals(getCycleDays(), cycleDates);
        //Then
        assertEquals(9090,tested.quality);
    }

@Test
public void shouldCountValidKgTon(){
        //Given
    CycleDatesDto cycleDatesDto = new CycleDatesDto();
    cycleDatesDto.weight = 34000;
    ZarobkiByCycleDto zarobkiByCycleDto = new ZarobkiByCycleDto();
    zarobkiByCycleDto.totalExportG = 2674000;
    //When
    CycleDayDto tested = service.convertZarobkiToCycleDay(zarobkiByCycleDto,cycleDatesDto);
    //Then
    assertEquals(78647,tested.kgTon);
}


    private List<CycleDayDto> getCycleDays() {
        List<CycleDayDto> list = new ArrayList<>();
        list.add(createCycleDay("20170101",100,200,1000));
        list.add(createCycleDay("20170102",200,400,2000));
        list.add(createCycleDay("20170103",300,500,3000));
        return list;
    }

    private CycleDayDto createCycleDay(String date, int kraj, int inne, int export){
        CycleDayDto dto = new CycleDayDto();
        dto.date = formatter.parseDateTime(date).toDate();
        dto.summaryKraj = kraj;
        dto.summaryInne = inne;
        dto.summaryExport = export;
        dto.summaryTotal = dto.summaryKraj + dto.summaryInne + dto.summaryExport;
        return dto;
    }

    private List<DailyHarvestByTypeGroupDto> getHarvestGroups(){
        List<DailyHarvestByTypeGroupDto> result = new ArrayList<>();
        result.add(createHarvestGroup(1,"ala","20170101",10000));
        result.add(createHarvestGroup(2,"ola","20170101",11000));
        result.add(createHarvestGroup(1,"ala","20170102",20000));
        result.add(createHarvestGroup(2,"ola","20170102",22000));
        result.add(createHarvestGroup(1,"ala","20170103",30000));
        result.add(createHarvestGroup(2,"ola","20170103",33000));
        result.add(createHarvestGroup(1,"ala","20170104",40000));
        result.add(createHarvestGroup(2,"ola","20170104",44000));
        result.add(createHarvestGroup(1,"ala","20170105",50000));
        result.add(createHarvestGroup(2,"ola","20170105",55000));
        result.add(createHarvestGroup(1,"ala","20170106",60000));
        result.add(createHarvestGroup(2,"ola","20170106",66000));
        return result;
    }

    private DailyHarvestByTypeGroupDto createHarvestGroup(int groupId, String groupName,String date,int amount) {
        DailyHarvestByTypeGroupDto dto1 = new DailyHarvestByTypeGroupDto();
        dto1.groupId = groupId;
        dto1.groupName = groupName;
        dto1.date = formatter.parseDateTime(date).toDate();
        dto1.totalG = amount;
        return dto1;
    }

    private List<ZarobkiByCycleDto> getZarobki() {
        List<ZarobkiByCycleDto> zarobki = new ArrayList<>();
        zarobki.add(createSingleZarobkiByDate("20170101", 8000, 1000, 1000));
        zarobki.add(createSingleZarobkiByDate("20170102", 6000, 4000, 0));
        zarobki.add(createSingleZarobkiByDate("20170103", 10000, 0, 0));
        zarobki.add(createSingleZarobkiByDate("20170104", 0, 10000, 0));
        zarobki.add(createSingleZarobkiByDate("20170105", 5000, 5000, 0));
        zarobki.add(createSingleZarobkiByDate("20170106", 20000, 10000, 0));
        return zarobki;
    }

    private ZarobkiByCycleDto createSingleZarobkiByDate(String day, int export, int kraj, int inne) {
        ZarobkiByCycleDto dto1 = new ZarobkiByCycleDto();
        dto1.date = formatter.parseDateTime(day).toDate();
        dto1.totalExportG = export;
        dto1.totalKrajG = kraj;
        dto1.totalInneG = inne;
        dto1.totalG = dto1.totalExportG + dto1.totalInneG + dto1.totalKrajG;
        dto1.quality = ((dto1.totalExportG * 100)/ dto1.totalG);
        return dto1;
    }



}
