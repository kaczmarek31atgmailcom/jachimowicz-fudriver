package com.fungisearch.fudriver.timeRecorder.command.model;

import java.util.Date;

/** Published synchronously after a work period mutation, in its transaction. */
public class WorkTimeChanged {
    public final Long personId;
    public final Date previousStart;
    public final Date previousEnd;
    public final Date start;
    public final Date end;

    public WorkTimeChanged(Long personId, Date previousStart, Date previousEnd, Date start, Date end) {
        this.personId = personId;
        this.previousStart = previousStart;
        this.previousEnd = previousEnd;
        this.start = start;
        this.end = end;
    }
}
