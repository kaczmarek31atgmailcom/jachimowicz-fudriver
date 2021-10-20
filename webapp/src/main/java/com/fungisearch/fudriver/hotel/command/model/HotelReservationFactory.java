package com.fungisearch.fudriver.hotel.command.model;

import com.fungisearch.fudriver.hotel.command.repository.HotelRepository;
import com.fungisearch.fudriver.payroll.calendar.command.model.CalendarFactory;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.stereotype.Service;

@Service
public class HotelReservationFactory {
    private final HotelRepository hotelRepository;
    private final BeanValidator beanValidator;
    private final CalendarFactory calendarFactory;
    private final PersonFactory personFactory;

    public HotelReservationFactory(HotelRepository hotelRepository, BeanValidator beanValidator, CalendarFactory calendarFactory, PersonFactory personFactory) {
        this.hotelRepository = hotelRepository;
        this.beanValidator = beanValidator;
        this.calendarFactory = calendarFactory;
        this.personFactory = personFactory;
    }

    public HotelReservation.HotelReservationBuilder getBuilder() {
        return new HotelReservation.HotelReservationBuilder(hotelRepository, beanValidator, calendarFactory,personFactory);
    }

    public HotelReservation find(long bookingId) {
        HotelReservation hotelReservation = hotelRepository.findHotelReservation(bookingId);
        if(hotelReservation != null){
            hotelReservation.hotelRepository = hotelRepository;
            hotelReservation.calendarFactory = calendarFactory;
            hotelReservation.beanValidator = beanValidator;
            hotelReservation.getOccupancies().forEach(o->{o.hotelRepository = hotelRepository;o.beanValidator=beanValidator;});
        }
        return hotelReservation;
    }
}
