package com.fungisearch.fudriver.hotel.query.dto.old;

import com.fungisearch.fudriver.hotel.command.model.HotelBedStatus;

import java.util.Date;

public class BedOccupancyDto implements Comparable<BedOccupancyDto> {
    public long occupancyId;
    public Long reservationId;
    public long bedId;
    public Date date;
    public HotelBedStatus hotelBedStatus;
    public Long personId;
    public String personName;
    public String personSurname;
    public String description;


    @Override
    public int compareTo(BedOccupancyDto o) {
        return date.compareTo(o.date);
    }

    @Override
    public String toString() {
        return "BedOccupancyDto{" +
                "occupancyId=" + occupancyId +
                ", reservationId=" + reservationId +
                ", bedId=" + bedId +
                ", date=" + date +
                ", hotelBedStatus=" + hotelBedStatus +
                ", personId=" + personId +
                ", personName='" + personName + '\'' +
                ", personSurname='" + personSurname + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
