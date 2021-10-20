package com.fungisearch.fudriver.reports.typesByPickers.dao;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.reports.typesByPickers.dto.PickerDto;
import com.fungisearch.fudriver.reports.typesByPickers.dto.TypesByPickerDto;
import com.fungisearch.fudriver.reports.typesByPickers.dto.TypesByPickersDto;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class TypesByPickersDao {
    private final JdbcTemplate jdbcTemplate;

    public TypesByPickersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TypesByPickersDto> getTypesByPicker(Date startDate, Date endDate) {
        return jdbcTemplate.query("select  " +
                "       z.ludzie_id         as pickerId, " +
                "       l.nr                as pickerNr, " +
                "       l.imie              as pickerName, " +
                "       l.nazwisko          as pickerSurname, " +
                "       z.rodzaj_id         as typeId, " +
                "       r.name              as typeName, " +
                "       r.export            as export, " +
                "       r.waga * 1000       as typeWeight, " +
                "       sum(z.ilosc) * 1000 as totalWeight " +
                " from zarobki z " +
                "       left join ludzie l on l.id = z.ludzie_id " +
                "       left join rodzaj r on r.id = z.rodzaj_id " +
                " where z.time between ? and ? " +
                " group by z.ludzie_id, z.rodzaj_id ", new Object[]{DateUtils.getStartOfDay(startDate), DateUtils.getEndOfDay(endDate)}, new TypesByPickersResultSetExtractor());
    }

    private class TypesByPickersResultSetExtractor implements ResultSetExtractor<List<TypesByPickersDto>> {
        @Override
        public List<TypesByPickersDto> extractData(ResultSet rs) throws SQLException {
            List<TypesByPickersDto> list = new ArrayList<>();
            while (rs.next()) {
                TypesByPickersDto dto = new TypesByPickersDto();
                dto.pickerId = rs.getLong("pickerId");
                dto.pickerNr = rs.getInt("pickerNr");
                dto.pickerName = rs.getString("pickerName");
                dto.pickerSurname = rs.getString("pickerSurname");
                dto.typeId = rs.getLong("typeId");
                dto.typeName = rs.getString("typeName");
                dto.export = rs.getInt("export");
                dto.typeWeight = rs.getInt("typeWeight");
                dto.totalWeight = rs.getLong("totalWeight");
                list.add(dto);
            }
            return list;
        }
    }

    public List<TypesByPickerDto> getPickersTypes(long pickerId, Date startDate, Date endDate) {
        return jdbcTemplate.query("select " +
                "       z.time as date, " +
                "       z.rodzaj_id as typeId, " +
                "       r.name as typeName, " +
                "       r.export as export, " +
                "       r.waga * 1000 as typeWeight, " +
                "       sum(z.ilosc) * 1000 as totalWeight " +
                "from zarobki z, rodzaj r " +
                "where r.id = z.rodzaj_id " +
                "and z.ludzie_id = ? " +
                "and z.time between ? and ? " +
                "group by z.time,z.rodzaj_id ", new Object[]{pickerId, DateUtils.getStartOfDay(startDate), DateUtils.getEndOfDay(endDate)}, new TypesByPickerResultSetExtractor());
    }

    private class TypesByPickerResultSetExtractor implements ResultSetExtractor<List<TypesByPickerDto>> {
        @Override
        public List<TypesByPickerDto> extractData(ResultSet rs) throws SQLException {
            List<TypesByPickerDto> list = new ArrayList<>();
            while (rs.next()) {
                TypesByPickerDto dto = new TypesByPickerDto();
                dto.date = rs.getDate("date");
                dto.typeId = rs.getLong("typeId");
                dto.typeName = rs.getString("typeName");
                dto.typeWeight = rs.getInt("typeWeight");
                dto.export = rs.getInt("export");
                dto.totalWeight = rs.getLong("typeWeight");
                dto.totalWeight = rs.getLong("totalWeight");
                list.add(dto);
            }
            return list;
        }
    }

    public PickerDto getPicker(int id){
        return jdbcTemplate.query("select " +
                "l.id as id ," +
                "l.nr as nr, " +
                "l.imie as name, " +
                "l.nazwisko as surname " +
                "from ludzie l " +
                "where l.id = ? ", new Object[]{id}, new PickerResultSetExtractor());
    }

    private class PickerResultSetExtractor implements ResultSetExtractor<PickerDto> {
        @Override
        public PickerDto extractData(ResultSet rs) throws SQLException {
            PickerDto dto = new PickerDto();
            while (rs.next()){
                dto.id = rs.getInt("id");
                dto.nr = rs.getInt("nr");
                dto.name = rs.getString("name");
                dto.surname = rs.getString("surname");
            }
        return  dto;
        }
    }
}
