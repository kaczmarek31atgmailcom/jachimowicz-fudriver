package com.fungisearch.fudriver.box.query.dao;

import com.fungisearch.fudriver.box.query.dto.BoxDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BoxDaoImpl implements BoxDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<BoxDto> findActive() {
        return jdbcTemplate.query("select id as id, name as name from skrz_rodzaj where active = 1", new BoxResultSetExtractor());
    }

    private static class BoxResultSetExtractor implements ResultSetExtractor<List<BoxDto>>{

        @Override
        public List<BoxDto> extractData(ResultSet rs) throws SQLException {
            List<BoxDto> list = new ArrayList<>();
            while (rs.next()){
                BoxDto dto = new BoxDto();
                dto.id = rs.getLong("id");
                dto.name = rs.getString("name");
                list.add(dto);
            }
        return list;
        }
    }
}
