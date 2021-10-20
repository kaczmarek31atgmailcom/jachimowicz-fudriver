package com.fungisearch.fudriver.zarobki.query.dao;

import com.fungisearch.fudriver.zarobki.query.dto.ScannerManReportTotalsDto;
import com.fungisearch.fudriver.zarobki.query.dto.ScannerManReportTypeDetailDto;

import java.util.Date;
import java.util.List;

public interface ScannerManReportDao {
    List<ScannerManReportTotalsDto> getTotals(Date startDate, Date endDate);
    List<ScannerManReportTypeDetailDto> getDetails(Date startDate, Date endDate);
}
