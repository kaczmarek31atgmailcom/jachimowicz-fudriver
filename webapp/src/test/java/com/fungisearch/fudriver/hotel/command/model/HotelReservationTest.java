package com.fungisearch.fudriver.hotel.command.model;

import com.fungisearch.fudriver.hotel.command.repository.HotelRepository;
import com.fungisearch.fudriver.payroll.calendar.command.model.Calendar;
import com.fungisearch.fudriver.payroll.calendar.command.model.CalendarFactory;
import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.calendar.command.repository.CalendarRepository;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.person.person.command.repository.PersonRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Incubating;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HotelReservationTest {

    @Mock
    private HotelRepository hotelRepository;
    @Mock
    private BeanValidator beanValidator;
    @Mock
    private CalendarRepository calendarRepository;
    @Mock
    private CalendarFactory calendarFactory;
    @Mock
    private PersonFactory personFactory;
    @Mock
    private PersonRepository personRepository;

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @InjectMocks
    private HotelReservation hotelReservation;
    @InjectMocks
    private HotelBedOccupancy hotelBedOccupancy;

    @Test
    public void shouldCreateTestReservation() throws ParseException {
        //Given
        Date startDate = formatter.parse("2018-01-10");
        Date endDate = formatter.parse("2018-01-20");
        String description = "initial description";
        Hotel hotel = createHotel("Hotel A");
        HotelRoom room = createRoom(hotel,"Room A");
        HotelBed bed = createBed(room,"Bed A", 1L);
        Calendar startDay = createDay(startDate);
        Calendar endDay = createDay(endDate);
        Person person = createPerson("Kasia");
        //When
        HotelReservation reservation = createReservation(bed,startDay,endDay,person,description);
        //Then
        Assert.assertEquals(person,reservation.getPerson());
    }



    private Hotel createHotel(String name){
        return new Hotel.HotelBuilder(hotelRepository,beanValidator).roomsAmount(1).bedsInRoomAmount(1).name(name).build();
    }
    private HotelRoom createRoom(Hotel hotel, String name){
        return new HotelRoom.HotelRoomBuilder(hotelRepository,beanValidator,hotel).name(name).bedsAmount(1).build();
    }
    private HotelBed createBed(HotelRoom room, String name, long id){
        HotelBed bed = new HotelBed.HotelBedBuilder(hotelRepository,beanValidator).hotelRoom(room).name(name).build();
        bed.setId(id);
        return bed;
    }
    private Calendar createDay(Date date){
        return new Calendar.CalendarBuilder(calendarRepository,beanValidator).date(date).salaryDayType(SalaryDayTypeEnum.REGULAR_DAY).build();
    }
    private Person createPerson(String name){
        return new Person.PersonBuilder(personRepository,beanValidator).name(name).build();
    }


    private HotelReservation createReservation (Date startDate, Date endDate,String description){
        Hotel hotel = createHotel("Hotel A");
        HotelRoom room = createRoom(hotel,"Room A");
        HotelBed bed = createBed(room,"Bed A", 1L);
        Calendar startDay = createDay(startDate);
        Calendar endDay = createDay(endDate);
        Person person = createPerson("Kasia");
        when(hotelRepository.findBed(any(Long.class))).thenReturn(bed);
        when(calendarFactory.findDay(startDate)).thenReturn(startDay);
        when(calendarFactory.findDay(endDate)).thenReturn(endDay);
        when(personFactory.find(any(Long.class))).thenReturn(person);
        when(hotelRepository.findOccupancyByDateAndBed(any(Date.class),any(Long.class))).thenReturn(null);
        return new HotelReservation.HotelReservationBuilder(hotelRepository,beanValidator,calendarFactory,personFactory)
                .bedId(1L)
                .personId(1L)
                .reservationType(ReservationType.BOOKING)
                .description(description)
                .startDate(startDay.getDate())
                .endDate(endDay.getDate())
                .build();
    }

    private HotelReservation createReservation (HotelBed bed, Calendar startDay, Calendar endDay, Person person, String description){
        when(hotelRepository.findBed(any(Long.class))).thenReturn(bed);
        when(calendarFactory.findDay(startDay.getDate())).thenReturn(startDay);
        when(calendarFactory.findDay(endDay.getDate())).thenReturn(endDay);
        when(personFactory.find(any(Long.class))).thenReturn(person);
        when(hotelRepository.findOccupancyByDateAndBed(any(Date.class),any(Long.class))).thenReturn(null);
        return new HotelReservation.HotelReservationBuilder(hotelRepository,beanValidator,calendarFactory,personFactory)
                .bedId(1L)
                .personId(1L)
                .reservationType(ReservationType.BOOKING)
                .description(description)
                .startDate(startDay.getDate())
                .endDate(endDay.getDate())
                .build();
    }
}

