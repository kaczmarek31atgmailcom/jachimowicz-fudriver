package com.fungisearch.fudriver.person.barcode.query.dao;

import com.fungisearch.fudriver.person.barcode.query.dto.PersonBarcodeHeaderDto;

import java.util.List;

/**
 * Created by marcin on 09.12.16.
 */
public interface PersonBarcodeDao {
    List<PersonBarcodeHeaderDto> findHeaders();

    void deleteUniqs(int personId);
}
