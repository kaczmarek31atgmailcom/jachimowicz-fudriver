package com.fungisearch.fudriver.timeRecorder.command;

import com.fungisearch.fudriver.common.command.Command;

import java.util.Date;

/**
 * Created by marcin on 09.06.16.
 */
public class UpdateTimeRecordCommand implements Command {
    public Long id;
    public Date startDate;
    public Date endDate;
}
