package com.fungisearch.fudriver.hotel.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.hotel.command.repository.HotelRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;

@Entity
@Table(name = "hotel_room")
public class HotelRoom extends BaseEntity {

    transient HotelRepository hotelRepository;
    transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private boolean active = true;

    public HotelRoom(HotelRepository hotelRepository, BeanValidator beanValidator) {
        this.hotelRepository = hotelRepository;
        this.beanValidator = beanValidator;
    }

    private void save() {
        beanValidator.validate(this);
        hotelRepository.save(this);
    }

    private HotelRoom() {
    }

    public Hotel getHotel() {
        return hotel;
    }

    public static class HotelRoomBuilder {
        private final HotelRepository hotelRepository;
        private final BeanValidator beanValidator;
        private String name;
        private Hotel hotel;
        private Integer bedsAmount;

        public HotelRoomBuilder(HotelRepository hotelRepository, BeanValidator beanValidator, Hotel hotel) {
            this.hotelRepository = hotelRepository;
            this.beanValidator = beanValidator;
            this.hotel = hotel;
        }

        public HotelRoomBuilder name(String name) {
            this.name = name;
            return this;
        }

        public HotelRoomBuilder hotel(Hotel hotel) {
            this.hotel = hotel;
            return this;
        }

        public HotelRoomBuilder bedsAmount(int bedsAmount) {
            this.bedsAmount = bedsAmount;
            return this;
        }

        public HotelRoom build() {
            HotelRoom hotelRoom = new HotelRoom(hotelRepository, beanValidator);
            hotelRoom.hotel = hotel;
            hotelRoom.name = name;
            hotelRoom.save();
            for (int i = 0; i < bedsAmount; i++) {
                new HotelBed.HotelBedBuilder(hotelRepository, beanValidator)
                        .hotelRoom(hotelRoom)
                        .name(Integer.toString(i+1))
                        .build();
            }
            return hotelRoom;
        }

    }

}
