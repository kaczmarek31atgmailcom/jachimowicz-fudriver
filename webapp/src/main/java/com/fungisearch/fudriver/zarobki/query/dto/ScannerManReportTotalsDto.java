package com.fungisearch.fudriver.zarobki.query.dto;

import java.util.List;

public class ScannerManReportTotalsDto {
    public Long personId;
    public String login;
    public String name;
    public String surname;
    public long inne;
    public long kraj;
    public long export;
    public long totalPcs;
    public List<ScannerManReportTypeDetailDto> details;
}
