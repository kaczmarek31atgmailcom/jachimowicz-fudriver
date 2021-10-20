package com.fungisearch.fudriver.reclassification.command;

import com.fungisearch.fudriver.common.command.CommandHandler;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.reclassification.command.model.ReclassificationDetailSkup;
import com.fungisearch.fudriver.reclassification.command.repository.ReclassificationDetailSkupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marcin on 17.02.16.
 */
@Service
public class TurnOnOffCommandHandler implements CommandHandler<TurnDetailOnOffCommand> {

    @Autowired
    private ReclassificationDetailSkupRepository reclassificationDetailSkupRepository;


    @Override
    public CommandResult handle(TurnDetailOnOffCommand command) {
        ReclassificationDetailSkup detail = reclassificationDetailSkupRepository.find(command.id);
        detail.setActive(command.status);
        reclassificationDetailSkupRepository.update(detail);
        return CommandResult.OK;
    }
}
