package com.fungisearch.fudriver.paletteType.query.dao;

import com.fungisearch.fudriver.fileGenerator.fedor.model.PaletteLabel;
import com.fungisearch.fudriver.paletteType.query.dto.OutgoPaletteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marcin on 25.04.16.
 */
@Repository
public class PaletteDaoImpl implements PaletteDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<OutgoPaletteDto> findOutgoPalettes() {
        return jdbcTemplate.query("select id as id, name as name from palety where active = 1", new OutogoResultSetExtractor());
    }


    private class OutogoResultSetExtractor implements ResultSetExtractor<List<OutgoPaletteDto>> {

        @Override
        public List<OutgoPaletteDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<OutgoPaletteDto> list = new ArrayList<OutgoPaletteDto>();
            while (rs.next()) {
                OutgoPaletteDto dto = new OutgoPaletteDto();
                dto.id = rs.getLong("id");
                dto.name = rs.getString("name");
                list.add(dto);
            }
            return list;
        }
    }

    @Override
    public PaletteLabel generatePaletteLabel(Long paletteId) {
        return jdbcTemplate.query("SELECT " +
                " p.id            AS paletteId, " +
                " p.order_id      AS orderId, " +
                " c.name          AS customerName, " +
                " o.required_date AS required, " +
                " t.plate         AS plate, " +
                " b.type_id       AS typeId, " +
                " r.name          AS typeName, " +
                " r.waga          AS typeWeight, " +
                " b.amount        AS amount " +
                " FROM palette p, orders o, skrz_cust c, batch b, rodzaj r, truck t " +
                " WHERE p.order_id = o.id " +
                "  AND o.customer_id = c.id " +
                "  AND b.palette_id = p.id " +
                "  AND r.id = b.type_id " +
                "  AND  o.truck_id = t.id " +
                "  AND p.id = ? ", new Object[]{paletteId}, new PaletteLabelResultSetExtractor());
    }

    private static class PaletteLabelResultSetExtractor implements ResultSetExtractor<PaletteLabel>{

        @Override
        public PaletteLabel extractData(ResultSet rs) throws SQLException, DataAccessException {
            PaletteLabel paletteLabel = new PaletteLabel();
            Map<String,Long> details = new HashMap<String, Long>();
            while (rs.next()){
                if(paletteLabel.getPaletteId() == null ) {
                    paletteLabel.setPaletteId(rs.getLong("paletteId"));
                    paletteLabel.setOrderId((rs.getLong("orderId")));
                    paletteLabel.setCustomerName(rs.getString("customerName"));
                    paletteLabel.setRequired(rs.getDate("required"));
                    paletteLabel.setPlate(rs.getString("plate"));
                }
                String typeName = rs.getString("typeName") + " " + rs.getString("typeWeight") + " kg";
                details.put(typeName, rs.getLong("amount"));
            }
            paletteLabel.setDetails(details);
            return paletteLabel;
        }
    }
}
