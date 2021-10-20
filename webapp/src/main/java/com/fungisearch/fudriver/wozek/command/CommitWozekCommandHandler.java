package com.fungisearch.fudriver.wozek.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.common.event.CustomApplicationEvent;
import com.fungisearch.fudriver.common.event.EventTypeEnum;
import com.fungisearch.fudriver.settings.model.Settings;
import com.fungisearch.fudriver.settings.query.dao.SettingsDao;
import com.fungisearch.fudriver.wozek.command.model.WozekEntry;
import com.fungisearch.fudriver.wozek.command.model.WozekEntryFactory;
import com.fungisearch.fudriver.wozek.query.dao.WozekDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommitWozekCommandHandler {

    @Autowired
    SettingsDao settingsDao;

    @Autowired
    WozekDao wozekDao;

    @Autowired
    private WozekEntryFactory wozekEntryFactory;

    @Autowired
    private AddZarobki addZarobki;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public CommandResult handle(CommitWozekCommand command) {

        if (settingsDao.getValue(Settings.ZATWIERDZANIE_PALET_PRZEZ_MAGAZYNIERA.index) == 0) {
            addZarobki.addZarobki(command.wozekId);
            List<WozekEntry> wozekEntries = wozekEntryFactory.getWozekEntriesOfPalleteId(command.wozekId);
            for(WozekEntry wozekEntry: wozekEntries){
                wozekEntry.delete();
            }
        } else {
            wozekDao.sendForApproval(command.wozekId);
        }

        CustomApplicationEvent event = new CustomApplicationEvent(EventTypeEnum.EAST_WAREHOUSE_HARVEST_PALETTE_CREATED, command.wozekId);
        applicationEventPublisher.publishEvent(event);
        return new CommandResult(command.wozekId, CommandResult.Status.OK, "Trolley Commited");
    }


}
