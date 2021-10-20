package com.fungisearch.fudriver.warehouseEastMushrooms.stockreport.dao;

import com.fungisearch.fudriver.warehouseEastMushrooms.stockreport.dto.StockReportDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StockReportDao {
    private final JdbcTemplate jdbcTemplate;

    public StockReportDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<StockReportDto> getStockReport() {
        return jdbcTemplate.query("select a.remoteTypeId as remoteTypeId, " +
                "       a.localTypeId  as localTypeId, " +
                "       a.remoteWeight as remoteWeight, " +
                "       sum(a.amount)  as amount " +
                "from ( " +
                "         select sr.remote_id    as remoteTypeId, " +
                "                u.local_type_id as localTypeId, " +
                "                sr.weight       as remoteWeight, " +
                "                count(*)        as amount " +
                "         from east_mushrooms_warehouse_unit u, " +
                "              skup_rodzaj sr, " +
                "              rodzaj r " +
                "         where u.status = 1 " +
                "           and u.local_type_id = r.id " +
                "           and sr.id = r.skup_rodzaj_id " +
                "         group by sr.remote_id " +
                "         union all " +
                "         select sr.remote_id as remoteTypeId, " +
                "                w.rodzaj_id  as localTypeId, " +
                "                sr.weight    as remoteWeight, " +
                "                count(*)     as amount " +
                "         from wozek w, " +
                "              skup_rodzaj sr, " +
                "              rodzaj r " +
                "         where (w.przestrzen_id = 1 OR w.przestrzen_id = 0) " +
                "           and w.rodzaj_id = r.id " +
                "           and r.skup_rodzaj_id = sr.id " +
                "         group by sr.remote_id " +
                "     ) a " +
                "group by a.remoteTypeId " , new StockReportResultSetExtractor());
    }

    private class StockReportResultSetExtractor implements ResultSetExtractor<List<StockReportDto>> {
        @Override
        public List<StockReportDto> extractData(ResultSet rs) throws SQLException {
            List<StockReportDto> list = new ArrayList<>();
            while (rs.next()) {
                StockReportDto dto = new StockReportDto();
                dto.remoteTypeId = rs.getInt("remoteTypeId");
                dto.localTypeId = rs.getInt("localTypeId");
                dto.remoteWeight = rs.getInt("remoteWeight");
                dto.amount = rs.getInt("amount");
                list.add(dto);
            }
            return list;
        }
    }
}
