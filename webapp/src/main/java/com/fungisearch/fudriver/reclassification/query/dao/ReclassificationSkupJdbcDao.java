package com.fungisearch.fudriver.reclassification.query.dao;

import com.fungisearch.fudriver.reclassification.command.model.ReclassificationDetailSkup;
import com.fungisearch.fudriver.reclassification.query.dto.LocalReclassificationDetailDto;
import com.fungisearch.fudriver.reclassification.query.dto.LocalReclassificationHeaderDto;
import com.fungisearch.fudriver.reclassification.query.dto.ReclassificationPickerDto;
import com.fungisearch.fudriver.reclassification.query.dto.UnitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by marcin on 03.02.16.
 */
@Repository
public class ReclassificationSkupJdbcDao implements ReclassificationSkupDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Set<Long> findRecliassificationSkupIds() {
        Set<Long> set = jdbcTemplate.query("select distinct reclassification_id  from skup_reclassification_details", new ReclassificationResultSetExtractor());
        return set;
    }

    @Override
    public List<LocalReclassificationHeaderDto> findNotProcessedLocalHeaders() {
        List<LocalReclassificationHeaderDto> headers = jdbcTemplate.query("select h.id as id, " +
                "h.remote_id as remoteId, " +
                "h.description as description, " +
                "h.created as created, " +
                "count(d.id) as totalAmount,  " +
                "h.processed as processed " +
                "from skup_reclassification h , skup_reclassification_details d " +
                "where d.reclassification_id = h.id and h.processed = 0 group by h.id ", new HeadersResultSetExtractor());
        return headers;
    }


    @Override
    public List<LocalReclassificationHeaderDto> findProcessedLocalHeaders() {
        List<LocalReclassificationHeaderDto> headers = jdbcTemplate.query("select h.id as id, " +
                "h.remote_id as remoteId, " +
                "h.description as description, " +
                "h.created as created, " +
                "count(d.id) as totalAmount,  " +
                "h.processed as processed " +
                "from skup_reclassification h , skup_reclassification_details d " +
                "where d.reclassification_id = h.id and h.processed = 1 group by h.id ", new HeadersResultSetExtractor());
        return headers;
    }


    @Override
    public LocalReclassificationHeaderDto findLocalHeader(Long id) {
        LocalReclassificationHeaderDto header = null;
        String sql = "select h.id as id, " +
                "h.remote_id as remoteId, " +
                "h.description as description, " +
                "h.created as created, " +
                "h.processed as processed, " +
                "h.processing_date as processedDate, " +
                "count(d.id) as totalAmount  " +
                "from skup_reclassification h , skup_reclassification_details d " +
                "where d.reclassification_id = h.id and h.id = ? ";
        try{
            header = (LocalReclassificationHeaderDto) jdbcTemplate.queryForObject(sql, new Object[]{id}, new HeaderRowMapper());
        } catch (EmptyResultDataAccessException e){}
        return header;
    }

    class HeaderRowMapper implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int i) throws SQLException {
            LocalReclassificationHeaderDto header = new LocalReclassificationHeaderDto();
            header.id = rs.getLong("id");
            header.remoteId  = rs.getLong("remoteId");
            header.description = rs.getString("message");
            header.created = rs.getDate("created");
            header.totalAmount = rs.getLong("totalAmount");
            header.processedDate = rs.getDate("processedDate");
            int processed = rs.getInt("processed");
            header.processed = false;
            if(processed == 1){
                header.processed = true;
            }
            return header;
        }
    }


    @Override
    public List<LocalReclassificationDetailDto> getDetails(Long reclassificationId) {
        List<LocalReclassificationDetailDto> list = jdbcTemplate.query("select d.id as id, " +
                "d.active as active, " +
                "d.barcode as barcode, " +
                "d.picker_id as pickerId, " +
                "d.uniq_id as uniqId, " +
                "d.local_rodzaj_id as afterReclassificationTypeId, " +
                "sr.local_rodzaj_id as reclassificationTypeId from " +
                "skup_reclassification_details d , skup_rodzaj sr where " +
                "sr.remote_id = d.remote_rodzaj_id " +
                "and d.reclassification_id = ?", new Object[]{reclassificationId}, new LocalReclassificactionDetailsResultSetExtractor());
        return list;
    }

    @Override
    public UnitDto findSingleUnit(Long pickerId, Long uniqId) {
        UnitDto unitDto = null;
        String sql = "select z.id as id, " +
                "z.uniq_id as uniqId, " +
                "z.ludzie_id as pickerId, " +
                "l.nr as pickerNr, " +
                "l.imie as pickerName, " +
                "l.nazwisko as pickerSurname, " +
                "g.name as pickerGroupName, " +
                "h.name as halaName, " +
                "z.time as harvestDate, " +
                "r.name as typeName, " +
                "r.waga as typeWeight, " +
                "z.zaplacono as payed, " +
                "u.user_name as wagowyLogin, " +
                "u.imie as wagowyName, " +
                "u.nazwisko as wagowySurname " +
                "from zarobki z, ludzie l, grupy g, hala h, cykle c, rodzaj r, users u " +
                "where z.ludzie_id = l.id and " +
                "l.grupa = g.id and " +
                "z.hala_id = c.id and " +
                "h.id = c.hala_id and " +
                "z.rodzaj_id = r.id and " +
                "u.id = z.user_id and " +
                "z.uniq_id = ? and " +
                "z.ludzie_id = ?";
        try {
            unitDto = (UnitDto) jdbcTemplate.queryForObject(sql, new Object[]{uniqId, pickerId}, new UnitDtoRowMapper());
        } catch (EmptyResultDataAccessException e){}
        return unitDto;
    }

    class UnitDtoRowMapper implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int i) throws SQLException {
            UnitDto unitDto = new UnitDto();
            unitDto.id = rs.getLong("id");
            unitDto.uniqId = rs.getLong("uniqId");
            unitDto.pickerId = rs.getLong("pickerId");
            unitDto.pickerNr = rs.getInt("pickerNr");
            unitDto.pickerName = rs.getString("pickerName");
            unitDto.pickerSurname = rs.getString("pickerSurname");
            unitDto.pickerGroupName = rs.getString("pickerGroupName");
            unitDto.halaName = rs.getString("halaName");
            unitDto.harvestDate = rs.getDate("harvestDate");
            unitDto.typeName = rs.getString("typeName");
            unitDto.typeWeight = rs.getDouble("typeWeight");
            int payed = rs.getInt("payed");
            if(payed == 1){
                unitDto.payed = true;
            } else {
                unitDto.payed = false;
            }
            unitDto.wagowyLogin = rs.getString("wagowyLogin");
            unitDto.wagowyName = rs.getString("wagowyName");
            unitDto.wagowySurname = rs.getString("wagowySurname");
            return unitDto;
        }
    }

    class LocalReclassificactionDetailsResultSetExtractor implements ResultSetExtractor<List<LocalReclassificationDetailDto>> {

        @Override
        public List<LocalReclassificationDetailDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, LocalReclassificationDetailDto> map = new LinkedHashMap<Long, LocalReclassificationDetailDto>();
            LocalReclassificationDetailDto detail = null;
            while (rs.next()) {
                long detailId = rs.getLong("id");
                if (map.containsKey(detailId)) {
                    detail = map.get(detailId);
                } else {
                    detail = new LocalReclassificationDetailDto();
                    detail.id = detailId;
                    detail.barcode = to13Chars(rs.getLong("barcode"));
                    detail.remotePickerId = rs.getLong("pickerId");
                    detail.remoteUniqId = rs.getLong("uniqId");
                    detail.reclassifcationTypeId = rs.getLong("reclassificationTypeId");
                    detail.afterReclassifcationTypeId = rs.getLong("afterReclassificationTypeId");
                    detail.active = false;
                    int active = rs.getInt("active");
                    if(active == 1){
                        detail.active =  true;
                    }
                    map.put(detailId, detail);
                }
            }
            return new ArrayList<LocalReclassificationDetailDto>(map.values());
        }
    }

    class ReclassificationResultSetExtractor implements ResultSetExtractor<Set<Long>> {

        @Override
        public Set<Long> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Set<Long> result = new HashSet<Long>();
            while (rs.next()) {
                result.add(rs.getLong("reclassification_id"));
            }
            return result;
        }
    }

    class HeadersResultSetExtractor implements ResultSetExtractor<List<LocalReclassificationHeaderDto>> {

        @Override
        public List<LocalReclassificationHeaderDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, LocalReclassificationHeaderDto> map = new LinkedHashMap<Long, LocalReclassificationHeaderDto>();
            LocalReclassificationHeaderDto header = null;
            while (rs.next()) {
                long headerId = rs.getLong("id");
                if (map.containsKey(headerId)) {
                    header = map.get(headerId);
                } else {
                    header = new LocalReclassificationHeaderDto();
                    header.id = headerId;
                    header.remoteId = rs.getLong("remoteId");
                    header.description = rs.getString("message");
                    header.created = rs.getDate("created");
                    header.totalAmount = rs.getLong("totalAmount");
                    header.processed = false;
                    int processed = rs.getInt("processed");
                    if(processed == 1){
                        header.processed = true;
                    }
                    map.put(headerId, header);
                }
            }
            return new ArrayList(map.values());
        }
    }


    private static String to13Chars(Long inputBarcode) {
        String barcode = String.valueOf(inputBarcode);
        while (barcode.length() < 13) {
            barcode = "0" + barcode;
        }
        return barcode;

    }
}
