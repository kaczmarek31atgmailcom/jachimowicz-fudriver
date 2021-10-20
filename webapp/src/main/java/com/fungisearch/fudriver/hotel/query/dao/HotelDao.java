package com.fungisearch.fudriver.hotel.query.dao;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.hotel.command.model.ReservationType;
import com.fungisearch.fudriver.hotel.query.dto.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class HotelDao {
    private final JdbcTemplate jdbcTemplate;

    public HotelDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<HotelSettingsHeaderDto> getHotelHeaders() {
        return jdbcTemplate.query("select " +
                "       h.id as id, " +
                "       h.name as name, " +
                "       (select count(*) " +
                "        from hotel_room r " +
                "        where r.hotel_id = h.id " +
                "          and r.active = 1)   as roomsAmount, " +
                "       (select count(*) " +
                "        from hotel_bed bed " +
                "        where bed.room_id in (select distinct r.id from hotel_room r where r.hotel_id = h.id) " +
                "          and bed.active = 1) as bedsAmount " +
                "from hotel h " +
                "where h.active = 1 ", new HotelSettingsHeaderResultSetExtractor());
    }


    private class HotelSettingsHeaderResultSetExtractor implements ResultSetExtractor<List<HotelSettingsHeaderDto>> {

        @Override
        public List<HotelSettingsHeaderDto> extractData(ResultSet rs) throws SQLException {
            List<HotelSettingsHeaderDto> list = new ArrayList<>();
            while (rs.next()) {
                HotelSettingsHeaderDto dto = new HotelSettingsHeaderDto();
                dto.id = rs.getLong("id");
                dto.name = rs.getString("name");
                dto.roomsAmount = rs.getInt("roomsAmount");
                dto.bedsAmount = rs.getInt("bedsAmount");
                list.add(dto);
            }
            return list;
        }
    }


    public List<ReservationDto> getReservations(Date startDate, Date endDate, long hotelId) {
        return jdbcTemplate.query("select r.id as id, " +
                " r.start_date as startDate, " +
                "      r.end_date as endDate, " +
                "       r.type as reservationType, " +
                "       r.person_id as personId , " +
                "       l.imie as personName, " +
                "       l.nazwisko as personSurname, " +
                "       occupancy.bed_id as bedId, " +
                "       r.description as description " +
                "from hotel_reservation r " +
                "left join ludzie l on l.id = r.person_id " +
                "left join hotel_bed_occupancy occupancy on occupancy.reservation_id = r.id " +
                "where r.hotel_id = ? " +
                "and r.start_date <= ? and r.end_date >= ? group by r.id ", new Object[]{hotelId, DateUtils.getEndOfDay(endDate), DateUtils.getStartOfDay(startDate)}, new ReservationDtoResultSetExtractor());
    }

    private class ReservationDtoResultSetExtractor implements ResultSetExtractor<List<ReservationDto>> {
        @Override
        public List<ReservationDto> extractData(ResultSet rs) throws SQLException {
            List<ReservationDto> list = new ArrayList<>();
            while (rs.next()) {
                ReservationDto dto = new ReservationDto();
                dto.id = rs.getLong("id");
                dto.startDate = rs.getDate("startDate");
                dto.endDate = rs.getDate("endDate");
                dto.reservationType = ReservationType.values()[rs.getInt("reservationType")];
                dto.personId = rs.getLong("personId");
                dto.personName = rs.getString("personName");
                dto.personSurname = rs.getString("personSurname");
                dto.bedId = rs.getLong("bedId");
                dto.description = rs.getString("description");
                list.add(dto);
            }
            return list;
        }
    }

    public List<HotelRoomDto> getRooms(long hotelId) {
        return jdbcTemplate.query("select  " +
                "       r.id as roomId,  " +
                "       r.name as roomName,  " +
                "       b.id as bedId,  " +
                "       b.name as bedName " +
                " from hotel_room r " +
                "       left join hotel_bed b on b.room_id = r.id " +
                "where r.hotel_id = ? ", new Object[]{hotelId}, new HotelRoomResultSetExtractor());
    }

    private class HotelRoomResultSetExtractor implements ResultSetExtractor<List<HotelRoomDto>> {
        @Override
        public List<HotelRoomDto> extractData(ResultSet rs) throws SQLException {
            Map<Long, HotelRoomDto> map = new HashMap<>();
            while (rs.next()) {
                HotelRoomDto hotelRoomDto;
                long roomId = rs.getLong("roomId");
                if (map.containsKey(roomId)) {
                    hotelRoomDto = map.get(roomId);
                } else {
                    hotelRoomDto = new HotelRoomDto();
                    hotelRoomDto.id = roomId;
                    map.put(roomId, hotelRoomDto);
                }
                hotelRoomDto.name = rs.getString("roomName");
                HotelBedDto bedDto = new HotelBedDto();
                bedDto.bedId = rs.getLong("bedId");
                bedDto.bedName = rs.getString("bedName");
                hotelRoomDto.beds.add(bedDto);
            }
            return new ArrayList<>(map.values());
        }
    }

    public NotReservedPeriodDto getNotReservedPeriod(long reservationId) {
        return jdbcTemplate.query("select " +
                "       max(bf.end_date) as startDate, " +
                "       min(af.start_date) as endDate " +
                "from hotel_reservation current " +
                "left join hotel_reservation bf on bf.bed_id = current.bed_id and bf.end_date <= current.start_date and bf.id <> current.id " +
                "left join hotel_reservation af on af.bed_id = current.bed_id and af.start_date >= current.end_date and af.id <> current.id " +
                "where current.id = ?", new Object[]{reservationId}, new NotReservedPeriodResultSetExtractor());
    }

    private class NotReservedPeriodResultSetExtractor implements ResultSetExtractor<NotReservedPeriodDto> {
        @Override
        public NotReservedPeriodDto extractData(ResultSet rs) throws SQLException {
            NotReservedPeriodDto notReservedPeriodDto = new NotReservedPeriodDto();
            while (rs.next()){
                notReservedPeriodDto.startDate = rs.getDate("startDate");
                notReservedPeriodDto.endDate = rs.getDate("endDate");
            }
        return notReservedPeriodDto;
        }
    }
}
