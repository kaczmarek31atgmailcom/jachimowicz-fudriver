package com.fungisearch.fudriver.person.person.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by marcin on 04.04.16.
 */
@Service
@Transactional
public class DeleteRfidCommandHandler {

    @Autowired
    private PersonFactory personFactory;

    public CommandResult handle(DeleteRfidCommand command) {
        Person person = personFactory.builder().id(command.employeeId).version(command.version).build();
        person.deleteBadge();
        return CommandResult.OK;
    }
}
