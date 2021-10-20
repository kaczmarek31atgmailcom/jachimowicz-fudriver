package com.fungisearch.fudriver.payroll.Exception;

import java.util.Date;

/**
 * Created by marcin on 14.06.16.
 */
public class WageNotFoundException extends RuntimeException {
    public WageNotFoundException() {
        super("Brak stawki dla wybranego dnia i rodzaju");
    }

    public WageNotFoundException(Long typeId, Date day) {
        super("Brak stawki dla wybranego dla typuId " + typeId + " i dnia " + day);
    }

    public WageNotFoundException(String message) {
        super("Brak stawki dla " + message);
    }
}
