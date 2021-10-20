package com.fungisearch.fudriver.common;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DateUtilsTest {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfminutes = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Test
    public void shouldGet1DateBetween() throws ParseException {
        //Given
        Date startDate = sdf.parse("2018-01-01");
        Date endDate = sdf.parse("2018-01-03");
        //When
        List<Date> tested = DateUtils.getDaysBetweenDates(startDate, endDate);
        //Then
        Assert.assertEquals(3, tested.size());
        Assert.assertEquals(sdf.parse("2018-01-01"),tested.get(0));
        Assert.assertEquals(sdf.parse("2018-01-02"),tested.get(1));
        Assert.assertEquals(sdf.parse("2018-01-03"),tested.get(2));
    }

    @Test
    public void shouldGet0DateBetween() throws ParseException {
        //Given
        Date startDate = sdf.parse("2018-01-01");
        Date endDate = sdf.parse("2018-01-02");
        //When
        List<Date> tested = DateUtils.getDaysBetweenDates(startDate, endDate);
        //Then
        Assert.assertEquals(2, tested.size());
        Assert.assertEquals(sdf.parse("2018-01-01"),tested.get(0));
        Assert.assertEquals(sdf.parse("2018-01-02"),tested.get(1));
    }


    @Test
    public void shouldConvertDatesBetween() throws ParseException {
        //Given
        Date startDate = sdf.parse("2018-01-03");
        Date endDate = sdf.parse("2018-01-01");
        //When
        List<Date> tested = DateUtils.getDaysBetweenDates(startDate, endDate);
        //Then
        Assert.assertEquals(3, tested.size());
        Assert.assertEquals(sdf.parse("2018-01-01"),tested.get(0));
        Assert.assertEquals(sdf.parse("2018-01-02"),tested.get(1));
        Assert.assertEquals(sdf.parse("2018-01-03"),tested.get(2));
    }

    @Test
    public void shouldCountMinutesBetweenTwoDates() throws ParseException {
        Date startDate = sdfminutes.parse("2018-01-03 08:00");
        Date endDate = sdfminutes.parse("2018-01-03 10:00");
        long tested = DateUtils.getMinutesBetweenTwoDates(startDate,endDate);
        Assert.assertEquals(120,tested);
    }
}
