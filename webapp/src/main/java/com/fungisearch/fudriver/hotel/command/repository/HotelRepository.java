package com.fungisearch.fudriver.hotel.command.repository;

import com.fungisearch.fudriver.hotel.command.model.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

@Repository
public class HotelRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Hotel hotel){
        em.persist(hotel);
    }

    public Hotel find(Long hotelId) {
        return em.find(Hotel.class,hotelId);
    }

    public void save(HotelRoom hotelRoom) {
        em.persist(hotelRoom);
    }

    public HotelRoom findRoom(Long roomId) {
        return em.find(HotelRoom.class,roomId);
    }

    public void save(HotelBed hotelBed) {
        em.persist(hotelBed);
    }

    public HotelBed findBed(long bedId){
        return em.find(HotelBed.class,bedId);
    }

    public void save(HotelBedOccupancy hotelBedOccupancy) {
        em.persist(hotelBedOccupancy);
    }

    public HotelBedOccupancy findOccupancyByDateAndBed(Date day, long bedId) {
        Query query = em.createQuery("select o from HotelBedOccupancy o where o.date = :day and o.hotelBed.id = :bedId");
        query.setParameter("day",day);
        query.setParameter("bedId",bedId);
        List<HotelBedOccupancy> theList = query.getResultList();
        HotelBedOccupancy result = null;
        if(!theList.isEmpty()){
            result = theList.get(0);
        }
        return result;
    }

    public HotelBedOccupancy findOccupancy(long occupancyId) {
        return em.find(HotelBedOccupancy.class,occupancyId);
    }

    public void remove(HotelBedOccupancy hotelBedOccupancy) {
        em.remove(hotelBedOccupancy);
    }

    public void save(HotelReservation hotelReservation) {
        em.persist(hotelReservation);
    }

    public HotelReservation findHotelReservation(long bookingId) {
        return em.find(HotelReservation.class,bookingId);
    }

    public void removeReservation(HotelReservation hotelReservation) {
        em.remove(hotelReservation);
    }
}
