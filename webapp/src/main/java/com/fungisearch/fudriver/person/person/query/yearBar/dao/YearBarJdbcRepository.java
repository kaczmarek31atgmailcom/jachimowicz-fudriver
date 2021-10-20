package com.fungisearch.fudriver.person.person.query.yearBar.dao;

import com.fungisearch.fudriver.person.person.query.yearBar.dto.SimpleWorkPeriodDto;
import com.fungisearch.fudriver.person.person.query.yearBar.dto.WorkPeriodDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by idea on 05.03.16.
 */
@Repository
public class YearBarJdbcRepository implements YearBarRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public WorkPeriodDto findActive(Long personId) {
        WorkPeriodDto period = jdbcTemplate.query("select startDate , version from okresy_zatrudnienia where ludzie_id = ? and enddate is null", new Object[]{personId}, new ActivePeriodResultSetExtractor());
        return period;
    }

    @Override
    public List<WorkPeriodDto> findAllForPerson(Long personId) {
        List<WorkPeriodDto> periods = jdbcTemplate.query("select startdate , enddate, version from okresy_zatrudnienia where ludzie_id = ?", new Object[]{personId}, new ListActivePeriodResultSetExtractor());
        return periods;
    }

    @Override
    public List<SimpleWorkPeriodDto> findSimpleWorkPeriodsForPerson(Long personId) {
        List<SimpleWorkPeriodDto> periods = jdbcTemplate.query("select id, startdate, enddate, version from okresy_zatrudnienia where ludzie_id = ?", new Object[]{personId}, new SimpleWorkPeriodsResultSetExtractor());
        return periods;
    }


    class ActivePeriodResultSetExtractor implements ResultSetExtractor<WorkPeriodDto> {

        @Override
        public WorkPeriodDto extractData(ResultSet rs) throws SQLException, DataAccessException {
            WorkPeriodDto period = new WorkPeriodDto();
            while (rs.next()) {
                period.setStartDate(rs.getDate("startDate"));
                period.version = (rs.getLong("version"));
                period.setActive(true);
            }
            return period;
        }
    }

    class ListActivePeriodResultSetExtractor implements ResultSetExtractor<List<WorkPeriodDto>> {

        @Override
        public List<WorkPeriodDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<WorkPeriodDto> list = new ArrayList<WorkPeriodDto>();
            while (rs.next()) {
                WorkPeriodDto period = new WorkPeriodDto();
                period.setStartDate(rs.getDate("startdate"));
                period.setEndDate(rs.getDate("enddate"));
                period.version = (rs.getLong("version"));
                period.setActive(true);
                list.add(period);
            }
            return list;
        }
    }

    class SimpleWorkPeriodsResultSetExtractor implements ResultSetExtractor<List<SimpleWorkPeriodDto>> {

        @Override
        public List<SimpleWorkPeriodDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, SimpleWorkPeriodDto> map = new LinkedHashMap<Long, SimpleWorkPeriodDto>();
            SimpleWorkPeriodDto period = null;
            while (rs.next()) {
                long periodId = rs.getLong("id");
                if (map.containsKey(periodId)) {
                    period = map.get(periodId);
                } else {
                    period = new SimpleWorkPeriodDto();
                    period.id = periodId;
                }
                    period.startDate = rs.getDate("startdate");
                    period.endDate = rs.getDate("enddate");
                    period.version = rs.getLong("version");
                    map.put(periodId, period);
            }
            return new ArrayList<SimpleWorkPeriodDto>(map.values());
        }
    }

}
