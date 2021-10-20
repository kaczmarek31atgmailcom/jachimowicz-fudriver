package com.fungisearch.fudriver.paletteType.query.dao;

import com.fungisearch.fudriver.fileGenerator.fedor.model.PaletteLabel;
import com.fungisearch.fudriver.paletteType.query.dto.OutgoPaletteDto;

import java.util.List;

/**
 * Created by marcin on 25.04.16.
 */
public interface PaletteDao {
    List<OutgoPaletteDto> findOutgoPalettes();
    PaletteLabel generatePaletteLabel(Long paletteId);
}
