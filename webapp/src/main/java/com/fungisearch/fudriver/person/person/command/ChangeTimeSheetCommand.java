package com.fungisearch.fudriver.person.person.command;

import com.fungisearch.fudriver.common.command.Command;

import java.util.Date;

/**
 * Created by idea on 06.03.16.
 */
public class ChangeTimeSheetCommand implements Command {
    public Long id;
    public Long personId;
    public Date startDate;
    public Date endDate;
    public Long version;
}
