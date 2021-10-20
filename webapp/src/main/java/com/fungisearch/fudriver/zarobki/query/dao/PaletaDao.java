package com.fungisearch.fudriver.zarobki.query.dao;

import com.fungisearch.fudriver.zarobki.query.dto.PaletaDetailsDto;
import com.fungisearch.fudriver.zarobki.query.dto.PaletaDetailsHeaderDto;
import com.fungisearch.fudriver.zarobki.query.dto.PaletaHeaderDto;

import java.util.Date;
import java.util.List;

/**
 * Metody do wyświetlania palet w menu administratora dla dalszej zmiany rodzaju lub hali.
 */
public interface PaletaDao {
    List<PaletaHeaderDto> getPaletaHeaders(Date dateFrom, Date dateTo);
    List<PaletaDetailsDto> getPaletaDetails(Long paletaId);
    PaletaDetailsHeaderDto getPaletaHeader(Long paletaNr);
}
