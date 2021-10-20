package com.fungisearch.fudriver.hotel.query.dto;

import java.util.ArrayList;
import java.util.List;

public class HotelRoomDto {
    public long id;
    public String name;
    public List<HotelBedDto> beds = new ArrayList();
}
