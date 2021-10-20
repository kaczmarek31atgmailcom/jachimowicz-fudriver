package com.fungisearch.fudriver.hotel.query.dto.old;

import com.fungisearch.fudriver.hotel.command.model.HotelBedStatus;

import java.util.Date;

public class BedStatusDto {
    public long bedId;
    public long reservationId;
    public String bedName;
    public long roomId;
    public String roomName;
    public Date date;
    public HotelBedStatus hotelBedStatus;
    public Long personId;
    public String personName;
    public String personSurname;
    public String description;
}
