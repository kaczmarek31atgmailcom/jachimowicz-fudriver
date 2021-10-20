package com.fungisearch.fudriver.timeRecorder.query.dao;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.person.person.query.yearBar.model.BarPeriod;
import com.fungisearch.fudriver.person.person.query.yearBar.model.BarType;
import com.fungisearch.fudriver.timeRecorder.query.dto.PayedSalaryWorkTimeDto;
import com.fungisearch.fudriver.timeRecorder.query.dto.PersonDailyWorkTimeDto;
import com.fungisearch.fudriver.timeRecorder.query.dto.PersonWorkTimeDto;
import com.fungisearch.fudriver.timeRecorder.query.dto.WorkTimeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 19.05.16.
 */
@Repository
public class WorkTimeLogJdbcDao implements WorkTimeLogDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<BarPeriod> getPeriodsForPerson(Long personId, Date startDate, Date endDate) {
        return jdbcTemplate.query("select " +
                "start_time as startDate, " +
                "end_time as endDate, " +
                "isOpened as isOpened " +
                "from time_work_log " +
                "where person_id = ? " +
                "and (start_time BETWEEN str_to_date(?, '%Y-%m-%d %H:%i:%s') and str_to_date(?, '%Y-%m-%d %H:%i:%s') " +
                "or end_time BETWEEN  str_to_date(?, '%Y-%m-%d %H:%i:%s') and str_to_date(?, '%Y-%m-%d %H:%i:%s')) " +
                "order by start_time", new Object[]{personId, startDate, endDate, startDate, endDate}, new DayPeriodsResultSetExtractor());
    }

    private class DayPeriodsResultSetExtractor implements ResultSetExtractor<List<BarPeriod>> {

        @Override
        public List<BarPeriod> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<BarPeriod> list = new ArrayList<BarPeriod>();
            while (rs.next()) {
                BarPeriod barPeriod = new BarPeriod();
                barPeriod.setStartDate(rs.getTimestamp("startDate"));
                barPeriod.setEndDate(rs.getTimestamp("endDate"));
                barPeriod.setOpened(rs.getBoolean("isOpened"));
                barPeriod.setBarType(BarType.ACTIVE);
                list.add(barPeriod);
            }
            return list;
        }
    }

    @Override
    public List<WorkTimeDto> getWorkTimeForPersonInPeriod(Long personId, Date startDate, Date endDate) {
        return jdbcTemplate.query("select id as id, " +
                "start_time as startDate, " +
                "end_time as endDate " +
                "from time_work_log " +
                "where person_id = ? " +
                "and ((start_time >= str_to_date(?, '%Y-%m-%d %H:%i:%s') and start_time <= str_to_date(?, '%Y-%m-%d %H:%i:%s')) " +
                " or (end_time >= str_to_date(?, '%Y-%m-%d %H:%i:%s') and end_time <= str_to_date(?, '%Y-%m-%d %H:%i:%s'))) " +
                "order by start_time", new Object[]{personId, startDate, endDate, startDate, endDate}, new WorkTimeResultSetExtractor());
    }


    private class WorkTimeResultSetExtractor implements ResultSetExtractor<List<WorkTimeDto>> {

        @Override
        public List<WorkTimeDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<WorkTimeDto> result = new ArrayList<WorkTimeDto>();
            WorkTimeDto editWorkTimeDto;
            while (rs.next()) {
                editWorkTimeDto = new WorkTimeDto();
                editWorkTimeDto.id = rs.getLong("id");
                editWorkTimeDto.startDate = rs.getTimestamp("startDate");
                editWorkTimeDto.endDate = rs.getTimestamp("endDate");
                result.add(editWorkTimeDto);
            }
            return result;
        }
    }

    @Override
    public List<PersonWorkTimeDto> getPersonWorkTime(Date startDate, Date endDate) {
        return jdbcTemplate.query("select t.person_id as personId, " +
                " t.start_time as startTime, " +
                " t.end_time as endTime " +
                " from time_work_log t " +
                " where t.start_time <= ? " +
                " and ((t.end_time >= ?) or (t.end_time is null))", new Object[]{endDate, startDate}, new PersonWorkTimeResultSetExtractor());
    }

    private static class PersonWorkTimeResultSetExtractor implements ResultSetExtractor<List<PersonWorkTimeDto>> {

        @Override
        public List<PersonWorkTimeDto> extractData(ResultSet rs) throws SQLException {
            List<PersonWorkTimeDto> result = new ArrayList<>();
            PersonWorkTimeDto dto;
            while (rs.next()) {
                dto = new PersonWorkTimeDto();
                dto.personId = rs.getLong("personId");
                dto.startDate = rs.getTimestamp("startTime");
                dto.endDate = rs.getTimestamp("endTime");
                result.add(dto);
            }
            return result;
        }
    }

    @Override
    public List<PayedSalaryWorkTimeDto> getWorkTime(int personId, Date startDay, Date endDay) {
        return jdbcTemplate.query("select " +
                "DATE_FORMAT(t.start_time, '%Y-%m-%d') as date, " +
                "sum(time_to_sec(timediff(t.end_time, t.start_time))) as seconds " +
                "from time_work_log t " +
                "where t.person_id = ? " +
                "  and t.start_time between ? and ? " +
                "group by DATE_FORMAT(t.start_time, '%Y-%m-%d')", new Object[]{personId,
                DateUtils.getStartOfDay(startDay),
                DateUtils.getEndOfDay(endDay)}, new PayedSalaryResultSetExtractor());
    }


    private class PayedSalaryResultSetExtractor implements ResultSetExtractor<List<PayedSalaryWorkTimeDto>> {
        @Override
        public List<PayedSalaryWorkTimeDto> extractData(ResultSet rs) throws SQLException {
            List<PayedSalaryWorkTimeDto> list = new ArrayList<>();
            while (rs.next()) {
                PayedSalaryWorkTimeDto dto = new PayedSalaryWorkTimeDto();
                dto.date = rs.getString("date");
                dto.seconds = rs.getInt("seconds");
                list.add(dto);
            }
            return list;
        }
    }

    @Override
    public List<PersonDailyWorkTimeDto> getWorkTimeIncludingPauses(Date startTime, Date endTime){
        return jdbcTemplate.query("select " +
                "  t.person_id as personId, " +
                "  l.nr as nr, " +
                "  l.imie as name, " +
                "  l.nazwisko as surname, " +
                "  DATE_FORMAT(start_time,'%Y-%m-%d') as day, " +
                "  min(t.start_time) as startTime, " +
                "  max(t.end_time) as endTime " +
                " from time_work_log t " +
                " left join ludzie l on l.id = t.person_id " +
                " where t.start_time <= ? " +
                "  and t.end_time >= ? " +
                "group by t.person_id, DATE_FORMAT(start_time,'%Y-%m-%d')", new Object [] {endTime,startTime}, new PersonDailWorkTimeResultSetExtractor());
    }

    private class PersonDailWorkTimeResultSetExtractor implements ResultSetExtractor<List<PersonDailyWorkTimeDto>> {
        @Override
        public List<PersonDailyWorkTimeDto> extractData(ResultSet rs) throws SQLException {
            List<PersonDailyWorkTimeDto> list = new ArrayList<>();
            while(rs.next()){
                PersonDailyWorkTimeDto dto = new PersonDailyWorkTimeDto();
                dto.personId = rs.getLong("personId");
                dto.nr = rs.getLong("nr");
                dto.name = rs.getString("name");
                dto.surname = rs.getString("surname");
                dto.day = rs.getDate("day");
                dto.workMinutes = DateUtils.getMinutesBetweenTwoDates(rs.getTime("startTime"), rs.getTime("endTime"));
                list.add(dto);
            }
            return list;
        }
    }
}
