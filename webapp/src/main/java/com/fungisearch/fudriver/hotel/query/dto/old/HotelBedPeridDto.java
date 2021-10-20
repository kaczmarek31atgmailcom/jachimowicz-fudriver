package com.fungisearch.fudriver.hotel.query.dto.old;

import com.fungisearch.fudriver.hotel.command.model.HotelBedStatus;

import java.util.Date;

public class HotelBedPeridDto {
    public Date startDate;
    public Date endDate;
    public HotelBedStatus hotelBedStatus;

    @Override
    public String toString() {
        return "HotelBedPeridDto{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", hotelBedStatus=" + hotelBedStatus +
                '}';
    }
}
