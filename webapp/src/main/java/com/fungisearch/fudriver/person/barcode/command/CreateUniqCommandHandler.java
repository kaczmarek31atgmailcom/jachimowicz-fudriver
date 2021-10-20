package com.fungisearch.fudriver.person.barcode.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.wozek.command.model.Uniq;
import com.fungisearch.fudriver.wozek.command.model.UniqFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by idea on 07.03.16.
 */
@Service
@Transactional
public class CreateUniqCommandHandler {

    @Autowired
    private UniqFactory uniqFactory;

    public CommandResult handle(CreateUniqCommand command) {
        long firstFree = 0;
        Long lastReserved = uniqFactory.findLastUsed(command.pickerId);

        if (command.numberOfUniqsToBeCreated > 0) {
            lastReserved++;
            firstFree = lastReserved;
            for (int i = 1; i <= command.numberOfUniqsToBeCreated; i++) {
                uniqFactory.
                        builder()
                        .pickerId(command.pickerId)
                        .uniqId(lastReserved)
                        .build().create();
                lastReserved++;
            }
        }
        return new CommandResult(firstFree);
    }
}
