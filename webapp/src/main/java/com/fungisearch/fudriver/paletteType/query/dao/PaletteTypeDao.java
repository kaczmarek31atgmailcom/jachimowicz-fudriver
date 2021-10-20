package com.fungisearch.fudriver.paletteType.query.dao;

import com.fungisearch.fudriver.paletteType.query.dto.PaletteTypeDto;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PaletteTypeDao {
    private final JdbcTemplate jdbcTemplate;

    public PaletteTypeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PaletteTypeDto> getActivePaletteTypes(){
        return jdbcTemplate.query("select " +
                "remote_palette_id as paletteId, " +
                "name as name, " +
                "ifnull(sort_order,0) as sortOrder " +
                "from palette_type where active = 1", new PaletteTypeResultSetExtractor());
    }

    private class PaletteTypeResultSetExtractor implements ResultSetExtractor<List<PaletteTypeDto>> {
        @Override
        public List<PaletteTypeDto> extractData(ResultSet rs) throws SQLException {
            List<PaletteTypeDto> list = new ArrayList<>();
            while (rs.next()){
                PaletteTypeDto dto = new PaletteTypeDto();
                dto.paletteId = rs.getInt("paletteId");
                dto.name = rs.getString("name");
                dto.sortOrder = rs.getInt("sortOrder");
                list.add(dto);
            }
            return list;
        }
    }
}
