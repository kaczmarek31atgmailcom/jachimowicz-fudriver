package com.fungisearch.fudriver.wozek.query.dao;

import com.fungisearch.fudriver.wozek.query.dto.TotalTrolleysStatusDto;
import com.fungisearch.fudriver.wozek.query.dto.TrolleysSummaryDto;
import com.fungisearch.fudriver.wozek.query.dto.WozekEntryDto;
import com.fungisearch.fudriver.wozek.query.dto.WozekHeaderDto;
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
 * Created by idea on 13.03.16.
 */
@Repository
public class WozekJdbcDao implements WozekDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<WozekEntryDto> getWozekStatus(Long wozekNr) {
        return jdbcTemplate.query("SELECT " +
                "w.id AS id, " +
                "r.name AS typeName, " +
                "r.waga AS typeWeight, " +
                "w.nr_rwaczki AS pickerId, " +
                "l.nr AS pickerNr, " +
                "w.uniq_id AS uniqId, " +
                "w.brygadzista_id AS leaderId, " +
                "w.wozkowy_id AS trolleyManId, " +
                "h.name AS chamberName, " +
                "w.test_jakosci AS qualityStatus " +
                "FROM wozek w, rodzaj r, cykle c, hala h, ludzie l " +
                "WHERE w.rodzaj_id = r.id " +
                "AND l.id = w.nr_rwaczki " +
                "AND w.hala = c.id " +
                "AND c.hala_id = h.id " +
                "AND w.przestrzen_id = 0 " +
                "AND w.wozek_nr = ? ORDER BY w.uniq_id, w.nr_rwaczki", new Object[]{wozekNr}, new WozekStatusResultSetExtractor());
    }

    @Override
    public Map<Long, Long> getNumbersOnTrolley(Long userId) {
        return jdbcTemplate.query("SELECT rodzaj_id AS rodzaj_id, max(wozek_nr) AS wozek_nr FROM wozek WHERE przestrzen_id = 0 AND user_id = ? GROUP BY rodzaj_id", new Object[]{userId}, new NumbersOnTrolleyResultSetExtractor());
    }

    @Override
    public void setTrolleyNumber(Long userId, Long rodzajId, Long trolleyNumber) {
        jdbcTemplate.update("UPDATE wozek SET wozek_nr = ? WHERE przestrzen_id = 0 AND rodzaj_id = ? AND user_id = ?", new Object[]{trolleyNumber, rodzajId, userId});
    }

    @Override
    public List<WozekHeaderDto> getWozekHeaders(Long userId) {
        List<WozekHeaderDto> headers = jdbcTemplate.query("SELECT w.wozek_nr AS wozekNr, " +
                "r.id AS rodzajId, " +
                "r.name AS rodzajName, " +
                "r.waga AS rodzajWeight, " +
                "w.przestrzen_id AS spaceId, " +
                "count(*) AS totalPcs, " +
                "(count(*) * r.waga) AS totalWeight " +
                "FROM wozek w, rodzaj r " +
                "WHERE r.id = w.rodzaj_id " +
                "AND (w.przestrzen_id = 0 OR w.przestrzen_id = 3) " +
                "AND user_id = ? " +
                "GROUP BY w.wozek_nr", new Object[]{userId}, new WozekHeadersResultSetExtractor());
        return headers;
    }

    @Override
    public void sendForApproval(Long wozekId) {
        jdbcTemplate.update("UPDATE wozek SET przestrzen_id = 1 WHERE wozek_nr = ?", wozekId);
    }

    @Override
    public void onHold(Long wozekId) {
        jdbcTemplate.update("UPDATE wozek SET przestrzen_id = 3 WHERE wozek_nr = ?", wozekId);
    }

    @Override
    public void activate(Long wozekId) {
        jdbcTemplate.update("UPDATE wozek SET przestrzen_id = 0 WHERE wozek_nr = ?", wozekId);
    }

    @Override
    public Boolean isTrolleyReadyToActivate(Long wozekId, Long userId) {
        List<Integer> list = jdbcTemplate.query("SELECT wozek_nr FROM wozek WHERE user_id = ? AND przestrzen_id = 0", new Object[]{wozekId}, new SameTypeResultSetExtractor());
        return list.isEmpty();
    }

    class WozekStatusResultSetExtractor implements ResultSetExtractor<List<WozekEntryDto>> {

        @Override
        public List<WozekEntryDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<WozekEntryDto> wozek = new ArrayList<WozekEntryDto>();
            while (rs.next()) {
                WozekEntryDto dto = new WozekEntryDto();
                dto.id = rs.getLong("id");
                dto.typeName = rs.getString("typeName");
                dto.typeWeight = rs.getDouble("typeWeight");
                dto.pickerId = rs.getLong("pickerId");
                dto.pickerNr = rs.getInt("pickerNr");
                dto.uniqId = rs.getLong("uniqId");
                dto.trolleyManId = rs.getLong("trolleyManId");
                dto.leaderId = rs.getLong("trolleyManId");
                dto.chamberName = rs.getString("chamberName");
                dto.qualityStatus = rs.getInt("qualityStatus");
                wozek.add(dto);
            }
            return wozek;
        }
    }


    class NumbersOnTrolleyResultSetExtractor implements ResultSetExtractor<Map<Long, Long>> {

        @Override
        public Map<Long, Long> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Long> result = new LinkedHashMap<Long, Long>();
            while (rs.next()) {
                result.put(rs.getLong("rodzaj_id"), rs.getLong("wozek_nr"));
            }
            return result;
        }
    }

    class WozekHeadersResultSetExtractor implements ResultSetExtractor<List<WozekHeaderDto>> {

        @Override
        public List<WozekHeaderDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<WozekHeaderDto> headers = new ArrayList<WozekHeaderDto>();
            while (rs.next()) {
                WozekHeaderDto header = new WozekHeaderDto();
                header.rodzajId = rs.getLong("rodzajId");
                header.wozekNr = rs.getLong("wozekNr");
                header.rodzajName = rs.getString("rodzajName");
                header.rodzajWeight = rs.getDouble("rodzajWeight");
                header.totalPcs = rs.getLong("totalPcs");
                header.totalWeight = (double) ((long) (rs.getDouble("totalWeight") * 100)) / 100;
                header.spaceId = rs.getInt("spaceId");
                headers.add(header);
            }
            return headers;
        }
    }

    class SameTypeResultSetExtractor implements ResultSetExtractor<List<Integer>> {

        @Override
        public List<Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Integer> list = new ArrayList<Integer>();
            while (rs.next()) {
                list.add(rs.getInt("wozek_nr"));
            }
            return list;
        }
    }


    @Override
    public List<TotalTrolleysStatusDto> getTotalTrolleyStatus() {
        return jdbcTemplate.query("SELECT " +
                "  w.rodzaj_id AS typeId, " +
                "  r.name      AS typeName, " +
                "  r.waga      AS typeWeight, " +
                "  count(*)    AS amount " +
                "FROM wozek w " +
                "  LEFT JOIN rodzaj r ON r.id = w.rodzaj_id " +
                "GROUP BY w.rodzaj_id", new TotalTrolleysStatusResultSetExtractor());
    }

    private static class TotalTrolleysStatusResultSetExtractor implements ResultSetExtractor<List<TotalTrolleysStatusDto>> {

        @Override
        public List<TotalTrolleysStatusDto> extractData(ResultSet rs) throws SQLException {
            List<TotalTrolleysStatusDto> list = new ArrayList<>();
            TotalTrolleysStatusDto dto;
            while (rs.next()) {
                dto = new TotalTrolleysStatusDto();
                dto.typeId = rs.getLong("typeId");
                dto.typeName = rs.getString("typeName");
                dto.typeWeight = rs.getDouble("typeWeight");
                dto.amount = rs.getLong("amount");
                list.add(dto);
            }
            return list;
        }

    }

    @Override
    public List<TrolleysSummaryDto> getTrollyesSummary(long userId) {
        return jdbcTemplate.query("SELECT " +
                "  l.id as pickerId, " +
                "  l.nr as pickerNr, " +
                "  l.imie as pickerName, " +
                "  l.nazwisko as pickerSurname, " +
                "  r.name as typeName, " +
                "  r.waga as typeWeight, " +
                "  h.name as chamberName, " +
                "  count(*) as totalAmount, " +
                "  r.waga * count(*) as totalWeight " +
                "  from wozek w " +
                "    left JOIN ludzie l on l.id = w.nr_rwaczki " +
                "    left join rodzaj r on r.id = w.rodzaj_id " +
                "    left join cykle c on c.id = w.hala " +
                "    left join hala h on h.id = c.hala_id " +
                "where przestrzen_id = 0 " +
                "and w.user_id = ? " +
                " GROUP BY w.nr_rwaczki,w.hala,w.rodzaj_id" +
                " order by w.nr_rwaczki, w.hala, w.rodzaj_id desc ", new Object[]{userId}, new TrolleysSummaryResultSetExtractor());
    }

    private static class TrolleysSummaryResultSetExtractor implements ResultSetExtractor<List<TrolleysSummaryDto>>{

        @Override
        public List<TrolleysSummaryDto> extractData(ResultSet rs) throws SQLException {
            List<TrolleysSummaryDto> list = new ArrayList<>();
            while(rs.next()){
                TrolleysSummaryDto dto = new TrolleysSummaryDto();
                dto.pickerId = rs.getLong("pickerId");
                dto.pickerNr = rs.getInt("pickerNr");
                dto.pickerName = rs.getString("pickerName");
                dto.pickerSurname = rs.getString("pickerSurname");
                dto.typeName = rs.getString("typeName");
                dto.typeWeight =(long) (rs.getDouble("typeWeight") * 1000);
                dto.chamberName = rs.getString("chamberName");
                dto.totalAmount = rs.getLong("totalAmount");
                dto.totalWeight = (long) (rs.getDouble("totalWeight") * 1000);
                list.add(dto);
            }
        return list;
        }
    }
}
