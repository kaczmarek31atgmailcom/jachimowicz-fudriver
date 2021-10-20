package com.fungisearch.fudriver.hotel.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.hotel.command.model.HotelReservation;
import com.fungisearch.fudriver.hotel.command.model.HotelReservationFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EditBookingCommandHandler {
    private final HotelReservationFactory hotelReservationFactory;

    public EditBookingCommandHandler(HotelReservationFactory hotelReservationFactory) {
        this.hotelReservationFactory = hotelReservationFactory;
    }

    public CommandResult handle(EditBookingCommand command) {
        hotelReservationFactory.find(command.bookingId).edit(new HotelReservation.Edit()
                .startDate(command.startDate)
                .endDate(command.endDate)
                .description(command.description));
        return new CommandResult(CommandResult.Status.OK, "Reservation Updated");
    }
}
