package com.fungisearch.fudriver.timeRecorder.command.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeWorkLogDay {
    public Date day;
    public Date startTime;
    public Date endTime;
    public long totalMinutes = 0L;
    public long pauseMinutes = 0L;
    public long workMinutes = 0L;
    public List<TimeWorkLog> timeWorkLogs = new ArrayList<>();
}
