package com.fungisearch.fudriver.settings.query.dao;


import com.fungisearch.fudriver.settings.command.model.Mycelium;
import com.fungisearch.fudriver.settings.query.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by idea on 17.03.16.
 */
@Repository
public class SettingsJdbcDao implements SettingsDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int getValue(int id) {
        int value = jdbcTemplate.queryForObject("SELECT value FROM config WHERE id = ?", Integer.class, new Object[]{id});
        return value;
    }

    @Override
    public Map<Long, String> getReclassifyReasons() {
        return jdbcTemplate.query("SELECT id AS id, " +
                "description AS description " +
                "FROM reclassify_reason " +
                "WHERE active = 1", new ReclassifyReasonResultSetExtractor());
    }


    private static class ReclassifyReasonResultSetExtractor implements ResultSetExtractor<Map<Long, String>> {

        @Override
        public Map<Long, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, String> map = new HashMap<Long, String>();
            while (rs.next()) {
                map.put(rs.getLong("id"), rs.getString("message"));
            }
            return map;
        }
    }

    @Override
    public List<CurrencyDto> getActiveCurencies() {
        return jdbcTemplate.query("SELECT code AS code, " +
                " order_index AS order_index " +
                " FROM active_currency ORDER BY order_index", new ActiveCurrencyResultSetExtractor());
    }

    private static class ActiveCurrencyResultSetExtractor implements ResultSetExtractor<List<CurrencyDto>> {

        @Override
        public List<CurrencyDto> extractData(ResultSet rs) throws SQLException {
            List<CurrencyDto> result = new ArrayList<>();
            CurrencyDto dto;
            while (rs.next()) {
                dto = new CurrencyDto();
                dto.code = rs.getString("code");
                dto.index = rs.getInt("order_index");
                result.add(dto);
            }
            return result;
        }
    }

    @Override
    public CompanyDto getCompany(long id) {
        return jdbcTemplate.query("SELECT " +
                " id as id, " +
                " name AS name, " +
                " street AS street, " +
                " city AS city, " +
                " nip AS nip," +
                " regon AS regon," +
                " phone_no AS phoneNo, " +
                " email AS email, " +
                " ggn as ggn " +
                " FROM company WHERE id  = ?", new Object[]{id}, new CompanyResultSetExtractor());
    }

    private static class CompanyResultSetExtractor implements ResultSetExtractor<CompanyDto> {

        @Override
        public CompanyDto extractData(ResultSet rs) throws SQLException {
            CompanyDto dto = new CompanyDto();
            while (rs.next()) {
                dto.id = rs.getInt("id");
                dto.name = rs.getString("name");
                dto.street = rs.getString("street");
                dto.city = rs.getString("city");
                dto.nip = rs.getString("nip");
                dto.regon = rs.getString("regon");
                dto.phoneNo = rs.getString("phoneNo");
                dto.email = rs.getString("email");
                dto.ggn = rs.getString("ggn");
            }
            return dto;
        }
    }

    @Override
    public List<CompanyDto> getCompanies() {
        return jdbcTemplate.query("SELECT " +
                " id as id, " +
                " name AS name, " +
                " street AS street, " +
                " city AS city, " +
                " nip AS nip," +
                " regon AS regon," +
                " phone_no AS phoneNo, " +
                " email AS email," +
                " ggn as ggn " +
                " FROM company ", new CompaniesResultSetExtractor());
    }

    private class CompaniesResultSetExtractor implements ResultSetExtractor<List<CompanyDto>> {
        @Override
        public List<CompanyDto> extractData(ResultSet rs) throws SQLException {
            List<CompanyDto> list= new ArrayList<>();
            while (rs.next()){
                CompanyDto dto = new CompanyDto();
                dto.id = rs.getInt("id");
                dto.name = rs.getString("name");
                dto.street = rs.getString("street");
                dto.city = rs.getString("city");
                dto.nip = rs.getString("nip");
                dto.regon = rs.getString("regon");
                dto.phoneNo = rs.getString("phoneNo");
                dto.email = rs.getString("email");
                dto.ggn = rs.getString("ggn");
                list.add(dto);
            }
        return list;
        }
    }

    @Override
    public Set<DepotDto> getActiveDepots() {
        return jdbcTemplate.query("SELECT " +
                "  ch.id       AS id, " +
                "  ch.name     AS name, " +
                "  count(h.id) AS used " +
                "FROM chlodnie ch " +
                "  LEFT JOIN hala h ON h.chlodnia_id = ch.id " +
                "WHERE ch.active = 1 " +
                "GROUP BY ch.id;", new ActiveDepotResultSetExtractor());
    }

    private static class ActiveDepotResultSetExtractor implements ResultSetExtractor<Set<DepotDto>> {

        @Override
        public Set<DepotDto> extractData(ResultSet rs) throws SQLException {
            Set<DepotDto> set = new HashSet<>();
            while (rs.next()) {
                DepotDto dto = new DepotDto();
                dto.id = rs.getLong("id");
                dto.name = rs.getString("name");
                dto.isUsed = rs.getInt("used") > 0;
                set.add(dto);
            }
            return set;
        }

    }

    @Override
    public Set<ChamberDto> getActiveChambers() {
        return jdbcTemplate.query("SELECT h.id AS id, " +
                "  h.name AS name, " +
                "  h.powierzchnia  AS area, " +
                "  h.chlodnia_id AS depotId, " +
                "  ch.name AS depotName, " +
                "  c.id as companyId, " +
                "  c.name as companyName " +
                "  FROM hala h " +
                "LEFT JOIN chlodnie ch ON ch.id = h.chlodnia_id " +
                "LEFT JOIN company c ON c.id = h.company_id " +
                "WHERE h.aktywna > 0", new ActiveChambersResultSetExtractor());
    }

    private static class ActiveChambersResultSetExtractor implements ResultSetExtractor<Set<ChamberDto>> {

        @Override
        public Set<ChamberDto> extractData(ResultSet rs) throws SQLException {
            Set<ChamberDto> set = new HashSet<>();
            while (rs.next()) {
                ChamberDto dto = new ChamberDto();
                dto.id = rs.getLong("id");
                dto.name = rs.getString("name");
                dto.area = rs.getInt("area");
                dto.depotId = rs.getInt("depotId");
                dto.depotName = rs.getString("depotName");
                dto.companyId = rs.getInt("companyId");
                dto.companyName = rs.getString("companyName");
                set.add(dto);
            }
            return set;
        }

    }

    @Override
    public Set<SubsoilDto> getActiveSubsoils() {
        return jdbcTemplate.query("select id as id, nazwa as name from kompostownia where active = 1", new SubsoilResultSetExtractor());
    }

    private static class SubsoilResultSetExtractor implements ResultSetExtractor<Set<SubsoilDto>>{

        @Override
        public Set<SubsoilDto> extractData(ResultSet rs) throws SQLException {
            Set<SubsoilDto> set = new HashSet<>();
            while(rs.next()){
                SubsoilDto dto = new SubsoilDto();
                dto.id = rs.getLong("id");
                dto.name = rs.getString("name");
                set.add(dto);
            }
        return set;
        }
    }

    @Override
    public Set<MyceliumDto> getActiveMyceliums() {
        return jdbcTemplate.query("select id as id, nazwa as name from grzybnia where active = 1", new MyceliumResultSetExtractor());
    }

    private static class MyceliumResultSetExtractor implements ResultSetExtractor<Set<MyceliumDto>>{

        @Override
        public Set<MyceliumDto> extractData(ResultSet rs) throws SQLException {
            Set<MyceliumDto> set = new HashSet<>();
            while (rs.next()){
                MyceliumDto dto = new MyceliumDto();
                dto.id = rs.getLong("id");
                dto.name = rs.getString("name");
                set.add(dto);
            }
        return set;
        }
    }

}
