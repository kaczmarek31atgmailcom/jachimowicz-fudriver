package com.fungisearch.fudriver.hotel.query.dto;

import com.fungisearch.fudriver.hotel.command.model.ReservationType;

import java.util.Date;

public class ReservationDto {
    public Long id;
    public Date startDate;
    public Date endDate;
    public ReservationType reservationType;
    public Long personId;
    public String personName;
    public String personSurname;
    public long bedId;
    public String description;
}
