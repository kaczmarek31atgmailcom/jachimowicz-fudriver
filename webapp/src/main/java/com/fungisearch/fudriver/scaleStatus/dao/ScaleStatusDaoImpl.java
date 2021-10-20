package com.fungisearch.fudriver.scaleStatus.dao;

import com.fungisearch.fudriver.scaleStatus.model.ScaleStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ScaleStatusDaoImpl implements ScaleStatusDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ScaleStatusDto> getScalesStatus() throws UnsupportedEncodingException {
        return jdbcTemplate.query("select w.hala as chamberId, " +
                "                h.name as chamberName, " +
                "                w.user_id as userId,  " +
                "                u.user_name as userName,  " +
                "                w.rodzaj_id as typeId, " +
                "                r.name as typeName, " +
                "                r.waga as typeWeight, " +
                "                count(*) as amount  " +
                "                from wozek w ,hala h ,rodzaj r, users u, cykle c  " +
                "                where w.hala = c.id  " +
                "                and c.hala_id = h.id  " +
                "                and w.rodzaj_id = r.id  " +
                "                and w.user_id = u.id  " +
                "                group by w.hala, w.rodzaj_id, w.user_id", new ScaleStatusResultSetExtractor());
    }

    private static class ScaleStatusResultSetExtractor implements ResultSetExtractor<List<ScaleStatusDto>> {
        @Override
        public List<ScaleStatusDto> extractData(ResultSet rs) throws SQLException {
            List<ScaleStatusDto> result = new ArrayList<>();
            ScaleStatusDto dto;
            while (rs.next()) {
                dto = new ScaleStatusDto();
                dto.halaId = rs.getLong("chamberId");
                dto.halaName = rs.getString("chamberName");
                dto.scaleManId = rs.getLong("userId");
                dto.scaleManName = rs.getString("userName");
                dto.typeId =rs.getInt("typeId");
                dto.typeName =rs.getString("typeName");
                dto.typeWeight = rs.getDouble("typeWeight");
                dto.amount = rs.getLong("amount");
                result.add(dto);
            }
            return result;
        }
    }
}