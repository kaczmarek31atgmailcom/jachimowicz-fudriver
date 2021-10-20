package com.fungisearch.fudriver.wozek.query.dao;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.wozek.query.dto.TrolleyManDeliverablesDto;
import com.fungisearch.fudriver.wozek.query.dto.TrolleyManDto;
import com.fungisearch.fudriver.wozek.query.dto.TrolleyManReportDto;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class TrolleyManDao {
    private final JdbcTemplate jdbcTemplate;

    public TrolleyManDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TrolleyManDto> getAllTrolleyMan(){
        return jdbcTemplate.query("select " +
                "id as id, " +
                "name as name, " +
                "surname as surname, " +
                "active as active " +
                "from trolley_man ", new TrolleyManResultSetExtractor());
    }

    private class TrolleyManResultSetExtractor implements ResultSetExtractor<List<TrolleyManDto>> {
        @Override
        public List<TrolleyManDto> extractData(ResultSet rs) throws SQLException {
            List<TrolleyManDto> list = new ArrayList<>();
            while (rs.next()){
                TrolleyManDto dto = new TrolleyManDto();
                dto.id = rs.getInt("id");
                dto.name = rs.getString("name");
                dto.surname = rs.getString("surname");
                dto.active =rs.getBoolean("active");
                list.add(dto);
            }
            return list;
        }
    }
    
    public List<TrolleyManReportDto> getTrolleyManReport(Date startDate, Date endDate){
        return jdbcTemplate.query("select " +
                "       z.wozkowy_id  as trolleyManId, " +
                "       tm.name       as trolleyManName, " +
                "       tm.surname    as trolleyManSurname, " +
                "       z.rodzaj_id   as typeId, " +
                "       r.name        as typeName, " +
                "       round(r.waga * 1000) as typeWeight, " +
                "       sum(z.ilosc) * 1000 as totalWeight, " +
                "       z.time        as date " +
                " from zarobki z " +
                "         left join trolley_man tm on z.wozkowy_id = tm.id " +
                "         left join rodzaj r on z.rodzaj_id = r.id " +
                " where z.wozkowy_id is not null " +
                " and z.time between ? and ? " +
                " group by z.wozkowy_id, z.rodzaj_id, z.time ", new Object[]{DateUtils.getStartOfDay(startDate), DateUtils.getEndOfDay(endDate)}, new TrolleyManReportResultSetExtractor());
    }

    private class TrolleyManReportResultSetExtractor implements ResultSetExtractor<List<TrolleyManReportDto>> {
        @Override
        public List<TrolleyManReportDto> extractData(ResultSet rs) throws SQLException {
            Map<Integer,TrolleyManReportDto> map = new HashMap<>();
            int trolleyManId;
            while (rs.next()) {
                trolleyManId = rs.getInt("trolleyManId");
                TrolleyManReportDto trolleyManReportDto;
                if(map.containsKey(trolleyManId)){
                    trolleyManReportDto = map.get(trolleyManId);
                } else {
                    trolleyManReportDto = new TrolleyManReportDto();
                    trolleyManReportDto.trolleyManDto = new TrolleyManDto();
                    trolleyManReportDto.trolleyManDto.id = trolleyManId;
                    trolleyManReportDto.trolleyManDto.name = rs.getString("trolleyManName");
                    trolleyManReportDto.trolleyManDto.surname = rs.getString("trolleyManSurname");
                    trolleyManReportDto.deliverables = new ArrayList<>();
                    map.put(trolleyManId,trolleyManReportDto);
                }
                TrolleyManDeliverablesDto deliverablesDto = new TrolleyManDeliverablesDto();
                deliverablesDto.typeId = rs.getInt("typeId");
                deliverablesDto.typeName = rs.getString("typeName");
                deliverablesDto.typeWeight = rs.getInt("typeWeight");
                deliverablesDto.totalWeight = rs.getInt("totalWeight");
                deliverablesDto.date = rs.getDate("date");
                trolleyManReportDto.deliverables.add(deliverablesDto);
            }
            return new ArrayList<>(map.values());
        }
    }
}
