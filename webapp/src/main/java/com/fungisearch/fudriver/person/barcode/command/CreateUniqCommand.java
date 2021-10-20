package com.fungisearch.fudriver.person.barcode.command;

import com.fungisearch.fudriver.common.command.Command;

/**
 * Created by idea on 07.03.16.
 */
public class CreateUniqCommand implements Command {
    public Long pickerId;
    public Integer numberOfUniqsToBeCreated;
}
