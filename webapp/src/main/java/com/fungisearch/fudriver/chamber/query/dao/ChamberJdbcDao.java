package com.fungisearch.fudriver.chamber.query.dao;

import com.fungisearch.fudriver.chamber.query.dto.ChamberTypeDto;
import com.fungisearch.fudriver.chamber.query.dto.ChamberTypesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by marcin on 17.03.16.
 */
@Repository
public class ChamberJdbcDao implements ChamberDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Map<Long, Long> getCycleDepotMapping() {
        return jdbcTemplate.query("select c.id as cycleId, h.chlodnia_id as depotId from hala h , cykle c where c.hala_id = h.id", new ChamberDepotResultSetExtractor());
    }

    class ChamberDepotResultSetExtractor implements ResultSetExtractor<Map<Long, Long>> {

        @Override
        public Map<Long, Long> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Long> cycles = new LinkedHashMap<Long, Long>();
            while (rs.next()) {
                cycles.put(rs.getLong("cycleId"), rs.getLong("depotId"));
            }
            return cycles;
        }
    }

    @Override
    public List<ChamberTypesDto> getHarvestByChamber(Date startDate, Date endDate) {
        return jdbcTemplate.query("SELECT " +
                "  z.hala_id    AS cycleId, " +
                "  h.name       AS chamberName, " +
                "  c.poczatek   AS startDate, " +
                "  z.rodzaj_id  AS typeId, " +
                "  r.name       AS typeName, " +
                "  r.waga       AS weight, " +
                "  sum(z.ilosc) AS totalWeight, " +
                "  r.export     AS exportType " +
                "FROM zarobki z " +
                "  LEFT JOIN cykle c ON c.id = z.hala_id " +
                "  LEFT JOIN hala h ON h.id = c.hala_id " +
                "  LEFT JOIN rodzaj r ON r.id = z.rodzaj_id " +
                " WHERE rodzaj_id >0 " +
                " AND z.time >= ? " +
                " AND z.time <= ? " +
                " GROUP BY z.hala_id, z.rodzaj_id ", new Object[]{startDate, endDate}, new HarvestByChamberResultSetExtractor());
    }

    private static class HarvestByChamberResultSetExtractor implements ResultSetExtractor<List<ChamberTypesDto>> {

        @Override
        public List<ChamberTypesDto> extractData(ResultSet rs) throws SQLException {
            Map<Long, ChamberTypesDto> map = new HashMap<>();
            ChamberTypesDto chamberTypesDto;
            ChamberTypeDto chamberTypeDto;
            while (rs.next()) {
                long cycleId = rs.getLong("cycleId");
                if (map.containsKey(cycleId)) {
                    chamberTypesDto = map.get(cycleId);
                } else {
                    chamberTypesDto = new ChamberTypesDto();
                    chamberTypesDto.cycleId = cycleId;
                    chamberTypesDto.chamberName = rs.getString("chamberName");
                    chamberTypesDto.startDate = rs.getDate("startDate");
                    chamberTypesDto.types = new ArrayList<>();
                    map.put(cycleId, chamberTypesDto);
                }
                chamberTypeDto = new ChamberTypeDto();
                chamberTypeDto.id = rs.getLong("typeId");
                chamberTypeDto.name = rs.getString("typeName");
                chamberTypeDto.weight = rs.getDouble("weight");
                chamberTypeDto.totalWeight = rs.getDouble("totalWeight");
                chamberTypeDto.exportType = rs.getInt("exportType");
                chamberTypesDto.types.add(chamberTypeDto);
            }
            return new ArrayList<>(map.values());
        }
    }

}
