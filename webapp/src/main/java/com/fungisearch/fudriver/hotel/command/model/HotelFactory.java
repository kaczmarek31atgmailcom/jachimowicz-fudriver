package com.fungisearch.fudriver.hotel.command.model;

import com.fungisearch.fudriver.hotel.command.repository.HotelRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.stereotype.Service;

@Service
public class HotelFactory {
    private final HotelRepository hotelRepository;
    private final BeanValidator beanValidator;


    public HotelFactory(HotelRepository hotelRepository, BeanValidator beanValidator) {
        this.hotelRepository = hotelRepository;
        this.beanValidator = beanValidator;
    }


    public Hotel find(Long hotelId) {
        Hotel hotel = hotelRepository.find(hotelId);
        if(hotel != null){
            hotel.hotelRepository = hotelRepository;
            hotel.beanValidator = beanValidator;
        }
        return hotel;
    }

    public Hotel.HotelBuilder getBuilder(){
        return new Hotel.HotelBuilder(hotelRepository,beanValidator);
    }


}
