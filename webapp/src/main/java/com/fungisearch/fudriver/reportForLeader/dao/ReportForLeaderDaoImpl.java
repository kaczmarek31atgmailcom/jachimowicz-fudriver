package com.fungisearch.fudriver.reportForLeader.dao;

import com.fungisearch.fudriver.reportForLeader.dto.WorkPeriodDto;
import com.fungisearch.fudriver.reportForLeader.model.CollectedByChamber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Dell Latitude E6430 on 2014-11-25.
 */
@Repository
public class ReportForLeaderDaoImpl implements ReportForLeaderDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyymmdd");


    @Override
    public List<CollectedByChamber> getCollectedByChamber(String startDate, String endDate) {
        List<CollectedByChamber> collectedByChambers = new ArrayList<CollectedByChamber>();
        double krajTotal = 0.0;
        double exportTotal = 0.0;
        double total = 0.0;
        String sql = "select z.hala_id as cycleId, h.name as chamberName, sum(IFNULL(z.kraj,0)) as krajTotal, sum(IFNULL(z.export,0)) as exportTotal from zarobki z, hala h, cykle c where z.hala_id = c.id and c.hala_id = h.id and z.time between ? and ? group by z.hala_id order by h.id, h.name";
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        for (Map<String, Object> row : rows) {
            CollectedByChamber collectedByChamber = new CollectedByChamber();
            krajTotal = (Double) row.get("krajTotal");
            exportTotal = (Double) row.get("exportTotal");
            total = krajTotal + exportTotal;
            collectedByChamber.setCycleId((Integer) row.get("cycleId"));
            collectedByChamber.setChamberName((String) row.get("chamberName"));

            collectedByChamber.setKraj(krajTotal);
            collectedByChamber.setExport(exportTotal);
            collectedByChamber.setTotal(total);
            collectedByChambers.add(collectedByChamber);
        }
        return collectedByChambers;
    }

    @Override
    public List<WorkPeriodDto> getWorkPeriods(Date startDate, Date endDate) {
        return jdbcTemplate.query("select " +
                "person_id as personId, " +
                "start_time as startDate, " +
                "end_time as endDate " +
                "from time_work_log " +
                "where (start_time >= ? and start_time <= ? " +
                "or (end_time >= ? and end_time <= ?)) " +
                "or (start_time <= ? and end_time is null)", new Object[]{startDate, endDate, startDate, endDate, endDate}, new WorkPeriodResultSetExtractor());
    }

private static class WorkPeriodResultSetExtractor implements ResultSetExtractor<List<WorkPeriodDto>>{

    @Override
    public List<WorkPeriodDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<WorkPeriodDto> result = new ArrayList<WorkPeriodDto>();
        WorkPeriodDto dto;
        while (rs.next()){
            dto = new WorkPeriodDto();
            dto.personId = rs.getLong("personId");
            dto.startDate = rs.getTimestamp("startDate");
            Date endDate = rs.getTimestamp("endDate");
            if(endDate == null){
                dto.endDate = new Date();
            } else {
                dto.endDate = endDate;
            }
            result.add(dto);
        }
    return result;
    }
}
}
