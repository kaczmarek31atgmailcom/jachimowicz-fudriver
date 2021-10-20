package com.fungisearch.fudriver.payroll.salary.query.dao;

import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.salary.query.dto.PersonSalaryAccountHistoryDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.PersonSalaryAccountStatusDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.*;
import com.fungisearch.fudriver.payroll.salary.query.dto.payed.*;
import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class SalaryDaoImpl implements SalaryDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SalaryDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<HarvestByPersonAndWageDto> getHarvestByPersonAndWage(String timeshort) {
        return jdbcTemplate.query("SELECT " +
                "  z.ludzie_id            AS personId, " +
                "  w.value                AS wage, " +
                "  c.editable             AS dayType, " +
                "  sum(z.ilosc) * 1000    AS totalKg, " +
                "  sum(z.ilosc) * w.value AS salary " +
                "FROM zarobki z, wage w, ludzie l, calendar c " +
                "WHERE z.timeshort = ? " +
                "      AND z.rodzaj_id = w.type_id " +
                "      AND l.wage_header_id = w.header_id " +
                "      AND z.ludzie_id = l.id " +
                "      AND l.czy_akord = 1 " +
                "      AND c.date = z.time " +
                "      AND c.editable = w.salary_day_type " +
                "GROUP BY " +
                "  z.ludzie_id, " +
                "  w.value, " +
                "  c.editable;", new Object[]{timeshort}, new HarvestByPersonAndWageResultSetExtractor());
    }

    private static class HarvestByPersonAndWageResultSetExtractor implements ResultSetExtractor<List<HarvestByPersonAndWageDto>> {

        @Override
        public List<HarvestByPersonAndWageDto> extractData(ResultSet rs) throws SQLException {
            List<HarvestByPersonAndWageDto> list = new ArrayList<>();
            while (rs.next()) {
                HarvestByPersonAndWageDto dto = new HarvestByPersonAndWageDto();
                dto.personId = rs.getLong("personId");
                dto.wage = rs.getLong("wage");
                int dt = rs.getInt("dayType");
                switch (dt) {
                    case 0:
                        dto.dayType = SalaryDayTypeEnum.REGULAR_DAY;
                        break;
                    case 1:
                        dto.dayType = SalaryDayTypeEnum.SUNDAY;
                        break;
                    case 2:
                        dto.dayType = SalaryDayTypeEnum.BONUS_DAY;
                        break;
                    default:
                        throw new IllegalStateException("Non existing day type");
                }
                dto.totalKg = rs.getLong("totalKg");
                dto.salary = rs.getLong("salary");
                list.add(dto);
            }
            return list;
        }
    }

    @Override
    public List<WorkTimeByPersonAndDayTypeDto> getWorkTimeByPersonAndDayType(String timeshort) {
        return jdbcTemplate.query("SELECT " +
                "  t.person_id           AS personId, " +
                "  t.start_time          AS startTime, " +
                "  t.end_time            AS endTime, " +
                "  ifnull(c.editable, 0) AS dayType " +
                "FROM time_work_log t " +
                "  LEFT JOIN calendar c ON DATE_FORMAT(c.date, '%Y-%m-%d') = DATE_FORMAT(t.start_time, '%Y-%m-%d') " +
                "  LEFT JOIN ludzie l ON l.id = t.person_id " +
                "WHERE t.isOpened = 0 " +

                "      AND DATE_FORMAT(t.start_time, '%Y%m') = ?", new Object[]{timeshort}, new WorkTimeByPersonAndDayTypeResultSetExtractor());
    }

    private static class WorkTimeByPersonAndDayTypeResultSetExtractor implements ResultSetExtractor<List<WorkTimeByPersonAndDayTypeDto>> {

        @Override
        public List<WorkTimeByPersonAndDayTypeDto> extractData(ResultSet rs) throws SQLException {
            List<WorkTimeByPersonAndDayTypeDto> list = new ArrayList<>();
            while (rs.next()) {
                WorkTimeByPersonAndDayTypeDto dto = new WorkTimeByPersonAndDayTypeDto();
                dto.personId = rs.getLong("personId");
                dto.startTime = rs.getTimestamp("startTime");
                dto.endTime = rs.getTimestamp("endTime");
                int dayType = rs.getInt("dayType");
                switch (dayType) {
                    case 0:
                        dto.dayType = SalaryDayTypeEnum.REGULAR_DAY;
                        break;
                    case 1:
                        dto.dayType = SalaryDayTypeEnum.SUNDAY;
                        break;
                    case 2:
                        dto.dayType = SalaryDayTypeEnum.BONUS_DAY;
                        break;
                    default:
                        throw new IllegalStateException("Non supported salary day type: ");
                }
                list.add(dto);
            }
            return list;
        }
    }

    @Override
    public List<PersonSalaryHeaderDto> getPersonNames(String timeshort) {
        return jdbcTemplate.query("SELECT " +
                "  l.id           AS id, " +
                "  l.nr           AS nr, " +
                "  l.imie         AS name, " +
                "  l.nazwisko     AS surname, " +
                "  l.grupa        AS groupId, " +
                "  l.wage_header_id AS wageHeaderId, " +
                "  g.name         AS groupName, " +
                "  sum(z.ilosc) * 1000   AS totalHarvest, " +
                "  (sum(z.export) / sum(z.ilosc)) * 100 AS quality, " +
                "  l.czy_akord    AS payrollType, " +
                "  l.regular_wage AS regularHoursWage, " +
                "  l.sunday_wage  AS sundayHoursWage, " +
                "  l.bonus_wage   AS bonusHoursWage " +
                "FROM ludzie l " +
                "  LEFT JOIN grupy g ON g.id = l.grupa " +
                "  LEFT JOIN zarobki z ON z.ludzie_id = l.id AND z.timeshort = ? " +
                "GROUP BY l.id", new Object[]{timeshort}, new PersonSalaryHeaderResultSetExtractor());
    }

    private static class PersonSalaryHeaderResultSetExtractor implements ResultSetExtractor<List<PersonSalaryHeaderDto>> {

        @Override
        public List<PersonSalaryHeaderDto> extractData(ResultSet rs) throws SQLException {
            List<PersonSalaryHeaderDto> list = new ArrayList<>();
            while (rs.next()) {
                PersonSalaryHeaderDto dto = new PersonSalaryHeaderDto();
                dto.id = rs.getLong("id");
                dto.nr = rs.getLong("nr");
                dto.name = rs.getString("name");
                dto.surname = rs.getString("surname");
                dto.groupId = rs.getLong("groupId");
                dto.groupName = rs.getString("groupName");
                dto.wageHeaderId = rs.getLong("wageHeaderId");
                dto.totalHarvest = rs.getLong("totalHarvest");
                dto.quality = rs.getInt("quality");
                dto.regularHoursWage = rs.getLong("regularHoursWage");
                dto.sundayHoursWage = rs.getLong("sundayHoursWage");
                dto.bonusHoursWage = rs.getLong("bonusHoursWage");
                int payrollTypeId = rs.getInt("payrollType");
                switch (payrollTypeId) {
                    case 0:
                        dto.payrollType = PayrollTypeEnum.HOURLY;
                        break;
                    case 1:
                        dto.payrollType = PayrollTypeEnum.ACCORD;
                        break;
                    default:
                        throw new IllegalStateException("Not valid payroll type for person id: " + dto.id);
                }
                list.add(dto);
            }
            return list;
        }
    }

    @Override
    public List<PayrollMonthDto> getPayrollMonths() {
        return jdbcTemplate.query("SELECT " +
                "  p.id                              AS id, " +
                "  DATE_FORMAT(p.first_day, '%Y-%m-%d') AS name, " +
                "  p.is_closed                       AS isClosed " +
                "FROM payroll_month p " +
                "WHERE p.first_day <= DATE_FORMAT(curdate(), '%Y-%m-%d') " +
                "ORDER BY p.first_day DESC", new PayrollMonthResultSetExtractor());
    }

    private static class PayrollMonthResultSetExtractor implements ResultSetExtractor<List<PayrollMonthDto>> {

        @Override
        public List<PayrollMonthDto> extractData(ResultSet rs) throws SQLException {
            List<PayrollMonthDto> list = new ArrayList<>();
            while (rs.next()) {
                PayrollMonthDto dto = new PayrollMonthDto();
                dto.monthId = rs.getInt("id");
                dto.month = rs.getDate("name");
                dto.isClosed = rs.getBoolean("isClosed");
                list.add(dto);
            }
            return list;
        }

    }

    @Override
    public List<PayrollBonusDto> getActiveBonuses() {
        return jdbcTemplate.query("SELECT " +
                "  id    AS id, " +
                "  dtype AS type, " +
                "  name  AS name, " +
                "  param AS param " +
                "FROM payroll_bonus " +
                "WHERE active = 1", new ActiveBonusesResultSetExtractor());
    }

    private static class ActiveBonusesResultSetExtractor implements ResultSetExtractor<List<PayrollBonusDto>> {

        @Override
        public List<PayrollBonusDto> extractData(ResultSet rs) throws SQLException {
            List<PayrollBonusDto> list = new ArrayList<>();
            while (rs.next()) {
                PayrollBonusDto dto = new PayrollBonusDto();
                dto.id = rs.getLong("id");
                dto.type = rs.getString("type");
                dto.name = rs.getString("name");
                dto.param = rs.getLong("param");
                list.add(dto);
            }
            return list;
        }
    }


    @Override
    public List<BonusPersonAssignementDto> getAssignedBonuses(long payrollMonthId) {
        return jdbcTemplate.query("SELECT person_id AS personId, bonus_id AS bonusId FROM bonus_person_month WHERE payroll_month_id = ?",
                new Object[]{payrollMonthId}, new BonusPersonAssignmentResultSetExtractor());
    }

    private static class BonusPersonAssignmentResultSetExtractor implements ResultSetExtractor<List<BonusPersonAssignementDto>> {

        @Override
        public List<BonusPersonAssignementDto> extractData(ResultSet rs) throws SQLException {
            List<BonusPersonAssignementDto> list = new ArrayList<>();
            while (rs.next()) {
                BonusPersonAssignementDto dto = new BonusPersonAssignementDto();
                dto.personId = rs.getLong("personId");
                dto.bonusId = rs.getLong("bonusId");
                list.add(dto);
            }
            return list;
        }

    }

    @Override
    public List<PayedPersonSalaryHeaderDto> getPayedHeaders(long monthId) {
        return jdbcTemplate.query("SELECT" +
                "  det.id                                          AS payoffDetailId, " +
                "  det.person_id                                   AS personId, " +
                "  l.nr                                            AS personNr, " +
                "  det.person_name                                 AS personName, " +
                "  det.person_surname                              AS personSurname, " +
                "  det.payroll_type                                AS payrollType, " +
                "  sum(hs.amount_money * (hs.salary_day_type = 0)) AS regularHarvestSalary, " +
                "  sum(hs.amount_money * (hs.salary_day_type = 1)) AS sundayHarvestSalary, " +
                "  sum(hs.amount_money * (hs.salary_day_type = 2)) AS bonusHarvestSalary, " +
                "  sum(ts.regular_minutes * ts.regular_rate) / 60  AS regularWorkTimeSalary, " +
                "  sum(ts.sunday_minutes * ts.sunday_rate) / 60    AS sundayWorkTimeSalary, " +
                "  sum(ts.bonus_minutes * ts.bonus_rate) / 60      AS bonusWorkTimeSalary " +
                "FROM monthly_payoff_detail det" +
                "  LEFT JOIN harvest_salary hs ON hs.payoff_detail_id = det.id AND det.person_id = hs.person_id " +
                "  LEFT JOIN work_time_salary ts ON ts.payoff_detail_id = det.id AND det.person_id = ts.person_id " +
                "  LEFT JOIN ludzie l ON l.id = det.person_id " +
                "WHERE det.month_id = ? " +
                "GROUP BY det.id", new Object[]{monthId}, new PayedHeadersResultSetExtractor());
    }

    private static class PayedHeadersResultSetExtractor implements ResultSetExtractor<List<PayedPersonSalaryHeaderDto>> {

        @Override
        public List<PayedPersonSalaryHeaderDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, PayedPersonSalaryHeaderDto> map = new HashMap<>();
            while (rs.next()) {
                PayedPersonSalaryHeaderDto dto;
                long personId = rs.getLong("personId");
                if (map.containsKey(personId)) {
                    dto = map.get(personId);
                } else {
                    dto = new PayedPersonSalaryHeaderDto();
                    dto.payoffDetailId = rs.getLong("payoffDetailId");
                    dto.personId = personId;
                    dto.personNr = rs.getLong("personNr");
                    dto.personName = rs.getString("personName");
                    dto.personSurname = rs.getString("personSurname");
                    int payrollTypeId = rs.getInt("payrollType");
                    switch (payrollTypeId) {
                        case 0:
                            dto.payrollType = PayrollTypeEnum.HOURLY;
                            break;
                        case 1:
                            dto.payrollType = PayrollTypeEnum.ACCORD;
                            break;
                        default:
                            throw new IllegalStateException("Not valid payroll type for person id: " + dto.personId);
                    }
                    map.put(personId, dto);
                }
                dto.regularHarvestSalary += rs.getLong("regularHarvestSalary");
                dto.sundayHarvestSalary += rs.getLong("sundayHarvestSalary");
                dto.bonusHarvestSalary += rs.getLong("bonusHarvestSalary");
                dto.regularWorkTimeSalary += rs.getLong("regularWorkTimeSalary");
                dto.sundayWorkTimeSalary += rs.getLong("sundayWorkTimeSalary");
                dto.bonusWorkTimeSalary += rs.getLong("bonusWorkTimeSalary");
            }
            return new ArrayList<>(map.values());
        }

    }

    @Override
    public List<PayedPersonBonusHeaderDto> getPayedBonuses(long monthId) {
        return jdbcTemplate.query(" SELECT " +
                "  bs.person_id AS personId, " +
                "  pb.id AS bonusId, " +
                "  pb.name AS bonusName, " +
                "  bs.amount_money AS amountMoney " +
                "FROM bonus_salary bs, payroll_bonus pb " +
                "WHERE bs.bonus_id = pb.id " +
                "AND bs.month_id = ?", new Object[]{monthId}, new PayedPersonResultSetExtractor());
    }


    private static class PayedPersonResultSetExtractor implements ResultSetExtractor<List<PayedPersonBonusHeaderDto>> {

        @Override
        public List<PayedPersonBonusHeaderDto> extractData(ResultSet rs) throws SQLException {
            List<PayedPersonBonusHeaderDto> list = new ArrayList<>();
            while (rs.next()) {
                PayedPersonBonusHeaderDto dto = new PayedPersonBonusHeaderDto();
                dto.personId = rs.getLong("personId");
                dto.bonusId = rs.getLong("bonusId");
                dto.bonusName = rs.getString("bonusName");
                dto.moneyAmount = rs.getLong("amountMoney");
                list.add(dto);
            }
            return list;
        }
    }

    @Override
    public List<PersonSalaryAccountStatusDto> getSalaryAccountStatus() {
        return jdbcTemplate.query("SELECT " +
                "  pl.person_id AS personId, " +
                "  l.active     AS isPersonActive, " +
                "  l.nr         AS personNr, " +
                "  l.imie       AS personName, " +
                "  l.nazwisko   AS personSurname, " +
                "  sum(pl.amount) AS moneyAmount " +
                "FROM payroll_transaction pl, ludzie l " +
                "WHERE l.id = pl.person_id " +
                "GROUP BY l.id", new PersonSalaryAccountStatusResultSetExtractor());
    }

    private static class PersonSalaryAccountStatusResultSetExtractor implements ResultSetExtractor<List<PersonSalaryAccountStatusDto>> {

        @Override
        public List<PersonSalaryAccountStatusDto> extractData(ResultSet rs) throws SQLException {
            List<PersonSalaryAccountStatusDto> list = new ArrayList<>();
            while (rs.next()) {
                PersonSalaryAccountStatusDto dto = new PersonSalaryAccountStatusDto();
                dto.personId = rs.getLong("personId");
                int active = rs.getInt("isPersonActive");
                if (active == 1) {
                    dto.isPersonActive = true;
                } else {
                    dto.isPersonActive = false;
                }
                dto.personNr = rs.getLong("personNr");
                dto.personName = rs.getString("personName");
                dto.personSurname = rs.getString("personSurname");
                dto.moneyAmount = rs.getLong("moneyAmount");
                list.add(dto);
            }
            return list;
        }

    }

    @Override
    public List<PersonSalaryAccountHistoryDto> getPersonSalaryAccountHistory(long personId) {
        return jdbcTemplate.query("SELECT  " +
                "  person_id       AS id,  " +
                "  person_name     AS name,  " +
                "  person_surname  AS surname,  " +
                "  creator_login   AS creatorLogin,  " +
                "  creator_name    AS creatorName,  " +
                "  creator_surname AS creatorSurname,  " +
                "  date            AS date,  " +
                "  amount          AS amount  " +
                "FROM payroll_transaction  " +
                "WHERE person_id = ?  " +
                "ORDER BY date DESC", new Object[]{personId}, new PersonSalaryAccountHistoryResultSetExtractor());
    }

    private static class PersonSalaryAccountHistoryResultSetExtractor implements ResultSetExtractor<List<PersonSalaryAccountHistoryDto>> {

        @Override
        public List<PersonSalaryAccountHistoryDto> extractData(ResultSet rs) throws SQLException {
            List<PersonSalaryAccountHistoryDto> list = new ArrayList<>();
            while (rs.next()) {
                PersonSalaryAccountHistoryDto dto = new PersonSalaryAccountHistoryDto();
                dto.id = rs.getLong("id");
                dto.name = rs.getString("name");
                dto.surname = rs.getString("surname");
                dto.creatorLogin = rs.getString("creatorLogin");
                dto.creatorName = rs.getString("creatorName");
                dto.creatorSurname = rs.getString("creatorSurname");
                dto.date = rs.getDate("date");
                dto.amount = rs.getLong("amount");
                list.add(dto);
            }
            return list;
        }

    }

    @Override
    public Map<Long, Long> getExportRate(String timeshort) {
        return jdbcTemplate.query("SELECT ludzie_id AS personId, " +
                        "(sum(export) * 100) / sum(ilosc) AS quality " +
                        "FROM zarobki " +
                        " WHERE timeshort = ? " +
                        "GROUP BY ludzie_id",
                new Object[]{timeshort}, new ExportRateResultSetExtractor());
    }

    private static class ExportRateResultSetExtractor implements ResultSetExtractor<Map<Long, Long>> {

        @Override
        public Map<Long, Long> extractData(ResultSet rs) throws SQLException {
            Map<Long, Long> result = new HashMap<>();
            while (rs.next()) {
                result.put(rs.getLong("personId"), rs.getLong("quality"));
            }
            return result;
        }
    }

    @Override
    public List<PayedPersonSalaryHarvestDetailDto> getPayedHarvestDetails(long personId, long payoffDetailId) {
        return jdbcTemplate.query("SELECT" +
                "  h.type_id         AS typeId, " +
                "  t.name            AS typeName, " +
                "  t.waga * 1000     AS typeWeight, " +
                "  h.salary_day_type AS dayType, " +
                "  h.wage_id         AS wageId, " +
                "  h.wage_value      AS wageValue, " +
                "  h.amount_kg       AS kgAmount, " +
                "  h.amount_money    AS moneyAmount " +
                "FROM harvest_salary h " +
                "  LEFT JOIN rodzaj t ON t.id = h.type_id " +
                "WHERE h.person_id = ? " +
                "      AND h.payoff_detail_id = ?", new Object[]{personId, payoffDetailId}, new PayedPersonSalaryHarvestResultSetExtractor());
    }

    private static class PayedPersonSalaryHarvestResultSetExtractor implements ResultSetExtractor<List<PayedPersonSalaryHarvestDetailDto>> {

        @Override
        public List<PayedPersonSalaryHarvestDetailDto> extractData(ResultSet rs) throws SQLException {
            List<PayedPersonSalaryHarvestDetailDto> list = new ArrayList<>();
            while (rs.next()) {
                PayedPersonSalaryHarvestDetailDto dto = new PayedPersonSalaryHarvestDetailDto();
                dto.typeId = rs.getLong("typeId");
                dto.typeName = rs.getString("typeName");
                dto.typeWeight = rs.getLong("typeWeight");
                dto.dayType = SalaryDayTypeEnum.values()[rs.getInt("dayType")];
                dto.wageId = rs.getLong("wageId");
                dto.wageValue = rs.getLong("wageValue");
                dto.kgAmount = rs.getLong("kgAmount");
                dto.moneyAmount = rs.getLong("moneyAmount");
                list.add(dto);
            }
            return list;

        }
    }

    @Override
    public List<PayedPersonSalaryBonusDetailDto> getPayedBonusesDetails(long personId, long payoffDetailId) {
        return jdbcTemplate.query("SELECT " +
                "pb.name AS bonusName, " +
                "bs.amount_money AS moneyAmount " +
                "FROM bonus_salary bs " +
                "LEFT JOIN payroll_bonus pb ON pb.id = bs.bonus_id " +
                "WHERE bs.person_id = ? " +
                "AND bs.payoff_detail_id = ?", new Object[]{personId, payoffDetailId}, new PayedPersonSalaryBonusResultSetExtractor());
    }

    private static class PayedPersonSalaryBonusResultSetExtractor implements ResultSetExtractor<List<PayedPersonSalaryBonusDetailDto>> {

        @Override
        public List<PayedPersonSalaryBonusDetailDto> extractData(ResultSet rs) throws SQLException {
            List<PayedPersonSalaryBonusDetailDto> list = new ArrayList<>();
            while (rs.next()) {
                PayedPersonSalaryBonusDetailDto dto = new PayedPersonSalaryBonusDetailDto();
                dto.bonusName = rs.getString("bonusName");
                dto.moneyAmount = rs.getLong("moneyAmount");
                list.add(dto);
            }
            return list;
        }
    }

    @Override
    public String getPayrollMonthNameById(long id) {
        return jdbcTemplate.query("SELECT pm.first_day " +
                "FROM monthly_payoff_detail mpd, monthly_payoff_header mph, payroll_month pm " +
                "WHERE mpd.id = ? " +
                "AND mpd.header_id = mph.id " +
                "AND pm.id = mph.month_id", new Object[]{id}, new PayrollMonthNameResultSetExtractor());
    }


    private static class PayrollMonthNameResultSetExtractor implements ResultSetExtractor<String> {

        String result;

        @Override
        public String extractData(ResultSet rs) throws SQLException {
            while (rs.next()) {
                result = rs.getString("first_day");
            }
            return "{\"monthName\": \"" + result + "\"}";
        }

    }

    @Override
    public PayedPersonSalaryTimeDetailDto getPayedTimeDetails(long personId, long payoffDetailId) {
        return jdbcTemplate.query("SELECT " +
                "  wts.regular_minutes AS regularMinutes, " +
                "  wts.regular_rate AS regularRate, " +
                "  wts.sunday_minutes AS sundayMinutes, " +
                "  wts.sunday_rate AS sundayRate, " +
                "  wts.bonus_minutes AS bonusMinutes, " +
                "  wts.bonus_rate AS bonuseRate " +
                "  FROM work_time_salary wts " +
                "WHERE wts.person_id = ? " +
                "  AND wts.payoff_detail_id = ? ", new Object[]{personId, payoffDetailId}, new PayedPersonSalaryTimeResultSetExtractor());
    }

    private static class PayedPersonSalaryTimeResultSetExtractor implements ResultSetExtractor<PayedPersonSalaryTimeDetailDto> {
        @Override
        public PayedPersonSalaryTimeDetailDto extractData(ResultSet rs) throws SQLException {
            PayedPersonSalaryTimeDetailDto dto = new PayedPersonSalaryTimeDetailDto();
            while (rs.next()) {
                dto.regularMinutes += rs.getLong("regularMinutes");
                dto.regularRate = rs.getLong("regularRate");
                dto.sundayMinutes += rs.getLong("sundayMinutes");
                dto.sundayRate = rs.getLong("sundayRate");
                dto.bonusMinutes += rs.getLong("bonusMinutes");
                dto.bonusReate = rs.getLong("bonusRate");
            }
            return dto;
        }
    }

    @Override
    public List<PersonWorkTimeDto> getMonthlyWorkTime(Date startDate, Date endDate) {
        return jdbcTemplate.query("SELECT " +
                "  person_id  AS personId, " +
                "  start_time AS startTime, " +
                "  end_time   AS endTime, " +
                "  isOpened   AS isOpened " +
                "FROM time_work_log " +
                "WHERE start_time BETWEEN str_to_date(?, '%Y-%m-%d %H:%i:%s') AND str_to_date(?, '%Y-%m-%d %H:%i:%s') " +
                "      OR end_time BETWEEN str_to_date(?, '%Y-%m-%d %H:%i:%s') AND str_to_date(?, '%Y-%m-%d %H:%i:%s')", new Object[]{startDate, endDate, startDate, endDate}, new PersonWorkTimeResultSetExtractor());
    }

    private static class PersonWorkTimeResultSetExtractor implements ResultSetExtractor<List<PersonWorkTimeDto>> {
        @Override
        public List<PersonWorkTimeDto> extractData(ResultSet rs) throws SQLException {
            List<PersonWorkTimeDto> list = new ArrayList<>();
            while (rs.next()) {
                PersonWorkTimeDto dto = new PersonWorkTimeDto();
                dto.personId = rs.getLong("personId");
                dto.startTime = rs.getTimestamp("startTime");
                dto.endTime = rs.getTimestamp("endTime");
                dto.isClosed = !rs.getBoolean("isOpened");
                list.add(dto);
            }
            return list;
        }
    }

    @Override
    public Map<Long, Integer> getWorkingDaysAmount(String timeshort) {
        return jdbcTemplate.query("select distinct personId as personId, day as day from (select " +
                " person_id as personId, " +
                " DATE_FORMAT(start_time, '%d') as day " +
                " from time_work_log " +
                " where DATE_FORMAT(start_time, '%Y%m') = ? " +
                " group by person_id, DATE_FORMAT(start_time, '%d') " +
                " union all " +
                " select ludzie_id as personId, " +
                " DATE_FORMAT(time, '%d') as day " +
                " from zarobki " +
                " where DATE_FORMAT(time, '%Y%m') = ? " +
                "  and ilosc > 0 " +
                " group by ludzie_id, DATE_FORMAT(time, '%d'))a", new Object[]{timeshort,timeshort}, new WorkingDaysAmountResultSetExtractor());
    }


    private class WorkingDaysAmountResultSetExtractor implements ResultSetExtractor<Map<Long, Integer>> {
        @Override
        public Map<Long, Integer> extractData(ResultSet rs) throws SQLException {
            Map<Long, Integer> result = new HashMap<>();
            Map<Long,Set<Integer>> days = new HashMap<>();
            Long personId;
            Integer day;
            while (rs.next()) {
                personId = rs.getLong("personId");
                day = rs.getInt("day");
                if (! days.containsKey(personId)) {
                    days.put(personId, new HashSet<>(day));
                    result.put(personId,1);
                } else {
                    if(!days.get(personId).contains(day)){
                        days.get(personId).add(day);
                        int amountOfDays = result.get(personId);
                        amountOfDays++;
                        result.put(personId,amountOfDays);
                    }
                }
            }
            return result;
        }
    }
}


