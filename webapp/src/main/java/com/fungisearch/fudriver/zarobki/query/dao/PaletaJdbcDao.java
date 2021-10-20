package com.fungisearch.fudriver.zarobki.query.dao;

import com.fungisearch.fudriver.zarobki.query.dto.PaletaDetailsDto;
import com.fungisearch.fudriver.zarobki.query.dto.PaletaDetailsHeaderDto;
import com.fungisearch.fudriver.zarobki.query.dto.PaletaHeaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Dao dla przeklasyfikowań palet (lokalnie)
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@Repository
public class PaletaJdbcDao implements PaletaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<PaletaHeaderDto> getPaletaHeaders(Date dateFrom, Date dateTo) {
        return jdbcTemplate.query("select z.wozek_nr as nr, " +
                "r.id as rodzajId, " +
                "r.name as rodzajName, " +
                "r.waga as rodzajWeight, " +
                "count(*) as totalPcs, " +
                "sum(z.ilosc) as totalWeight, " +
                "z.time as harvestDate, " +
                "h.name as halaName, " +
                "c.id as cycleId " +
                "from zarobki z, rodzaj r, cykle c, hala h " +
                "where z.rodzaj_id = r.id and c.id = z.hala_id and h.id = c.hala_id " +
                "and z.time between ? and ? group by z.wozek_nr, c.id", new Object[]{dateFrom, dateTo}, new PaletaHeadersResultSetExtractor());
    }

    @Override
    public List<PaletaDetailsDto> getPaletaDetails(Long paletaId) {
        return jdbcTemplate.query("select " +
                "       z.id        as id, " +
                "       z.uniq_id   as uniqId, " +
                "       z.ludzie_id as pickerId, " +
                "       l.nr        as pickerNr, " +
                "       l.imie      as pickerName, " +
                "       l.nazwisko  as pickerSurname, " +
                "       u.user_name as createdByLogin, " +
                "       u.imie      as createdByName, " +
                "       u.nazwisko  as createdBySurname, " +
                "       tm.id       as trolleyManId, " +
                "       tm.name     as trolleyManName, " +
                "       tm.surname  as trolleyManSurname, " +
                "       z.time      as harvestDate, " +
                "       h.name      as halaName " +
                "from zarobki z " +
                "         left join ludzie l on z.ludzie_id = l.id " +
                "         left join users u on z.user_id = u.id " +
                "         left join cykle c on z.hala_id = c.id " +
                "         left join hala h on c.hala_id = h.id " +
                "         left join rodzaj r on z.rodzaj_id = r.id " +
                "left join trolley_man tm on z.wozkowy_id = tm.id " +
                "where z.wozek_nr = ?", new Object[]{paletaId}, new PaletaDetailsResultSetExtractor());
    }

    @Override
    public PaletaDetailsHeaderDto getPaletaHeader(Long paletaNr) {
        PaletaDetailsHeaderDto dto = new PaletaDetailsHeaderDto();
        List<PaletaDetailsHeaderDto> list = jdbcTemplate.query("select " +
                "r.id as rodzajId, " +
                "r.name as rodzajName, " +
                "r.waga as rodzajWeight, " +
                "z.time as harvestDate, " +
                "h.name as halaName, " +
                "c.id as cycleId " +
                "from zarobki z, rodzaj r, cykle c, hala h " +
                "where z.rodzaj_id = r.id and c.id = z.hala_id and h.id = c.hala_id " +
                "and z.wozek_nr = ?", new Object[]{paletaNr}, new PaletaDetailHeaderResultSetExtractor());
        if(!list.isEmpty()){
            dto = list.get(0);
        }
        return dto;
    }

    private class PaletaHeadersResultSetExtractor implements ResultSetExtractor<List<PaletaHeaderDto>> {

        @Override
        public List<PaletaHeaderDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, PaletaHeaderDto> map = new LinkedHashMap<Long, PaletaHeaderDto>();
            @SuppressWarnings("UnusedAssignment")
            PaletaHeaderDto dto = null;
            while (rs.next()) {
                long wozekNr = rs.getLong("nr");
                if (map.containsKey(wozekNr)) {
                    dto = map.get(wozekNr);
                    dto.totalPcs += rs.getLong("totalPcs");
                    dto.totalWeight += rs.getDouble("totalWeight");

                } else {
                    dto = new PaletaHeaderDto();
                    dto.nr = wozekNr;
                    dto.totalPcs = rs.getLong("totalPcs");
                    dto.totalWeight = rs.getDouble("totalWeight");
                    dto.hale = new LinkedHashMap<Long, String>();
                }
                dto.rodzajId = rs.getLong("rodzajId");
                dto.rodzajName = rs.getString("rodzajName");
                dto.rodzajWeight = rs.getDouble("rodzajWeight");
                dto.harvestDate = rs.getDate("harvestDate");
                dto.hale.put(rs.getLong("cycleId"), rs.getString("halaName"));
                map.put(wozekNr, dto);
            }
            return new ArrayList<PaletaHeaderDto>(map.values());
        }
    }

    private class PaletaDetailsResultSetExtractor implements ResultSetExtractor<List<PaletaDetailsDto>> {

        @Override
        public List<PaletaDetailsDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<PaletaDetailsDto> result = new ArrayList<PaletaDetailsDto>();
            while (rs.next()) {
                PaletaDetailsDto dto = new PaletaDetailsDto();
                dto.id = rs.getLong("id");
                dto.uniqId = rs.getLong("uniqId");
                dto.pickerId = rs.getLong("pickerId");
                dto.pickerNr = rs.getInt("pickerNr");
                dto.pickerName = rs.getString("pickerName");
                dto.pickerSurname = rs.getString("pickerSurname");
                dto.createdByLogin = rs.getString("createdByLogin");
                dto.createdByName = rs.getString("createdByName");
                dto.createdBySurname = rs.getString("createdBySurname");
                dto.trolleyManId = rs.getInt("trolleyManId");
                dto.trolleyManName = rs.getString("trolleyManName");
                dto.trolleyManSurname = rs.getString("trolleyManSurname");
                dto.halaName = rs.getString("halaName");
                dto.harvestDate = rs.getDate("harvestDate");
                result.add(dto);
            }
            return result;
        }
    }

    private class PaletaDetailHeaderResultSetExtractor implements ResultSetExtractor<List<PaletaDetailsHeaderDto>>{

        @Override
        public List<PaletaDetailsHeaderDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<PaletaDetailsHeaderDto> result = new ArrayList<PaletaDetailsHeaderDto>();
            while(rs.next()){
                PaletaDetailsHeaderDto dto = new PaletaDetailsHeaderDto();
                dto.rodzajId = rs.getLong("rodzajId");
                dto.rodzajName = rs.getString("rodzajName");
                dto.rodzajWeight = rs.getDouble("rodzajWeight");
                dto.cycleId = rs.getLong("cycleId");
                dto.halaName = rs.getString("halaName");
                dto.harvestDate = rs.getDate("harvestDate");
                result.add(dto);
            }
        return result;
        }
    }
}

