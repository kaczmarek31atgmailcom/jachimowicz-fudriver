package com.fungisearch.fudriver.wozek.query.dao;


import com.fungisearch.fudriver.wozek.query.dto.CheckCodeDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UniqDao {
    private final JdbcTemplate jdbcTemplate;

    public UniqDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CheckCodeDto checkBarcode(long uniqId, long pickerId) {
        return jdbcTemplate.query("select z.uniq_id     as uniqId, " +
                "       z.ludzie_id   as pickerId, " +
                "       l.imie        as pickerName, " +
                "       l.nazwisko    as pickerSurname, " +
                "       z.hala_id     as cycleId, " +
                "       h.name        as chamberName, " +
                "       z.time           harvestDate, " +
                "       z.wozek_nr    as harvestPaletteId, " +
                "       eu.palette_id as warehousePaletteId, " +
                "       z.rodzaj_id   as typeId, " +
                "       r.name        as typeName, " +
                "       r.waga * 1000 as typeWeight, " +
                "       z.user_id     as scannerManId, " +
                "       u.imie        as scannerManName, " +
                "       u.nazwisko    as scannerManSurname " +
                "from zarobki z " +
                "         left join ludzie l on l.id = z.ludzie_id " +
                "         left join cykle c on c.id = z.hala_id " +
                "         left join hala h on h.id = c.hala_id " +
                "         left join east_mushrooms_warehouse_unit eu on eu.uniq_id = z.uniq_id and eu.picker_id = z.ludzie_id " +
                "         left join rodzaj r on r.id = z.rodzaj_id " +
                "         left join users u on u.id = z.user_id " +
                "where z.uniq_id = ? and z.ludzie_id = ?" ,new Object[]{uniqId,pickerId}, new CheckBarcodeResultSetExtractor());
    }


    private class CheckBarcodeResultSetExtractor implements ResultSetExtractor<CheckCodeDto>{
        @Override
        public CheckCodeDto extractData(ResultSet rs) throws SQLException {
            CheckCodeDto dto = new CheckCodeDto();
                while (rs.next()){
                    dto.uniqId = rs.getLong("uniqId");
                    dto.pickerId = rs.getInt("pickerId");
                    dto.pickerName = rs.getString("pickerName");
                    dto.pickerSurname = rs.getString("pickerSurname");
                    dto.cycleId = rs.getInt("cycleId");
                    dto.chamberName = rs.getString("chamberName");
                    dto.harvestDate = rs.getDate("harvestDate");
                    dto.harvestPaletteId = rs.getInt("harvestPaletteId");
                    dto.warehousePaletteId = rs.getInt("warehousePaletteId");
                    dto.typeId = rs.getInt("typeId");
                    dto.typeName = rs.getString("typeName");
                    dto.typeWeight = rs.getInt("typeWeight");
                    dto.scannerManId = rs.getInt("scannerManId");
                    dto.scannerManName = rs.getString("scannerManName");
                    dto.scannerManSurname = rs.getString("scannerManSurname");
                }
            return dto;
        }
    }
}
