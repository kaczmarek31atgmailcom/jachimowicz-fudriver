package com.fungisearch.fudriver.reclassification.query.dao;

import com.fungisearch.fudriver.reclassification.query.dto.LocalRodzajDto;
import com.fungisearch.fudriver.reclassification.query.dto.SkupRodzajDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marcin on 03.02.16.
 */

@Repository
public class SkupRodzajJdbcDao implements SkupRodzajDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<SkupRodzajDto> getAllTypes() {
        List<SkupRodzajDto> types = jdbcTemplate.query("select id, remote_id, name, weight,description,group_id, group_name,active from skup_rodzaj", new RemoteTypeResultSetExtractor());
        return types;
    }

    @Override
    public Map<Long, LocalRodzajDto> getMapOfAllLocalTypes() {
        Map<Long, LocalRodzajDto> types = jdbcTemplate.query("select id, name, waga, archiwalne from rodzaj", new MapTypeResultSetExtractor());
        return types;
    }

    @Override
    public List<LocalRodzajDto> getAllLocalTypes() {
        List<LocalRodzajDto> types = jdbcTemplate.query("select id, name, waga, archiwalne,skup_rodzaj_id from rodzaj", new LocalRodzajResultSetExtractor());
        return types;
    }

    @Override
    public List<LocalRodzajDto> getActiveLocalTypes() {
        List<LocalRodzajDto> types = jdbcTemplate.query("select id, name, waga, archiwalne, skup_rodzaj_id from rodzaj where archiwalne = 0", new LocalRodzajResultSetExtractor());
        return types;
    }

    class RemoteTypeResultSetExtractor implements ResultSetExtractor<List<SkupRodzajDto>> {

        @Override
        public List<SkupRodzajDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, SkupRodzajDto> map = new LinkedHashMap<Long, SkupRodzajDto>();
            SkupRodzajDto type = null;
            while (rs.next()) {
                long typeId = rs.getLong("id");
                if (map.containsKey(typeId)) {
                    type = map.get(typeId);
                } else {
                    type = new SkupRodzajDto();
                    type.id = typeId;
                }
                type.remoteId = rs.getLong("remote_id");
                type.name = rs.getString("name");
                type.weight = rs.getDouble("weight");
                type.description = rs.getString("description");
                type.typeGroupId = rs.getLong("group_id");
                type.typeGroupName = rs.getString("group_name");
                type.active = rs.getBoolean("active");
                map.put(typeId, type);
            }
            return new ArrayList(map.values());
        }
    }

    class LocalRodzajResultSetExtractor implements ResultSetExtractor<List<LocalRodzajDto>> {

        @Override
        public List<LocalRodzajDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, LocalRodzajDto> map = new LinkedHashMap<Long, LocalRodzajDto>();
            LocalRodzajDto dto = null;
            while (rs.next()) {
                long rodzajId = rs.getLong("id");
                if (map.containsKey(rodzajId)) {
                    dto = map.get(rodzajId);
                } else {
                    dto = new LocalRodzajDto();
                    dto.id = rodzajId;
                    dto.name = rs.getString("name");
                    dto.weight = rs.getDouble("waga");
                    dto.remoteTypeId = rs.getInt("skup_rodzaj_id");
                    if (((Integer) rs.getInt("archiwalne")).equals(0)) {
                        dto.active = true;
                    } else {
                        dto.active = false;
                    }
                    map.put(rodzajId, dto);
                }
            }
            return new ArrayList<LocalRodzajDto>(map.values());
        }
    }

    class MapTypeResultSetExtractor implements ResultSetExtractor<Map<Long, LocalRodzajDto>>{

        @Override
        public Map<Long, LocalRodzajDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long,LocalRodzajDto> map = new LinkedHashMap<Long, LocalRodzajDto>();
            LocalRodzajDto rodzaj = null;
            while(rs.next()){
                long rodzajId = rs.getLong("id");
                if(map.containsKey(rodzajId)){
                    rodzaj = map.get(rodzajId);
                } else {
                    rodzaj = new LocalRodzajDto();
                    rodzaj.id = rodzajId;
                    rodzaj.name = rs.getString("name");
                    rodzaj.weight = rs.getDouble("waga");
                    map.put(rodzajId, rodzaj);
                }
            }
        return map;
        }
    }



}
