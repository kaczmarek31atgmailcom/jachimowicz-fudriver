package com.fungisearch.fudriver.zarobki.query.dao;

import com.fungisearch.fudriver.type.command.model.ExportType;
import com.fungisearch.fudriver.zarobki.query.dto.ScannerManReportTotalsDto;
import com.fungisearch.fudriver.zarobki.query.dto.ScannerManReportTypeDetailDto;
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

@Repository
public class ScannerManReportDaoImpl implements ScannerManReportDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ScannerManReportTotalsDto> getTotals(Date startDate, Date endDate) {
        return jdbcTemplate.query("SELECT" +
                "  u.id                 AS personId, " +
                "  u.user_name          AS login, " +
                "  u.imie               AS name, " +
                "  u.nazwisko           AS surname, " +
                "  sum(z.inne * 1000)   AS inne, " +
                "  sum(z.kraj * 1000)   AS kraj, " +
                "  sum(z.export * 1000) AS export, " +
                "  count(*)             AS totalPcs " +
                "FROM zarobki z, users u " +
                "WHERE u.id = z.user_id " +
                "      AND time BETWEEN ? AND ? " +
                "GROUP BY u.id ", new Object[]{startDate, endDate}, new ScannerManReportTotalsResultSetExtractor());
    }

    private class ScannerManReportTotalsResultSetExtractor implements ResultSetExtractor<List<ScannerManReportTotalsDto>> {
        @Override
        public List<ScannerManReportTotalsDto> extractData(ResultSet rs) throws SQLException {
            List<ScannerManReportTotalsDto> list = new ArrayList<>();
            while (rs.next()) {
                ScannerManReportTotalsDto dto = new ScannerManReportTotalsDto();
                dto.personId = rs.getLong("personId");
                dto.login = rs.getString("login");
                dto.name = rs.getString("name");
                dto.surname = rs.getString("surname");
                dto.inne = rs.getLong("inne");
                dto.kraj = rs.getLong("kraj");
                dto.export = rs.getLong("export");
                dto.totalPcs = rs.getLong("totalPcs");
                list.add(dto);
            }
            return list;
        }
    }

    @Override
    public List<ScannerManReportTypeDetailDto> getDetails(Date startDate, Date endDate) {
        return jdbcTemplate.query("SELECT " +
                "  z.user_id AS userId, " +
                "  z.rodzaj_id AS typeId, " +
                "  t.name AS typeName, " +
                "  t.waga AS typeWeight, " +
                "  t.export AS exportType, " +
                "  sum(z.ilosc * 1000) AS totalWeight, " +
                "  count(*) AS numberPcs, " +
                "  t.export AS exportType " +
                "  FROM zarobki z, rodzaj t " +
                "WHERE z.rodzaj_id = t.id " +
                "AND time BETWEEN ? AND ? " +
                "GROUP BY z.rodzaj_id, z.user_id ", new Object[]{startDate, endDate}, new ScannerManReportDetailsResultSetExtractor());
    }


    private class ScannerManReportDetailsResultSetExtractor implements ResultSetExtractor<List<ScannerManReportTypeDetailDto>> {
        @Override
        public List<ScannerManReportTypeDetailDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<ScannerManReportTypeDetailDto> list = new ArrayList<>();
            while (rs.next()) {
                ScannerManReportTypeDetailDto dto = new ScannerManReportTypeDetailDto();
                dto.personId = rs.getInt("userId");
                dto.typeId = rs.getInt("typeId");
                dto.typeName = rs.getString("typeName");
                dto.typeWeight = (int) (rs.getDouble("typeWeight") * 1000);
                dto.numberPcs = rs.getLong("numberPcs");
                dto.totalWeight = rs.getLong("totalWeight");
                dto.exportType = ExportType.values()[rs.getInt("exportType")];
                list.add(dto);
            }
            return list;
        }
    }
}
