package com.fungisearch.fudriver.zarobki.query.dao;

import com.fungisearch.fudriver.type.command.model.ExportType;
import com.fungisearch.fudriver.zarobki.query.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ZarobkiJdbcDao implements ZarobkiDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ZarobkiJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BoxBrcDto findBoxBrcByWarehouseId(Long boxId) {
        return jdbcTemplate.query("SELECT w.id AS id, " +
                "z.ludzie_id AS pickerId, " +
                "z.uniq_id AS uniqId, " +
                "l.imie AS pickerName, " +
                "l.nazwisko AS pickerSurname, " +
                "z.rodzaj_id AS rodzajId, " +
                "r.name AS rodzajName, " +
                "r.waga AS rodzajWeight, " +
                "z.time AS harvestDate, " +
                "h.name AS halaName " +
                "FROM zarobki z, ludzie l, rodzaj r, hala h, cykle c, warehouse_brc w " +
                "WHERE z.ludzie_id = l.id " +
                "AND z.rodzaj_id = r.id " +
                "AND z.hala_id = c.id " +
                "AND c.hala_id = h.id " +
                "AND w.picker_id = z.ludzie_id " +
                "AND w.uniq_id = z.uniq_id " +
                "AND w.id = ? ", new Object[]{boxId}, new BoxBrcDtoResultSetExtractor()).get(0);
    }

    @Override
    public List<BoxBrcDto> findReservedBoxesByUserId(Long userId) {
        return jdbcTemplate.query("SELECT w.id AS id, " +
                "z.ludzie_id AS pickerId, " +
                "z.uniq_id AS uniqId, " +
                "l.imie AS pickerName, " +
                "l.nazwisko AS pickerSurname, " +
                "z.rodzaj_id AS rodzajId, " +
                "r.name AS rodzajName, " +
                "r.waga AS rodzajWeight, " +
                "z.time AS harvestDate, " +
                "h.name AS halaName " +
                "FROM zarobki z, ludzie l, rodzaj r, hala h, cykle c, warehouse_brc w " +
                "WHERE z.ludzie_id = l.id " +
                "AND z.rodzaj_id = r.id " +
                "AND z.hala_id = c.id " +
                "AND c.hala_id = h.id " +
                "AND w.picker_id = z.ludzie_id " +
                "AND w.uniq_id = z.uniq_id " +
                "AND w.reserved_by_id = ? ", new Object[]{userId}, new BoxBrcDtoResultSetExtractor());
    }

    @Override
    public List<SummaryBrcDto> findBrcSummaryByUserId(Long userId) {
        return jdbcTemplate.query("SELECT r.id, " +
                "r.name AS rodzajName, " +
                "r.waga AS rodzajWeight, " +
                "count(*) AS amount, " +
                "sum(r.waga * (r.id = w.rodzaj_id)) AS weight " +
                "FROM rodzaj r, warehouse_brc w WHERE " +
                "r.id = w.rodzaj_id AND w.reservation_status = 1 AND w.reserved_by_id = ? " +
                "GROUP BY r.id", new Object[]{userId}, new BrcSummaryResultSetExtractor());
    }


    private class BoxBrcDtoResultSetExtractor implements ResultSetExtractor<List<BoxBrcDto>> {

        @Override
        public List<BoxBrcDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<BoxBrcDto> list = new ArrayList<>();
            while (rs.next()) {
                BoxBrcDto dto = new BoxBrcDto();
                dto.id = rs.getLong("id");
                dto.uniqId = rs.getLong("uniqId");
                dto.pickerId = rs.getLong("pickerId");
                dto.pickerName = rs.getString("pickerName");
                dto.pickerSurname = rs.getString("pickerSurname");
                dto.rodzajId = rs.getLong("rodzajId");
                dto.rodzajName = rs.getString("rodzajName");
                dto.rodzajWeight = rs.getDouble("rodzajWeight");
                dto.harvestDate = rs.getDate("harvestDate");
                dto.halaName = rs.getString("halaName");
                list.add(dto);
            }
            return list;
        }
    }

    private class BrcSummaryResultSetExtractor implements ResultSetExtractor<List<SummaryBrcDto>> {

        @Override
        public List<SummaryBrcDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<SummaryBrcDto> list = new ArrayList<>();
            while (rs.next()) {
                SummaryBrcDto dto = new SummaryBrcDto();
                dto.rodzajName = rs.getString("rodzajName");
                dto.rodzajWeight = rs.getDouble("rodzajWeight");
                dto.amount = rs.getLong("amount");
                dto.weight = rs.getDouble("weight");
                list.add(dto);
            }
            return list;
        }
    }

    @Override
    public List<PickerZarobkiDto> getPickerZarobki(Date startDate, Date endDate) {
        return jdbcTemplate.query("SELECT " +
                "  l.id                                                                                           AS id, " +
                "  l.nr                                                                                           AS nr, " +
                "  l.imie                                                                                         AS name, " +
                "  l.nazwisko                                                                                     AS surname, " +
                "  g.name                                                                                         AS groupName, " +
                "  ifnull(round(sum(z.ilosc * 1000 * (r.export = 3))), 0)                                          AS inne, " +
                "  ifnull(round(sum(z.ilosc * 1000 * (r.export = 1))), 0)                                          AS kraj, " +
                "  ifnull(round(sum(z.ilosc * 1000 * (r.export = 2))), 0)                                          AS export, " +
                "  ifnull(round(sum(z.ilosc * 1000 * (z.test_jakosci > 0))), 0)                                    AS checked, " +
                "  ifnull(round(sum(z.ilosc * 1000 * (z.test_jakosci = 2))), 0)                                    AS rejected " +
                "FROM zarobki z " +
                "  LEFT JOIN ludzie l ON l.id = z.ludzie_id " +
                "  LEFT JOIN grupy g ON g.id = l.grupa " +
                "  LEFT JOIN rodzaj r ON z.rodzaj_id = r.id " +
                "WHERE z.time >= ? " +
                "      AND z.time <= ? " +
                "GROUP BY l.id", new Object[]{startDate, endDate}, new PickerZarobkiResultSetExtractor());
    }

    private static class PickerZarobkiResultSetExtractor implements ResultSetExtractor<List<PickerZarobkiDto>> {

        @Override
        public List<PickerZarobkiDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<PickerZarobkiDto> result = new ArrayList<>();
            PickerZarobkiDto dto;
            while (rs.next()) {
                dto = new PickerZarobkiDto();
                dto.id = rs.getLong("id");
                dto.nr = rs.getLong("nr");
                dto.name = rs.getString("name");
                dto.surname = rs.getString("surname");
                dto.groupName = rs.getString("groupName");
                dto.inne = rs.getLong("inne");
                dto.kraj = rs.getLong("kraj");
                dto.export = rs.getLong("export");
                dto.checked = rs.getLong("checked");
                dto.rejected = rs.getLong("rejected");
                result.add(dto);
            }
            return result;
        }
    }

    @Override
    public Map<Long, List<PickerZarobkiTypeGroupsDto>> getPickerZarobkiByGroups(Date startDate, Date endDate) {
        return jdbcTemplate.query("SELECT " +
                "  z.ludzie_id AS pickerId, " +
                "  r.grupa_id  AS groupId, " +
                "  g.name      AS groupName, " +
                "  round(sum(r.waga)*1000) AS total " +
                "FROM zarobki z, rodzaj r, grupy_rodzaje g " +
                "WHERE z.rodzaj_id = r.id " +
                "AND g.id = r.grupa_id " +
                "AND z.time >= ? " +
                "AND z.time <= ?" +
                "GROUP BY z.ludzie_id, r.grupa_id", new Object[]{startDate, endDate}, new PickerZarobkiByGroupsResultSetExtractor());
    }

    private static class PickerZarobkiByGroupsResultSetExtractor implements ResultSetExtractor<Map<Long, List<PickerZarobkiTypeGroupsDto>>> {

        @Override
        public Map<Long, List<PickerZarobkiTypeGroupsDto>> extractData(ResultSet rs) throws SQLException {
            Map<Long, List<PickerZarobkiTypeGroupsDto>> result = new LinkedHashMap<>();
            List<PickerZarobkiTypeGroupsDto> dtos;
            PickerZarobkiTypeGroupsDto dto;
            while (rs.next()) {
                long pickerId = rs.getLong("pickerId");
                if (result.containsKey(pickerId)) {
                    dtos = result.get(pickerId);
                } else {
                    dtos = new ArrayList<>();
                    result.put(pickerId, dtos);
                }
                dto = new PickerZarobkiTypeGroupsDto();
                dto.groupId = rs.getLong("groupId");
                dto.groupName = rs.getString("groupName");
                dto.groupTotal = rs.getLong("total");
                dtos.add(dto);
            }
            return result;
        }
    }

    @Override
    public Map<Long, Long> getPickerHours(Date startDate, Date endDate) {
        return jdbcTemplate.query("SELECT twl.person_id AS personId, " +
                "ifnull(sum(timestampdiff(MINUTE, twl.start_time, twl.end_time)), 0) AS minutes " +
                "FROM time_work_log twl " +
                "WHERE twl.start_time >= ? " +
                "AND twl.end_time <= ? " +
                "GROUP BY twl.person_id", new Object[]{startDate, endDate}, new PickerHoursResultSetExtractor());
    }

    private static class PickerHoursResultSetExtractor implements ResultSetExtractor<Map<Long, Long>> {

        @Override
        public Map<Long, Long> extractData(ResultSet rs) throws SQLException {
            Map<Long, Long> result = new HashMap<>();
            while (rs.next()) {
                result.put(rs.getLong("personId"), rs.getLong("minutes"));
            }
            return result;
        }
    }

    @Override
    public List<ZarobkiByCycleDto> getZarobkiByCycle(long cycleId) {
        return jdbcTemplate.query("SELECT " +
                "  z.time                                                   AS date, " +
                "  sum(z.ilosc * (r.export = 1)) * 1000                     AS totalKrajG, " +
                "  sum(z.ilosc * (r.export = 3)) * 1000                     AS totalInneG, " +
                "  sum(z.ilosc * (r.export = 2)) * 1000                     AS totalExportG, " +
                "  sum(z.ilosc) * 1000                                      AS totalG, " +
                "  (sum(z.ilosc * (r.export = 2)) * 10000) / (sum(ifnull(z.export,0)) + sum(ifnull(z.kraj,0)))  AS quality " +
                "FROM zarobki z " +
                "  LEFT JOIN rodzaj r ON r.id = z.rodzaj_id " +
                "WHERE z.hala_id = ? " +
                "GROUP BY z.time", new Object[]{cycleId}, new ZarobkiByCycleResultSetExtractor());
    }


    private static class ZarobkiByCycleResultSetExtractor implements ResultSetExtractor<List<ZarobkiByCycleDto>> {

        @Override
        public List<ZarobkiByCycleDto> extractData(ResultSet rs) throws SQLException {
            List<ZarobkiByCycleDto> list = new ArrayList<>();
            while (rs.next()) {
                ZarobkiByCycleDto dto = new ZarobkiByCycleDto();
                dto.date = rs.getDate("date");
                dto.totalKrajG = rs.getInt("totalKrajG");
                dto.totalInneG = rs.getInt("totalInneG");
                dto.totalExportG = rs.getInt("totalExportG");
                dto.totalG = rs.getInt("totalG");
                dto.quality = rs.getInt("quality");
                list.add(dto);
            }
            return list;
        }
    }

    @Override
    public List<DailyHarvestByTypeGroupDto> getDailyHarvestByGroupsForCycle(long cycleId) {
        return jdbcTemplate.query("SELECT z.time AS date, " +
                "  g.id AS groupId, " +
                "  g.name AS groupName, " +
                "  sum(z.ilosc) * 1000  AS totalG " +
                "  FROM zarobki z " +
                "LEFT JOIN rodzaj r ON r.id = z.rodzaj_id " +
                "LEFT JOIN grupy_rodzaje g ON g.id = r.grupa_id " +
                "WHERE z.hala_id = ? " +
                "GROUP BY z.time,g.id ", new Object[]{cycleId}, new DailyGroupsHarvestResultSetExtractor());
    }

    private static class DailyGroupsHarvestResultSetExtractor implements ResultSetExtractor<List<DailyHarvestByTypeGroupDto>> {

        @Override
        public List<DailyHarvestByTypeGroupDto> extractData(ResultSet rs) throws SQLException {
            List<DailyHarvestByTypeGroupDto> list = new ArrayList<>();
            while (rs.next()) {
                DailyHarvestByTypeGroupDto dto = new DailyHarvestByTypeGroupDto();
                dto.date = rs.getDate("date");
                dto.groupId = rs.getInt("groupId");
                dto.groupName = rs.getString("groupName");
                dto.totalG = rs.getInt("totalG");
                list.add(dto);
            }
            return list;
        }
    }

    @Override
    public List<StandDetailDto> getStandDetails(long personId, Date startDate, Date endDate) {
        return jdbcTemplate.query("SELECT " +
                "  z.rodzaj_id         AS typeId, " +
                "  r.name              AS typeName, " +
                "  r.waga * 1000       AS typeWeight, " +
                "  r.export            AS export, " +
                "  gr.id               AS groupId, " +
                "  gr.name             AS groupName, " +
                "  ts.id               AS sizeId, " +
                "  ts.name             AS sizeName, " +
                "  sum(z.ilosc) * 1000 AS weight, " +
                "  count(z.id)         AS amount " +
                "FROM zarobki z " +
                "  LEFT JOIN rodzaj r ON r.id = z.rodzaj_id " +
                "  LEFT JOIN grupy_rodzaje gr ON gr.id = r.grupa_id " +
                "  LEFT JOIN type_size ts ON r.size_id = ts.id " +
                "WHERE z.ludzie_id = ? " +
                "      AND time BETWEEN str_to_date(?, '%Y-%m-%d %H:%i:%s') AND str_to_date(?, '%Y-%m-%d %H:%i:%s') " +
                "GROUP BY z.rodzaj_id", new Object[]{personId, startDate, endDate}, new StandDetailsResultSetExtractor());
    }

    private static class StandDetailsResultSetExtractor implements ResultSetExtractor<List<StandDetailDto>> {
        @Override
        public List<StandDetailDto> extractData(ResultSet rs) throws SQLException {
            List<StandDetailDto> list = new ArrayList<>();
            while (rs.next()) {
                StandDetailDto dto = new StandDetailDto();
                dto.typeId = rs.getInt("typeId");
                dto.typeName = rs.getString("typeName");
                dto.typeWeight = rs.getInt("typeWeight");
                dto.exportType = ExportType.values()[rs.getInt("export")];
                dto.groupId = rs.getInt("groupId");
                dto.groupName = rs.getString("groupName");
                dto.sizeId = rs.getInt("sizeId");
                dto.sizeName = rs.getString("sizeName");
                dto.weight = rs.getLong("weight");
                dto.amount = rs.getInt("amount");
                list.add(dto);
            }
            return list;
        }
    }

    @Override
    public void resetUniq(int personId) {
        jdbcTemplate.update("update zarobki set uniq_id = null where ludzie_id =? ", new Object[]{personId});
    }

}
