package com.fungisearch.fudriver.type.query.dao;

import com.fungisearch.fudriver.type.command.model.ExportType;
import com.fungisearch.fudriver.type.query.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by idea on 09.03.16.
 */
@Repository
public class TypeJdbcDao implements TypeDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TypeJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<WozekTypeDto> findTypesForWozek() {
        return jdbcTemplate.query("SELECT id, name, waga FROM rodzaj WHERE archiwalne = 0 ORDER BY name", new WozekTypesResultSetExtractor());
    }

    @Override
    public List<MassHarvestTypeDto> findTypesForMassHarvest() {
        return jdbcTemplate.query("SELECT id, name, waga FROM rodzaj WHERE archiwalne = 0 ORDER BY name", new MassTypesResultSetExtractor());
    }


    @Override
    public Long findExportId(Long id) {
        return jdbcTemplate.queryForObject("SELECT export FROM rodzaj WHERE id = ?", new Object[]{id}, Long.class);
    }

    @Override
    public Double findWeight(Long id) {
        return jdbcTemplate.queryForObject("SELECT waga FROM rodzaj WHERE id = ?", new Object[]{id}, Double.class);
    }

    private static class WozekTypesResultSetExtractor implements ResultSetExtractor<List<WozekTypeDto>> {

        @Override
        public List<WozekTypeDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<WozekTypeDto> types = new ArrayList<>();
            while (rs.next()) {
                WozekTypeDto type = new WozekTypeDto();
                type.id = rs.getLong("id");
                type.name = rs.getString("name");
                type.weight = rs.getDouble("waga");
                types.add(type);
            }
            return types;
        }
    }

    class MassTypesResultSetExtractor implements ResultSetExtractor<List<MassHarvestTypeDto>> {

        @Override
        public List<MassHarvestTypeDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<MassHarvestTypeDto> types = new ArrayList<>();
            while (rs.next()) {
                MassHarvestTypeDto type = new MassHarvestTypeDto();
                type.id = rs.getLong("id");
                type.name = rs.getString("name");
                type.weight = rs.getDouble("waga");
                types.add(type);
            }
            return types;
        }
    }

    @Override
    public List<OrderTypeDto> findOrderTypes() {
        return jdbcTemplate.query("SELECT id, name, waga FROM rodzaj WHERE archiwalne = 0 ORDER BY name", new OrderTypesResultSetExtractor());
    }

    private class OrderTypesResultSetExtractor implements ResultSetExtractor<List<OrderTypeDto>> {

        @Override
        public List<OrderTypeDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<OrderTypeDto> result = new ArrayList<>();
            while (rs.next()) {
                OrderTypeDto dto = new OrderTypeDto();
                dto.id = rs.getLong("id");
                dto.name = rs.getString("name");
                dto.weight = rs.getDouble("waga");
                result.add(dto);
            }
            return result;
        }
    }



    @Override
    public List<TypeGroupDto> getActiveTypeGroups() {
        return jdbcTemplate.query("SELECT id AS id, name AS name FROM grupy_rodzaje WHERE active = 1", new TypeGroupResultSetExtractor());
    }

    private static class TypeGroupResultSetExtractor implements ResultSetExtractor<List<TypeGroupDto>> {

        @Override
        public List<TypeGroupDto> extractData(ResultSet rs) throws SQLException {
            List<TypeGroupDto> result = new ArrayList<>();
            TypeGroupDto dto;
            while (rs.next()) {
                dto = new TypeGroupDto();
                dto.id = rs.getLong("id");
                dto.name = rs.getString("name");
                result.add(dto);
            }
            return result;
        }
    }

    @Override
    public List<TypeDto> getActiveTypes() {
        return jdbcTemplate.query("SELECT " +
                "  t.id                 AS id, " +
                "  t.name               AS name, " +
                "  round(t.waga * 1000) AS weight, " +
                "  t.export             AS export, " +
                "  b.id                 AS boxId, " +
                "  b.name               AS boxName, " +
                "  g.id                 AS groupId, " +
                "  g.name               AS groupName, " +
                "  s.id                 AS sizeId, " +
                "  s.name               AS sizeName " +
                "FROM rodzaj t " +
                "  LEFT JOIN skrz_rodzaj b ON b.id = t.skrzynka_id " +
                "  LEFT JOIN grupy_rodzaje g ON g.id = t.grupa_id " +
                "  LEFT JOIN type_size s ON s.id = t.size_id " +
                "WHERE t.archiwalne = 0 ORDER BY t.name", new TypeResultSetExtractor());
    }

    private static class TypeResultSetExtractor implements ResultSetExtractor<List<TypeDto>> {

        @Override
        public List<TypeDto> extractData(ResultSet rs) throws SQLException {
            List<TypeDto> list = new ArrayList<>();
            while (rs.next()) {
                TypeDto dto = new TypeDto();
                dto.id = rs.getInt("id");
                dto.name = rs.getString("name");
                dto.weight = rs.getLong("weight");
                dto.exportType = ExportType.values()[rs.getInt("export")];
                dto.boxId = rs.getInt("boxId");
                dto.boxName = rs.getString("boxName");
                dto.groupId = rs.getInt("groupId");
                dto.groupName = rs.getString("groupName");
                dto.sizeId = rs.getInt("sizeId");
                dto.sizeName = rs.getString("sizeName");
                list.add(dto);
            }
            return list;
        }


    }

    @Override
    public List<TypeSizeDto> getActiveTypeSizes() {
        return jdbcTemplate.query("SELECT id AS id, name AS name FROM type_size WHERE active = 1", new TypeSizeResultSetExtractor());
    }


    private static class TypeSizeResultSetExtractor implements ResultSetExtractor<List<TypeSizeDto>> {

        @Override
        public List<TypeSizeDto> extractData(ResultSet rs) throws SQLException {
            List<TypeSizeDto> list = new ArrayList<>();
            while (rs.next()) {
                TypeSizeDto dto = new TypeSizeDto();
                dto.id = rs.getLong("id");
                dto.name = rs.getString("name");
                list.add(dto);
            }
            return list;
        }
    }

    @Override
    public List<TypeSizeInCyclesDto> getTypeSizesInCycles(Date startDate, Date endDate) {
        return jdbcTemplate.query("SELECT " +
                "  r.size_id  AS sizeId, " +
                "  ts.name    AS sizeName, " +
                "  z.hala_id  AS cycleId, " +
                "  h.name AS cycleName, " +
                "  c.poczatek AS cycleStartDate, " +
                "  sum(z.ilosc) * 1000 AS amount " +
                "FROM rodzaj r, type_size ts, cykle c, hala h, zarobki z " +
                "WHERE r.id = z.rodzaj_id " +
                "      AND c.id = z.hala_id " +
                "      AND h.id = c.hala_id " +
                "      AND ts.id = r.size_id " +
                "      AND z.time BETWEEN ? AND ? " +
                "GROUP BY r.size_id, z.hala_id ", new Object[]{startDate, endDate}, new TypeSizesInCyclesResultSetExtractor());
    }

    private static class TypeSizesInCyclesResultSetExtractor implements ResultSetExtractor<List<TypeSizeInCyclesDto>> {
        @Override
        public List<TypeSizeInCyclesDto> extractData(ResultSet rs) throws SQLException {
            List<TypeSizeInCyclesDto> list = new ArrayList<>();
            while (rs.next()) {
                TypeSizeInCyclesDto dto = new TypeSizeInCyclesDto();
                dto.sizeId = rs.getLong("sizeId");
                dto.sizeName = rs.getString("sizeName");
                dto.cycleId = rs.getLong("cycleId");
                dto.cycleName = rs.getString("cycleName");
                dto.cycleStartDate = rs.getString("cycleStartDate");
                dto.amount = rs.getLong("amount");
                list.add(dto);
            }
            return list;
        }
    }
}
