package com.fungisearch.fudriver.person.person.command;

import com.fungisearch.fudriver.common.command.Command;

/**
 * Created by marcin on 19.06.16.
 */
public class DeleteUniqCommand implements Command {
    public Long startNumber;
    public Long endNumber;
    public Long personId;
}
