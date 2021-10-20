package com.fungisearch.fudriver.reportForLeader.dao;

import com.fungisearch.fudriver.reportForLeader.dto.WorkPeriodDto;
import com.fungisearch.fudriver.reportForLeader.model.CollectedByChamber;

import java.util.Date;
import java.util.List;

public interface ReportForLeaderDao {

    List<CollectedByChamber> getCollectedByChamber(String startDate, String endDate);

    List<WorkPeriodDto> getWorkPeriods(Date startDate, Date endDate);
}
