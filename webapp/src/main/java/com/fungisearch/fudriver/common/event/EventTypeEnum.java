package com.fungisearch.fudriver.common.event;

/**
 * Created by marcin on 21.12.16.
 */
public enum EventTypeEnum {
    DELIVERY_LETTER_UPDATED,
    EMPLOYEE_WORK_STARTED,
    EMPLOYEE_WORK_ENDED,
    NULL_PAYROLL_TYPE,
    NULL_WORK_DATE,
    TIME_MINUTE_CHANGED,
    EAST_WAREHOUSE_PALETTE_CREATED,
    EAST_WAREHOUSE_PALETTE_ASSIGNED,
    EAST_WAREHOUSE_HARVEST_PALETTE_CREATED,  //Paleta wagowa została wysłana do kolejki oczekujących na akceptację do chłodni
    EAST_WAREHOUSE_HARVEST_PALETTE_REJECTED,
    EAST_WAREHOUSE_PALETTE_ACCEPTED_TO_WAREHOUSE, // Paleta magazynowa została wpuszczona do chłodni
    EAST_WAREHOUSE_PALETTE_REMOVED,
    EAST_WAREHOUSE_PALETTE_TYPE_CHANGED       //Zmiana rodzaju palety (EURO, Triplet itp)
    ;
}
