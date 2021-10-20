package com.fungisearch.fudriver.person.person.command;

import com.fungisearch.fudriver.common.command.Command;

/**
 * Created by marcin on 03.04.16.
 */
public class RegisterBadgeCommand implements Command {
    public Long employeeId;
    public String badge;
    public Long version;
}
