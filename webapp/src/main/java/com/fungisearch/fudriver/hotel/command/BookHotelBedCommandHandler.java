package com.fungisearch.fudriver.hotel.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.hotel.command.model.HotelReservationFactory;
import com.fungisearch.fudriver.hotel.command.model.ReservationType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookHotelBedCommandHandler {
    private final HotelReservationFactory hotelReservationFactory;


    public BookHotelBedCommandHandler(HotelReservationFactory hotelReservationFactory) {
        this.hotelReservationFactory = hotelReservationFactory;
    }

    public CommandResult handle(BookHotelBedCommand command){
        hotelReservationFactory.getBuilder()
                .startDate(command.startDate)
                .endDate(command.endDate)
                .reservationType(ReservationType.BOOKING)
                .personId(command.personId)
                .bedId(command.bedId)
                .description(command.message)
                .build();
    return new CommandResult(CommandResult.Status.OK,"HotelBedSuccessfullyReserved");
}
}
