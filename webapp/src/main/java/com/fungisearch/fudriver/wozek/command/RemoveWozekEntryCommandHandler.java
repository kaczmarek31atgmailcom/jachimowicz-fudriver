package com.fungisearch.fudriver.wozek.command;

import com.fungisearch.fudriver.common.command.CommandHandler;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.wozek.command.model.Uniq;
import com.fungisearch.fudriver.wozek.command.model.UniqFactory;
import com.fungisearch.fudriver.wozek.command.model.WozekEntry;
import com.fungisearch.fudriver.wozek.command.model.WozekEntryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by idea on 14.03.16.
 */
@Service
public class RemoveWozekEntryCommandHandler implements CommandHandler<RemoveWozekEntryCommand> {


    @Autowired
    WozekEntryFactory wozekEntryFactory;

    @Autowired
    UniqFactory uniqFactory;


    @Override
    public CommandResult handle(RemoveWozekEntryCommand command) {
        WozekEntry wozekEntry = wozekEntryFactory.get(command.id);
        Uniq uniq = uniqFactory.findUniqTransactional(wozekEntry.getUniqId(),wozekEntry.getPickerId());
        uniq.reclaim();
        wozekEntry.delete();
        return CommandResult.OK;
    }
}
