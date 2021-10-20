package com.fungisearch.fudriver.timeRecorder.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CreateClosedTimeRecordCommandHandler {

    private final PersonFactory personFactory;
    private final TimeWorkLogFactory timeWorkLogFactory;

    @Autowired
    public CreateClosedTimeRecordCommandHandler(PersonFactory personFactory, TimeWorkLogFactory timeWorkLogFactory) {
        this.personFactory = personFactory;
        this.timeWorkLogFactory = timeWorkLogFactory;
    }

    public CommandResult handle(CreateClosedTimeRecordCommand command){
        TimeWorkLog timeWorkLog = timeWorkLogFactory.getClosedTimeWorkLogBuilder()
                .person(personFactory.find(command.personId))
                .startTime(command.startTime)
                .endTime(command.endTime)
                .build();
        return new CommandResult(timeWorkLog.getId(), CommandResult.Status.OK,"TimePeriodCreated");
    }
}
