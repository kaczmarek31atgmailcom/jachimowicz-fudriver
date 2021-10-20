package com.fungisearch.fudriver.timeRecorder.query.service;

import com.fungisearch.fudriver.person.person.query.yearBar.model.BarPeriod;
import com.fungisearch.fudriver.person.person.query.yearBar.model.BarType;
import com.fungisearch.fudriver.timeRecorder.query.dao.WorkTimeLogDao;
import com.fungisearch.fudriver.timeRecorder.query.dto.DayBarDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkTimeLogServiceTest {

    @Mock
    private WorkTimeLogDao workTimeLogDao;

    @InjectMocks
    private WorkTimeLogService workTimeLogService;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @Test
    public void shouldReturnDayBarsOfLengthEqualsFour() {
        //Given
        when(workTimeLogDao.getPeriodsForPerson(any(Long.class), any(Date.class), any(Date.class))).thenReturn(getFourDaysBarPeriods());
        //When
        List<DayBarDto> tested = workTimeLogService.getDays(1L, Date.from(LocalDateTime.parse("2017-12-01 10:00", formatter).atZone(ZoneId.systemDefault()).toInstant()), Date.from(LocalDateTime.parse("2017-12-03 10:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        //Then
        assertEquals(3,tested.size());
    }

    @Test
    public void shouldAddMissingStartDates() {
        //Given
        when(workTimeLogDao.getPeriodsForPerson(any(Long.class), any(Date.class), any(Date.class))).thenReturn(getOpenOpenOneDayBarPeriods());
        //When
        List<DayBarDto> tested = workTimeLogService.getDays(1L, Date.from(LocalDateTime.parse("2017-12-01 10:00", formatter).atZone(ZoneId.systemDefault()).toInstant()), Date.from(LocalDateTime.parse("2017-12-03 10:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        //Then
        assertEquals(tested.get(0).barPeriods.get(0).getStartDate(), Date.from(LocalDateTime.parse("2017-12-01 00:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
    }

    @Test
    public void shouldAddPausePeriod(){
        //Given
        when(workTimeLogDao.getPeriodsForPerson(any(Long.class), any(Date.class), any(Date.class))).thenReturn(getTwoPeriodsInOneDay());
        //When
        List<DayBarDto> tested = workTimeLogService.getDays(1L, Date.from(LocalDateTime.parse("2017-12-01 10:00", formatter).atZone(ZoneId.systemDefault()).toInstant()), Date.from(LocalDateTime.parse("2017-12-01 10:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        //Then
        assertEquals(BarType.PAUSE,tested.get(0).barPeriods.get(2).getBarType() );
    }

    @Test
    public void shouldReturnSevenDays(){
        //Given
        when(workTimeLogDao.getPeriodsForPerson(any(Long.class), any(Date.class), any(Date.class))).thenReturn(getSixPeriods());
        //When
        List<DayBarDto> tested = workTimeLogService.getDays(1L,
                Date.from(LocalDateTime.parse("2017-12-01 10:00", formatter).atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.parse("2017-12-09 10:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        //Then
        assertEquals(9,tested.size());
    }

    @Test
    public void emptyPeriodsAreCreatedForNullData(){
        //Given
        when(workTimeLogDao.getPeriodsForPerson(any(Long.class), any(Date.class), any(Date.class))).thenReturn(getSixPeriods());
        //When
        List<DayBarDto> tested = workTimeLogService.getDays(1L,
                Date.from(LocalDateTime.parse("2017-12-01 10:00", formatter).atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.parse("2017-12-09 10:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        //Then
        for(DayBarDto dayBarDto: tested){
            //System.out.println(dayBarDto.barPeriods.size());
            for(BarPeriod barPeriod: dayBarDto.barPeriods){
                //System.out.println(dayBarDto.day + " startDate: " + barPeriod.getStartDate() + " endDate: " + barPeriod.getEndDate() + " barType: " + barPeriod.getBarType());
            }
        }

        assertEquals(1,tested.get(8).barPeriods.size());
    }


    private List<BarPeriod> getTwoPeriodsInOneDay() {
        List<BarPeriod> activeBarPeriods = new ArrayList<>();
        BarPeriod bp1 = new BarPeriod();
        bp1.setStartDate(Date.from(LocalDateTime.parse("2017-12-01 07:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp1.setEndDate(Date.from(LocalDateTime.parse("2017-12-01 08:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp1.setOpened(false);

        BarPeriod bp2 = new BarPeriod();
        bp2.setStartDate(Date.from(LocalDateTime.parse("2017-12-01 09:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp2.setEndDate(Date.from(LocalDateTime.parse("2017-12-01 10:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp2.setOpened(false);

        activeBarPeriods.add(bp1);
        activeBarPeriods.add(bp2);
        return activeBarPeriods;
    }


    private List<BarPeriod> getOpenOpenOneDayBarPeriods() {
        List<BarPeriod> activeBarPeriods = new ArrayList<>();

        BarPeriod bp1 = new BarPeriod();
        bp1.setEndDate(Date.from(LocalDateTime.parse("2017-12-01 06:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp1.setOpened(false);

        BarPeriod bp2 = new BarPeriod();
        bp2.setStartDate(Date.from(LocalDateTime.parse("2017-12-01 07:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp2.setEndDate(Date.from(LocalDateTime.parse("2017-12-01 11:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp2.setOpened(false);

        BarPeriod bp3 = new BarPeriod();
        bp3.setStartDate(Date.from(LocalDateTime.parse("2017-12-01 12:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp3.setEndDate(Date.from(LocalDateTime.parse("2017-12-01 13:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp3.setOpened(false);

        BarPeriod bp4 = new BarPeriod();
        bp4.setStartDate(Date.from(LocalDateTime.parse("2017-12-01 15:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp4.setOpened(true);

        activeBarPeriods.add(bp1);
        activeBarPeriods.add(bp2);
        activeBarPeriods.add(bp3);
        activeBarPeriods.add(bp4);
        return activeBarPeriods;
    }

    private List<BarPeriod> getFourDaysBarPeriods() {
        List<BarPeriod> activeBarPeriods = new ArrayList<>();

        BarPeriod bp1 = new BarPeriod();
        bp1.setStartDate(Date.from(LocalDateTime.parse("2017-12-01 06:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp1.setEndDate(Date.from(LocalDateTime.parse("2017-12-01 12:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp1.setOpened(false);

        BarPeriod bp2 = new BarPeriod();
        bp2.setStartDate(Date.from(LocalDateTime.parse("2017-12-02 07:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp2.setEndDate(Date.from(LocalDateTime.parse("2017-12-02 11:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp2.setOpened(false);

        BarPeriod bp3 = new BarPeriod();
        bp3.setStartDate(Date.from(LocalDateTime.parse("2017-12-03 12:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp3.setEndDate(Date.from(LocalDateTime.parse("2017-12-03 13:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp3.setOpened(false);

        BarPeriod bp4 = new BarPeriod();
        bp4.setStartDate(Date.from(LocalDateTime.parse("2017-12-04 07:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp4.setEndDate(Date.from(LocalDateTime.parse("2017-12-04 15:00", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp4.setOpened(false);

        activeBarPeriods.add(bp1);
        activeBarPeriods.add(bp2);
        activeBarPeriods.add(bp3);
        activeBarPeriods.add(bp4);
        return activeBarPeriods;
    }

    private List<BarPeriod> getSixPeriods(){
        List<BarPeriod> activeBarPeriods = new ArrayList<>();

        BarPeriod bp1 = new BarPeriod();
        bp1.setStartDate(Date.from(LocalDateTime.parse("2017-12-01 07:20", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp1.setEndDate(Date.from(LocalDateTime.parse("2017-12-01 13:02", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp1.setOpened(false);

        BarPeriod bp2 = new BarPeriod();
        bp2.setStartDate(Date.from(LocalDateTime.parse("2017-12-03 06:46", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp2.setEndDate(Date.from(LocalDateTime.parse("2017-12-03 11:57", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp2.setOpened(false);

        BarPeriod bp3 = new BarPeriod();
        bp3.setStartDate(Date.from(LocalDateTime.parse("2017-12-04 07:19", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp3.setEndDate(Date.from(LocalDateTime.parse("2017-12-04 12:38", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp3.setOpened(false);

        BarPeriod bp4 = new BarPeriod();
        bp4.setStartDate(Date.from(LocalDateTime.parse("2017-12-05 07:12", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp4.setEndDate(Date.from(LocalDateTime.parse("2017-12-05 14:28", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp4.setOpened(false);

        BarPeriod bp5 = new BarPeriod();
        bp5.setStartDate(Date.from(LocalDateTime.parse("2017-12-06 07:11", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp5.setEndDate(Date.from(LocalDateTime.parse("2017-12-06 14:50", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp5.setOpened(false);

        BarPeriod bp6 = new BarPeriod();
        bp6.setStartDate(Date.from(LocalDateTime.parse("2017-12-07 07:15", formatter).atZone(ZoneId.systemDefault()).toInstant()));
        bp6.setOpened(true);

        activeBarPeriods.add(bp1);
        activeBarPeriods.add(bp2);
        activeBarPeriods.add(bp3);
        activeBarPeriods.add(bp4);
        activeBarPeriods.add(bp5);
        activeBarPeriods.add(bp6);

        return activeBarPeriods;
    }

}