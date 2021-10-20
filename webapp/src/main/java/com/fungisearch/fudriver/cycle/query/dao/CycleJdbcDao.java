package com.fungisearch.fudriver.cycle.query.dao;

import com.fungisearch.fudriver.cycle.query.dto.*;
import com.fungisearch.fudriver.cycle.query.service.CycleTechnologistReport.TechnoCycleDetail;
import com.fungisearch.fudriver.cycle.query.service.CycleTechnologistReport.TechnoCycleHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Repository
public class CycleJdbcDao implements CycleDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CycleJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<WozekCycleDto> findCyclesForWozek() {
        return jdbcTemplate.query("SELECT c.id AS id, h.id AS chamberId, h.name AS name FROM cykle c, hala h WHERE c.hala_id = h.id AND c.aktywna = 2", new CyclesForWozekResultSetExtractor());
    }

    @Override
    public List<MassHarvestCycleDto> findCycleForMassHarvest() {
        return jdbcTemplate.query("SELECT c.id AS id, h.name AS name FROM cykle c, hala h WHERE c.hala_id = h.id AND c.aktywna = 2", new MassHarvesetResultSetExtroactor());
    }


    class CyclesForWozekResultSetExtractor implements ResultSetExtractor<List<WozekCycleDto>> {

        @Override
        public List<WozekCycleDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<WozekCycleDto> cycles = new ArrayList<>();
            while (rs.next()) {
                WozekCycleDto cycle = new WozekCycleDto();
                cycle.id = rs.getLong("id");
                cycle.chamberId = rs.getLong("chamberId");
                cycle.name = rs.getString("name");
                cycles.add(cycle);
            }
            return cycles;
        }
    }

    class MassHarvesetResultSetExtroactor implements ResultSetExtractor<List<MassHarvestCycleDto>> {

        @Override
        public List<MassHarvestCycleDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<MassHarvestCycleDto> list = new ArrayList<>();
            while (rs.next()) {
                MassHarvestCycleDto dto = new MassHarvestCycleDto();
                dto.id = rs.getLong("id");
                dto.name = rs.getString("name");
                list.add(dto);
            }
            return list;
        }
    }



    @Override
    public Set<CycleHeaderDto> findAllCycles(int startDay, int endDay) {
        return jdbcTemplate.query("SELECT c.id                                                                                                        AS id, " +
                "       h.id                                                                                                        AS chamberId, " +
                "       h.name                                                                                                      AS chamberName, " +
                "       g.id                                                                                                        AS myceliumId, " +
                "       g.nazwa                                                                                                     AS myceliumName, " +
                "       k.id                                                                                                        AS subsoilId, " +
                "       k.nazwa                                                                                                     AS subsoilName, " +
                "       c.poczatek                                                                                                  AS start, " +
                "       c.koniec_1                                                                                                  AS startSecondPeriod, " +
                "       c.koniec_2                                                                                                  AS startThirdPeriod, " +
                "       c.koniec                                                                                                    AS end, " +
                "       c.aktywna                                                                                                   AS isActive, " +
                "       c.ile_metrow                                                                                                AS area, " +
                "       c.ile_ton                                                                                                   AS weight, " +
                "       c.wilgotnosc                                                                                                AS humidity, " +
                "       c.technolog_id                                                                                              AS technologistId, " +
                "       u.user_name                                                                                                 AS technologistLogin, " +
                "       u.imie                                                                                                      AS technologistName, " +
                "       u.nazwisko                                                                                                  AS technologistSurname, " +
                "       c.description                                                                                               AS description, " +
                "       c.version                                                                                                   AS version, " +
                "       sum((ifnull(z.export, 0) + ifnull(z.kraj, 0)) * 1000)                                                       AS total, " +
                "       sum(z.kraj) * 1000                                                                                          AS kraj, " +
                "       sum(z.inne) * 1000                                                                                          AS inne, " +
                "       sum(z.export) * 1000                                                                                        AS export, " +
                "       (sum(z.export) * 10000) / (sum(ifnull(z.export, 0) + ifnull(z.kraj, 0)))                                    AS quality, " +
                "       (sum(ifnull(z.export, 0) + ifnull(z.kraj, 0)) * 1000) / c.ile_metrow                                        AS amountPerMeter, " +
                "       (sum(ifnull(z.export, 0) + ifnull(z.kraj, 0)) * 1000000) / c.ile_ton                                        AS amountPerTon, " +
                "       (sum(ifnull(z.export, 0) + ifnull(z.kraj, 0)) * 1000000) / (c.ile_ton - (c.ile_ton * (c.wilgotnosc / 100))) AS dryAmount, " +
                "       count(DISTINCT (z.time))                                                                                    AS activeDaysAmount " +
                "FROM cykle c " +
                "         LEFT JOIN hala h ON h.id = c.hala_id " +
                "         LEFT JOIN grzybnia g ON g.id = c.grzybnia_id " +
                "         LEFT JOIN kompostownia k ON k.id = c.kompostownia_id " +
                "         LEFT JOIN users u ON u.id = c.technolog_id " +
                "         LEFT JOIN zarobki z ON z.hala_id = c.id " +
                "         LEFT JOIN rodzaj r ON r.id = z.rodzaj_id " +
                "WHERE c.poczatek >= ? " +
                "  AND c.poczatek <= ?  " +
                "GROUP BY c.id ", new Object[]{startDay, endDay}, new CyclesResultSetExtractor());
    }


    private static class CyclesResultSetExtractor implements ResultSetExtractor<Set<CycleHeaderDto>> {

        @Override
        public Set<CycleHeaderDto> extractData(ResultSet rs) throws SQLException {
            Set<CycleHeaderDto> set = new HashSet<>();
            while (rs.next()) {
                CycleHeaderDto dto = new CycleHeaderDto();
                dto.id = rs.getLong("id");
                dto.chamberId = rs.getLong("chamberId");
                dto.chamberName = rs.getString("chamberName");
                dto.myceliumId = rs.getLong("myceliumId");
                dto.myceliumName = rs.getString("myceliumName");
                dto.subsoilId = rs.getLong("subsoilId");
                dto.subsoilName = rs.getString("subsoilName");
                dto.startDate = rs.getInt("start");
                dto.startSecondPeriod = rs.getInt("startThirdPeriod");
                dto.startThirdPeriod = rs.getInt("startThirdPeriod");
                dto.endDate = rs.getInt("end");
                dto.isActive = rs.getInt("isActive") == 2;
                dto.area = rs.getInt("area");
                dto.weight = rs.getInt("weight");
                dto.humidity = rs.getInt("humidity");
                dto.technologistId = rs.getLong("technologistId");
                dto.technologistLogin = rs.getString("technologistLogin");
                dto.technologistName = rs.getString("technologistName");
                dto.technologistSurname = rs.getString("technologistSurname");
                dto.description = rs.getString("description");
                dto.version = rs.getInt("version");
                dto.total = rs.getLong("total");
                dto.export = rs.getLong("export");
                dto.kraj = rs.getLong("kraj");
                dto.inne = rs.getLong("inne");
                dto.quality = rs.getInt("quality");
                dto.amountPerMeter = rs.getInt("amountPerMeter");
                dto.amountPerTon = rs.getInt("amountPerTon");
                dto.dryAmount = rs.getInt("dryAmount");
                dto.activeDaysAmount = rs.getInt("activeDaysAmount");
                set.add(dto);
            }
            return set;
        }
    }

    @Override
    public Set<CycleDatesDto> findCurrentCycleDates() {
        return jdbcTemplate.query("SELECT " +
                "  c.id              AS cycleId, " +
                "  h.id              AS chamberId, " +
                "  h.name            AS chamberName, " +
                "  c.grzybnia_id     AS myceliumId, " +
                "  m.nazwa           AS myceliumName, " +
                "  c.kompostownia_id AS subsoilId, " +
                "  s.nazwa           AS subsoilName, " +
                "  c.zalozenie       AS initDate, " +
                "  c.poczatek        AS startFirstPeriod, " +
                "  c.koniec_1        AS startSecondPeriod, " +
                "  c.koniec_2        AS startThirdPeriod, " +
                "  c.ile_metrow      AS cycleArea, " +
                "  h.powierzchnia    AS chamberArea, " +
                "  c.ile_ton         AS cycleWeight, " +
                "  c.wilgotnosc      AS humidity, " +
                "  c.technolog_id    AS technologistId, " +
                "  u.user_name       AS technologistLogin, " +
                "  u.imie            AS technologistName, " +
                "  u.nazwisko        AS technologistSurname, " +
                "  c.description     AS description, " +
                "  c.version         AS version " +
                "FROM hala h " +
                "  LEFT JOIN cykle c ON c.hala_id = h.id AND c.aktywna = 2 " +
                "  LEFT JOIN kompostownia s ON c.kompostownia_id = s.id " +
                "  LEFT JOIN grzybnia m ON c.grzybnia_id = m.id " +
                "  LEFT JOIN users u ON u.id = c.technolog_id" +
                " where h.aktywna > 0 ", new CycleDatesResultSetExtractor());
    }

    private static class CycleDatesResultSetExtractor implements ResultSetExtractor<Set<CycleDatesDto>> {

        @Override
        public Set<CycleDatesDto> extractData(ResultSet rs) throws SQLException {
            Set<CycleDatesDto> set = new HashSet<>();
            while (rs.next()) {
                CycleDatesDto dto = new CycleDatesDto();
                dto.cycleId = rs.getInt("cycleId");
                dto.chamberId = rs.getInt("chamberId");
                dto.chamberName = rs.getString("chamberName");
                dto.initDate = rs.getInt("initDate");
                if (dto.initDate > 0) {
                    dto.startFirstPeriod = rs.getInt("startFirstPeriod");
                    dto.startSecondPeriod = rs.getInt("startSecondPeriod");
                    dto.startThirdPeriod = rs.getInt("startThirdPeriod");
                    dto.area = rs.getInt("cycleArea");
                    dto.myceliumId = rs.getInt("myceliumId");
                    dto.myceliumName = rs.getString("myceliumName");
                    dto.subsoilId = rs.getInt("subsoilId");
                    dto.subsoilName = rs.getString("subsoilName");
                    dto.humidity = rs.getInt("humidity");
                    dto.weight = rs.getInt("cycleWeight");
                    dto.technologistId = rs.getInt("technologistId");
                    dto.technologistLogin = rs.getString("technologistLogin");
                    dto.technologistName = rs.getString("technologistName");
                    dto.technologistSurname = rs.getString("technologistSurname");
                    dto.description = rs.getString("description");
                    dto.version = rs.getInt("version");
                    dto.isActive = true;
                } else {
                    dto.area = rs.getInt("chamberArea");
                    dto.isActive = false;
                }
                set.add(dto);
            }
            return set;
        }
    }

    @Override
    public CycleDatesDto findCycleDatesById(long id) {
        return jdbcTemplate.query("SELECT " +
                "  c.id              AS cycleId, " +
                "  h.id              AS chamberId, " +
                "  h.name            AS chamberName, " +
                "  c.grzybnia_id     AS myceliumId, " +
                "  m.nazwa           AS myceliumName, " +
                "  c.kompostownia_id AS subsoilId, " +
                "  s.nazwa           AS subsoilName, " +
                "  c.zalozenie       AS initDate, " +
                "  c.poczatek        AS startFirstPeriod, " +
                "  c.koniec_1        AS startSecondPeriod, " +
                "  c.koniec_2        AS startThirdPeriod, " +
                "  c.ile_metrow      AS cycleArea, " +
                "  h.powierzchnia    AS chamberArea, " +
                "  c.ile_ton         AS cycleWeight, " +
                "  c.wilgotnosc      AS humidity, " +
                "  c.technolog_id    AS technologistId, " +
                "  u.user_name       AS technologistLogin, " +
                "  u.imie            AS technologistName, " +
                "  u.nazwisko        AS technologistSurname, " +
                "  c.description     AS description, " +
                "  c.version         AS version " +
                " FROM cykle c  " +
                "  LEFT JOIN hala h ON h.id = c.hala_id" +
                "  LEFT JOIN kompostownia s ON c.kompostownia_id = s.id " +
                "  LEFT JOIN grzybnia m ON c.grzybnia_id = m.id " +
                "  LEFT JOIN users u ON user_name = c.technolog_id " +
                " WHERE c.id = ?", new Object[]{id}, new CycleDatesByIdResultSetExtractor());
    }


    private static class CycleDatesByIdResultSetExtractor implements ResultSetExtractor<CycleDatesDto> {

        @Override
        public CycleDatesDto extractData(ResultSet rs) throws SQLException {
            CycleDatesDto dto = new CycleDatesDto();
            while (rs.next()) {
                dto.cycleId = rs.getInt("cycleId");
                dto.chamberId = rs.getInt("chamberId");
                dto.chamberName = rs.getString("chamberName");
                dto.initDate = rs.getInt("initDate");
                dto.startFirstPeriod = rs.getInt("startFirstPeriod");
                dto.startSecondPeriod = rs.getInt("startSecondPeriod");
                dto.startThirdPeriod = rs.getInt("startThirdPeriod");
                dto.area = rs.getInt("cycleArea");
                dto.myceliumId = rs.getInt("myceliumId");
                dto.myceliumName = rs.getString("myceliumName");
                dto.subsoilId = rs.getInt("subsoilId");
                dto.subsoilName = rs.getString("subsoilName");
                dto.humidity = rs.getInt("humidity");
                dto.weight = rs.getInt("cycleWeight");
                dto.technologistId = rs.getInt("technologistId");
                dto.technologistLogin = rs.getString("technologistLogin");
                dto.technologistName = rs.getString("technologistName");
                dto.technologistSurname = rs.getString("technologistSurname");
                dto.description = rs.getString("description");
                dto.version = rs.getInt("version");
                dto.isActive = true;
            }
            return dto;
        }
    }

    @Override
    public CycleDatesDto findCycleDatesByChamberId(long id) {
        return jdbcTemplate.query("SELECT " +
                "  c.id              AS cycleId, " +
                "  h.id              AS chamberId, " +
                "  h.name            AS chamberName, " +
                "  c.grzybnia_id     AS myceliumId, " +
                "  m.nazwa           AS myceliumName, " +
                "  c.kompostownia_id AS subsoilId, " +
                "  s.nazwa           AS subsoilName, " +
                "  c.zalozenie       AS initDate, " +
                "  c.poczatek        AS startFirstPeriod, " +
                "  c.koniec_1        AS startSecondPeriod, " +
                "  c.koniec_2        AS startThirdPeriod, " +
                "  c.ile_metrow      AS cycleArea, " +
                "  h.powierzchnia    AS chamberArea, " +
                "  c.ile_ton         AS cycleWeight, " +
                "  c.wilgotnosc      AS humidity, " +
                "  c.technolog_id    AS technologistId, " +
                "  u.user_name       AS technologistLogin, " +
                "  u.imie            AS technologistName, " +
                "  u.nazwisko        AS technologistSurname, " +
                "  c.description     AS description, " +
                "  c.version         AS version " +
                "FROM hala h " +
                "  LEFT JOIN cykle c ON c.hala_id = h.id AND c.aktywna = 2 " +
                "  LEFT JOIN kompostownia s ON c.kompostownia_id = s.id " +
                "  LEFT JOIN grzybnia m ON c.grzybnia_id = m.id " +
                "  LEFT JOIN users u ON user_name = c.technolog_id" +
                " where h.id = ? ", new Object[]{id}, new CycleDatesByIdResultSetExtractor());
    }

    @Override
    public CycleHeaderDto findHeader(long cycleId) {
        return jdbcTemplate.query("SELECT " +
                "       c.id                                                                                                            AS id,  " +
                "       h.id                                                                                                            AS chamberId,  " +
                "       h.name                                                                                                          AS chamberName,  " +
                "       g.id                                                                                                            AS myceliumId,  " +
                "       g.nazwa                                                                                                         AS myceliumName,  " +
                "       k.id                                                                                                            AS subsoilId,  " +
                "       k.nazwa                                                                                                         AS subsoilName,  " +
                "       c.poczatek                                                                                                      AS start,  " +
                "       c.koniec_1                                                                                                      AS startSecondPeriod,  " +
                "       c.koniec_2                                                                                                      AS startThirdPeriod,  " +
                "       c.koniec                                                                                                        AS end,  " +
                "       c.aktywna                                                                                                       AS isActive,  " +
                "       c.ile_metrow                                                                                                    AS area,  " +
                "       c.ile_ton                                                                                                       AS weight,  " +
                "       c.wilgotnosc                                                                                                    AS humidity,  " +
                "       c.technolog_id                                                                                                  AS technologistId,  " +
                "       u.user_name                                                                                                     AS technologistLogin,  " +
                "       u.imie                                                                                                          AS technologistName,  " +
                "       u.nazwisko                                                                                                      AS technologistSurname,  " +
                "       c.description                                                                                                   AS description,  " +
                "       c.version                                                                                                       AS version,  " +
                "       sum(ifnull(z.export, 0) + ifnull(z.kraj, 0)) * 1000                                                             AS total,  " +
                "       sum(z.export) * 1000                                                                                            AS export,  " +
                "       sum(z.kraj) * 1000                                                                                              AS kraj,  " +
                "       sum(z.inne) * 1000                                                                                              AS inne,  " +
                "       (sum(z.export) * 10000) / sum((ifnull(z.export, 0) + ifnull(z.kraj, 0)))                                        AS quality,  " +
                "       (sum((ifnull(z.export, 0) + ifnull(z.kraj, 0))) * 1000) / c.ile_metrow                                          AS amountPerMeter,  " +
                "       (sum((ifnull(z.export, 0) + ifnull(z.kraj, 0))) * 1000000) / c.ile_ton                                          AS amountPerTon,  " +
                "       (sum((ifnull(z.export, 0) + ifnull(z.kraj, 0)) * 1000000) / (c.ile_ton - (c.ile_ton * (c.wilgotnosc / 100))))   AS dryAmount,  " +
                "       (sum((ifnull(z.export, 0) + ifnull(z.kraj, 0))) * 1000000) / c.ile_ton                                          AS amountPerTon,  " +
                "       (sum(((ifnull(z.export, 0) + ifnull(z.kraj, 0))) * 1000000) / (c.ile_ton - (c.ile_ton * (c.wilgotnosc / 100)))) AS dryAmount,  " +
                "       count(DISTINCT (z.time))                                                                                        AS activeDaysAmount  " +
                "FROM cykle c  " +
                "         LEFT JOIN hala h ON h.id = c.hala_id  " +
                "         LEFT JOIN grzybnia g ON g.id = c.grzybnia_id  " +
                "         LEFT JOIN kompostownia k ON k.id = c.kompostownia_id  " +
                "         LEFT JOIN users u ON u.id = c.technolog_id  " +
                "         LEFT JOIN zarobki z ON z.hala_id = c.id  " +
                "         LEFT JOIN rodzaj r ON r.id = z.rodzaj_id  " +
                "WHERE c.id = ?  " +
                "GROUP BY c.id", new Object[]{cycleId}, new CycleResultSetExtractor());

    }


    private static class CycleResultSetExtractor implements ResultSetExtractor<CycleHeaderDto> {

        @Override
        public CycleHeaderDto extractData(ResultSet rs) throws SQLException {
            CycleHeaderDto dto = new CycleHeaderDto();
            while (rs.next()) {

                dto.id = rs.getLong("id");
                dto.chamberId = rs.getLong("chamberId");
                dto.chamberName = rs.getString("chamberName");
                dto.myceliumId = rs.getLong("myceliumId");
                dto.myceliumName = rs.getString("myceliumName");
                dto.subsoilId = rs.getLong("subsoilId");
                dto.subsoilName = rs.getString("subsoilName");
                dto.startDate = rs.getInt("start");
                dto.startSecondPeriod = rs.getInt("startThirdPeriod");
                dto.startThirdPeriod = rs.getInt("startThirdPeriod");
                dto.endDate = rs.getInt("end");
                dto.isActive = rs.getInt("isActive") == 2;
                dto.area = rs.getInt("area");
                dto.weight = rs.getInt("weight");
                dto.humidity = rs.getInt("humidity");
                dto.technologistId = rs.getLong("technologistId");
                dto.technologistLogin = rs.getString("technologistLogin");
                dto.technologistName = rs.getString("technologistName");
                dto.technologistSurname = rs.getString("technologistSurname");
                dto.description = rs.getString("description");
                dto.version = rs.getInt("version");
                dto.total = rs.getLong("total");
                dto.export = rs.getLong("export");
                dto.kraj = rs.getLong("kraj");
                dto.inne = rs.getLong("inne");
                dto.quality = rs.getInt("quality");
                dto.amountPerMeter = rs.getInt("amountPerMeter");
                dto.amountPerTon = rs.getInt("amountPerTon");
                dto.dryAmount = rs.getInt("dryAmount");
                dto.activeDaysAmount = rs.getInt("activeDaysAmount");
            }
            return dto;
        }
    }

    @Override
    public List<CycleByBrigadeDto> findCycleByBrigades(Date startDate, Date endDate) {
        return jdbcTemplate.query("SELECT " +
                "  g.id as brigadeId, " +
                "  g.name as brigadeName, " +
                "  c.id as cycleId, " +
                "  c.poczatek as startDate, " +
                "  h.name as chamberName, " +
                "  sum(z.ilosc * (r.export = 1)) * 1000 AS kraj, " +
                "  sum(z.ilosc * (r.export = 2)) * 1000 AS export, " +
                "  sum(z.ilosc * (r.export = 3)) * 1000 AS inne, " +
                "  sum(z.ilosc) * 1000                 AS total " +
                "FROM zarobki z " +
                "  LEFT JOIN rodzaj r ON r.id = z.rodzaj_id " +
                "  LEFT JOIN cykle c ON c.id = z.hala_id " +
                "  LEFT JOIN hala h ON h.id = c.hala_id " +
                "  LEFT JOIN ludzie l ON l.id = z.ludzie_id " +
                "  LEFT JOIN grupy g ON g.id = l.grupa " +
                " WHERE z.time >= ? " +
                " AND z.time <= ? " +
                " AND z.ilosc > 0 " +
                "GROUP BY g.id,c.id", new Object[]{startDate,endDate}, new CycleByBrigadeResultSetExtractor());
    }

    private static class CycleByBrigadeResultSetExtractor implements ResultSetExtractor<List<CycleByBrigadeDto>>{

        @Override
        public List<CycleByBrigadeDto> extractData(ResultSet rs) throws SQLException{
            List<CycleByBrigadeDto> list = new ArrayList<>();
            while (rs.next()){
                CycleByBrigadeDto dto = new CycleByBrigadeDto();
                dto.brigadeId = rs.getLong("brigadeId");
                dto.brigadeName = rs.getString("brigadeName");
                dto.cycleId = rs.getLong("cycleId");
                dto.startDate = rs.getInt("startDate");
                dto.chamberName = rs.getString("chamberName");
                dto.kraj = rs.getLong("kraj");
                dto.inne = rs.getLong("inne");
                dto.export = rs.getLong("export");
                dto.total = rs.getLong("total");
                list.add(dto);
            }
        return list;
        }
    }

    @Override
    public List<TechnoCycleHeader> findTechnoHeaders(int startDate, int endDate) {
        return jdbcTemplate.query("SELECT " +
                "  c.technolog_id    AS technologistId, " +
                "  u.user_name       AS technologistLogin, " +
                "  u.imie            AS technologistName, " +
                "  u.nazwisko        AS technologistSurname, " +
                "  sum(c.ile_metrow) AS area, " +
                "  sum(c.ile_ton)    AS weight, " +
                "  count(*)          AS cycleAmount " +
                "FROM cykle c " +
                "  LEFT JOIN users u ON u.id = c.technolog_id " +
                "WHERE c.koniec >= ? " +
                "AND c.koniec <= ? " +
                "GROUP BY c.technolog_id", new Object[] {startDate,endDate}, new TechnoHeadersResultSetExtractor());
    }

    private static class TechnoHeadersResultSetExtractor implements ResultSetExtractor<List<TechnoCycleHeader>>{

        @Override
        public List<TechnoCycleHeader> extractData(ResultSet rs) throws SQLException {
            List<TechnoCycleHeader> list = new ArrayList<>();
            while(rs.next()){
                TechnoCycleHeader header = new TechnoCycleHeader();
                header.technologistId = rs.getLong("technologistId");
                header.technologistName = rs.getString("technologistName");
                header.technologistSurname = rs.getString("technologistSurname");
                header.area = rs.getInt("area");
                header.weight = rs.getInt("weight");
                header.cycleAmount = rs.getInt("cycleAmount");
                list.add(header);
            }
            return list;
        }
    }
    @Override
    public List<TechnoCycleDetail> findTechnoDetails(int startDate, int endDate) {
        return jdbcTemplate.query("SELECT " +
                "  z.time                               AS date, " +
                "  c.technolog_id                       AS technologistId, " +
                "  u.user_name                          AS technologistLogin, " +
                "  u.imie                               AS technologistName, " +
                "  u.nazwisko                           AS technologistSurname, " +
                "  sum(z.inne) * 1000 AS inne, " +
                "  sum(z.kraj) * 1000 AS kraj, " +
                "  sum(z.export) * 1000 AS export, " +
                "  sum(ifnull(z.kraj,0) + ifnull(z.export,0)) * 1000                  AS total " +
                "FROM cykle c " +
                "  LEFT JOIN zarobki z ON z.hala_id = c.id " +
                "  LEFT JOIN users u ON u.id = c.technolog_id " +
                "  LEFT JOIN rodzaj r ON r.id = z.rodzaj_id " +
                "WHERE c.koniec >= ? AND c.koniec <= ? " +
                "GROUP BY z.time, c.technolog_id " +
                "HAVING sum(z.ilosc) > 0", new Object[]{startDate,endDate}, new TechnoDetailsResultSetExtractor());
    }

    private static class TechnoDetailsResultSetExtractor implements ResultSetExtractor<List<TechnoCycleDetail>>{

        @Override
        public List<TechnoCycleDetail> extractData(ResultSet rs) throws SQLException{
            List<TechnoCycleDetail> list = new ArrayList<>();
            while (rs.next()){
                TechnoCycleDetail detail = new TechnoCycleDetail();
                detail.day = rs.getDate("date");
                detail.technologistId = rs.getLong("technologistId");
                detail.technologistName = rs.getString("technologistName");
                detail.technologistSurname = rs.getString("technologistSurname");
                detail.inne = rs.getLong("inne");
                detail.kraj = rs.getLong("kraj");
                detail.export = rs.getLong("export");
                detail.total = rs.getLong("total");
                list.add(detail);
            }
        return list;
        }
    }

}

