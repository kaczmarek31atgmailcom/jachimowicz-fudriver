package com.fungisearch.fudriver.audit.query.dao;

import com.fungisearch.fudriver.audit.query.dto.LocalReclassificationAuditDto;
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
 * Created by marcin on 03.08.16.
 */

@Repository
public class AuditJdbcDao implements AuditDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<LocalReclassificationAuditDto> findReclassifications(Date startDate, Date endDate) {
        return jdbcTemplate.query("SELECT " +
                "log.uniq_id         AS uniqId, " +
                "log.date            AS date, " +
                "log.supplier_id     AS supplierId, " +
                "log.picker_id       AS pickerId, " +
                "l.nr                AS pickerNr, " +
                "l.imie              AS pickerName, " +
                "l.nazwisko          AS pickerSurname, " +
                "u.user_name         AS login, " +
                "u.imie              AS userName, " +
                "u.nazwisko          AS userSurname, " +
                "source_type.name    AS sourceTypeName, " +
                "source_type.waga    AS sourceTypeWeight, " +
                "target_type.name    AS targetTypeName, " +
                "target_type.waga    AS targetTypeWeight, " +
                "source_chamber.name AS sourceChamberName, " +
                "target_chamber.name AS targetChamberName, " +
                "reason.description  AS reason " +
                "FROM local_reclassification_log log " +
                "LEFT JOIN ludzie l ON log.picker_id = l.id " +
                "LEFT JOIN users u ON u.id = log.user_id " +
                "LEFT JOIN rodzaj source_type ON log.source_type_id = source_type.id " +
                "LEFT JOIN rodzaj target_type ON log.target_type_id = target_type.id " +
                "LEFT JOIN cykle source_cycle ON log.source_cycle_id = source_cycle.id " +
                "LEFT JOIN hala source_chamber ON source_cycle.hala_id = source_chamber.id " +
                "LEFT JOIN cykle target_cycle ON log.target_cycle_id = target_cycle.id " +
                "LEFT JOIN hala target_chamber ON target_cycle.hala_id = target_chamber.id " +
                "LEFT JOIN reclassify_reason reason ON log.reason_id = reason.id " +
                "WHERE log.date >= ? " +
                "AND log.date <= ?", new Object[]{startDate, endDate}, new LocalReclassificationAuditResultSetExtractor());
    }

    private static class LocalReclassificationAuditResultSetExtractor implements ResultSetExtractor<List<LocalReclassificationAuditDto>> {

        @Override
        public List<LocalReclassificationAuditDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<LocalReclassificationAuditDto> list = new ArrayList<LocalReclassificationAuditDto>();
            LocalReclassificationAuditDto dto;
            while (rs.next()) {
                dto = new LocalReclassificationAuditDto();
                dto.uniqId = rs.getLong("uniqId");
                dto.date = rs.getTimestamp("date");
                dto.supplierId = rs.getLong("supplierId");
                dto.pickerId = rs.getLong("pickerId");
                dto.pickerNr = rs.getInt("pickerNr");
                dto.pickerName = rs.getString("pickerName");
                dto.pickerSurname = rs.getString("pickerSurname");
                dto.login = rs.getString("login");
                dto.userName = rs.getString("userName");
                dto.userSurname = rs.getString("userSurname");
                dto.sourceTypeName = rs.getString("sourceTypeName");
                dto.sourceTypeWeight = rs.getDouble("sourceTypeWeight");
                dto.targetTypeName = rs.getString("targetTypeName");
                dto.targetTypeWeight = rs.getDouble("targetTypeWeight");
                dto.sourceChamberName = rs.getString("sourceChamberName");
                dto.targetChamberName = rs.getString("targetChamberName");
                dto.reason = rs.getString("reason");
                list.add(dto);
            }
            return list;
        }
    }
}
