package com.fungisearch.fudriver.hotel.query.dao;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.hotel.command.model.HotelBedStatus;
import com.fungisearch.fudriver.hotel.query.dto.HotelBedDto;
import com.fungisearch.fudriver.hotel.query.dto.old.BedOccupancyDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class HotelDaoOLD {
    private final JdbcTemplate jdbcTemplate;

    public HotelDaoOLD(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    /*    public List<HotelBedDto> getBeds(long hotelId) {
            return jdbcTemplate.query("select b.id as bedId, " +
                    "       b.name as bedName, " +
                    "       h.id as roomId, " +
                    "       h.name as roomName " +
                    "from hotel_bed b, " +
                    "     hotel_room h " +
                    "where h.id = b.room_id " +
                    "  and b.active = 1 " +
                    "  and h.active = 1 " +
                    "  and h.hotel_id = ? ", new Object[]{hotelId}, new HotelBedsResultSetExtractor());
        }

        private class HotelBedsResultSetExtractor implements ResultSetExtractor<List<HotelBedDto>> {

            @Override
            public List<HotelBedDto> extractData(ResultSet rs) throws SQLException {
                List<HotelBedDto> list = new ArrayList<>();
                while (rs.next()) {
                    HotelBedDto dto = new HotelBedDto();
                    dto.bedId = rs.getLong("bedId");
                    dto.bedName = rs.getString("bedName");
                    dto.roomId = rs.getLong("roomId");
                    dto.roomName = rs.getString("roomName");
                    list.add(dto);
                }
                return list;
            }
        }

        public List<BedOccupancyDto> getBedStatus(long bedId) {
            return jdbcTemplate.query("select h.id as occupancyId, " +
                            "       h.bed_id as bedId, " +
                            "       c.date as date, " +
                            "       h.status as hotelBedStatus, " +
                            "       h.description as description, " +
                            "       l.id as personId, " +
                            "       l.imie as personName, " +
                            "       l.nazwisko as personSurname " +
                            "from hotel_bed_occupancy h " +
                            "     left join calendar c on h.day_id = c.id " +
                            "     left join hotel_bed bed on h.bed_id = bed.id " +
                            "     left join ludzie l on l.id = h.person_id " +
                            " where bed.id = ? ", new Object[]{bedId},
                    new BedsStatusResultSetExtractor());
        }

    */
    public List<BedOccupancyDto> getBedsStatus(Date startDate, Date endDate, long hotelId) {
        return jdbcTemplate.query("select h.id as occupancyId, " +
                        "       h.bed_id as bedId, " +
                        "       c.date as date, " +
                        "       h.status as hotelBedStatus, " +
                        "       h.description as description, " +
                        "       l.id as personId, " +
                        "       l.imie as personName, " +
                        "       l.nazwisko as personSurname " +
                        "from hotel_bed_occupancy h " +
                        "     left join calendar c on h.day_id = c.id " +
                        "     left join hotel_bed bed on h.bed_id = bed.id " +
                        "     left join hotel_room room on bed.room_id = room.id " +
                        "     left join ludzie l on l.id = h.person_id " +
                        "where room.hotel_id = ? " +
                        "  and c.date between ? and ?", new Object[]{hotelId, DateUtils.getStartOfDay(startDate), DateUtils.getEndOfDay(endDate)},
                new BedsStatusResultSetExtractor());
    }

    private class BedsStatusResultSetExtractor implements ResultSetExtractor<List<BedOccupancyDto>> {

        @Override
        public List<BedOccupancyDto> extractData(ResultSet rs) throws SQLException {
            List<BedOccupancyDto> list = new ArrayList<>();
            while (rs.next()) {
                BedOccupancyDto dto = new BedOccupancyDto();
                dto.occupancyId = rs.getLong("occupancyId");
                dto.bedId = rs.getLong("bedId");
                dto.date = rs.getDate("date");
                dto.hotelBedStatus = HotelBedStatus.values()[rs.getInt("hotelBedStatus")];
                dto.description = rs.getString("message");
                dto.personId = rs.getLong("personId");
                dto.personName = rs.getString("personName");
                dto.personSurname = rs.getString("personSurname");
                list.add(dto);
            }
            return list;
        }
    }
    public Date getMaxDateBeforeDate(long bedId, Date day){
    return jdbcTemplate.queryForObject("select max(c.date) as date " +
                "from calendar c, hotel_bed_occupancy o " +
                "where o.day_id = c.id " +
                "and o.bed_id = ? " +
                "and c.date < ?", new Object[]{bedId,DateUtils.getStartOfDay(day)}, Date.class);
    }

    public Date getMinDateAfterDate(long bedId, Date day){
        return jdbcTemplate.queryForObject("select min(c.date) as date " +
                "from calendar c, hotel_bed_occupancy o " +
                "where o.day_id = c.id " +
                "and o.bed_id = ? " +
                "and c.date > ?", new Object[]{bedId,DateUtils.getEndOfDay(day)}, Date.class);
    }

    public BedOccupancyDto getBedStatus(long bedId, Date day) {
        return jdbcTemplate.query(" select h.id as occupancyId, " +
                "h.bed_id as bedId,   +  " +
                "c.date as date, " +
                "h.status as hotelBedStatus, " +
                "h.description as description, " +
                "l.id as personId, " +
                "l.imie as personName, " +
                "l.nazwisko as personSurname " +
                "from hotel_bed_occupancy h " +
                " left join calendar c on h.day_id = c.id " +
                " left join hotel_bed bed on h.bed_id = bed.id " +
                " left join ludzie l on l.id = h.person_id " +
                " where bed.id = ? " +
                " and c.date = ? ", new Object[]{bedId,DateUtils.getStartOfDay(day)}, new ResultSetExtractor<BedOccupancyDto>(){

            @Override
            public BedOccupancyDto extractData(ResultSet rs) throws SQLException {
                BedOccupancyDto dto = new BedOccupancyDto();
                if(rs.next()) {
                    dto.bedId = rs.getLong("bedId");
                    dto.date = rs.getDate("date");
                    dto.hotelBedStatus = HotelBedStatus.values()[rs.getInt("hotelBedStatus")];
                    dto.description = rs.getString("message");
                    dto.personId = rs.getLong("personId");
                    dto.personName = rs.getString("personName");
                    dto.personSurname = rs.getString("personSurname");
                }
                return dto;
            }
        });
    }
}
