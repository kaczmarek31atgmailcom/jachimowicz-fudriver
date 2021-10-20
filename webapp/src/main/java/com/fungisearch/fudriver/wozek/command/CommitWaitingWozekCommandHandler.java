package com.fungisearch.fudriver.wozek.command;

import com.fungisearch.fudriver.common.command.CommandHandler;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.wozek.command.model.WozekEntryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marcin on 21.04.16.
 */
@Service
public class CommitWaitingWozekCommandHandler implements CommandHandler<CommitWaitingWozekCommand> {

    @Autowired
    private WozekEntryFactory wozekEntryFactory;

    @Autowired
    private AddZarobki addZarobki;

    @Override
    public CommandResult handle(CommitWaitingWozekCommand command) {
        if(wozekEntryFactory.isProposedWozekAmountValid(command.amount, command.nr)){
            addZarobki.addZarobki(command.nr);
            return new CommandResult(command.nr, CommandResult.Status.OK, "Trolley Commited");
        } else {
            return new CommandResult(CommandResult.Status.ERROR,"Invalid amount");
        }
    }
}
