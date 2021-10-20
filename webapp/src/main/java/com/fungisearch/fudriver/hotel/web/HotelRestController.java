package com.fungisearch.fudriver.hotel.web;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.hotel.command.*;
import com.fungisearch.fudriver.hotel.query.dao.HotelDao;
import com.fungisearch.fudriver.hotel.query.dto.HotelRoomDto;
import com.fungisearch.fudriver.hotel.query.dto.HotelSettingsHeaderDto;
import com.fungisearch.fudriver.hotel.query.dto.NotReservedPeriodDto;
import com.fungisearch.fudriver.hotel.query.dto.ReservationDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/hotel", produces = "application/json; charset=UTF-8")
public class HotelRestController {

    private final CreateHotelCommandHandler createHotelCommandHandler;
    private final HotelDao hotelDao;
    private final BookHotelBedCommandHandler bookHotelBedCommandHandler;
    private final EditBookingCommandHandler editBookingCommandHandler;
    private final ReserveHotelBedCommandHandler reserveHotelBedCommandHandler;
    private final CancelBookHotelBedCommandHandler cancelBookHotelBedCommandHandler;
    private final EditHotelReservationCommandHandler editHotelReservationCommandHandler;
    private final RemoveHotelReservationCommandHandler removeHotelReservationCommandHandler;

    public HotelRestController(CreateHotelCommandHandler createHotelCommandHandler, HotelDao hotelDao, BookHotelBedCommandHandler bookHotelBedCommandHandler, EditBookingCommandHandler editBookingCommandHandler, ReserveHotelBedCommandHandler reserveHotelBedCommandHandler, CancelBookHotelBedCommandHandler cancelBookHotelBedCommandHandler, EditHotelReservationCommandHandler editHotelReservationCommandHandler, RemoveHotelReservationCommandHandler removeHotelReservationCommandHandler) {
        this.createHotelCommandHandler = createHotelCommandHandler;
        this.hotelDao = hotelDao;
        this.bookHotelBedCommandHandler = bookHotelBedCommandHandler;
        this.editBookingCommandHandler = editBookingCommandHandler;
        this.reserveHotelBedCommandHandler = reserveHotelBedCommandHandler;
        this.cancelBookHotelBedCommandHandler = cancelBookHotelBedCommandHandler;
        this.editHotelReservationCommandHandler = editHotelReservationCommandHandler;
        this.removeHotelReservationCommandHandler = removeHotelReservationCommandHandler;
    }

    @PostMapping
    public CommandResult createHotel(@RequestBody CreateHotelCommand command){
        return createHotelCommandHandler.handle(command);
    }

    @GetMapping("/settings/hotel/headers")
    public List<HotelSettingsHeaderDto> getSettingsHeaders(){
        return hotelDao.getHotelHeaders();
    }

    @PostMapping("/book")
    public CommandResult book(@RequestBody BookHotelBedCommand command){
        return bookHotelBedCommandHandler.handle(command);
    }

    @PutMapping("/book")
    public CommandResult editBook(@RequestBody EditBookingCommand command){
        return editBookingCommandHandler.handle(command);
    }


    @PostMapping("/reserve")
    public CommandResult book(@RequestBody ReserveHotelBedCommand command){
        return reserveHotelBedCommandHandler.handle(command);
    }

    @PutMapping("/reserve")
    public CommandResult book(@RequestBody EditHotelReservationCommand command){
        return editHotelReservationCommandHandler.handle(command);
    }


    @DeleteMapping("/booking/{bookingId}")
    public CommandResult removeReservation(@PathVariable(name = "bookingId") long  bookingId){
        return removeHotelReservationCommandHandler.handle(bookingId);
    }

    @GetMapping("{hotelId}/rooms")
    public List<HotelRoomDto> getRooms(@PathVariable(name = "hotelId") long hotelId){
        return hotelDao.getRooms(hotelId);
    }

    @DeleteMapping("/book/{occupancyId}")
    public CommandResult cancelBook(@PathVariable(name = "occupancyId") long occupancyId){
        return cancelBookHotelBedCommandHandler.handle(occupancyId);
    }

    @GetMapping("/reservation/{reservationId}/max-period")
    public NotReservedPeriodDto getReservationPeriod(@PathVariable (name = "reservationId") long reservationId){
        return hotelDao.getNotReservedPeriod(reservationId);
    }

    @GetMapping("/book/{hotelId}/{startDate}/{endDate}")
    public List<ReservationDto> getBookingStatus(@PathVariable(name = "hotelId") long hotelId,
                                                 @PathVariable(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                 @PathVariable(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        return hotelDao.getReservations(startDate,endDate,hotelId);
    }


}
