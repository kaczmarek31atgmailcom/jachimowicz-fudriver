package com.fungisearch.fudriver.hotel.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.hotel.command.repository.HotelRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;

@Entity
@Table(name = "hotel_bed")
public class HotelBed extends BaseEntity {

    transient HotelRepository hotelRepository;
    transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private HotelRoom hotelRoom;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private boolean active = true;

    public HotelBed(HotelRepository hotelRepository, BeanValidator beanValidator) {
        this.hotelRepository = hotelRepository;
        this.beanValidator = beanValidator;
    }

    private HotelBed(){}

    public HotelRoom getHotelRoom() {
        return hotelRoom;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    private void save(){
        beanValidator.validate(this);
        hotelRepository.save(this);
    }

    public static class HotelBedBuilder{
        private final HotelRepository hotelRepository;
        private final BeanValidator beanValidator;
        private String name;
        private HotelRoom hotelRoom;

        public HotelBedBuilder(HotelRepository hotelRepository, BeanValidator beanValidator) {
            this.hotelRepository = hotelRepository;
            this.beanValidator = beanValidator;
        }

        public HotelBedBuilder name(String name){
            this.name = name;
            return this;
        }

        public HotelBedBuilder hotelRoom(HotelRoom hotelRoom){
            this.hotelRoom = hotelRoom;
            return this;
        }

        public HotelBed build(){
            HotelBed hotelBed = new HotelBed(hotelRepository,beanValidator);
            hotelBed.name = name;
            hotelBed.hotelRoom = hotelRoom;
            hotelBed.save();
            return hotelBed;
        }

    }
}
