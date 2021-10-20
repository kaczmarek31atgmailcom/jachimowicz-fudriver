package com.fungisearch.fudriver.hotel.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.hotel.command.model.HotelFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CreateHotelCommandHandler {
    private final HotelFactory hotelFactory;

    public CreateHotelCommandHandler(HotelFactory hotelFactory) {
        this.hotelFactory = hotelFactory;
    }

    public CommandResult handle(CreateHotelCommand command){
        hotelFactory.getBuilder()
                .name(command.name)
                .roomsAmount(command.roomsAmount)
                .bedsInRoomAmount(command.bedsInRoomAmount)
                .build();
        return new CommandResult(CommandResult.Status.OK,"HotelCreated");
    }
}
