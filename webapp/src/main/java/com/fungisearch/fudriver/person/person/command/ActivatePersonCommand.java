package com.fungisearch.fudriver.person.person.command;

import com.fungisearch.fudriver.common.command.Command;

/**
 * Created by marcin on 03.03.16.
 */
public class ActivatePersonCommand implements Command{
    public Long personId;
    public Long version;
    public Long periodsVersion;
}
