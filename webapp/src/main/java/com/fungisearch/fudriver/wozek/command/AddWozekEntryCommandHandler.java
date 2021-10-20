package com.fungisearch.fudriver.wozek.command;

import com.fungisearch.fudriver.common.command.CommandHandler;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.wozek.command.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marcin on 23.02.16.
 */
@Service
public class AddWozekEntryCommandHandler implements CommandHandler<AddWozekEntryCommand> {

    @Autowired
    WozekEntryFactory wozekEntryFactory;

    @Autowired
    UserService userService;

    @Autowired
    UniqFactory uniqFactory;

    @Override
    public CommandResult handle(AddWozekEntryCommand command) {

        Uniq uniq = uniqFactory.findUniqTransactional(command.uniqId,command.pickerId);
        uniq.utilise();

        Long id = null;
        WozekEntry wozekEntry = wozekEntryFactory.builder().
                rodzajId(command.rodzajId).
                cykleId(command.cycleId).
                pickerId(command.pickerId).
                uniqId(command.uniqId).
                wagowyId(userService.getCurrentUserId()).
                wozekStatus(WozekStatus.ZEBRANY).
                qualityStatus(QualityStatus.NOT_CHECKED)
                .brygadzistaId(userService.getCurrentUserId())
                .wozkowyId(command.wozkowyId)
                .jakoscowiecId(userService.getCurrentUserId())
                .build();

        id = wozekEntry.create();
        return new CommandResult(command.pickerId);
    }
}
