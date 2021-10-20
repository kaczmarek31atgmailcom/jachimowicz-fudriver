package com.fungisearch.fudriver.reclassification.estimation.query.dao;

import com.fungisearch.fudriver.reclassification.estimation.query.dto.CycleEstimationDetailDto;
import com.fungisearch.fudriver.reclassification.estimation.query.dto.CycleEstimationHeaderDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EstimationDaoImpl implements EstimationDao {

    private final JdbcTemplate jdbcTemplate;

    public EstimationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CycleEstimationHeaderDto> getAllCycles() {
        return jdbcTemplate.query("select " +
                "       c.id         as cycleId, " +
                "       c.ile_metrow as squareMeters, " +
                "       c.zalozenie  as initDate, " +
                "       z.time       as day, " +
                "       r.group_id   as groupId, " +
                "       sum(z.ilosc) * 100 as weight " +
                "from cykle c, " +
                "     zarobki z, " +
                "     skup_rodzaj r " +
                "where c.id = z.hala_id " +
                "  and z.rodzaj_id = r.local_rodzaj_id " +
                "group by c.id, z.time, r.group_id ", new AllCyclesExtractor());
    }

    private static class AllCyclesExtractor implements ResultSetExtractor<List<CycleEstimationHeaderDto>> {
        @Override
        public List<CycleEstimationHeaderDto> extractData(ResultSet rs) throws SQLException {
            Map<Long, CycleEstimationHeaderDto> map = new HashMap<>();
            while (rs.next()) {
                long cycleId = rs.getLong("cycleId");
                CycleEstimationHeaderDto header;
                if (map.containsKey(cycleId)) {
                    header = map.get(cycleId);
                } else {
                    header = new CycleEstimationHeaderDto();
                    header.cycleId = cycleId;
                    header.initDate = rs.getDate("initDate");
                    header.squareMeters = rs.getInt("squareMeters");
                    header.details = new ArrayList<>();
                    map.put(cycleId, header);
                }
                CycleEstimationDetailDto detail = new CycleEstimationDetailDto();
                detail.day = rs.getDate("day");
                detail.groupId = rs.getInt("groupId");
                detail.weight = rs.getInt("weight");
                header.details.add(detail);
            }
            return new ArrayList<>(map.values());
        }
    }
}
