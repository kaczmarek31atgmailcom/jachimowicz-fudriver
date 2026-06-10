package com.fungisearch.fudriver.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by marcin on 20.01.17.
 */
@Service
public class DateUtils {


    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        //calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        //calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDateAfterNumberOfDays(Date startDate, int numberOfDays){
        if (startDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE, numberOfDays);
            return calendar.getTime();
        }
        return null;
    }

    public static Date getYesterday(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -1);
            return calendar.getTime();
        }
        return null;
    }

    public static Date getTomorrow(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);
            return calendar.getTime();
        }
        return null;
    }

    public static boolean isTheSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static List<Date> getDaysBetweenDates(Date startdate, Date enddate) {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        Date startDay;
        Date endDay;
        if (startdate.after(enddate)) {
            startDay = getStartOfDay(enddate);
            endDay = getStartOfDay(startdate);
        } else {
            startDay = getStartOfDay(startdate);
            endDay = getStartOfDay(enddate);
        }
        calendar.setTime(startDay);
        while (calendar.getTime().before(endDay)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        dates.add(endDay);
        Comparator<Date> comparator = (a, b) -> a.compareTo(b);
        dates.sort(comparator);
        return dates;
    }

    public static Date getDayBefore(Date date, int amountOfDays) {
        amountOfDays = amountOfDays * -1;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, amountOfDays);
            return calendar.getTime();
        }
        return null;
    }


    public static Long getMinutesBetweenTwoDates(Date startDate, Date endDate){
        long duration =  endDate.getTime() - startDate.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(duration);
    }

    public static Date getFirstDayOfMonth(Date today){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        return calendar.getTime();
    }
}
