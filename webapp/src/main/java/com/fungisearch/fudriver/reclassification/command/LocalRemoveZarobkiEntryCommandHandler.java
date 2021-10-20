package com.fungisearch.fudriver.reclassification.command;

import com.fungisearch.fudriver.common.command.CommandHandler;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.wozek.command.model.Uniq;
import com.fungisearch.fudriver.wozek.command.model.UniqFactory;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiEntry;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marcin on 09.05.16.
 */
@Service
public class LocalRemoveZarobkiEntryCommandHandler implements CommandHandler<LocalRemoveZarobkiEntryCommand> {

    @Autowired
    private ZarobkiFactory zarobkiFactory;

    @Autowired
    private UniqFactory uniqFactory;

    @Override
    public CommandResult handle(LocalRemoveZarobkiEntryCommand command) {
        ZarobkiEntry zarobkiEntry = zarobkiFactory.getById(command.id);
        Uniq uniq = uniqFactory.findUniqTransactional(zarobkiEntry.uniqId, zarobkiEntry.pickerId);
        uniq.reclaim();
        zarobkiEntry.delete();
        return CommandResult.OK;
    }
}
