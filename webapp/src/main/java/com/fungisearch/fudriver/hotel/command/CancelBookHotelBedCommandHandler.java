package com.fungisearch.fudriver.hotel.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.exception.HotelBedNotReservedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class CancelBookHotelBedCommandHandler {




    public CommandResult handle(long occupancyId){
        try{
//            hotelBedOccupancyFactory.findById(occupancyId).cancelBook();
        } catch (HotelBedNotReservedException ex){
            return new CommandResult(CommandResult.Status.ERROR,"CanNotCancelBookingThatDoesNotExist");
        }
        return new CommandResult(CommandResult.Status.OK,"BookingCancelled");
    }
}
