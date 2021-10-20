package com.fungisearch.fudriver.hotel.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.hotel.command.repository.HotelRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "hotel_bed_occupancy")
public class HotelBedOccupancy extends BaseEntity {

    transient HotelRepository hotelRepository;
    transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "date",nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "bed_id", nullable = false)
    private HotelBed hotelBed;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private HotelReservation hotelReservation;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private ReservationType reservationType;

    public HotelBedOccupancy(HotelRepository hotelRepository, BeanValidator beanValidator) {
        this.hotelRepository = hotelRepository;
        this.beanValidator = beanValidator;
    }

    private HotelBedOccupancy() {
    }

    private void save() {
        beanValidator.validate(this);
        hotelRepository.save(this);
    }


    public void remove() {
        hotelRepository.remove(this);
    }

    public static class HotelBedOccupancyBuilder {
        private final HotelRepository hotelRepository;
        private final BeanValidator beanValidator;
        private Date day;
        private HotelBed hotelBed;
        private ReservationType reservationType;
        private HotelReservation hotelReservation;

        public HotelBedOccupancyBuilder(HotelRepository hotelRepository, BeanValidator beanValidator) {
            this.hotelRepository = hotelRepository;

            this.beanValidator = beanValidator;
        }

        public HotelBedOccupancyBuilder hotelBed(HotelBed hotelBed) {
            this.hotelBed = hotelBed;
            return this;
        }

        public HotelBedOccupancyBuilder day(Date day) {
            this.day = day;
            return this;
        }

        public HotelBedOccupancyBuilder reservationType(ReservationType reservationType){
        this.reservationType = reservationType;
            return this;
        }

        public HotelBedOccupancyBuilder hotelReservation(HotelReservation hotelReservation){
            this.hotelReservation = hotelReservation;
            return this;
        }

        public HotelBedOccupancy build() {
            HotelBedOccupancy occupancy = hotelRepository.findOccupancyByDateAndBed(day,hotelBed.getId());
            if(occupancy == null) {
                occupancy = new HotelBedOccupancy(hotelRepository, beanValidator);
                occupancy.date = day;
                occupancy.hotelBed = hotelBed;
                occupancy.reservationType = reservationType;
                occupancy.hotelReservation = hotelReservation;
                occupancy.save();
                return occupancy;
            } else {
                throw new IllegalStateException("Hotel Occupancy already exists. id: " + occupancy.id);
            }
        }
    }
}
