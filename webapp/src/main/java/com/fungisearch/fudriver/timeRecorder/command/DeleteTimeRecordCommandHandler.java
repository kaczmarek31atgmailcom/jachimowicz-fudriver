package com.fungisearch.fudriver.timeRecorder.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by marcin on 12.06.16.
 */
@Service
@Transactional
public class DeleteTimeRecordCommandHandler {

    @Autowired
    private TimeWorkLogFactory timeWorkLogFactory;

    public CommandResult handle(DeleteTimeRecordCommand command){
        TimeWorkLog timeWorkLog = timeWorkLogFactory.find(command.workPeriodId);
        timeWorkLog.delete();
        return new CommandResult(command.workPeriodId, CommandResult.Status.OK,"Work Period Deleted");
    }

}
