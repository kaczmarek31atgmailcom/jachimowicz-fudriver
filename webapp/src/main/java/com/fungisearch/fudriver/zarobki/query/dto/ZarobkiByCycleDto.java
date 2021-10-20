package com.fungisearch.fudriver.zarobki.query.dto;

import java.util.Date;

/**
 * Używany przez raport cykl dzień po dniu.
 */
public class ZarobkiByCycleDto {
    public Date date;
    public int totalKrajG;
    public int totalInneG;
    public int totalExportG;
    public int totalG;
    public int quality;

    public Date getDate() {
        return date;
    }
}
