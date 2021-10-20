package com.fungisearch.fudriver.payroll.calendar.query.dao;

import com.fungisearch.fudriver.payroll.calendar.query.dto.CalendarDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcin on 13.05.16.
 */
@Repository
public class CalendarJdbcDao implements CalendarDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<CalendarDto> getDaysInMonth(String yearMonth) {
        return jdbcTemplate.query("select " +
                "id as id, " +
                "date as date, " +
                "editable as dayType " +
                "from calendar where DATE_FORMAT(date, '%Y-%m') = ?", new Object[]{yearMonth}, new DaysInMonthResultSetExtractor());
    }


    private class DaysInMonthResultSetExtractor implements ResultSetExtractor<List<CalendarDto>>{

        @Override
        public List<CalendarDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<CalendarDto> list = new ArrayList<CalendarDto>();
            CalendarDto calendarDto;
            while(rs.next()){
                calendarDto = new CalendarDto();
                calendarDto.id = rs.getLong("id");
                calendarDto.date = rs.getDate("date");
                calendarDto.dayType = rs.getInt("dayType");
                list.add(calendarDto);
            }
        return list;
        }
    }
}
