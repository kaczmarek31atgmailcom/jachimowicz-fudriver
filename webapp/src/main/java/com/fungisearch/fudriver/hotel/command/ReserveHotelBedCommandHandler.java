package com.fungisearch.fudriver.hotel.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.hotel.command.model.HotelReservationFactory;
import com.fungisearch.fudriver.hotel.command.model.ReservationType;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ReserveHotelBedCommandHandler {
    private final HotelReservationFactory hotelReservationFactory;

    public ReserveHotelBedCommandHandler(HotelReservationFactory hotelReservationFactory) {
        this.hotelReservationFactory = hotelReservationFactory;
    }

    public CommandResult handle(ReserveHotelBedCommand command) {
        hotelReservationFactory.getBuilder()
                .bedId(command.bedId)
                .description(command.message)
                .startDate(command.startDate)
                .endDate(command.endDate)
                .reservationType(ReservationType.RESERVATION)
                .description(command.message)
                .build();
        return new CommandResult(CommandResult.Status.OK, "HotelBedSuccessfullyReserved");
    }
}
