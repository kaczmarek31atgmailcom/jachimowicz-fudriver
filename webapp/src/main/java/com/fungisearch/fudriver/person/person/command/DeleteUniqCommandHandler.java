package com.fungisearch.fudriver.person.person.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.wozek.command.model.Uniq;
import com.fungisearch.fudriver.wozek.command.model.UniqFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by marcin on 19.06.16.
 */
@Service
@Transactional
public class DeleteUniqCommandHandler {

    @Autowired
    private UniqFactory uniqFactory;

    public CommandResult handle(DeleteUniqCommand command){
        for(long i = command.startNumber; i<= command.endNumber; i++){
            Uniq uniq = uniqFactory.findUniqTransactional(i,command.personId);
            if(uniq != null){
                uniq.deleteIfNotUsed();
            }
        }
    return CommandResult.OK;
    }

}
