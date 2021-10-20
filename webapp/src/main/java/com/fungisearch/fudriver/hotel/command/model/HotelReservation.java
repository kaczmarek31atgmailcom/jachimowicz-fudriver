package com.fungisearch.fudriver.hotel.command.model;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.hotel.command.repository.HotelRepository;
import com.fungisearch.fudriver.payroll.calendar.command.model.CalendarFactory;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "hotel_reservation")
public class HotelReservation extends BaseEntity {

    transient HotelRepository hotelRepository;
    transient BeanValidator beanValidator;
    transient CalendarFactory calendarFactory;

    transient Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Id
    @GeneratedValue
    private Long id;


    @Column(name = "start_date", nullable = false)
    private Date startDate;


    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "type")
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private ReservationType type;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "bed_id")
    private HotelBed bed;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Column(name = "description")
    private String description;

    @OneToMany(targetEntity = HotelBedOccupancy.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "hotelReservation")
    private Set<HotelBedOccupancy> occupancies;

    private HotelReservation() {
    }

    public HotelReservation(HotelRepository hotelRepository, BeanValidator beanValidator, CalendarFactory calendarFactory) {
        this.hotelRepository = hotelRepository;
        this.beanValidator = beanValidator;
        this.calendarFactory = calendarFactory;
    }

    private void save() {
        beanValidator.validate(this);
        hotelRepository.save(this);
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public ReservationType getType() {
        return type;
    }

    public Person getPerson() {
        return person;
    }

    public HotelBed getBed() {
        return bed;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public String getDescription() {
        return description;
    }

    public Set<HotelBedOccupancy> getOccupancies() {
        return occupancies;
    }

    public void edit(Edit edit) {
        if (DateUtils.getStartOfDay(edit.startDate).before(DateUtils.getStartOfDay(this.startDate))) {
            List<Date> daysToAdd = DateUtils.getDaysBetweenDates(edit.startDate, this.startDate).stream().map(d -> DateUtils.getStartOfDay(d)).collect(Collectors.toList());
            daysToAdd.remove(DateUtils.getStartOfDay(this.startDate));
            Set<HotelBedOccupancy> newOccupancies = daysToAdd.stream().map(d -> new HotelBedOccupancy.HotelBedOccupancyBuilder(hotelRepository, beanValidator)
                    .reservationType(this.getType())
                    .hotelBed(this.bed)
                    .day(DateUtils.getStartOfDay(d))
                    .hotelReservation(this)
                    .build()).collect(Collectors.toSet());
            this.occupancies.addAll(newOccupancies);
            this.startDate = DateUtils.getStartOfDay(edit.startDate);
        }

        if (DateUtils.getStartOfDay(edit.startDate).after(DateUtils.getStartOfDay(this.startDate))) {
            List<Date> daysToRemove = DateUtils.getDaysBetweenDates(this.startDate, edit.startDate).stream().map(d -> DateUtils.getStartOfDay(d)).collect(Collectors.toList());
            daysToRemove.remove(DateUtils.getStartOfDay(edit.startDate));
            List<HotelBedOccupancy> occupanciesToRemove = daysToRemove.stream()
                    .map(d -> hotelRepository.findOccupancyByDateAndBed(d, this.bed.getId()))
                    .collect(Collectors.toList());
            occupanciesToRemove.forEach(o -> {
                o.hotelRepository = this.hotelRepository;
                o.beanValidator = this.beanValidator;
            });
            occupanciesToRemove.forEach(o -> this.occupancies.remove(o));
            occupanciesToRemove.forEach(HotelBedOccupancy::remove);
            this.startDate = DateUtils.getStartOfDay(edit.startDate);
        }

        if (DateUtils.getStartOfDay(edit.endDate).after(DateUtils.getStartOfDay(this.endDate))) {
            List<Date> daysToAdd = DateUtils.getDaysBetweenDates(this.endDate, edit.endDate).stream().map(d -> DateUtils.getStartOfDay(d)).collect(Collectors.toList());
            daysToAdd.remove(DateUtils.getStartOfDay(this.endDate));
            Set<HotelBedOccupancy> newOccupancies = daysToAdd.stream().map(d -> new HotelBedOccupancy.HotelBedOccupancyBuilder(hotelRepository, beanValidator)
                    .reservationType(this.getType())
                    .hotelBed(this.bed)
                    .day(DateUtils.getStartOfDay(d))
                    .hotelReservation(this)
                    .build()).collect(Collectors.toSet());
            this.occupancies.addAll(newOccupancies);
            this.endDate = DateUtils.getStartOfDay(edit.endDate);
        }

        if (DateUtils.getStartOfDay(edit.endDate).before(DateUtils.getStartOfDay(this.endDate))) {
            List<Date> daysToRemove = DateUtils.getDaysBetweenDates(edit.endDate, this.endDate).stream().map(d -> DateUtils.getStartOfDay(d)).collect(Collectors.toList());
            daysToRemove.remove(DateUtils.getStartOfDay(edit.endDate));
            List<HotelBedOccupancy> occupanciesToRemove = daysToRemove.stream()
                    .map(d -> hotelRepository.findOccupancyByDateAndBed(d, this.bed.getId()))
                    .collect(Collectors.toList());
            occupanciesToRemove.forEach(o -> {
                o.hotelRepository = this.hotelRepository;
                o.beanValidator = this.beanValidator;
            });
            occupanciesToRemove.forEach(o -> this.occupancies.remove(o));
            occupanciesToRemove.forEach(HotelBedOccupancy::remove);
            this.endDate = DateUtils.getEndOfDay(edit.endDate);
        }


        this.description = edit.description;
    }

    public void remove() {
        this.occupancies.forEach(HotelBedOccupancy::remove);
        hotelRepository.removeReservation(this);
    }

    public static class Edit {
        private Date startDate;
        private Date endDate;
        private String description;

        public Edit startDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public Edit endDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public Edit description(String descriptin) {
            this.description = descriptin;
            return this;
        }
    }

    public static class HotelReservationBuilder {
        private final HotelRepository hotelRepository;
        private final BeanValidator beanValidator;
        private final CalendarFactory calendarFactory;
        private final PersonFactory personFactory;
        private Date startDate;
        private Date endDate;
        private Long personId;
        private long bedId;
        private ReservationType reservationType;
        private String description;

        public HotelReservationBuilder(HotelRepository hotelRepository, BeanValidator beanValidator, CalendarFactory calendarFactory, PersonFactory personFactory) {
            this.hotelRepository = hotelRepository;
            this.beanValidator = beanValidator;
            this.calendarFactory = calendarFactory;
            this.personFactory = personFactory;
        }

        public HotelReservationBuilder startDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public HotelReservationBuilder endDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public HotelReservationBuilder personId(long personId) {
            this.personId = personId;
            return this;
        }

        public HotelReservationBuilder reservationType(ReservationType reservationType) {
            this.reservationType = reservationType;
            return this;
        }

        public HotelReservationBuilder description(String description) {
            this.description = description;
            return this;
        }

        public HotelReservationBuilder bedId(long bedId) {
            this.bedId = bedId;
            return this;
        }

        public HotelReservation build() {
            HotelBed hotelBed = hotelRepository.findBed(bedId);
            hotelBed.hotelRepository = hotelRepository;
            hotelBed.beanValidator = beanValidator;
            HotelReservation hotelReservation = new HotelReservation(hotelRepository, beanValidator, calendarFactory);
            hotelReservation.startDate = startDate;
            hotelReservation.endDate = endDate;
            if (personId != null) {
                hotelReservation.person = personFactory.find(personId);
            }
            hotelReservation.type = reservationType;
            hotelReservation.description = description;
            hotelReservation.bed = hotelBed;
            hotelReservation.hotel = hotelBed.getHotelRoom().getHotel();
            hotelReservation.save();
            List<Date> reservationDays = DateUtils.getDaysBetweenDates(startDate, endDate);
            hotelReservation.occupancies = reservationDays.stream().map(r -> new HotelBedOccupancy.HotelBedOccupancyBuilder(hotelRepository, beanValidator)
                    .day(r)
                    .hotelBed(hotelBed)
                    .reservationType(hotelReservation.type)
                    .hotelReservation(hotelReservation)
                    .build()).collect(Collectors.toSet());
            return hotelReservation;
        }
    }
}
