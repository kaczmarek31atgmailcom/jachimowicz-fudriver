package com.fungisearch.fudriver.wozek.command;

import com.fungisearch.fudriver.common.command.CommandHandler;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.common.event.CustomApplicationEvent;
import com.fungisearch.fudriver.common.event.EventTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Created by marcin on 21.04.16.
 */
@Service
public class RejectWozekCommandHandler implements CommandHandler<RejectWozekCommand> {

    @Autowired
    private RejectWozek rejectWozek;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public CommandResult handle(RejectWozekCommand command) {
        rejectWozek.reject(command.wozekNr);
        CustomApplicationEvent event = new CustomApplicationEvent(EventTypeEnum.EAST_WAREHOUSE_HARVEST_PALETTE_REJECTED, command.wozekNr);
        applicationEventPublisher.publishEvent(event);
        return CommandResult.OK;
    }
}
