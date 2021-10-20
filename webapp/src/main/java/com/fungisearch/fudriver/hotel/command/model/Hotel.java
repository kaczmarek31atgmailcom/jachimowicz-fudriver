package com.fungisearch.fudriver.hotel.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.hotel.command.repository.HotelRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;

@Entity
@Table(name = "hotel")
public class Hotel extends BaseEntity {

    transient HotelRepository hotelRepository;
    transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private boolean active = true;


    public Hotel(HotelRepository hotelRepository, BeanValidator beanValidator) {
        this.hotelRepository = hotelRepository;
        this.beanValidator = beanValidator;
    }

    private Hotel() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void save() {
        beanValidator.validate(this);
        hotelRepository.save(this);
    }


    public static class HotelBuilder {
        private final HotelRepository hotelRepository;
        private final BeanValidator beanValidator;
        private String name;
        private int roomsAmount;
        private int bedsInRoomAmount;

        public HotelBuilder(HotelRepository hotelRepository, BeanValidator beanValidator) {
            this.hotelRepository = hotelRepository;
            this.beanValidator = beanValidator;
        }

        public HotelBuilder name(String name) {
            this.name = name;
            return this;
        }

        public HotelBuilder roomsAmount(int roomsAmount) {
            this.roomsAmount = roomsAmount;
            return this;
        }

        public HotelBuilder bedsInRoomAmount(int bedsInRoomAmount) {
            this.bedsInRoomAmount = bedsInRoomAmount;
            return this;
        }

        public Hotel build() {
            Hotel hotel = new Hotel(hotelRepository, beanValidator);
            hotel.name = name;
            hotel.save();
            for (int i = 0; i < roomsAmount; i++) {
                new HotelRoom.HotelRoomBuilder(hotelRepository, beanValidator, hotel)
                        .hotel(hotel)
                        .name(Integer.toString(i+1))
                        .bedsAmount(bedsInRoomAmount)
                        .build();
            }
            return hotel;
        }
    }
}
