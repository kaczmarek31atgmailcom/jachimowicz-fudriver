package com.fungisearch.fudriver.reclassification.command;


import com.fungisearch.fudriver.common.command.Command;

/**
 * Created by marcin on 04.02.16.
 */
public class ChangeAssignedRodzajCommand implements Command {
    public Long remoteTypeId;
    public Long localTypeId;
}
