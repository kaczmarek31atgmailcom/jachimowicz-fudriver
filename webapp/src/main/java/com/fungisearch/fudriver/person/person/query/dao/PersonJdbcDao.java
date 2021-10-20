package com.fungisearch.fudriver.person.person.query.dao;

import com.fungisearch.fudriver.person.person.command.model.ForeignerAlert;
import com.fungisearch.fudriver.person.person.query.dto.*;
import com.fungisearch.fudriver.wozek.query.dto.ScannerPersonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Repository
public class PersonJdbcDao implements PersonDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<PersonHeaderDto> getActiveHeaders() {
        return jdbcTemplate.query("SELECT " +
                "l.id         AS id, " +
                "l.nr         AS nr, " +
                "l.imie       AS name, " +
                "l.nazwisko   AS surname, " +
                "l.active     AS active, " +
                "l.koniec_zameldowania AS dataWymeldowania, " +
                "l.koniec_waznosci_paszportu as koniecWaznosciPaszportu, " +
                "l.koniec_waznosci_wizy as koniecWaznosciWizy, " +
                "l.koniec_waznosci_zezwolenia as koniecWaznosciZezwolenia, " +
                "l.czy_obcokrajowiec AS isForeigner, " +
                "g.name       AS groupName, " +
                "twl.isOpened AS isPresent " +
                "FROM ludzie l " +
                "LEFT JOIN time_work_log twl ON l.id = twl.person_id AND twl.isOpened = 1 " +
                "LEFT JOIN grupy g ON g.id = l.grupa " +
                "WHERE l.id <> 1000 AND l.active = 1;", new HeaderResultSetExtractor());
    }

    @Override
    public List<PersonHeaderDto> getAllHeaders() {
        return jdbcTemplate.query("SELECT " +
                "l.id         AS id, " +
                "l.nr         AS nr, " +
                "l.imie       AS name, " +
                "l.nazwisko   AS surname, " +
                "l.active     AS active, " +
                "l.koniec_zameldowania AS dataWymeldowania, " +
                "l.koniec_waznosci_paszportu as koniecWaznosciPaszportu, " +
                "l.koniec_waznosci_wizy as koniecWaznosciWizy, " +
                "l.koniec_waznosci_zezwolenia as koniecWaznosciZezwolenia, " +
                "l.czy_obcokrajowiec AS isForeigner, " +
                "g.name       AS groupName, " +
                "twl.isOpened AS isPresent " +
                "FROM ludzie l " +
                "LEFT JOIN time_work_log twl ON l.id = twl.person_id AND twl.isOpened = 1 " +
                "LEFT JOIN grupy g ON g.id = l.grupa " +
                "WHERE l.id <> 1000", new HeaderResultSetExtractor());
    }


    private class HeaderResultSetExtractor implements ResultSetExtractor<List<PersonHeaderDto>> {

        @Override
        public List<PersonHeaderDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, PersonHeaderDto> map = new LinkedHashMap<>();
            PersonHeaderDto person;
            while (rs.next()) {
                long personId = rs.getLong("id");
                if (map.containsKey(personId)) {
                    person = map.get(personId);
                } else {
                    person = new PersonHeaderDto();
                }
                person.id = personId;
                person.nr = rs.getLong("nr");
                person.name = rs.getString("name");
                person.surname = rs.getString("surname");
                person.groupName = rs.getString("groupName");
                person.active = rs.getInt("active") == 1;
                person.isForeigner = rs.getBoolean("isForeigner");
                person.dataWymeldowania = rs.getDate("dataWymeldowania");
                person.koniecWaznosciPaszportu = rs.getDate("koniecWaznosciPaszportu");
                person.koniecWaznosciWizy = rs.getDate("koniecWaznosciWizy");
                person.koniecWaznosciZezwolenia = rs.getDate("koniecWaznosciZezwolenia");
                person.isPresent = rs.getInt("isPresent") == 1;
                map.put(personId, person);
            }
            return new ArrayList<>(map.values());
        }
    }


    @Override
    public PersonDto getPerson(Long id) {
        return jdbcTemplate.query("select " +
                "l.id as id, " +
                "l.nr as nr, " +
                "l.imie as imie, " +
                "l.nazwisko as nazwisko, " +
                "l.adres as adres, " +
                "l.nr_telefonu as mobile, " +
                "g.id as groupId, " +
                "g.name as groupName, " +
                "l.data_urodzenia as birthDate, " +
                "l.miasto as city, " +
                "l.rejon as rejon, " +
                "l.indeks as indeks, " +
                "l.imiona_rodzicow as imionaRodzicow, " +
                "l.poczatek_zameldowania as dataZameldowania, " +
                "l.koniec_zameldowania as dataWymeldowania, " +
                "l.nr_wizy as nrWizy, " +
                "l.koniec_waznosci_wizy as koniecWaznosciWizy, " +
                "l.pesel as pesel, " +
                "l.czy_akord as payrollStatus, " +
                "l.numer_paszportu as passportNo, " +
                "l.koniec_waznosci_paszportu as koniecWaznosciPaszportu, " +
                "l.numer_oswiadczenia as nrOswiadczenia, " +
                "l.numer_zezwolenia as nrZezwolenia, " +
                "l.poczatek_waznosci_zezwolenia as poczatekWaznosciZezwolenia, " +
                "l.koniec_waznosci_zezwolenia as koniecWaznosciZezwolenia, " +
                "l.rfid as rfid, " +
                "l.active as active, " +
                "l.czy_obcokrajowiec as isForeigner, " +
                "l.version as version " +
                "from ludzie l, grupy g " +
                "where l.grupa = g.id and l.id = ? ", new Object[]{id}, new PersonResultSetExtractor());
    }

    @Override
    public List<PersonGroupDto> getActiveGroups() {
        return jdbcTemplate.query("select id, name from grupy where active = 1", new PersonGroupResultSetExtractor());
    }

    @Override
    public List<PersonGroupDto> getGroups() {
        return jdbcTemplate.query("select id, name from grupy", new PersonGroupResultSetExtractor());
    }

    @Override
    public List<Long> findFreeBarcodes(Long personId) {
        return jdbcTemplate.query("select uniq_id from uniq where ludzie_id = ? and used = 0", new Object[]{personId}, new FindBarcodesResultSetExtractor());
    }

    @Override
    public List<ScannerPersonDto> findPeopleForScanner() {
        return jdbcTemplate.query("select id, nr, imie, nazwisko from ludzie where active = 1", new ScannerPeopleForScannerResultSetExtractor());
    }

    @Override
    public List<PersonMassHarvestDto> findMassHarverstPeople() {
        return jdbcTemplate.query("select l.id as id, " +
                "l.nr as nr, " +
                "l.imie as name, " +
                "l.nazwisko as surname, " +
                "g.id as groupId, " +
                "g.name as groupName from ludzie l, grupy g where " +
                " l.grupa = g.id and l.id <> 1000 and l.active = 1 order by l.nr", new MassHarvestPeopleResultSetExtractor());
    }

    @Override
    public PersonHeaderDto findPersonByRfid(String rfid) {
        PersonHeaderDto person = new PersonHeaderDto();
        List<PersonHeaderDto> list = jdbcTemplate.query("select l.id as id, " +
                "l.nr as nr, " +
                "l.imie as name, " +
                "l.nazwisko as surname, " +
                "l.active as active, " +
                "l.czy_obcokrajowiec AS isForeigner, " +
                "g.name as groupName " +
                "from ludzie l, grupy g where g.id = l.rfid like ?", new Object[]{rfid}, new FindPersonByRfidResultSetExtractor());
        if (!list.isEmpty()) {
            person = list.get(0);
        }
        return person;
    }

    private static class FindPersonByRfidResultSetExtractor implements ResultSetExtractor<List<PersonHeaderDto>> {
        @Override
        public List<PersonHeaderDto> extractData(ResultSet rs) throws SQLException {
            List<PersonHeaderDto> list = new ArrayList<>();
            PersonHeaderDto person;
            while(rs.next()){
                person = new PersonHeaderDto();
                person.id = rs.getLong("id");
                person.nr = rs.getLong("nr");
                person.name = rs.getString("name");
                person.surname = rs.getString("surname");
                person.active = rs.getBoolean("active");
                person.isForeigner = rs.getBoolean("isForeigner");
                person.groupName = rs.getString("groupName");
                list.add(person);
            }
            return list;
        }
    }


    @Override
    public WorkTimePersonHeaderDto findWorkTimePerson(Long id) {
        WorkTimePersonHeaderDto person = new WorkTimePersonHeaderDto();
        List<WorkTimePersonHeaderDto> people = jdbcTemplate.query("select id, imie, nazwisko, rfid from ludzie where id = ?", new Object[]{id}, new WorkTimePersonResultSetExtractor());
        if (!people.isEmpty()) {
            person = people.get(0);
        }
        return person;
    }


    class FindBarcodesResultSetExtractor implements ResultSetExtractor<List<Long>> {

        @Override
        public List<Long> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Set<Long> codes = new HashSet<>();
            while (rs.next()) {
                codes.add(rs.getLong("uniq_id"));
            }
            return new ArrayList<>(codes);
        }
    }


    class PersonGroupResultSetExtractor implements ResultSetExtractor<List<PersonGroupDto>> {

        @Override
        public List<PersonGroupDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, PersonGroupDto> map = new LinkedHashMap<>();
            PersonGroupDto group;
            while (rs.next()) {
                long groupId = rs.getLong("id");
                if (map.containsKey(groupId)) {
                    group = map.get(groupId);
                } else {
                    group = new PersonGroupDto();
                    group.id = groupId;
                }
                group.name = rs.getString("name");
                map.put(groupId, group);
            }
            return new ArrayList<>(map.values());
        }
    }

    class PersonResultSetExtractor implements ResultSetExtractor<PersonDto> {

        @Override
        public PersonDto extractData(ResultSet rs) throws SQLException, DataAccessException {
            PersonDto person = new PersonDto();
            while (rs.next()) {
                person.id = rs.getLong("id");
                person.nr = rs.getLong("nr");
                person.imie = rs.getString("imie");
                person.nazwisko = rs.getString("nazwisko");
                person.adres = rs.getString("adres");
                person.mobile = rs.getString("mobile");
                person.groupId = rs.getLong("groupId");
                person.groupName = rs.getString("groupName");
                person.birthDate = rs.getDate("birthDate");
                person.city = rs.getString("city");
                person.rejon = rs.getString("rejon");
                person.indeks = rs.getString("indeks");
                person.imionaRodzicow = rs.getString("imionaRodzicow");
                person.dataZameldowania = rs.getDate("dataZameldowania");
                person.dataWymeldowania = rs.getDate("dataWymeldowania");
                person.active = rs.getBoolean("active");
                person.dataWymeldowania = rs.getDate("dataWymeldowania");
                person.nrWizy = rs.getString("nrWizy");
                person.koniecWaznosciWizy = rs.getDate("koniecWaznosciWizy");
                person.pesel = rs.getString("pesel");
                person.payrollStatus = rs.getInt("payrollStatus");
                person.nrPaszportu = rs.getString("passportNo");
                person.koniecWaznosciPaszportu = rs.getDate("koniecWaznosciPaszportu");
                person.nrOswiadczenia = rs.getString("nrOswiadczenia");
                person.nrZezwolenia = rs.getString("nrZezwolenia");
                person.poczatekWaznosciZezwolenia = rs.getDate("poczatekWaznosciZezwolenia");
                person.koniecWaznosciZezwolenia = rs.getDate("koniecWaznosciZezwolenia");
                person.rfid = rs.getString("rfid");
                person.isForeigner = rs.getBoolean("isForeigner");
                person.version = rs.getLong("version");
            }
            return person;
        }
    }


    class ScannerPeopleForScannerResultSetExtractor implements ResultSetExtractor<List<ScannerPersonDto>> {

        @Override
        public List<ScannerPersonDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, ScannerPersonDto> map = new LinkedHashMap<>();
            ScannerPersonDto person;
            while (rs.next()) {
                long personId = rs.getLong("id");
                if (map.containsKey(personId)) {
                    person = map.get(personId);
                } else {
                    person = new ScannerPersonDto();
                    person.id = personId;
                }
                person.nr = rs.getInt("nr");
                person.name = rs.getString("imie");
                person.surname = rs.getString("nazwisko");
                map.put(personId, person);
            }
            return new ArrayList<>(map.values());
        }
    }

    class MassHarvestPeopleResultSetExtractor implements ResultSetExtractor<List<PersonMassHarvestDto>> {

        @Override
        public List<PersonMassHarvestDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, PersonMassHarvestDto> map = new LinkedHashMap<>();
            PersonMassHarvestDto person;
            while (rs.next()) {
                long personId = rs.getLong("id");
                if (!(map.containsKey(personId))) {
                    person = new PersonMassHarvestDto();
                    person.id = personId;
                    person.nr = rs.getLong("nr");
                    person.name = rs.getString("name");
                    person.surname = rs.getString("surname");
                    person.groupId = rs.getLong("groupId");
                    person.groupName = rs.getString("groupName");
                    map.put(personId, person);
                }
            }
            return new ArrayList<>(map.values());
        }
    }

    class WorkTimePersonResultSetExtractor implements ResultSetExtractor<List<WorkTimePersonHeaderDto>> {

        @Override
        public List<WorkTimePersonHeaderDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<WorkTimePersonHeaderDto> people = new ArrayList<>();
            while (rs.next()) {
                WorkTimePersonHeaderDto person = new WorkTimePersonHeaderDto();
                person.id = rs.getLong("id");
                person.name = rs.getString("imie");
                person.surname = rs.getString("nazwisko");
                person.rfid = rs.getString("rfid");
                people.add(person);
            }
            return people;
        }
    }

    @Override
    public List<Long> getReservedNumbers() {
        return jdbcTemplate.query("select nr as nr from ludzie where nr > 0 order by nr", new ReservedNumbersResultSetExtractor());
    }

    private static class ReservedNumbersResultSetExtractor implements ResultSetExtractor<List<Long>> {

        @Override
        public List<Long> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Long> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rs.getLong("nr"));
            }
            return result;
        }

    }


    @Override
    public ForeignerAlertDto getForeignerAlert() {
        return jdbcTemplate.query("select " +
                "visa_days as visaDays, " +
                "statement_days as statementDays, " +
                "passport_days as passportDays, " +
                "stay_days as stayDays " +
                "from foreigner_alert where id = 1", new ForeignerAlertResultSetExtractor());
    }

    private static class ForeignerAlertResultSetExtractor implements ResultSetExtractor<ForeignerAlertDto>{
        @Override
        public ForeignerAlertDto extractData(ResultSet rs) throws SQLException {
            ForeignerAlertDto dto = new ForeignerAlertDto();
            while (rs.next()) {
                dto.visaDays = rs.getInt("visaDays");
                dto.statementDays = rs.getInt("statementDays");
                dto.passportDays = rs.getInt("passportDays");
                dto.stayDays = rs.getInt("stayDays");
            }
            return dto;
        }
    }
}
