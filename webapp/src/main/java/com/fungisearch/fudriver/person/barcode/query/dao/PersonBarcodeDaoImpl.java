package com.fungisearch.fudriver.person.barcode.query.dao;


import com.fungisearch.fudriver.person.barcode.query.dto.PersonBarcodeHeaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcin on 09.12.16.
 */

@Repository
public class PersonBarcodeDaoImpl implements PersonBarcodeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<PersonBarcodeHeaderDto> findHeaders() {
        return jdbcTemplate.query(" SELECT " +
                "l.id              AS id, " +
                "l.nr              AS nr, " +
                "l.imie            AS name, " +
                "l.nazwisko        AS surname, " +
                "sum(u.used =0)    AS freeCodesAmount, " +
                "max(u.uniq_id) AS highestNumber " +
                "FROM ludzie l " +
                "LEFT JOIN uniq u ON u.ludzie_id = l.id " +
                "WHERE l.active = 1 " +
                "AND l.id <> 1000 " +
                "GROUP BY l.id ", new PersonBarcodeDaoResultSetExtractor());
    }

    private static class PersonBarcodeDaoResultSetExtractor implements ResultSetExtractor<List<PersonBarcodeHeaderDto>> {

        @Override
        public List<PersonBarcodeHeaderDto> extractData(ResultSet rs) throws SQLException {
            List<PersonBarcodeHeaderDto> list = new ArrayList<PersonBarcodeHeaderDto>();
            PersonBarcodeHeaderDto dto;
            while (rs.next()) {
                dto = new PersonBarcodeHeaderDto();
                dto.id = rs.getLong("id");
                dto.nr = rs.getLong("nr");
                dto.name = rs.getString("name");
                dto.surname = rs.getString("surname");
                dto.freeCodesAmount = rs.getLong("freeCodesAmount");
                dto.highestNumber = rs.getLong("highestNumber");
                list.add(dto);
            }
            return list;
        }
    }
}
