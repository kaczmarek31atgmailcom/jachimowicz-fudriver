package com.fungisearch.fudriver.payroll.wage.query.dao;

import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.wage.query.dto.PersonWageDto;
import com.fungisearch.fudriver.payroll.wage.query.dto.WageDto;
import com.fungisearch.fudriver.payroll.wage.query.dto.WageHeaderDto;
import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class WageJdbcDao implements WageDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public WageJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<WageHeaderDto> getWageHeaders() {
        return jdbcTemplate.query("SELECT " +
                "id AS id, " +
                "name AS name " +
                "FROM wage_header " +
                "WHERE active = 1", new WageHeadersResultSetExtractor());
    }

    private static class WageHeadersResultSetExtractor implements ResultSetExtractor<List<WageHeaderDto>> {

        @Override
        public List<WageHeaderDto> extractData(ResultSet rs) throws SQLException {
            List<WageHeaderDto> result = new ArrayList<>();
            while (rs.next()) {
                WageHeaderDto dto = new WageHeaderDto();
                dto.id = rs.getLong("id");
                dto.name = rs.getString("name");
                result.add(dto);
            }
            return result;
        }
    }

    @Override
    public List<WageDto> getWages(long headerId) {
        return jdbcTemplate.query("SELECT " +
                "  w.id              AS wageId, " +
                "  r.id              AS typeId, " +
                "  r.name            AS typeName, " +
                "  r.waga            AS typeWeight, " +
                "  w.salary_day_type AS dayType, " +
                "  w.value           AS value " +
                "FROM wage w " +
                "  LEFT JOIN rodzaj r ON r.id = w.type_id " +
                "WHERE w.header_id = ? " +
                " AND r.archiwalne = 0 ", new Object[]{headerId}, new WagesResultSetExtractor());
    }

    private static class WagesResultSetExtractor implements ResultSetExtractor<List<WageDto>> {

        @Override
        public List<WageDto> extractData(ResultSet rs) throws SQLException {
            List<WageDto> list = new ArrayList<>();
            while (rs.next()) {
                WageDto dto = new WageDto();
                dto.id = rs.getLong("wageId");
                dto.typeId = rs.getLong("typeId");
                dto.typeName = rs.getString("typeName");
                dto.typeWeight = rs.getDouble("typeWeight");
                int dayType = rs.getInt("dayType");
                switch (dayType) {
                    case 1:
                        dto.dayType = SalaryDayTypeEnum.SUNDAY;
                        break;
                    case 2:
                        dto.dayType = SalaryDayTypeEnum.BONUS_DAY;
                        break;
                    default:
                        dto.dayType = SalaryDayTypeEnum.REGULAR_DAY;
                        break;
                }
                dto.value = rs.getLong("value");
                list.add(dto);
            }
            return list;
        }

    }

    @Override
    public List<PersonWageDto> getActivePeopleWages() {
        return jdbcTemplate.query("SELECT " +
                "  l.id           AS id, " +
                "  l.nr           AS nr, " +
                "  l.imie         AS name, " +
                "  l.nazwisko     AS surname, " +
                "  l.czy_akord    AS payrollType, " +
                "  wh.id          AS wageHeaderId, " +
                "  wh.name        AS wageHeaderName, " +
                "  l.regular_wage AS hourlyRegularWage, " +
                "  l.sunday_wage  AS hourlySundayWage, " +
                "  l.bonus_wage   AS hourlyBonusWage, " +
                "  l.version AS version " +
                "FROM ludzie l " +
                "  LEFT JOIN wage_header wh ON wh.id = l.wage_header_id " +
                "WHERE l.active = 1 and l.id > 1000", new PeopleWageResultSetExtractor());
    }

    private static class PeopleWageResultSetExtractor implements ResultSetExtractor<List<PersonWageDto>> {

        @Override
        public List<PersonWageDto> extractData(ResultSet rs) throws SQLException {
            List<PersonWageDto> list = new ArrayList<>();
            while (rs.next()) {
                PersonWageDto dto = new PersonWageDto();
                dto.id = rs.getLong("id");
                dto.nr = rs.getLong("nr");
                dto.name = rs.getString("name");
                dto.surname = rs.getString("surname");
                int payrolType = rs.getInt("payrollType");
                switch (payrolType) {
                    case 0:
                        dto.payrollType = PayrollTypeEnum.HOURLY;
                        break;
                    default:
                        dto.payrollType = PayrollTypeEnum.ACCORD;
                        break;
                }
                dto.wageHeaderId = rs.getLong("wageHeaderId");
                dto.wageHeaderName = rs.getString("wageHeaderName");
                dto.hourlyRegularWage = rs.getLong("hourlyRegularWage");
                dto.hourlySundayWage = rs.getLong("hourlySundayWage");
                dto.hourlyBonusWage = rs.getLong("hourlyBonusWage");
                dto.version = rs.getLong("version");
                list.add(dto);
            }
            return list;
        }
    }
}
