package com.fungisearch.fudriver.hotel.query.service;

import com.fungisearch.fudriver.hotel.query.dao.HotelDao;
import org.springframework.stereotype.Service;

@Service
public class HotelQueryService {
    private final HotelDao hotelDao;

    public HotelQueryService(HotelDao hotelDao) {
        this.hotelDao = hotelDao;
    }

}
