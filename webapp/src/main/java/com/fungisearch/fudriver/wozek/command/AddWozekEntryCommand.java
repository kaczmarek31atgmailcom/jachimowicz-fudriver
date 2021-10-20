package com.fungisearch.fudriver.wozek.command;

import com.fungisearch.fudriver.common.command.Command;

/**
 * Created by marcin on 23.02.16.
 */
public class AddWozekEntryCommand implements Command {
    public Long pickerId;
    public Long rodzajId;
    public Long cycleId;
    public Long uniqId;
    public Long brygadzistaId;
    public Integer wozkowyId;
    public Integer qualityStatus;

}
