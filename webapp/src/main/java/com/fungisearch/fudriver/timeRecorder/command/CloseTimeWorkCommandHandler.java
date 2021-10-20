package com.fungisearch.fudriver.timeRecorder.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by marcin on 04.01.17.
 */
@Transactional
@Service
public class CloseTimeWorkCommandHandler {

    @Autowired
    private TimeWorkLogFactory timeWorkLogFactory;

    public CommandResult handle(Long timeWorkLogId){
        timeWorkLogFactory
                .find(timeWorkLogId)
                .close();
        return new CommandResult(timeWorkLogId, CommandResult.Status.OK,"TimeWorkLogClosed");
    }
}
