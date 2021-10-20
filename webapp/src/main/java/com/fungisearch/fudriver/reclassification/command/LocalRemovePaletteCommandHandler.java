package com.fungisearch.fudriver.reclassification.command;

import com.fungisearch.fudriver.common.command.CommandHandler;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.wozek.command.model.Uniq;
import com.fungisearch.fudriver.wozek.command.model.UniqFactory;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiEntry;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by marcin on 09.05.16.
 */
@Service
public class LocalRemovePaletteCommandHandler implements CommandHandler<LocalRemovePaletteCommand> {


    @Autowired
    private ZarobkiFactory zarobkiFactory;

    @Autowired
    UniqFactory uniqFactory;

    @Override
    public CommandResult handle(LocalRemovePaletteCommand command) {
        List<ZarobkiEntry> entries = zarobkiFactory.getWozek(command.paletteId);

        for (ZarobkiEntry wozekEntry : entries) {
            Uniq uniq = uniqFactory.findUniqTransactional(wozekEntry.uniqId,wozekEntry.pickerId);
            uniq.reclaim();
            wozekEntry.delete();
        }
        return CommandResult.OK;
    }
}
