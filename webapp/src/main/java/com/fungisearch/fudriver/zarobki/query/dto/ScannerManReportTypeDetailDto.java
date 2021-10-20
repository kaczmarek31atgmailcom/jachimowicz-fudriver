package com.fungisearch.fudriver.zarobki.query.dto;

import com.fungisearch.fudriver.type.command.model.ExportType;

public class ScannerManReportTypeDetailDto {
    public Integer personId;
    public Integer typeId;
    public String typeName;
    public int typeWeight;
    public long totalWeight;
    public long numberPcs;
    public ExportType exportType;

    public int getTypeId() {
        return typeId;
    }
}
