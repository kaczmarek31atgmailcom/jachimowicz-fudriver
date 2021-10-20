package com.fungisearch.fudriver.person.person.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.person.person.command.model.ForeignerAlert;
import com.fungisearch.fudriver.person.person.command.repository.ForeignerAlertFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class EditForeignerAlertCommandHandler {
    private final ForeignerAlertFactory foreignerAlertFactory;

    @Autowired
    public EditForeignerAlertCommandHandler(ForeignerAlertFactory foreignerAlertFactory) {
        this.foreignerAlertFactory = foreignerAlertFactory;
    }

    public CommandResult handle(EditForeignerAlertCommand command){
        foreignerAlertFactory.getForeingerAlert()
                .edit(new ForeignerAlert.Edit()
                .passportDays(command.passportDays)
                .statementDays(command.statementDays)
                .visaDays(command.visaDays)
                .stayDays(command.stayDays));

        return new CommandResult(CommandResult.Status.OK,"ForeignerAlertUpdated");
    }
}
