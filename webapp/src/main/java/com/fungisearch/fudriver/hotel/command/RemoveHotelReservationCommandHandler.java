package com.fungisearch.fudriver.hotel.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.hotel.command.model.HotelReservationFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RemoveHotelReservationCommandHandler {
    private final HotelReservationFactory hotelReservationFactory;

    public RemoveHotelReservationCommandHandler(HotelReservationFactory hotelReservationFactory) {
        this.hotelReservationFactory = hotelReservationFactory;
    }

    public CommandResult handle(long id){
        hotelReservationFactory.find(id).remove();
        return new CommandResult(CommandResult.Status.OK,"HotelReservationRemoved");
    }
}
