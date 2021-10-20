package com.fungisearch.fudriver.timeRecorder.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by marcin on 09.06.16.
 */
@Service
@Transactional
public class UpdateTimeRecordCommandHandler {

    @Autowired
    private TimeWorkLogFactory timeWorkLogFactory;

    public CommandResult handle(UpdateTimeRecordCommand command){
        TimeWorkLog timeWorkLog = timeWorkLogFactory.find(command.id);
        timeWorkLog.edit(new TimeWorkLog.Edit().startDate(command.startDate).endDate(command.endDate));
        return new CommandResult(command.id, CommandResult.Status.OK,"TimeWorkLogUpdated");
    }
}
