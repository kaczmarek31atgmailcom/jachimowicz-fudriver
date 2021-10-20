package com.fungisearch.fudriver.person.person.command;

import com.fungisearch.fudriver.common.command.Command;

/**
 * Created by marcin on 09.05.16.
 */
public class ChangePersonPasswordCommand implements Command {
    public Long personId;
    public String password;
}
