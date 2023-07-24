package com.fungisearch.fudriver.wozek.query.dto;

/**
 * Używane do wyświetlania sumy dla każdej zbieraczki na ekranie podsumowania wózków.
 * Na życzenie p. Powichrowskiej
 */
public class TrolleysSummaryDto {
    public long pickerId;
    public int pickerNr;
    public String pickerName;
    public String pickerSurname;
    public String typeName;
    public long typeWeight;
    public String chamberName;
    public long totalAmount;
    public long totalWeight;
    public long totalReclassified;
}
